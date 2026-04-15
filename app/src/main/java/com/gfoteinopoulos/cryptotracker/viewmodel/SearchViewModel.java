package com.gfoteinopoulos.cryptotracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.gfoteinopoulos.cryptotracker.database.entity.Coin;
import com.gfoteinopoulos.cryptotracker.repository.CoinRepository;

import java.util.List;
public class SearchViewModel  extends AndroidViewModel{

    private final CoinRepository repository;
    private final MutableLiveData<String> searchQuery;
    private final LiveData<List<Coin>> searchResults;
    private final LiveData<String> errorMessage;
    private final LiveData<Boolean> isLoading;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new CoinRepository(application);
        errorMessage = repository.getErrorMessage();
        isLoading = repository.getIsLoading();
        searchQuery = new MutableLiveData<>("");

        searchResults = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.trim().isEmpty()) {
                return repository.getAllCoins();
            } else {
                return repository.searchCoins(query);
            }
        });
    }

    public LiveData<List<Coin>> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public String getCurrentQuery() {
        return searchQuery.getValue();
    }
}
