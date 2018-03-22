package com.nikola.jakshic.dagger.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nikola.jakshic.dagger.data.local.CompetitiveDao;
import com.nikola.jakshic.dagger.data.local.DotaDatabase;
import com.nikola.jakshic.dagger.data.local.PeerDao;
import com.nikola.jakshic.dagger.data.local.PlayerDao;
import com.nikola.jakshic.dagger.data.local.SearchHistoryDao;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// TODO move network, database to separate Module
@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    OpenDotaService provideOpenDotaService(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(OpenDotaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(OpenDotaService.class);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    @Singleton
    DotaDatabase provideDotaDatabase() {
        return Room.databaseBuilder(context, DotaDatabase.class, "truesightdota.db").build();
    }

    @Provides
    @Singleton
    PlayerDao providePlayerDao(DotaDatabase db) {
        return db.playerDao();
    }

    @Provides
    @Singleton
    CompetitiveDao provideCompetitiveDao(DotaDatabase db) {
        return db.competitiveDao();
    }

    @Provides
    @Singleton
    SearchHistoryDao provideSearchHistoryDao(DotaDatabase db) {
        return db.searchHistoryDao();
    }

    @Provides
    @Singleton
    PeerDao providePeerDao(DotaDatabase db) {
        return db.peerDao();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .cache(new Cache(context.getCacheDir(), 10 * 1000 * 1000))
                .readTimeout(35, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
    }
}