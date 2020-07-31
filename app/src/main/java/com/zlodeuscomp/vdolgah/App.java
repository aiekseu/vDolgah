package com.zlodeuscomp.vdolgah;

import android.app.Application;

import androidx.room.Room;

import com.zlodeuscomp.vdolgah.data.AppDatabase;
import com.zlodeuscomp.vdolgah.data.DebtorDao;

public class App extends Application {

    private AppDatabase database;
    private DebtorDao debtorDao;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db-allDebtors").allowMainThreadQueries().build();

        debtorDao = database.debtorDao();
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public DebtorDao getDebtorDao() {
        return debtorDao;
    }

    public void setDebtorDao(DebtorDao debtorDao) {
        this.debtorDao = debtorDao;
    }
}
