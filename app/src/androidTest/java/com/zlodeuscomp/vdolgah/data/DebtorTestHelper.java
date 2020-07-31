package com.zlodeuscomp.vdolgah.data;

import com.zlodeuscomp.vdolgah.model.Debtor;

import java.util.ArrayList;
import java.util.List;


public class DebtorTestHelper {

    public static List<Debtor> createListOfDebtors(int count) {
        ArrayList<Debtor> debtors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            debtors.add(new Debtor());
        }
        return debtors;
    }

    public static Boolean debtorsAreIdential(Debtor first, Debtor second) {
        if ((first.uid == second.uid) && (first.name.equals(second.name))) return true;
        return false;
    }

}
