package com.gfoteinopoulos.cryptotracker.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "watchlist_coins",
        primaryKeys = {"watchlistId", "coinId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Watchlist.class,
                        parentColumns = "id",
                        childColumns = "watchlistId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Coin.class,
                        parentColumns = "id",
                        childColumns = "coinId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class WatchlistCoin {

    private int watchlistId;

    @NonNull
    private String coinId;
    private long addedAt;

    public WatchlistCoin(int watchlistId, @NonNull String coinId, long addedAt) {
        this.watchlistId = watchlistId;
        this.coinId = coinId;
        this.addedAt = addedAt;
    }

    public int getWatchlistId() {
        return watchlistId;
    }

    public void setWatchlistId(int watchlistId) {
        this.watchlistId = watchlistId;
    }

    @NonNull
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(@NonNull String coinId) {
        this.coinId = coinId;
    }

    public long getAddedAt() {
            return addedAt;
    }
    public void setAddedAt(long addedAt) {
            this.addedAt = addedAt;
    }
}
