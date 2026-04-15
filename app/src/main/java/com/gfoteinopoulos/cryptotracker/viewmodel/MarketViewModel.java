package com.gfoteinopoulos.cryptotracker.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleController;
import androidx.lifecycle.LiveData;

import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.repository.CoinRepository;

import java.util.List;
public class MarketViewModel extends AndroidViewModel{

    private final CoinRepository repository;
    private final LiveData<List<Coin>> allcoins;
    private final LiveData<List<Coin>> topGainers;
    private final LiveData<List<Coin>> topLosers;
    private final LiveData<String> errorMessage;
    private final LiveData<Boolean> isLoading;

    public MarketViewModel(@NonNull Application application) {
        super(application);
        repository = new CoinRepository(application);
        allcoins = repository.getAllCoins();
        topGainers = repository.getTopGainers();
        topLosers = repository.getTopLosers();
        errorMessage = repository.getErrorMessage();
        isLoading = repository.getIsLoading();
    }

    public LiveData<List<Coin>> getAllCoins() {
        return allcoins;
    }

    public LiveData<List<Coin>> getTopGainers() {
        return topGainers;
    }

    public LiveData<List<Coin>> getTopLosers() {
        return topLosers;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchTopCoins() {
        repository.fetchTopCoins();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
