package com.vasiachess.cookadvisor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Vasiliy on 27.03.2015.
 */
public class TimerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
