package net.mbmedia.drunkmerlin.Challenges;

import android.content.Context;
import android.content.Intent;

import net.mbmedia.drunkmerlin.Activity.ChallengeActivity;
import net.mbmedia.drunkmerlin.Activity.FragenActivity;
import net.mbmedia.drunkmerlin.Activity.KarteActivity;
import net.mbmedia.drunkmerlin.TO.Freund;

public class ChallengeContextMenu {

    public static final String Ichhabnochnie = "Ich hab noch nie ...";
    public static final String NAME_FLAG = "NAME_FLAG";
    public static final String FREUND_ID_FLAG = "FREUND_ID_FLAG";

    public static void startFrageActivity(Context context, Freund freund){
        Intent intent = new Intent(context, FragenActivity.class);
        intent.putExtra(FREUND_ID_FLAG, freund.getID());
        intent.putExtra(NAME_FLAG, freund.getName());
        context.startActivity(intent);
    }
}
