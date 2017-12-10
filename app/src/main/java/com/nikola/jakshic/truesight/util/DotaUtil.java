package com.nikola.jakshic.truesight.util;


import android.content.Context;
import android.util.SparseArray;

import com.nikola.jakshic.truesight.R;

public class DotaUtil {


    public static class Image {
        private static final SparseArray<String> hero = new SparseArray<>();
        private static final SparseArray<String> item = new SparseArray<>();


        private static void initializeHeroes(Context context) {

            String[] heroes = context.getResources().getStringArray(R.array.heroes);

            for (String x : heroes) {
                String a[] = x.split("#");
                hero.put(Integer.valueOf(a[0]), a[1]);
            }
        }

        private static void initializeItems(Context context) {

            String[] items = context.getResources().getStringArray(R.array.items);

            for (String x : items) {
                String a[] = x.split("#");
                item.put(Integer.valueOf(a[0]), a[1]);
            }
        }

        public static String getHeroUrl(Context context, int id) {
            if (hero.size() == 0)
                initializeHeroes(context);
            return hero.get(id);
        }

        public static String getItemUrl(Context context, int id) {
            if (item.size() == 0)
                initializeItems(context);
            return item.get(id);
        }
    }

    public static class Match {

        private static final SparseArray<String> mode = new SparseArray<String>() {{
            append(0, "Unknown");
            append(1, "All Pick");
            append(2, "Captains Mode");
            append(3, "Random Draft");
            append(4, "Single Draft");
            append(5, "All Random");
            append(6, "Intro");
            append(7, "Diretide");
            append(8, "Rev Captains Mode");
            append(9, "Greeviling");
            append(10, "Tutorial");
            append(11, "Mid Only");
            append(12, "Least Played");
            append(13, "Limited Heroes");
            append(14, "Compendium Matchmaking");
            append(15, "Custom");
            append(16, "Captains Draft");
            append(17, "Balanced Draft");
            append(18, "Ability Draft");
            append(19, "Event");
            append(20, "All Random Deathmatch");
            append(21, "1v1 Mid");
            append(22, "All Draft");
            append(23, "Turbo");
        }};

        private static final SparseArray<String> lobby = new SparseArray<String>() {{
            append(0, "Normal");
            append(1, "Practice");
            append(2, "Tournament");
            append(3, "Tutorial");
            append(4, "Coop Bots");
            append(5, "Ranked Team MM");
            append(6, "Ranked Solo MM");
            append(7, "Ranked");
            append(8, "1v1 Mid");
            append(9, "Battle Cup");
        }};

        private static final SparseArray<String> skill = new SparseArray<String>() {{
            append(1, "Normal");
            append(2, "High");
            append(3, "Very High");
        }};

        public static String getLobby(int id, String defaultValue) {
            return lobby.get(id, defaultValue);
        }

        public static String getMode(int id, String defaultValue) {
            return mode.get(id, defaultValue);
        }

        public static String getSkill(int id, String defaultValue) {
            return skill.get(id, defaultValue);
        }
    }

}
