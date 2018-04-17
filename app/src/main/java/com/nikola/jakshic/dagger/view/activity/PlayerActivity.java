package com.nikola.jakshic.dagger.view.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.DaggerApp;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.databinding.ActivityDetailBinding;
import com.nikola.jakshic.dagger.inspector.PlayerInspector;
import com.nikola.jakshic.dagger.model.Player;
import com.nikola.jakshic.dagger.view.FollowDialog;
import com.nikola.jakshic.dagger.view.adapter.PlayerPagerAdapter;
import com.nikola.jakshic.dagger.viewModel.DetailViewModel;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//TODO REFAKTORIZUJ FRAGMENTE I REPOSITORIJUME MNOGO JE KODA ZAJEDNICKOG

public class PlayerActivity extends AppCompatActivity {

    private Player mPlayer;
    private Button mButtonFollow;
    private ActivityDetailBinding mBinding;
    @Inject
    OpenDotaService service;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((DaggerApp) getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mPlayer = new Player();
        mPlayer.setPersonaName(getIntent().getStringExtra("player-personaname"));
        mPlayer.setId(getIntent().getLongExtra("player-account-id", -1));
        mPlayer.setAvatarUrl(getIntent().getStringExtra("player-avatar-full"));
        DetailViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
        viewModel.checkPlayer(mPlayer.getId());

        PlayerInspector playerInspector = new PlayerInspector(this, mPlayer);
        mBinding.toolbarPlayer.setViewModel(playerInspector);
        mBinding.setViewModel(playerInspector);

        Disposable disposable = service.getPlayerProfile(mPlayer.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            if (TextUtils.isEmpty(mPlayer.getAvatarUrl())) {
                                Glide.with(this)
                                        .load(t.getProfile().getAvatarUrl())
                                        .into(mBinding.toolbarPlayer.imageView);

                                mPlayer.setAvatarUrl(t.getProfile().getAvatarUrl());
                            }
                        },
                        error -> {
                            Crashlytics.logException(error);
                        }
                );

        compositeDisposable.add(disposable);

        viewModel.fetchPlayerWinLoss(mPlayer.getId());
        viewModel.getPlayerWinLoss().observe(this, player -> {
            playerInspector.setPlayerWins(player == null ? 0 : player.getWins());
            playerInspector.setPlayerLosses(player == null ? 0 : player.getLosses());

        });

        viewModel.getPlayer().observe(this, players -> {

            mButtonFollow.setTextColor(
                    players == null
                            ? ContextCompat.getColor(PlayerActivity.this, android.R.color.white)
                            : ContextCompat.getColor(PlayerActivity.this, R.color.colorAccent));

            mButtonFollow.setText(
                    players == null
                            ? "Follow"
                            : "Unfollow");

            mButtonFollow.setBackground(
                    players == null
                            ? ContextCompat.getDrawable(PlayerActivity.this, R.drawable.button_toolbar_follow_inactive)
                            : ContextCompat.getDrawable(PlayerActivity.this, R.drawable.button_toolbar_follow_active));
        });

        mButtonFollow = findViewById(R.id.buttonToolbar);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mButtonFollow.setOnClickListener(v -> {
            if (viewModel.isFollowed()) {
                FollowDialog.newInstance(viewModel.new OnClickListener(mPlayer.getId())).show(getSupportFragmentManager(), "follow-dialog");
            } else {
                viewModel.insertPlayer(mPlayer);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tablayout);
        CollapsingToolbarLayout collapsingLayout = findViewById(R.id.collapsingLayout);
        TextView textView = findViewById(R.id.user);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new PlayerPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        AppBarLayout appBar = findViewById(R.id.appBar);
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) ->
        {
            if (collapsingLayout.getHeight() + verticalOffset <= collapsingLayout.getScrimVisibleHeightTrigger() - 80) {
                textView.animate().alpha(1).setDuration(300);
            } else {
                textView.animate().alpha(0).setDuration(300);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}