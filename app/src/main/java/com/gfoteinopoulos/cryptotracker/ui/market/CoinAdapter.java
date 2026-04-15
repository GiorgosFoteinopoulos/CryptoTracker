package com.gfoteinopoulos.cryptotracker.ui.market;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import coil.Coil;
import coil.request.ImageRequest;

import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;

public class CoinAdapter extends  ListAdapter<Coin, CoinAdapter.CoinViewHolder> {
    private final OnCoinClickListener listener;

    public interface OnCoinClickListener {
        void onCoinClick(Coin coin);
    }

    public CoinAdapter(OnCoinClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Coin> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Coin>() {
        @Override
                public boolean areItemsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
        @Override
                public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
            return oldItem.getCurrentPrice() == newItem.getCurrentPrice()
                    && oldItem.getPriceChangePercentage24h()
                    == newItem.getPriceChangePercentage24h();
        }
            };
    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coin, parent, false);
        return new CoinViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
    public static class CoinViewHolder extends  RecyclerView.ViewHolder {

        private final ImageView coinImage;
        private final TextView coinName;
        private final TextView coinSymbol;
        private final TextView coinPrice;
        private final TextView coinChange;
        private final OnCoinClickListener listener;
        private Coin currentCoin;
        public CoinViewHolder(@NonNull View itemView, OnCoinClickListener listener) {
            super(itemView);
            this.listener = listener;
            coinImage = itemView.findViewById(R.id.coinImage);
            coinName = itemView.findViewById(R.id.coinName);
            coinSymbol = itemView.findViewById(R.id.coinSymbol);
            coinPrice = itemView.findViewById(R.id.coinPrice);
            coinChange = itemView.findViewById(R.id.coinChange);

            itemView.setOnClickListener(v -> {
                if (listener != null && currentCoin != null) {
                    listener.onCoinClick(currentCoin);
                }
        });
    }
    public void bind(Coin coin) {
        this.currentCoin = coin;
        Context context = itemView.getContext();

        coinName.setText(coin.getName());
        coinSymbol.setText(coin.getSymbol().toUpperCase());
        coinPrice.setText(String.format("$%,.2f", coin.getCurrentPrice()));

        double change = coin.getPriceChangePercentage24h();
        String changeText = String.format("%.2f%%", change);

        if (change >= 0) {
            coinChange.setText("+" + changeText);
            coinChange.setTextColor(0xFF4CAF50);
        } else {
            coinChange.setText(changeText);
            coinChange.setTextColor(0xFFF44336);
        }
        ImageRequest request = new ImageRequest.Builder(context)
                .data(coin.getImage())
                .target(coinImage)
                .build();
        Coil.imageLoader(context).enqueue(request);
        }
    }

}
