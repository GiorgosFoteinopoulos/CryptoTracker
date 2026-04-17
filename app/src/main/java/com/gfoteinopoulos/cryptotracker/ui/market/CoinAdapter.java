package com.gfoteinopoulos.cryptotracker.ui.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.ObjectAnimator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gfoteinopoulos.cryptotracker.R;
import com.gfoteinopoulos.cryptotracker.database.entity.Coin;

import java.util.ArrayList;
import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class CoinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_COIN = 1;

    public static final int VIEW_TYPE_GRID = 2;

    private final OnCoinClickListener listener;
    private List<Object> items = new ArrayList<>();
    private int coinViewType = VIEW_TYPE_COIN;
    private int lastPosition = -1;

    public CoinAdapter (OnCoinClickListener listener) {
        this.listener = listener;
    }

    public interface OnCoinClickListener {
        void onCoinClick(Coin coin);
    }

    public void setViewType(int viewType) {
        this.coinViewType = viewType;
        notifyDataSetChanged();
    }

    public int getCurrentViewType() {
        return coinViewType;
    }

    public void resetAnimation() {
        lastPosition = -1;
    }

    public void submitList(List<Coin> coins) {
        items = new ArrayList<>(coins);
        notifyDataSetChanged();
    }

    public void submitListWithHeader(String header, List<Coin> coins) {
        items = new ArrayList<>();
        items.add(header);
        items.addAll(coins);
        notifyDataSetChanged();
    }

    public List<Coin> getCurrentList() {
        List<Coin> coins = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof Coin) {
                coins.add((Coin) item);
            }
        }
        return coins;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof  String) {
            return VIEW_TYPE_HEADER;
        }
        return coinViewType;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_GRID) {
            View view = inflater.inflate(R.layout.item_coin_grid, parent, false);
            return new CoinViewHolder(view, listener);
        } else {
            View view = inflater.inflate(R.layout.item_coin, parent, false);
            return new CoinViewHolder(view, listener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((String) items.get(position));
        } else if (holder instanceof CoinViewHolder) {
            ((CoinViewHolder) holder).bind((Coin) items.get(position));
            setAnimation(holder.itemView, position);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            ObjectAnimator animator = ObjectAnimator
                    .ofFloat(viewToAnimate, "alpha", 0f, 1f);
            animator.setDuration(400);
            animator.start();

           ObjectAnimator slideAnimator = ObjectAnimator
                    .ofFloat(viewToAnimate, "translationY", 50f, 0f);
            slideAnimator.setDuration(400);
            slideAnimator.start();

            lastPosition = position;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView headerText;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.headerText);
        }

        public void bind(String header) {
            headerText.setText(header);
        }
        }
        public static class CoinViewHolder extends RecyclerView.ViewHolder {

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