package com.zamani.mindwaveremote;

import android.util.Log;
import android.widget.Toast;

public class AttentionLooperThread extends Thread {
    private static final String TAG = "AttentionLooperThread";
    boolean isReady;

    public AttentionLooperThread(boolean isReady) {
        this.isReady = isReady;
    }

    @Override
    public void run() {
        if (!isReady) {
            Log.d(TAG, "run: Sleep for 3 seconds");
        }
    }
}
