package com.gfoteinopoulos.cryptotracker.api;

import com.gfoteinopoulos.cryptotracker.api.model.CoinMarket;
import com.gfoteinopoulos.cryptotracker.api.model.CoinDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoinGeckoApi {

    @GET("coins/markets")
    Call<List<CoinMarket>> getTopCoins(
            @Query("vs_currency") String vsCurrency,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("page") int page,
            @Query("sparkline") boolean sparkline
    );

    @GET("coins/markets")
    Call<List<CoinMarket>> getTrendingCoins(
            @Query("vs_currency") String vsCurrency,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("page") int page,
            @Query("sparkline") boolean sparkline,
            @Query("price_change_percentage") String priceChangePercentage
    );

    @GET("coins/{id}")
    Call<CoinDetail> getCoinDetail(
            @Path("id") String coinId,
            @Query("localization") boolean localization,
            @Query("tickers") boolean tickers,
            @Query("market_data") boolean marketData,
            @Query("community_data") boolean communityData,
            @Query("developer_data") boolean developerData,
            @Query("sparkline") boolean sparkline
    );

    @GET("search")
    Call<List<CoinMarket>> searchCoins(
            @Query("query") String query
    );

}
