package com.gfoteinopoulos.cryptotracker.ui.search;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
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
import com.google.android.material.button.MaterialButton;

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

        MaterialButton toggleButton = findViewById(R.id.toggleViewButton);
        toggleButton.setOnClickListener(v -> toggleView());

        MaterialButton filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> showPriceFilterDialog());

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
        MaterialButton toggleButton = findViewById(R.id.toggleViewButton);
        if (isGridView) {
            int columns = calculateColumns();
            recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
            adapter.setViewType(CoinAdapter.VIEW_TYPE_GRID);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter.setViewType(CoinAdapter.VIEW_TYPE_COIN);
        }
    }

    private int calculateColumns() {
        android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int) (dpWidth / 180);
        return Math.max(2, columns);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.getSearchResults().observe(this, coins-> {
            if (coins != null) {
                adapter.submitListWithHeader("Search Results" , coins);
            }
        });
    }

    private void showPriceFilterDialog() {
        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(this);
        builder.setTitle("Filter by Price");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 24);

        android.widget.EditText minPriceInput = new android.widget.EditText(this);
        minPriceInput.setHint("Min Price (USD)");
        minPriceInput.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER |
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(minPriceInput);
        android.widget.EditText maxPriceInput = new android.widget.EditText(this);
        maxPriceInput.setHint("Max Price (USD)");
        maxPriceInput.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER |
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(maxPriceInput);

        builder.setView(layout);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            String minStr = minPriceInput.getText().toString().trim();
            String maxStr = maxPriceInput.getText().toString().trim();

            double min = minStr.isEmpty() ? 0 : Double.parseDouble(minStr);
            double max = maxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxStr);

            viewModel.getCoinsByPriceRange(min, max).observe(this, coins -> {
                if (coins != null) {
                    adapter.submitListWithHeader("Filtered Results", coins);
                }
            });
        });
        builder.setNegativeButton("Clear", (dialog, which) -> {
            viewModel.setSearchQuery("");
        });
        builder.show();
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