package net.kuludu.smartrecite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

interface ScreenStateListener {
    void onScreenOn();

    void onScreenOff();

    void onUnLock();
}

public class ScreenListener {
    private Context context;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenListener(Context context) {
        this.context = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    /* Listener begin Listening */
    public void begin(ScreenStateListener listener) {
        mScreenStateListener = listener;
        registerListener();
        getScreenState();
    }

    /* Require screen's state */
    private void getScreenState() {
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOn();
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOff();
            }
        }
    }

    /* Ongoing Listening  */
    public void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        context.registerReceiver(mScreenReceiver, filter);
    }

    /* Destroy listener */
    public void unregisterListener() {
        context.unregisterReceiver(mScreenReceiver);
    }

    class ScreenBroadcastReceiver extends BroadcastReceiver {
        /*
         * The BroadcastReceiver is for listening Screen's state
         * if Received message is SCREEN_ON,the get into MainActivity
         * else if Received message is SCREEN_OFF,change flag to true
         * else if Received message is UNLOCK,restore flag is false;
         */
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                mScreenStateListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mScreenStateListener.onScreenOff();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mScreenStateListener.onUnLock();
            }
        }
    }

}

