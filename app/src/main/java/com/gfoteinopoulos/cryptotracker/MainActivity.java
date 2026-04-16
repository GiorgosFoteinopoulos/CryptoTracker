package com.gfoteinopoulos.cryptotracker;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.ui.market.CoinAdapter;
import com.gfoteinopoulos.cryptotracker.ui.search.SearchActivity;
import com.gfoteinopoulos.cryptotracker.ui.watchlist.WatchlistActivity;
import com.gfoteinopoulos.cryptotracker.viewmodel.MarketViewModel;


public class MainActivity extends AppCompatActivity implements CoinAdapter.OnCoinClickListener {

    private MarketViewModel viewModel;
    private CoinAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView errorText;

    private LinearLayout emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupViewModel();
        setupTabs();
        setupBottomNavigation();
    }

    private void setupViews() {
        swipeRefresh = findViewById(R.id.swipeRefresh);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        emptyState = findViewById(R.id.emptyState);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new CoinAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new androidx.recyclerview.widget.DefaultItemAnimator());

        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.fetchTopCoins();
            swipeRefresh.setRefreshing(false);
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MarketViewModel.class);

        viewModel.getAllCoins().observe(this, coins -> {
            if (coins != null && !coins.isEmpty()) {
                adapter.submitList(coins);
                emptyState.setVisibility(View.GONE);
            } else {
                adapter.submitList(null);
                emptyState.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(error);
                emptyState.setVisibility(View.GONE);
            } else {
                errorText.setVisibility(View.GONE);
            }
        });
        viewModel.fetchTopCoins();
    }

    private void setupTabs() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("TOP 100"));
        tabLayout.addTab(tabLayout.newTab().setText("Gainers"));
        tabLayout.addTab(tabLayout.newTab().setText("Losers"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.resetAnimation();
                switch (tab.getPosition()) {
                    case 0:
                        viewModel.getAllCoins().observe(MainActivity.this,
                                coins -> adapter.submitList(coins));
                        break;
                    case 1:
                        viewModel.getTopGainers().observe(MainActivity.this,
                                coins -> adapter.submitList(coins));
                        break;
                    case 2:
                        viewModel.getTopLosers().observe(MainActivity.this,
                                coins -> adapter.submitList(coins));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_market);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.nav_watchlist) {
                startActivity(new Intent(this, WatchlistActivity.class));
                return true;
            }
            return true;

        });
        }
        @Override
                public void onCoinClick(Coin coin) {
                    Intent intent = new Intent(this, com.gfoteinopoulos.cryptotracker.ui.detail.CoinDetailActivity.class);
                    intent.putExtra("coin_id", coin.getId());
                    startActivity(intent);
        }
    }