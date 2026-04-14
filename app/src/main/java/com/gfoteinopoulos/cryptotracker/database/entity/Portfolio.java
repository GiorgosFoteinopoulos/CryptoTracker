package com.gfoteinopoulos.cryptotracker.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "portfolio",
        foreignKeys = {
                @ForeignKey(
                        entity = Coin.class,
                        parentColumns = "id",
                        childColumns = "coinId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)

public class Portfolio {

    @PrimaryKey(autoGenerate = true)
    private  int id;

    private String coinId;
    private double amountOwned;
    private double buyPrice;
    private long purchasedAt;

    public Portfolio(String coinId, double amountOwned, double buyPrice, long purchasedAt) {
        this.coinId = coinId;
        this.amountOwned = amountOwned;
        this.buyPrice = buyPrice;
        this.purchasedAt = purchasedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public double getAmountOwned() {
        return amountOwned;
    }

    public void setAmountOwned(double amountOwned) {
        this.amountOwned = amountOwned;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public long getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(long purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}
