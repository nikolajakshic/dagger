package com.nikola.jakshic.truesight.view.activity;

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
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.TrueSightApp;
import com.nikola.jakshic.truesight.databinding.ActivityDetailBinding;
import com.nikola.jakshic.truesight.inspector.PlayerInspector;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.view.FollowDialog;
import com.nikola.jakshic.truesight.view.adapter.PlayerPagerAdapter;
import com.nikola.jakshic.truesight.viewModel.DetailViewModel;

import javax.inject.Inject;

//TODO REFAKTORIZUJ FRAGMENTE I REPOSITORIJUME MNOGO JE KODA ZAJEDNICKOG

public class PlayerActivity extends AppCompatActivity {

    private Player mPlayer;
    private Button mButtonFollow;
    private ActivityDetailBinding mBinding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private static final String LOG_TAG = PlayerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TrueSightApp) getApplication()).getAppComponent().inject(this);
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
}