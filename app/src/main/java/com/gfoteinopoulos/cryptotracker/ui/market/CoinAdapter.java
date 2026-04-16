package com.gfoteinopoulos.cryptotracker.ui.market;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;

import coil.Coil;
import coil.request.ImageRequest;

public class CoinAdapter extends ListAdapter<Coin, CoinAdapter.CoinViewHolder> {
    public static final int VIEW_TYPE_LIST = 0;
    public static final int VIEW_TYPE_GRID = 1;

    private final OnCoinClickListener listener;
    private int viewType = VIEW_TYPE_LIST;

    public interface OnCoinClickListener {
        void onCoinClick(Coin coin);
    }

    public CoinAdapter(OnCoinClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    public int getCurrentViewType() {
        return viewType;
    }

    private static final DiffUtil.ItemCallback<Coin> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Coin>() {
        @Override
        public boolean areItemsTheSame(@NonNull Coin oldItem,
                                               @NonNull Coin newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
            return oldItem.getCurrentPrice() == newItem.getCurrentPrice()
                    && oldItem.getPriceChangePercentage24h()
                    == newItem.getPriceChangePercentage24h();
                }

    };
    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_GRID) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_coin_grid, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_coin, parent, false);
        }
        return new CoinViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        holder.bind(getItem(position));
        setAnimation(holder.itemView, position);
    }
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            android.animation.ObjectAnimator animator = android.animation.ObjectAnimator
                    .ofFloat(viewToAnimate, "alpha", 0f, 1f);
            animator.setDuration(400);
            animator.start();

            android.animation.ObjectAnimator slideAnimator = android.animation.ObjectAnimator
                    .ofFloat(viewToAnimate, "translationY", 50f, 0f);
            slideAnimator.setDuration(400);
            slideAnimator.start();

            lastPosition = position;
        }
    }

    public void resetAnimation() {
        lastPosition = -1;
    }
    public static class CoinViewHolder extends RecyclerView.ViewHolder {
        private final TextView coinName;
        private final TextView coinSymbol;
        private final TextView coinPrice;
        private final TextView coinChange;
        private final ImageView coinImage;
        private final OnCoinClickListener listener;
        private Coin currentCoin;

        public CoinViewHolder(@NonNull View itemView, OnCoinClickListener listener) {
            super(itemView);
            this.listener = listener;
            coinName = itemView.findViewById(R.id.coinName);
            coinSymbol = itemView.findViewById(R.id.coinSymbol);
            coinPrice = itemView.findViewById(R.id.coinPrice);
            coinChange = itemView.findViewById(R.id.coinChange);
            coinImage = itemView.findViewById(R.id.coinImage);

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
                coinChange.setTextColor(0xFF1B8A4C);
            } else {
                coinChange.setText(changeText);
                coinChange.setTextColor(0xFFBA1A1A);
            }

            ImageRequest request = new ImageRequest.Builder(context)
                    .data(coin.getImage())
                    .target(coinImage)
                    .build();
            Coil.imageLoader(context).enqueue(request);
        }

    }
    }
