package com.nikola.jakshic.truesight.comparator;

import com.nikola.jakshic.truesight.model.Peer;

import java.util.Comparator;

public class PeerComparator {

    public static class ByGames implements Comparator<Peer> {

        @Override
        public int compare(Peer o1, Peer o2) {
            if (o1.getWithGames() > o2.getWithGames())
                return -1;
            if (o1.getWithGames() < o2.getWithGames())
                return 1;
            return 0;
        }
    }

    public static class ByWinRate implements Comparator<Peer> {

        @Override
        public int compare(Peer o1, Peer o2) {
            float winRate1 = calculateWinRate(o1);
            float winRate2 = calculateWinRate(o2);

            if (winRate1 > winRate2)
                return -1;
            if (winRate1 < winRate2)
                return 1;
            return 0;
        }

        private float calculateWinRate(Peer peer) {
            float games = peer.getWithGames();
            if (games == 0) return 0;
            float win = peer.getWithWin();
            return (win / games) * 100.00f;
        }
    }
}