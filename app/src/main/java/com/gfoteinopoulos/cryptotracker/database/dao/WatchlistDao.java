package com.gfoteinopoulos.cryptotracker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.database.entity.Watchlist;
import com.gfoteinopoulos.cryptotracker.database.entity.WatchlistCoin;

import java.util.List;

@Dao
public interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWatchlist(Watchlist watchlist);

    @Update
    void updateWatchlist(Watchlist watchlist);

    @Delete
    void deleteWatchlist(Watchlist watchlist);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCoinToWatchlist(WatchlistCoin watchlistCoin);

    @Query("SELECT * FROM watchlist_coins WHERE watchlistId = :watchlistId")
    LiveData<List<WatchlistCoin>> getWatchlistCoinsRaw(int watchlistId);

    @Query("DELETE FROM watchlist_coins WHERE watchlistId = :watchlistId AND coinId = :coinId")
    void removeCoinFromWatchlist(int watchlistId, String coinId);

    @Query("SELECT * FROM watchlists ORDER BY createdAt DESC")
    LiveData<List<Watchlist>> getAllWatchlists();

    @Query("SELECT * FROM watchlists WHERE id = :watchlistId")
    LiveData<Watchlist> getWatchlistById(int watchlistId);

    @Query("SELECT coins.* FROM coins " +
            "INNER JOIN watchlist_coins ON coins.id = watchlist_coins.coinId " +
            "WHERE watchlist_coins.watchlistId = :watchlistId " +
            "ORDER BY watchlist_coins.addedAt DESC")
    LiveData<List<Coin>> getCoinsInWatchlist(int watchlistId);

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_coins WHERE watchlistId = :watchlistId AND coinId = :coinId)")
    LiveData<Boolean> isCoinInWatchlist(int watchlistId, String coinId);

}
