package com.nikola.jakshic.truesight;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.nikola.jakshic.truesight.data.remote.OpenDotaClient;
import com.nikola.jakshic.truesight.model.Hero;
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

public class HeroRepository {
    private static final String LOG_TAG = HeroRepository.class.getSimpleName();

    public void findPlayer(MutableLiveData<List<Hero>> list, MutableLiveData<Boolean> loading, long id) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenDotaClient.BASE_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenDotaClient client = retrofit.create(OpenDotaClient.class);

        loading.setValue(true);
        client.getHeroes(id).enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {
                list.setValue(response.body());
                loading.setValue(false);
                Log.d(LOG_TAG, "OkHttp: RESPONSE" + response.errorBody());
            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {
                loading.setValue(false);

                Log.d(LOG_TAG, "OkHttp: FAILURE");
            }
        });
    }
}
