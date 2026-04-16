package com.gfoteinopoulos.cryptotracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.database.entity.Watchlist;
import com.gfoteinopoulos.cryptotracker.repository.CoinRepository;

import java.util.List;

public class WatchlistViewModel extends  AndroidViewModel {

    private final CoinRepository repository;
    private final LiveData<List<Watchlist>> allWatchlists;
    private final LiveData<String> errorMessage;
    private final LiveData<Boolean> isLoading;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        repository = new CoinRepository(application);
        allWatchlists = repository.getAllWatchlists();
        errorMessage = repository.getErrorMessage();
        isLoading = repository.getIsLoading();
    }

    public LiveData<List<Watchlist>> getAllWatchlists() {
        return allWatchlists;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;

    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<List<Coin>> getCoinsInWatchlist(int watchlistId) {
        return repository.getCoinsInWatchlist(watchlistId);
    }

    public LiveData<List<com.gfoteinopoulos.cryptotracker.database.entity.WatchlistCoin>> getWatchlistCoinsRaw(int watchlistId) {
        return repository.getWatchlistCoinsRaw(watchlistId);
    }

    public LiveData<Boolean> isCoinInWatchlist(int watchlistId, String coinId) {
        return repository.isCoinInWatchlist(watchlistId, coinId);
    }

    public void createWatchlist(String name) {
        repository.insertWatchlist(name);
    }

    public void deleteWatchlist(Watchlist watchlist) {
        repository.deleteWatchlist(watchlist);
    }

    public void addCoinToWatchlist(int watchlistId, String coinId) {
        repository.addCoinToWatchlist(watchlistId, coinId);
    }
    public void removeCoinFromWatchlist(int watchlistId, String coinId) {
        repository.removeCoinFromWatchlist(watchlistId, coinId);
    }
}
