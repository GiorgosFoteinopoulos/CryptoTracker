package com.gfoteinopoulos.cryptotracker.ui.watchlist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.ui.market.CoinAdapter;
import com.gfoteinopoulos.cryptotracker.viewmodel.WatchlistViewModel;
import com.google.android.material.appbar.MaterialToolbar;

public class WatchlistDetailActivity extends AppCompatActivity implements CoinAdapter.OnCoinClickListener {

    private static final String EXTRA_WATCHLIST_ID = "watchlist_id";
    private static final String EXTRA_WATCHLIST_NAME = "watchlist_name";

    private WatchlistViewModel viewModel;
    private CoinAdapter adapter;
    private int watchlistId;

    public static void start(Context context, int watchlistId, String name) {
        Intent intent = new Intent(context, WatchlistDetailActivity.class);
        intent.putExtra(EXTRA_WATCHLIST_ID, watchlistId);
        intent.putExtra(EXTRA_WATCHLIST_NAME, name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist_detail);

        watchlistId = getIntent().getIntExtra(EXTRA_WATCHLIST_ID, -1);
        String watchlistName = getIntent().getStringExtra(EXTRA_WATCHLIST_NAME);
        android.util.Log.d("CryptoTracker_Debug",
                "WatchlistDetail opened with watchlistId: " + watchlistId + " name: " + watchlistName);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(watchlistName);
        }
        toolbar.setNavigationIconTint(0xFFFFFFFF);
        toolbar.setTitleTextColor(0xFFFFFFFF);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new CoinAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);

        if (watchlistId != -1) {
            viewModel.getCoinsInWatchlist(watchlistId).observe(this, coins -> {
                if (coins != null && !coins.isEmpty()) {
                    adapter.submitList(new java.util.ArrayList<>(coins));
                    findViewById(R.id.emptyText).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.emptyText).setVisibility(View.VISIBLE);
                }
            });

            viewModel.getWatchlistCoinsRaw(watchlistId).observe(this, raw -> {
                if (raw != null) {
                    for (com.gfoteinopoulos.cryptotracker.database.entity.WatchlistCoin wc : raw) {
                        android.util.Log.d("CryptoTracker_Debug",
                                "WatchlistCoin: watchlistId=" + wc.getWatchlistId()
                                        + " coinId=" + wc.getCoinId());
                    }
                }
            });
        }
    }
    @Override
    public void onCoinClick(
            com.gfoteinopoulos.cryptotracker.database.entity.Coin coin) {
        Intent intent = new Intent(this,
                com.gfoteinopoulos.cryptotracker.ui.detail.CoinDetailActivity.class);
        intent.putExtra("coin_id", coin.getId());
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
