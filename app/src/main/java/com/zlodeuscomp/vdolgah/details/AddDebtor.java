package com.zlodeuscomp.vdolgah.details;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.zlodeuscomp.vdolgah.App;
import com.zlodeuscomp.vdolgah.R;
import com.zlodeuscomp.vdolgah.model.Debtor;
import com.zlodeuscomp.vdolgah.screens.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDebtor extends AppCompatActivity {

    private static final String EXTRA_DEBT = "AddDebtor.EXTRA_DEBT";

    private Debtor debtor;

    EditText date;
    EditText name;
    EditText sum;
    EditText email;

    public static void start(Activity caller, Debtor debtor) {
        Intent intent = new Intent(caller, AddDebtor.class);
        if (debtor != null) {
            intent.putExtra(EXTRA_DEBT, debtor);
        }
        caller.startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOnAddPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(R.string.add_debtor_page_name);

        name = (EditText) findViewById(R.id.input_name);
        sum = (EditText) findViewById(R.id.input_sum);
        email = (EditText) findViewById(R.id.input_email);
        date = (EditText) findViewById(R.id.input_date);

        SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDate = dateF.format(Calendar.getInstance().getTime());
        date.setText(currentDate);

        if (getIntent().hasExtra(EXTRA_DEBT)) {
            debtor = getIntent().getParcelableExtra(EXTRA_DEBT);
            name.setText(debtor.name);
            sum.setText(debtor.sum);
            email.setText(debtor.email);
            date.setText(debtor.date);
        }else {
            debtor = new Debtor();
        }

        final Button debtor_save = (Button) findViewById(R.id.add);
        final Button send_email = (Button) findViewById(R.id.send_email);
        final Switch if_anonymous = (Switch) findViewById(R.id.if_anonymous);
        final TextView if_anonymous_text = (TextView) findViewById(R.id.if_anonymous_text);

        if (MainActivity.isOpenedToUpdate) {
            getSupportActionBar().setTitle("Обновить запись");
            debtor_save.setText("Обновить информацию");
        }else {
            send_email.setVisibility(View.GONE);
            if_anonymous.setVisibility(View.GONE);
            if_anonymous_text.setVisibility(View.GONE);
        }

        debtor_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((name.getText().length() > 0) && (sum.getText().length() > 0)) {
                    debtor.name = name.getText().toString();
                    debtor.sum = sum.getText().toString();
                    debtor.timestamp = System.currentTimeMillis();
                    if (email.getText().length() > 0) {
                        debtor.email = email.getText().toString();
                    }
                    if (date.getText().length() > 0) {
                        debtor.date = date.getText().toString();
                    }
                    if (getIntent().hasExtra(EXTRA_DEBT)) {
                        App.getInstance().getDebtorDao().update(debtor);
                    } else {
                        App.getInstance().getDebtorDao().insert(debtor);
                    }
                    finish();
                }
            }
        });

        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((name.getText().length() > 0) && (sum.getText().length() > 0)) {
                    debtor.name = name.getText().toString();
                    debtor.sum = sum.getText().toString();
                    debtor.timestamp = System.currentTimeMillis();
                    if (email.getText().length() > 0) {
                        debtor.email = email.getText().toString();
                    }
                    if (date.getText().length() > 0) {
                        debtor.date = date.getText().toString();
                    }
                    if (getIntent().hasExtra(EXTRA_DEBT)) {
                        App.getInstance().getDebtorDao().update(debtor);
                    } else {
                        App.getInstance().getDebtorDao().insert(debtor);
                    }
                }

                List<Debtor> debtors_id = App.getInstance().getDebtorDao().findByEmail(email.getText().toString());

                if (debtors_id.size() == 1 && !if_anonymous.isChecked()) {
                    sendMail();
                } else if (debtors_id.size() == 1 && if_anonymous.isChecked()) {
                    sendAnonymousMail();
                } else {
                    Intent intent = new Intent(AddDebtor.this, EmailChoosingDialog.class);
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("name", name.getText().toString());
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String message = data.getStringExtra("message");
        final Switch if_anonymous = (Switch) findViewById(R.id.if_anonymous);
        if (if_anonymous.isChecked()) {
            sendAnonymousMail(message);
        } else sendMail(message);
    }

    private void sendAnonymousMail() {
        String recipient = email.getText().toString();
        String subject = "Напоминание";
        String message = "Привет, " + name.getText().toString() + ", напоминаю тебе, что " + date.getText().toString() +
                " ты задолжал мне " + sum.getText().toString() + " рублей.\nПрошу отдать в скором времени";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this,recipient,subject,message);

        javaMailAPI.execute();
    }

    private void sendAnonymousMail(String message) {
        String recipient = email.getText().toString();
        String subject = "Напоминание";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this,recipient,subject,message);

        javaMailAPI.execute();
    }

    private void sendMail() {

        String recipient = email.getText().toString();
        String subject = "Напоминание";
        String message = "Привет, " + name.getText().toString() + ", напоминаю тебе, что " + date.getText().toString() +
                            " ты задолжал мне " + sum.getText().toString() + " рублей.\nПрошу отдать в скором времени";
        if (!isEmailValid(recipient)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введите корректный email", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("message/rfc822");
            MainActivity.isOpenedToUpdate = null;
            startActivity(Intent.createChooser(intent, "Выберите почтовый клиент"));
        }
    }

    private void sendMail(String message) {

        String recipient = email.getText().toString();
        String subject = "Напоминание";

        if (!isEmailValid(recipient)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введите корректный email", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("message/rfc822");
            MainActivity.isOpenedToUpdate = null;
            startActivity(Intent.createChooser(intent, "Выберите почтовый клиент"));
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

