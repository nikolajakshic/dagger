package com.nikola.jakshic.truesight;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.nikola.jakshic.truesight.data.remote.OpenDotaService;
import com.nikola.jakshic.truesight.model.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRepository {

    private static final String LOG_TAG = SearchRepository.class.getSimpleName();

    public void fetchPlayers(MutableLiveData<List<Player>> list, MutableLiveData<Boolean> loading, String name) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenDotaService.BASE_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenDotaService client = retrofit.create(OpenDotaService.class);

        loading.setValue(true);
        client.searchPlayers(name).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                list.setValue(response.body());
                loading.setValue(false);
                Log.d(LOG_TAG, "OkHttp: RESPONSE" + response.errorBody());
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                loading.setValue(false);

                Log.d(LOG_TAG, "OkHttp: FAILURE");
            }
        });
    }
}