package net.kalp.reflectr.setup;

import androidx.annotation.DrawableRes;

import net.kalp.reflectr.R;
import net.kalp.reflectr.models.Mood;

public class DefaultMoods {
    public static Mood Amazing = new Mood(R.drawable.emoticons7, "Amazing", 0, 5);
    public static Mood good = new Mood(R.drawable.emoticons12, "Good", 0, 4);
    public static Mood meh = new Mood(R.drawable.emoticons18, "Meh", 0, 3);
    public static Mood sad = new Mood(R.drawable.emoticons8, "Sad", 0, 2);
    public static Mood awful = new Mood(R.drawable.emoticons1, "Awful", 0, 1);
    public static Mood[] moods = {Amazing, good, meh, sad, awful};

    public @DrawableRes Integer getImage(Mood mood) {
        return mood.getEmoji();
    }
}
