package com.zlodeuscomp.vdolgah.screens.main;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zlodeuscomp.vdolgah.App;
import com.zlodeuscomp.vdolgah.R;
import com.zlodeuscomp.vdolgah.details.AddDebtor;
import com.zlodeuscomp.vdolgah.model.Debtor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static Boolean isOpenedToUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOpenedToUpdate = false;
                AddDebtor.start(MainActivity.this, null);
            }
        });

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getDebtorLiveData().observe(this, new Observer<List<Debtor>>() {
            @Override
            public void onChanged(List<Debtor> debtors) {
                adapter.setItems(debtors);
            }
        });

        TextView total_sum = (TextView) findViewById(R.id.total_sum);
        updateSum(total_sum);
    }

    public static void updateSum(TextView total_sum) {
        int sum = 0;
        List<Debtor> all_debtors = App.getInstance().getDebtorDao().getAll();
        for (Debtor debtor : all_debtors) {
            sum += Integer.parseInt(debtor.sum);
        }
        total_sum.setText(String.valueOf(sum));
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView total_sum = (TextView) findViewById(R.id.total_sum);
        updateSum(total_sum);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_open_myDebts) {
            Intent intent = new Intent(this, MyDebts.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
