package com.gfoteinopoulos.cryptotracker.api.model;

import com.google.gson.annotations.SerializedName;

public class CoinMarket {

    @SerializedName("id")
    private String id;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("current_price")
    private double currentPrice;

    @SerializedName("market_cap")
    private double marketCap;

    @SerializedName("total_volume")
    private double totalVolume;

    @SerializedName("price_change_percentage_24h")
    private double priceChangePercentage24h;

    @SerializedName("high_24h")
    private double high24h;

    @SerializedName("low_24h")
    private double low24h;

    @SerializedName("last_updated")
    private String lastUpdated;

    @SerializedName("market_cap_rank")
    private int marketCapRank;

    @SerializedName("sparkline_in_7d")
    private SparklineData sparkline;

    public String getId() { return id;}
    public String getSymbol() {return symbol;}
    public String getName() {return name; }
    public String getImage() {return image; }
    public double getCurrentPrice() {return currentPrice; }
    public double getMarketCap() {return marketCap;}
    public double getTotalVolume() {return totalVolume;}
    public double getPriceChangePercentage24h() {return priceChangePercentage24h;}
    public double getHigh24h() {return high24h;}
    public double getLow24h() {return low24h;}
    public String getLastUpdated() {return lastUpdated;}
    public int getMarketCapRank() {return marketCapRank;}
    public SparklineData getSparkline() {return sparkline;}

    public static class SparklineData {
        @SerializedName("price")
        private java.util.List<Double> price;

        public java.util.List<Double> getPrice() {return price;}
    }
}
