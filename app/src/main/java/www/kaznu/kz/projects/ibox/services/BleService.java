package www.kaznu.kz.projects.ibox.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class BleService extends Service {

    private final IBinder mBinder = new LocalBinder();


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public BleService getService() {
            return BleService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}