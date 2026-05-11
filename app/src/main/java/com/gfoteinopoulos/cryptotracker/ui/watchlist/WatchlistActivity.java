package com.gfoteinopoulos.cryptotracker.ui.watchlist;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gfoteinopoulos.cryptotracker.databinding.ActivityWatchlistBinding;
import com.gfoteinopoulos.cryptotracker.viewmodel.WatchlistViewModel;

public class WatchlistActivity extends AppCompatActivity {

    private ActivityWatchlistBinding binding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewModel();
        setupViews();

    }

    private void setupViews() {

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationIconTint(0xFFFFFFFF);
        binding.toolbar.setTitleTextColor(0xFFFFFFFF);


        adapter = new WatchlistAdapter(watchlist -> {
            WatchlistDetailActivity.start(this, watchlist.getId(), watchlist.getName());
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                com.gfoteinopoulos.cryptotracker.database.entity.Watchlist watchlist =
                        adapter.getCurrentList().get(position);
                viewModel.deleteWatchlist(watchlist);
                android.widget.Toast.makeText(WatchlistActivity.this,
                        watchlist.getName() + " deleted",
                        Toast.LENGTH_SHORT).show();
            }
        };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerView);


        binding.fab.setOnClickListener(v -> showCreateWatchlistDialog());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        viewModel.getAllWatchlists().observe(this, watchlists -> {
            if (watchlists != null) {
                adapter.submitList(watchlists);
            }
        });
    }

    private void showCreateWatchlistDialog() {
        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(this);
        builder.setTitle("Create Watchlist");

        android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Watchlist name");
        builder.setView(input);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                viewModel.createWatchlist(name);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}