package com.zlodeuscomp.vdolgah.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zlodeuscomp.vdolgah.model.Debtor;

@Database(entities = Debtor.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DebtorDao debtorDao();
}
