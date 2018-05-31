package ru.mobiskif.spb;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;

public class myRunnable extends Button implements Runnable {
    private float result = 0F;
    private final Handler handler;

    public myRunnable(Context c) {
        super(c);
        setText("==");

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                setText(String.format("%.2f", result));
            }
        };

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            long t1 = System.currentTimeMillis();
            for (long i = 0; i < 1000000; i++) {result = 6.345F * 3.1415F;}
            long t2 = System.currentTimeMillis();
            result = 1000/(t2-t1);
            handler.sendEmptyMessage(0);
        }
    }
}
