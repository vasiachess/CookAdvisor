package com.vasiachess.cookadvisor;

import java.sql.Time;

/**
 * Created by Vasiliy on 28.03.2015.
 */
public class Utility {

   private static Time timeUntilFinish = null;

   public static int getIconResourceForTitle(String title) {

        if (title.equals("Pasta")) {
            return R.drawable.pasta;
        } else if (title.equals("Rice")) {
            return R.drawable.rice;
        } else if (title.equals("Egg")) {
            return R.drawable.egg;
        } else if (title.equals("Sausage")) {
            return R.drawable.sausage;
        }
            return R.drawable.default_icon;
   }

    public static String getTime(int timeInSec) {

//        Integer h = timeInSec / 3600;
//        Integer m = (timeInSec % 3600) / 60;
//        Integer s = timeInSec % 60;
//        String stringTime = h.toString() + " : " + m.toString() + " : "  + s.toString();
//        return stringTime;
        timeUntilFinish = new Time(timeInSec*1000);
        return timeUntilFinish.toString();

    }

}
