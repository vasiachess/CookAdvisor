package com.vasiachess.cookadvisor;


import java.util.HashMap;

/**
 * Created by Vasiliy on 28.03.2015.
 */
public class Utility {

   public static HashMap<String, Integer> timers = new HashMap<>();
   public static int id = 0;
   public static final String TITLE = "title";
   public static final String TIME = "time";
   public static final String TIME_UNTIL_FINISH = "time_until_finish";
   public static final String ADVICE = "advice";
   public final static String BROADCAST_ACTION = "com.vasiachess.cookadvisor.timerservicebackbroadcast";

   public static int getIconResourceForTitle(String title) {

        if (title.contains("Pasta")||title.contains("pasta")) {
            return R.drawable.pasta;
        } else if (title.contains("Rice")||title.contains("rice")) {
            return R.drawable.rice;
        } else if (title.contains("Egg")||title.contains("egg")) {
            return R.drawable.egg;
        } else if (title.contains("Sausage")||title.contains("sausage")) {
            return R.drawable.sausage;
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
