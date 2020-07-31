package com.zlodeuscomp.vdolgah.screens.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zlodeuscomp.vdolgah.App;
import com.zlodeuscomp.vdolgah.model.Debtor;

import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<Debtor>> debtorLiveData = App.getInstance().getDebtorDao().getAllLiveData();

    public LiveData<List<Debtor>> getDebtorLiveData() {
        return debtorLiveData;
    }
}
