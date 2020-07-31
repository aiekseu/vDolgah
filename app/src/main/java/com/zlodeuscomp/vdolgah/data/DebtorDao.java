package com.zlodeuscomp.vdolgah.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zlodeuscomp.vdolgah.model.Debtor;

import java.util.List;

@Dao
public interface DebtorDao {    //data access object
    @Query("SELECT * FROM Debtor")
    List<Debtor> getAll();

    @Query("SELECT * FROM Debtor")
    LiveData<List<Debtor>> getAllLiveData();

    @Query("SELECT * FROM Debtor WHERE uid IN (:userIds)")
    List<Debtor> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Debtor WHERE name = :name LIMIT 1")
    Debtor findByName(String name);

    @Query("SELECT * FROM Debtor WHERE email = :email LIMIT 100")
    List<Debtor> findByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Debtor debtor);

    @Update
    void update(Debtor debtor);

    @Delete
    void delete(Debtor debtor);
}
