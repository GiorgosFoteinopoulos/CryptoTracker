package com.gfoteinopoulos.cryptotracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gfoteinopoulos.cryptotracker.database.dao.CoinDao;
import com.gfoteinopoulos.cryptotracker.database.dao.PortfolioDao;
import com.gfoteinopoulos.cryptotracker.database.dao.WatchlistDao;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.database.entity.Portfolio;
import com.gfoteinopoulos.cryptotracker.database.entity.Watchlist;
import com.gfoteinopoulos.cryptotracker.database.entity.WatchlistCoin;

@Database(
        entities = {Coin.class, Watchlist.class, WatchlistCoin.class, Portfolio.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public abstract CoinDao coinDao();
    public abstract WatchlistDao watchlistDao();
    public abstract PortfolioDao portfolioDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "cryptotracker_db")
                            .build();
                }
            }
        }
        return instance;
    }


}
