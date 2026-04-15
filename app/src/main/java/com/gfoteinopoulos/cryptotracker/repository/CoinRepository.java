package com.gfoteinopoulos.cryptotracker.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gfoteinopoulos.cryptotracker.api.ApiClient;
import com.gfoteinopoulos.cryptotracker.api.CoinGeckoApi;
import com.gfoteinopoulos.cryptotracker.api.model.CoinDetail;
import com.gfoteinopoulos.cryptotracker.api.model.CoinMarket;
import com.gfoteinopoulos.cryptotracker.database.AppDatabase;
import com.gfoteinopoulos.cryptotracker.database.dao.CoinDao;
import com.gfoteinopoulos.cryptotracker.database.dao.PortfolioDao;
import com.gfoteinopoulos.cryptotracker.database.dao.WatchlistDao;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.database.entity.Portfolio;
import com.gfoteinopoulos.cryptotracker.database.entity.Watchlist;
import com.gfoteinopoulos.cryptotracker.database.entity.WatchlistCoin;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CoinRepository {
    private final CoinDao coinDao;

    private final WatchlistDao watchlistDao;
    private final PortfolioDao portfolioDao;
    private final CoinGeckoApi api;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isLoading;

    public CoinRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        coinDao = database.coinDao();
        watchlistDao = database.watchlistDao();
        portfolioDao = database.portfolioDao();
        api = ApiClient.getInstance(context).getCoinGeckoApi();
        errorMessage = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<Coin>> getAllCoins() {
        return coinDao.getAllCoins();
    }

    public LiveData<Coin> getCoinById(String coinId) {
        return coinDao.getCoinById(coinId);
    }

    public LiveData<List<Coin>> searchCoins(String query) {
        return coinDao.searchCoins("%" + query + "%");
    }

    public LiveData<List<Coin>> getTopGainers() {
        return coinDao.getTopGainers();
    }

    public LiveData<List<Coin>> getTopLosers() {
        return coinDao.getTopLosers();
    }

    public void fetchTopCoins() {
        isLoading.setValue(true);
        api.getTopCoins("usd", "market_cap_desc", 100, 1, true)
                .enqueue(new Callback<List<CoinMarket>>() {
                    @Override
                    public void onResponse(Call<List<CoinMarket>> call,
                                           Response<List<CoinMarket>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Coin> coins = convertToCoinEntities(response.body());
                            AsyncTask.execute(() -> coinDao.insertCoins(coins));
                            ;
                        } else {
                            errorMessage.setValue("Failed to fetch coins: " + response.code());
                        }
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onFailure(Call<List<CoinMarket>> call, Throwable t) {
                        errorMessage.setValue("Network error: " + t.getMessage());
                        isLoading.setValue(false);
                    }
                });
    }

    private List<Coin> convertToCoinEntities(List<CoinMarket> coinMarkets) {
        List<Coin> coins = new ArrayList<>();
        for (CoinMarket market : coinMarkets) {
            Coin coin = new Coin(
                    market.getId(),
                    market.getSymbol(),
                    market.getName(),
                    market.getImage(),
                    market.getCurrentPrice(),
                    market.getMarketCap(),
                    market.getTotalVolume(),
                    market.getPriceChangePercentage24h(),
                    market.getMarketCapRank(),
                    market.getHigh24h(),
                    market.getLow24h(),
                    System.currentTimeMillis()
            );
            coins.add(coin);
        }
        return coins;

    }

    public LiveData<List<Watchlist>> getAllWatchlists() {
        return watchlistDao.getAllWatchlists();
    }

    public LiveData<List<Coin>> getCoinsInWatchlist(int watchlistId) {
        return watchlistDao.getCoinsInWatchlist(watchlistId);
    }

    public LiveData<Boolean> isCoinInWatchlist(int watchlistId, String coinId) {
        return watchlistDao.isCoinInWatchlist(watchlistId, coinId);
    }

    public void insertWatchlist(String name) {
        Watchlist watchlist = new Watchlist(name, System.currentTimeMillis());
        AsyncTask.execute(() -> watchlistDao.insertWatchlist(watchlist));

    }

    public void deleteWatchlist(Watchlist watchlist) {
        AsyncTask.execute(() -> watchlistDao.deleteWatchlist(watchlist));
    }

    public void addCoinToWatchlist(int watchlistId, String coinId) {
        WatchlistCoin watchlistCoin = new WatchlistCoin(watchlistId, coinId, System.currentTimeMillis());
        AsyncTask.execute(() ->
                watchlistDao.addCoinToWatchlist(watchlistCoin));;
    }

    public void removeCoinFromWatchlist(int watchlistId,String coinId) {
        AsyncTask.execute(() -> watchlistDao.removeCoinFromWatchlist(watchlistId, coinId));
    }

    public LiveData<List<Portfolio>> getAllPortfolioEntries() {
        return portfolioDao.getAllPortfolioEntries();
    }

    public void insertPortfolioEntry(String coinId, double amount, double buyPrice) {
        Portfolio portfolio = new Portfolio(
                coinId, amount, buyPrice, System.currentTimeMillis());
        AsyncTask.execute(() -> portfolioDao.insertPortfolioEntry(portfolio));

    }

    public void deletePortfolioEntry(Portfolio portfolio) {
        AsyncTask.execute(() -> portfolioDao.deletePortfolioEntry(portfolio));
    }
    public LiveData<Boolean> isCoinInPortfolio(String coinId) {
        return portfolioDao.isCoinInPortfolio(coinId);
    }
    }


