package com.gfoteinopoulos.cryptotracker.ui.watchlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.database.entity.Watchlist;

public class WatchlistAdapter extends ListAdapter<Watchlist, WatchlistAdapter.WatchlistViewHolder> {

    private final OnWatchlistClickListener listener;

    public interface OnWatchlistClickListener {
        void onWatchlistClick(Watchlist watchlist);
    }

    public WatchlistAdapter(OnWatchlistClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Watchlist> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Watchlist>() {
                @Override
                public boolean areItemsTheSame(@NonNull Watchlist oldItem,
                                               @NonNull Watchlist newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Watchlist oldItem,
                                                  @NonNull Watchlist newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }
            };

    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_watchlist, parent, false);
        return new WatchlistViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class WatchlistViewHolder extends RecyclerView.ViewHolder {

        private final TextView watchlistName;
        private final OnWatchlistClickListener listener;
        private Watchlist currentWatchlist;

        public WatchlistViewHolder(@NonNull View itemView,
                                   OnWatchlistClickListener listener) {
            super(itemView);
            this.listener = listener;
            watchlistName = itemView.findViewById(R.id.watchlistName);

            itemView.setOnClickListener(v -> {
                if (listener != null && currentWatchlist != null) {
                    listener.onWatchlistClick(currentWatchlist);
                }
            });
            itemView.setOnLongClickListener(v -> {
                return false;
            });
        }

        public void bind(Watchlist watchlist) {
            this.currentWatchlist = watchlist;
            watchlistName.setText(watchlist.getName());
        }
    }
}