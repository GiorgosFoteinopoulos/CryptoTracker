package com.gfoteinopoulos.cryptotracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.database.entity.Portfolio;
import com.gfoteinopoulos.cryptotracker.database.entity.Watchlist;
import com.gfoteinopoulos.cryptotracker.repository.CoinRepository;

import java.util.List;

public class CoinDetailViewModel extends AndroidViewModel {

    private final CoinRepository repository;
    private final MutableLiveData<String> selectedCoinId;
    private LiveData<Coin> selectedCoin;
    private LiveData<Boolean> isCoinInPortfolio;
    private final LiveData<String> errorMessage;
    private final LiveData<Boolean> isLoading;

    public CoinDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new CoinRepository(application);
        selectedCoinId = new MutableLiveData<>();
        errorMessage = repository.getErrorMessage();
        isLoading = repository.getIsLoading();
    }

    public void setCoinId(String coinId) {
        selectedCoinId.setValue(coinId);
        selectedCoin = repository.getCoinById(coinId);
        isCoinInPortfolio = repository.isCoinInPortfolio(coinId);
    }

    public LiveData<Coin> getSelectedCoin() { return selectedCoin; }
    public LiveData<Boolean> getIsCoinInPortfolio() { return isCoinInPortfolio; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public LiveData<List<Watchlist>> getAllWatchlists() {
        return repository.getAllWatchlists();
    }
    public void addCoinToWatchlist(int watchlistId, String coinId) {
        repository.addCoinToWatchlist(watchlistId, coinId);
    }

    public void addToPortfolio(String coinId, double amount, double buyPrice) {
        repository.insertPortfolioEntry(coinId, amount, buyPrice);
    }

    public void removeFromPortfolio(Portfolio portfolio) {
        repository.deletePortfolioEntry(portfolio);
    }
}