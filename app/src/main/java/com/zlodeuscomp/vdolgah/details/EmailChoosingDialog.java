package com.zlodeuscomp.vdolgah.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zlodeuscomp.vdolgah.App;
import com.zlodeuscomp.vdolgah.R;
import com.zlodeuscomp.vdolgah.model.Debtor;

import java.util.ArrayList;
import java.util.List;


public class EmailChoosingDialog extends AppCompatActivity {

    private ListView listView;
    private Button send;
    private String message;
    int whole_sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_choosing_dialog);

        TextView how_much_emails_have_founded = (TextView) findViewById(R.id.how_much_emails_have_founded);
        String email = getIntent().getStringExtra("email");
        final List<Debtor> debtors_id = App.getInstance().getDebtorDao().findByEmail(email);
        int same_emails_count = debtors_id.size();
        how_much_emails_have_founded.setText(String.valueOf(same_emails_count));

        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        initListViewData();

        send = (Button) findViewById(R.id.send_checked_emails);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray sbArray = listView.getCheckedItemPositions();

                if (sbArray.size() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы ничего не выбрали", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if (sbArray.get(key))
                        whole_sum += Integer.parseInt(debtors_id.get(i).sum);
                }

                String name = getIntent().getStringExtra("name");
                message = "Привет, " + name + ", напоминаю тебе, что ты задолжал мне " + whole_sum +
                        " рублей. Вот детализированный список:\n";
                int pos = 1;
                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if (sbArray.get(key)) {
                        message += pos + ") " + debtors_id.get(i).date + " - " + debtors_id.get(i).sum + " рублей.\n";
                        pos++;
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("message", message);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initListViewData() {
        String email = getIntent().getStringExtra("email");
        List<Debtor> debtors_id = App.getInstance().getDebtorDao().findByEmail(email);
        List<String> debtors_infos = new ArrayList<String>();
        for (int i = 0; i < debtors_id.size(); i++) {
            debtors_infos.add(debtors_id.get(i).name + " - " + debtors_id.get(i).sum + " руб. (" +
                    debtors_id.get(i).date + ")");
        }

        ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, debtors_infos);
        listView.setAdapter(arrayAdapter);
    }











}
