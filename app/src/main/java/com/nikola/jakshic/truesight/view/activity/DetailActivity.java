package com.nikola.jakshic.truesight.view.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
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
import com.nikola.jakshic.truesight.data.local.DotaContract.DotaSubscriber;
import com.nikola.jakshic.truesight.databinding.ActivityDetailBinding;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.view.adapter.DetailPagerAdapter;
import com.nikola.jakshic.truesight.viewModel.PlayerViewModel;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;
    private Player mPlayer;
    private Button mButtonFollow;
    private ActivityDetailBinding mBinding;
    private boolean mSubscribed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mPlayer = getIntent().getParcelableExtra("player-parcelable");
        mUri = ContentUris.withAppendedId(DotaSubscriber.CONTENT_URI, mPlayer.getID());
        mBinding.toolbarPlayer.setViewModel(new PlayerViewModel(this, mPlayer));
        mBinding.setViewModel(new PlayerViewModel(this, mPlayer));
        mButtonFollow = findViewById(R.id.buttonToolbar);
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getLoaderManager().initLoader(0, null, this);

        if (!mSubscribed) {
            mButtonFollow.setTextColor(ContextCompat.getColor(DetailActivity.this, R.color.colorAccent));
            mButtonFollow.setText("Follow");
            mButtonFollow.setBackground(ContextCompat.getDrawable(DetailActivity.this, R.drawable.button_toolbar_follow_active));
        } else {
            mButtonFollow.setTextColor(ContextCompat.getColor(DetailActivity.this, android.R.color.white));
            mButtonFollow.setText("Unfollow");
            mButtonFollow.setBackground(ContextCompat.getDrawable(DetailActivity.this, R.drawable.button_toolbar_follow_inactive));
        }

        mButtonFollow.setOnClickListener(v -> {
            if (mSubscribed) {
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to unfollow?")
                        .setPositiveButton("Confirm", (dialog, which) -> getContentResolver().delete(mUri, null, null)
                        )
                        .setNegativeButton("Cancel", (dialog, which) -> {
                        })
                        .show();
            } else {
                ContentValues values = new ContentValues();
                values.put(DotaSubscriber.COLUMN_ACC_ID, mPlayer.getID());
                values.put(DotaSubscriber.COLUMN_AVATAR_URL, mPlayer.getAvatarUrl());
                values.put(DotaSubscriber.COLUMN_PLAYER_NAME, mPlayer.getName());
                getContentResolver().insert(DotaSubscriber.CONTENT_URI, values);
            }
        });

       /* CircleImageView imageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load("https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/53/533866fc625699714e3d2871774541db789419cc_full.jpg")
                .into(imageView);*/
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ContentUris.withAppendedId(DotaSubscriber.CONTENT_URI, mPlayer.getID());
        return new CursorLoader(
                this,
                uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
            mSubscribed = true;
            mButtonFollow.setTextColor(ContextCompat.getColor(DetailActivity.this, android.R.color.white));
            mButtonFollow.setText("Unfollow");
            mButtonFollow.setBackground(ContextCompat.getDrawable(DetailActivity.this, R.drawable.button_toolbar_follow_inactive));
        } else {
            mSubscribed = false;
            mButtonFollow.setTextColor(ContextCompat.getColor(DetailActivity.this, R.color.colorAccent));
            mButtonFollow.setText("Follow");
            mButtonFollow.setBackground(ContextCompat.getDrawable(DetailActivity.this, R.drawable.button_toolbar_follow_active));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
