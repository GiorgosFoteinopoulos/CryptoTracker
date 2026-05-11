package com.gfoteinopoulos.cryptotracker;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gfoteinopoulos.cryptotracker.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.ui.market.CoinAdapter;
import com.gfoteinopoulos.cryptotracker.ui.search.SearchActivity;
import com.gfoteinopoulos.cryptotracker.ui.watchlist.WatchlistActivity;
import com.gfoteinopoulos.cryptotracker.viewmodel.MarketViewModel;



public class MainActivity extends AppCompatActivity implements CoinAdapter.OnCoinClickListener {

    private ActivityMainBinding binding;
    private MarketViewModel viewModel;
    private CoinAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViews();
        setupViewModel();
        setupTabs();
        setupBottomNavigation();
    }

    private void setupViews() {
            adapter = new CoinAdapter(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setItemAnimator(new androidx.recyclerview.widget.DefaultItemAnimator());

        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.fetchTopCoins();
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MarketViewModel.class);

        viewModel.getAllCoins().observe(this, coins -> {
            if (coins != null && !coins.isEmpty()) {
                adapter.submitListWithHeader("Top Cryptocurrencies", coins);
                binding.emptyState.setVisibility(View.GONE);
            } else {
                adapter.submitList(null);
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                binding.errorText.setVisibility(View.VISIBLE);
                binding.errorText.setText(error);
                binding.emptyState.setVisibility(View.GONE);
            } else {
                binding.errorText.setVisibility(View.GONE);
            }
        });
        viewModel.fetchTopCoins();
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("TOP 100"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Gainers"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Losers"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.resetAnimation();
                switch (tab.getPosition()) {
                    case 0:
                        viewModel.getAllCoins().observe(MainActivity.this,
                                coins -> adapter.submitListWithHeader("Top Cryptocurrencies",coins));
                        break;
                    case 1:
                        viewModel.getTopGainers().observe(MainActivity.this,
                                coins -> adapter.submitListWithHeader("Top Gainers",coins));
                        break;
                    case 2:
                        viewModel.getTopLosers().observe(MainActivity.this,
                                coins -> adapter.submitListWithHeader("Top Losers",coins));
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

        binding.bottomNavigation.setSelectedItemId(R.id.nav_market);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
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