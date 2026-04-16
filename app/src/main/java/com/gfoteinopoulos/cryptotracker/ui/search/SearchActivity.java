package com.gfoteinopoulos.cryptotracker.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.ui.market.CoinAdapter;
import com.gfoteinopoulos.cryptotracker.viewmodel.SearchViewModel;
import com.google.android.material.appbar.MaterialToolbar;

public class SearchActivity extends AppCompatActivity implements CoinAdapter.OnCoinClickListener {
    private SearchViewModel viewModel;
    private CoinAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isGridView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
        setupViewModel();
    }

    private void setupViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Search");
        }

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CoinAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ImageButton toggleButton = findViewById(R.id.toggleViewButton);
        toggleButton.setOnClickListener(v -> toggleView());

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    private void toggleView() {
        isGridView = !isGridView;
        if (isGridView) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            adapter.setViewType(CoinAdapter.VIEW_TYPE_GRID);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter.setViewType(CoinAdapter.VIEW_TYPE_LIST);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.getSearchResults().observe(this, coins -> {
            if (coins != null) {
                adapter.submitList(coins);
            }
        });
    }

    @Override
    public void onCoinClick(Coin coin) {
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