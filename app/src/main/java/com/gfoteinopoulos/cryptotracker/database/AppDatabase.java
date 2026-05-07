package com.gfoteinopoulos.cryptotracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gfoteinopoulos.cryptotracker.database.converter.Converters;
import com.gfoteinopoulos.cryptotracker.database.dao.CoinDao;
import com.gfoteinopoulos.cryptotracker.database.dao.PortfolioDao;
import com.gfoteinopoulos.cryptotracker.database.dao.WatchlistDao;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.database.entity.Portfolio;
import com.gfoteinopoulos.cryptotracker.database.entity.Watchlist;
import com.gfoteinopoulos.cryptotracker.database.entity.WatchlistCoin;
import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;
@androidx.room.TypeConverters(Converters.class)
@Database(
        entities = {Coin.class, Watchlist.class, WatchlistCoin.class, Portfolio.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public abstract CoinDao coinDao();
    public abstract WatchlistDao watchlistDao();
    public abstract PortfolioDao portfolioDao();

    private static final androidx.room.migration.Migration MIGRATION_1_2 =
            new androidx.room.migration.Migration(1, 2)  {
                @Override
                public void migrate(@NonNull androidx.sqlite.db.SupportSQLiteDatabase database) {
                    database.execSQL("CREATE TABLE IF NOT EXISTS 'watchlist_coins_new' " +
                            "('watchlistId' INTEGER NOT NULL, " +
                            "'coinId' TEXT NOT NULL, " +
                            "'addedAt' INTEGER NOT NULL, " +
                            "PRIMARY KEY('watchlistId', 'coinId'), " +
                            "FOREIGN KEY(`watchlistId`) REFERENCES `watchlists`(`id`) " +
                            "ON DELETE CASCADE)");
                    database.execSQL("INSERT INTO 'watchlist_coins_new' " +
                            "SELECT * FROM 'watchlist_coins'");
                    database.execSQL("DROP TABLE 'watchlist_coins'");
                    database.execSQL("ALTER TABLE 'watchlist_coins_new' RENAME TO 'watchlist_coins'");

                }
            };

    private static final androidx.room.migration.Migration MIGRATION_2_3 =
            new androidx.room.migration.Migration(2, 3)  {
        @Override
                public void migrate(@NonNull androidx.sqlite.db.SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'coins' " +
                    "ADD COLUMN 'sparklineData' TEXT");
        }
            };

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            "cryptotracker_db"
                    )
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }
        return instance;
    }


}
