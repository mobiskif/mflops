package ru.mflops;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;

public class myRunnable extends Button implements Runnable {
    float result = 0F;
    Handler handler;
    boolean isrun=true;

    public myRunnable(Context c) {
        super(c);
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                setText(String.valueOf(result));
            }
        };
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (isrun) {
            long t1 = System.currentTimeMillis();
            for (long i = 0; i < 1000000; i++) {float x =6.345F*3.1415F;}
            long t2 = System.currentTimeMillis();
            float time = t2-t1;
            //result = String.format("%.2f", value);
            result = 1000/time;
            handler.sendEmptyMessage(0);
        }
    }
    public void stop() {
        isrun=false;
    }
}
