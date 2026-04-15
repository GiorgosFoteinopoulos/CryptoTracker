package com.gfoteinopoulos.cryptotracker.ui.detail;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import coil.Coil;
import coil.request.ImageRequest;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.viewmodel.CoinDetailViewModel;

public class CoinDetailActivity extends AppCompatActivity {

    private CoinDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);

        String coinId = getIntent().getStringExtra("coin_id");

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(CoinDetailViewModel.class);

        if (coinId != null) {
            viewModel.setCoinId(coinId);
        }

        viewModel.getSelectedCoin().observe(this, coin -> {
            if (coin != null) {
                TextView coinName = findViewById(R.id.coinName);
                TextView coinPrice = findViewById(R.id.coinPrice);
                TextView coinChange = findViewById(R.id.coinChange);
                TextView coinMarketCap = findViewById(R.id.coinMarketCap);
                TextView coinVolume = findViewById(R.id.coinVolume);
                TextView coinHigh = findViewById(R.id.coinHigh);
                TextView coinLow = findViewById(R.id.coinLow);
                ImageView coinImage = findViewById(R.id.coinImage);

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
                    coinChange.setTextColor(
                            android.graphics.Color.parseColor("#4CAF50"));
                } else {
                    coinChange.setText(changeText);
                    coinChange.setTextColor(
                            android.graphics.Color.parseColor("#F44336"));
                }

                ImageRequest request = new ImageRequest.Builder(this)
                        .data(coin.getImage())
                        .target(coinImage)
                        .build();
                Coil.imageLoader(this).enqueue(request);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (coinId != null) {
                viewModel.addToPortfolio(coinId, 1.0, 0.0);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}