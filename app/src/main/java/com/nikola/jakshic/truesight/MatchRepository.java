package com.nikola.jakshic.truesight;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.nikola.jakshic.truesight.data.remote.OpenDotaClient;
import com.nikola.jakshic.truesight.model.Hero;
import com.nikola.jakshic.truesight.model.Match;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MatchRepository {

    private static final String LOG_TAG = MatchRepository.class.getSimpleName();

    public void fetchMatches(MutableLiveData<List<Match>> list, MutableLiveData<Boolean> loading, long id) {
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
        client.getMatches(id).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                list.setValue(response.body());
                loading.setValue(false);
                Log.d(LOG_TAG, "OkHttp: RESPONSE" + response.errorBody());
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                loading.setValue(false);

                Log.d(LOG_TAG, "OkHttp: FAILURE");
            }
        });
    }
}
