package com.gfoteinopoulos.cryptotracker.ui.detail;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import coil.Coil;
import coil.request.ImageRequest;

import com.gfoteinopoulos.cryptotracker.api.model.CoinDetail;
import com.gfoteinopoulos.cryptotracker.database.entity.Watchlist;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.viewmodel.CoinDetailViewModel;

import java.util.List;

public class CoinDetailActivity extends AppCompatActivity {
    private CoinDetailViewModel viewModel;

    private TextView coinDescription;
    private TextView coinAth;
    private TextView coinAtl;

    private androidx.cardview.widget.CardView descriptionCard;
    private List<com.gfoteinopoulos.cryptotracker.database.entity.Watchlist> watchlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);

        String coinId = getIntent().getStringExtra("coin_id");
        android.util.Log.d("CryptoTracker_Debug", "CoinDetail coinId: " + coinId);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewModel = new ViewModelProvider(this).get(CoinDetailViewModel.class);

        if (coinId != null) {
            viewModel.setCoinId(coinId);

        }

        coinDescription = findViewById(R.id.coinDescription);
        descriptionCard = findViewById(R.id.descriptionCard);
        coinAth = findViewById(R.id.coinAth);
        coinAtl = findViewById(R.id.coinAtl);


        findViewById(R.id.infoButton).setOnClickListener(v -> {
            if (descriptionCard.getVisibility() == View.VISIBLE) {
                descriptionCard.setVisibility(View.GONE);
            } else {
                descriptionCard.setVisibility(View.VISIBLE);
            }
        });

        viewModel.fetchCoinDetail(coinId, new retrofit2.Callback<com.gfoteinopoulos.cryptotracker.api.model.CoinDetail>() {

                    @Override
                    public void onResponse(retrofit2.Call<com.gfoteinopoulos.cryptotracker.api.model.CoinDetail> call,
                                           retrofit2.Response<com.gfoteinopoulos.cryptotracker.api.model.CoinDetail> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            com.gfoteinopoulos.cryptotracker.api.model.CoinDetail detail = response.body();
                            if (detail.getDescription() != null &&
                                    detail.getDescription().getEn() != null &&
                                    !detail.getDescription().getEn().isEmpty()) {
                                String description = detail.getDescription().getEn();
                                description = description.replaceAll("<[^>]*>", "");
                                final String cleanDescription = description;
                                runOnUiThread(() -> coinDescription.setText(cleanDescription));
                            }
                            if (detail.getMarketData() != null) {
                                java.util.Map<String, Double> ath = detail.getMarketData().getAth();
                                java.util.Map<String, Double> atl = detail.getMarketData().getAtl();

                                if (ath != null && ath.containsKey("usd")) {
                                    runOnUiThread(() -> coinAth.setText(
                                            String.format("$%,.2f", ath.get("usd"))));
                                }

                                if (atl != null && atl.containsKey("usd")) {
                                    runOnUiThread(() -> coinAtl.setText(
                                            String.format("$%,.2f", atl.get("usd"))));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<com.gfoteinopoulos.cryptotracker.api.model.CoinDetail> call,
                                          Throwable t) {
                        android.util.Log.e("CryptoTracker", "Failed to fetch coin detail: " + t.getMessage());
                    }
                });

        viewModel.getAllWatchlists().observe(this, result -> {
            this.watchlists = result;
        });
        viewModel.getSelectedCoin().observe(this, coin -> {
            if (coin != null) {
                TextView coinName = findViewById(R.id.coinName);
                TextView coinPrice = findViewById(R.id.coinPrice);
                TextView coinChange = findViewById(R.id.coinChange);
                ImageView coinImage = findViewById(R.id.coinImage);
                TextView coinMarketCap = findViewById(R.id.coinMarketCap);
                TextView coinVolume = findViewById(R.id.coinVolume);
                TextView coinHigh = findViewById(R.id.coinHigh);
                TextView coinLow = findViewById(R.id.coinLow);

                coinName.setText(coin.getName());
                coinPrice.setText(String.format("$%,.2f", coin.getCurrentPrice()));
                coinMarketCap.setText(String.format("$%,.0f", coin.getMarketCap()));
                coinVolume.setText(String.format("$%,.0f", coin.getTotalVolume()));
                coinHigh.setText(String.format("$%,.2f", coin.getHigh24h()));
                coinLow.setText(String.format("$%,.2f", coin.getLow24h()));

                double change = coin.getPriceChangePercentage24h();
                String changeText = String.format("%.2f%%", change);
                if (change >= 0) {
                    coinChange.setText("+" + changeText);
                    coinChange.setTextColor(0XFF4CAF50);
                } else {
                    coinChange.setText(changeText);
                    coinChange.setTextColor(0XFFF44336);
                }

                ImageRequest request = new ImageRequest.Builder(this)
                        .data(coin.getImage())
                        .target(coinImage)
                        .build();
                Coil.imageLoader(this).enqueue(request);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAddToPortfolio);
        fab.setOnClickListener(v -> {
            if (coinId != null) {
                showAddToWatchlistDialog(coinId);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void showAddToWatchlistDialog(String coinId) {
        if (watchlists == null || watchlists.isEmpty()) {
            android.widget.Toast.makeText(this,
                    "No watchlists found. Create one first.",
                    android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[watchlists.size()];
        for (int i = 0; i < watchlists.size(); i++) {
            names[i] = watchlists.get(i).getName();
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Add to Watchlist")
                .setItems(names, (dialog, which) -> {
                    int watchlistId = watchlists.get(which).getId();

                    viewModel.addCoinToWatchlist(watchlistId, coinId);
                    android.widget.Toast.makeText(this,
                            "Added to " + names[which],
                            android.widget.Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
