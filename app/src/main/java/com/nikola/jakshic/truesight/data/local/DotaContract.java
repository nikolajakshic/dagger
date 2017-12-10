package com.nikola.jakshic.truesight.data.local;


import android.net.Uri;
import android.provider.BaseColumns;

public class DotaContract {

    public static final String CONTENT_AUTHORITY = "com.nikola.jakshic.truesight";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ACC = "account";

    public static class DotaSubscriber implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACC).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_ACC_ID = "acc_id";
        public static final String COLUMN_PLAYER_NAME = "name";
        public static final String COLUMN_AVATAR_URL = "avatar_url";

    }
}
