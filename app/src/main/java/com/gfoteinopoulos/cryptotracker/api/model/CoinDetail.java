package com.gfoteinopoulos.cryptotracker.api.model;

import com.google.gson.annotations.SerializedName;

public class CoinDetail {

    @SerializedName("id")
    private String id;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private Description description;


    @SerializedName("image")
    private Image image;

    @SerializedName("market_cap_rank")
    private int marketCapRank;

    @SerializedName("market_data")
    private MarketData marketData;

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public int getMarketCapRank() {
        return marketCapRank;
    }

    public MarketData getMarketData() {
        return marketData;
    }

    public static class Description {
        @SerializedName("en")
        private String en;

        public String getEn() {
            return en;
        }
    }

        public static class Image {
            @SerializedName("thumb")
            private String thumb;
            @SerializedName("small")
            private String small;
            @SerializedName("large")
            private String large;

            public String getThumb() {
                return thumb;
            }

            public String getSmall() {
                return small;
            }

            public String getLarge() {
                return large;
            }
        }
            public static class MarketData {
                @SerializedName("current_price")
                private java.util.Map<String, Double> currentPrice;

                @SerializedName("market_cap")
                private java.util.Map<String, Double> marketCap;

                @SerializedName("total_volume")
                private java.util.Map<String, Double> totalVolume;

                @SerializedName("high_24h")
                private java.util.Map<String, Double> high24h;

                @SerializedName("low_24h")
                private java.util.Map<String, Double> low24h;

                @SerializedName("price_change_percentage_24h")
                private double priceChangePercentage24h;

                @SerializedName("price_change_percentage_7d")
                private double priceChangePercentage7d;

                @SerializedName("price_change_percentage_30d")
                private double priceChangePercentage30d;

                @SerializedName("circulating_supply")
                private double circulatingSupply;

                @SerializedName("total_supply")
                private double totalSupply;

                @SerializedName("ath")
                private java.util.Map<String, Double> ath;

                @SerializedName("atl")
                private java.util.Map<String, Double> atl;

                public java.util.Map<String, Double> getCurrentPrice() {
                    return currentPrice;
                }

                public java.util.Map<String, Double> getMarketCap() {
                    return marketCap;
                }

                public java.util.Map<String, Double> getTotalVolume() {
                    return totalVolume;
                }
                public java.util.Map<String, Double> getHigh24h() {
                    return high24h;
                }
                public java.util.Map<String, Double> getLow24h() {
                    return low24h;
                }
                public double getPriceChangePercentage24h() {
                    return priceChangePercentage24h;
                }
                public double getPriceChangePercentage7d() {
                    return priceChangePercentage7d;
                }
                public double getPriceChangePercentage30d() {
                    return priceChangePercentage30d;
                }
                public double getCirculatingSupply() {
                    return circulatingSupply;
                }
                public double getTotalSupply() {
                    return totalSupply;
                }

                public java.util.Map<String, Double> getAth() {
                    return ath;
                }
                public java.util.Map<String, Double> getAtl() {
                    return atl;
                }



            }
}




