package com.nikola.jakshic.truesight.view.activity;

import android.app.AlertDialog;
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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.nikola.jakshic.truesight.FollowDialog;
import com.nikola.jakshic.truesight.R;
import com.nikola.jakshic.truesight.Singletons;
import com.nikola.jakshic.truesight.databinding.ActivityDetailBinding;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.view.adapter.DetailPagerAdapter;
import com.nikola.jakshic.truesight.DetailViewModel;
import com.nikola.jakshic.truesight.viewModel.PlayerViewModel;

public class DetailActivity extends AppCompatActivity {

    private Player mPlayer;
    private Button mButtonFollow;
    private ActivityDetailBinding mBinding;
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mPlayer = getIntent().getParcelableExtra("player-parcelable");
        DetailViewModel viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.checkPlayer(mPlayer.getId());

        viewModel.getPlayer().observe(this, players -> {

            mButtonFollow.setTextColor(
                    players == null
                            ? ContextCompat.getColor(DetailActivity.this, android.R.color.white)
                            : ContextCompat.getColor(DetailActivity.this, R.color.colorAccent));

            mButtonFollow.setText(
                    players == null
                            ? "Follow"
                            : "Unfollow");

            mButtonFollow.setBackground(
                    players == null
                            ? ContextCompat.getDrawable(DetailActivity.this, R.drawable.button_toolbar_follow_inactive)
                            : ContextCompat.getDrawable(DetailActivity.this, R.drawable.button_toolbar_follow_active));
        });

        mBinding.toolbarPlayer.setViewModel(new PlayerViewModel(this, mPlayer));
        mBinding.setViewModel(new PlayerViewModel(this, mPlayer));
        mButtonFollow = findViewById(R.id.buttonToolbar);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mButtonFollow.setOnClickListener(v -> {
            if (viewModel.isFollowed()) {
                FollowDialog.newInstance(mPlayer).show(getSupportFragmentManager(), "follow-dialog");
            } else {
                Singletons.getDb(this).playerDao().insertPlayer(mPlayer);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tablayout);
        CollapsingToolbarLayout collapsingLayout = findViewById(R.id.collapsingLayout);
        TextView textView = findViewById(R.id.user);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new DetailPagerAdapter(getSupportFragmentManager()));
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