package com.gfoteinopoulos.cryptotracker.ui.watchlist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.viewmodel.WatchlistViewModel;

public class WatchlistActivity extends AppCompatActivity {

    private WatchlistViewModel viewModel;
    private WatchlistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);

        setupViews();
        setupViewModel();
    }

    private void setupViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new WatchlistAdapter(watchlist -> {
            android.widget.Toast.makeText(this, watchlist.getName(), android.widget.Toast.LENGTH_SHORT).show();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showCreateWatchlistDialog());
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