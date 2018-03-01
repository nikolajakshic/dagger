package com.nikola.jakshic.truesight;

import com.nikola.jakshic.truesight.model.Hero;

import java.util.Comparator;

public final class HeroComparator {

    private HeroComparator() {
    }

    public static class ByGames implements Comparator<Hero> {

        @Override
        public int compare(Hero o1, Hero o2) {
            if (o1.getGamesPlayed() > o2.getGamesPlayed())
                return -1;
            if (o1.getGamesPlayed() < o2.getGamesPlayed())
                return 1;
            return 0;
        }
    }

    public static class ByWinRate implements Comparator<Hero> {

        @Override
        public int compare(Hero o1, Hero o2) {
            float winRate1 = calculateWinRate(o1);
            float winRate2 = calculateWinRate(o2);

            if (winRate1 > winRate2)
                return -1;
            if (winRate1 < winRate2)
                return 1;
            return 0;
        }

        private float calculateWinRate(Hero hero) {
            float games = hero.getGamesPlayed();
            if (games == 0) return 0;
            float win = hero.getGamesWon();
            return (win / games) * 100.00f;
        }
    }

    public static class ByWins implements Comparator<Hero> {

        @Override
        public int compare(Hero o1, Hero o2) {
            if (o1.getGamesWon() > o2.getGamesWon())
                return -1;
            if (o1.getGamesWon() < o2.getGamesWon())
                return 1;
            return 0;
        }
    }

    public static class ByLosses implements Comparator<Hero> {

        @Override
        public int compare(Hero o1, Hero o2) {
            if ((o1.getGamesPlayed() - o1.getGamesWon()) > (o2.getGamesPlayed() - o2.getGamesWon()))
                return -1;
            if ((o1.getGamesPlayed() - o1.getGamesWon()) < (o2.getGamesPlayed() - o2.getGamesWon()))
                return 1;
            return 0;
        }
    }
}