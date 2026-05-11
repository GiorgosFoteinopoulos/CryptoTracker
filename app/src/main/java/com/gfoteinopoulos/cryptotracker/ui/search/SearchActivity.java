package com.gfoteinopoulos.cryptotracker.ui.search;


import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.databinding.ActivitySearchBinding;
import com.gfoteinopoulos.cryptotracker.ui.market.CoinAdapter;
import com.gfoteinopoulos.cryptotracker.viewmodel.SearchViewModel;

public class SearchActivity extends AppCompatActivity implements CoinAdapter.OnCoinClickListener {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private CoinAdapter adapter;
    private boolean isGridView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViews();
        setupViewModel();
    }

    private void setupViews() {
        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Search");
        }
        binding.toolbar.setNavigationIconTint(0xFFFFFFFF);
        binding.toolbar.setTitleTextColor(0xFFFFFFFF);



        adapter = new CoinAdapter(this);
        adapter.setUseDarkHeader(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);


        binding.toggleViewButton.setOnClickListener(v -> toggleView());


        binding.filterButton.setOnClickListener(v -> showPriceFilterDialog());


        int searchTextId = binding.searchView.getContext().getResources()
                        .getIdentifier("android:id/search_src_text", null, null);
        android.widget.TextView searchText = binding.searchView.findViewById(searchTextId);
        if (searchText != null) {
            searchText.setHintTextColor(0xFFAAAAAA);
            searchText.setTextColor(0xFFFFFFFF);
        }
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
            int columns = calculateColumns();
            binding.recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
            adapter.setViewType(CoinAdapter.VIEW_TYPE_GRID);
        } else {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                adapter.submitListWithHeader("Results" , coins);
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