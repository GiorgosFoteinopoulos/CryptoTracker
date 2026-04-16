package com.gfoteinopoulos.cryptotracker.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.util.List;

@Entity(tableName = "Coins")
public class Coin {

    @PrimaryKey
    @NonNull
    private String id;

    private String symbol;
    private String name;
    private String image;
    private double currentPrice;
    private double marketCap;
    private double totalVolume;
    private double priceChangePercentage24h;
    private int marketCapRank;
    private double high24h;
    private double low24h;
    private long lastUpdated;

    private List<Double> sparklineData;

    public Coin(@NonNull String id, String symbol, String name, String image, double currentPrice, double marketCap, double totalVolume, double priceChangePercentage24h, int marketCapRank, double high24h, double low24h, long lastUpdated) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.image = image;
        this.currentPrice = currentPrice;
        this.marketCap = marketCap;
        this.totalVolume = totalVolume;
        this.priceChangePercentage24h = priceChangePercentage24h;
        this.marketCapRank = marketCapRank;
        this.high24h = high24h;
        this.low24h = low24h;
        this.lastUpdated = lastUpdated;
    }

    @NonNull
    public String getId() {
        return id;

    }
    public List<Double> getSparklineData() {
        return sparklineData;
    }
    public void setSparklineData(List<Double> sparklineData) {
        this.sparklineData = sparklineData;
    }


    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public int getMarketCapRank() {
        return marketCapRank;
    }

    public double getHigh24h() {
        return high24h;
    }

    public double getLow24h() {
        return low24h;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public void setPriceChangePercentage24h(double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public void setMarketCapRank(int marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public void setHigh24h(double high24h) {
        this.high24h = high24h;
    }

    public void setLow24h(double low24h) {
        this.low24h = low24h;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
