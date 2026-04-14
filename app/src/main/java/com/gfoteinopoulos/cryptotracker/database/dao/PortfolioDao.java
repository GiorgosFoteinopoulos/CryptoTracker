package com.gfoteinopoulos.cryptotracker.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gfoteinopoulos.cryptotracker.database.entity.Portfolio;

import java.util.List;

@Dao
public interface PortfolioDao {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insertPortfolioEntry(Portfolio portfolio);

    @Update
    void updatePortfolioEntry(Portfolio portfolio);

    @Delete
    void deletePortfolioEntry(Portfolio portfolio);

    @Query("SELECT * FROM portfolio ORDER BY purchasedAt DESC")
    LiveData<List<Portfolio>> getAllPortfolioEntries();

    @Query("SELECT * FROM portfolio WHERE coinId = :coinId")
    LiveData<List<Portfolio>> getPortfolioEntriesForCoin(String coinId);

    @Query("SELECT SUM(amountOwned * buyPrice) FROM portfolio")
    LiveData<Double> getTotalInvested();

    @Query("DELETE FROM portfolio WHERE coinId = :coinId")
    void deleteAllEntriesForCoin(String coinId);

    @Query("SELECT EXISTS(SELECT 1 FROM portfolio WHERE coinId = :coinId)")
    LiveData<Boolean> isCoinInPortfolio(String coinId);
}
