package com.zlodeuscomp.vdolgah.data;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.zlodeuscomp.vdolgah.model.Debtor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DebtorDAOTest {

    private AppDatabase db;
    private DebtorDao debtorDao;

    @Before
    public void createDb() throws Exception {
        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase.class)
                .build();
        debtorDao = db.debtorDao();

    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }


    //в пизду этот тест, никогда не выполнится
    @Test
    public void inserted_debtor_are_equal_to_extracted() throws Exception {
        List<Debtor> debtors = DebtorTestHelper.createListOfDebtors(1);

        debtorDao.insert(debtors.get(0));
        List<Debtor> dbEmployees = debtorDao.getAll();

        assertEquals(1, dbEmployees.size());
        assertTrue(DebtorTestHelper.debtorsAreIdential(debtors.get(0), dbEmployees.get(0)));
    }
}
