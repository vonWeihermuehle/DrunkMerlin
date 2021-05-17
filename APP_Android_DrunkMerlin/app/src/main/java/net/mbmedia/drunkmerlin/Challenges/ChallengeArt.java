package net.mbmedia.drunkmerlin.Challenges;

public enum ChallengeArt {

    ALLGEMEIN(1, "Allgemein"),
    FRAGE(2, "Frage");


        private final int key;
        private final String text;

    ChallengeArt(int key, String text)
        {
            this.key = key;
            this.text = text;
        }


        @Override
        public String toString()
        {
            return text;
        }
    }

