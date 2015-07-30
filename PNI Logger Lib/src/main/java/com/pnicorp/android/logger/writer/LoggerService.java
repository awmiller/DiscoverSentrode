package com.pnicorp.android.logger.writer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LoggerService extends Service {
    public LoggerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
