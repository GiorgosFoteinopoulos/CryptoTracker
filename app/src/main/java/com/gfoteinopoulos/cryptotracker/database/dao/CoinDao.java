package com.gfoteinopoulos.cryptotracker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.gfoteinopoulos.cryptotracker.database.entity.Coin;

import java.util.List;

@Dao
public interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoins(List<Coin> coins);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoin(Coin coin);

    @Update
    void updateCoin(Coin coin);

    @Delete
    void deleteCoin(Coin coin);

    @Query("SELECT * FROM coins WHERE currentPrice >= :minPrice AND currentPrice <= :maxPrice ORDER BY marketCapRank ASC")
    LiveData<List<Coin>> getCoinsByPriceRange(double minPrice, double maxPrice);

    @Query("SELECT * FROM coins ORDER BY marketCapRank ASC")
    LiveData<List<Coin>> getAllCoins();

    @Query("SELECT * FROM coins WHERE id = :coinId")
    LiveData<Coin> getCoinById(String coinId);

    @Query("SELECT * FROM coins WHERE name LIKE :query OR symbol LIKE :query")
    LiveData<List<Coin>> searchCoins(String query);

    @Query("SELECT * FROM coins ORDER BY priceChangePercentage24h DESC")
    LiveData<List<Coin>> getTopGainers();

    @Query("SELECT * FROM coins ORDER BY priceChangePercentage24h ASC")
    LiveData<List<Coin>> getTopLosers();

    @Query("DELETE FROM coins")
    void deleteAllCoins();

    @Query("SELECT * FROM coins ORDER BY marketCapRank ASC LIMIT :limit")
    LiveData<List<Coin>> getTopCoins(int limit);
}
