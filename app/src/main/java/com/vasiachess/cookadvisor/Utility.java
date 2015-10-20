package com.vasiachess.cookadvisor;


import android.content.Context;

import java.util.HashMap;

/**
 * Created by Vasiliy on 28.03.2015.
 */
public class Utility {

   public static HashMap<String, Integer> timers = new HashMap<>();
   public static boolean twoPane;
   public static int id = 0;
   public static final String TITLE = "title";
   public static final String TIME = "time";
   public static final String TIME_UNTIL_FINISH = "time_until_finish";
   public static final String ADVICE = "advice";
   public static final String APPLICATION_ID = "56179c3027c1bf2200000000";
   public final static String BROADCAST_ACTION = "com.vasiachess.cookadvisor.timerservicebackbroadcast";

   public static int getIconResourceForTitle(Context ctx, String title) {

       title = title.toLowerCase();

        if (title.contains(ctx.getResources().getString(R.string.pasta).toLowerCase())) {
            return R.drawable.pasta;
        } else if (title.contains(ctx.getResources().getString(R.string.rice).toLowerCase())) {
            return R.drawable.rice;
        } else if (title.contains(ctx.getResources().getString(R.string.egg).toLowerCase())) {
            return R.drawable.egg;
        } else if (title.contains(ctx.getResources().getString(R.string.sausage).toLowerCase())) {
            return R.drawable.sausage;
        } else if (title.contains(ctx.getResources().getString(R.string.corn).toLowerCase())) {
            return R.drawable.corn;
        } else if (title.contains(ctx.getResources().getString(R.string.potato).toLowerCase())) {
            return R.drawable.potato;
        } else if (title.contains(ctx.getResources().getString(R.string.carrot).toLowerCase())) {
            return R.drawable.carrot;
        } else if (title.contains(ctx.getResources().getString(R.string.chicken).toLowerCase())) {
            return R.drawable.chicken;
        } else if (title.contains(ctx.getResources().getString(R.string.fish).toLowerCase())) {
            return R.drawable.fish;
        } else if (title.contains(ctx.getResources().getString(R.string.oat).toLowerCase())) {
            return R.drawable.porridge;
        } else if (title.contains(ctx.getResources().getString(R.string.peas).toLowerCase())) {
            return R.drawable.peas;
        } else if (title.contains(ctx.getResources().getString(R.string.beans).toLowerCase())) {
            return R.drawable.beans;
        } else if (title.contains(ctx.getResources().getString(R.string.beet).toLowerCase())) {
            return R.drawable.beet;
        } else if (title.contains(ctx.getResources().getString(R.string.buckwheat).toLowerCase())) {
            return R.drawable.buckwheat;
        }
            return R.drawable.default_icon;
   }

    public static String getTime(int timeInSec) {
            Integer h = timeInSec / 3600;
            Integer m = (timeInSec % 3600) / 60;
            Integer s = timeInSec % 60;
            String stringTime = String.format("%02d:%02d:%02d", h, m, s);
        return stringTime;
    }

    public static void setItemCurrentTime(String title, int currentTime ) {
        timers.put(title, currentTime);
    }

    public static void removeCurrentTimer(String title) {
        timers.remove(title);
    }

}
