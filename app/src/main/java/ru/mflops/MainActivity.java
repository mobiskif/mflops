package ru.mflops;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MainActivity extends Activity implements View.OnClickListener{
    LinearLayout lay;
    ArrayList buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lay = findViewById(R.id.lay);
        buttons = new ArrayList();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnplus) {
            myRunnable mr = new myRunnable(this);
            buttons.add(mr);
            lay.addView(mr);
        }
        if (view.getId()==R.id.btnminus) {
            if (buttons.size() > 0) {
                lay.removeViewAt(buttons.size()-1);
                buttons.remove(buttons.size()-1);
            }
        }
        if (view.getId()==R.id.btnn) {
            ((TextView) findViewById(R.id.tvInfo)).setText(getSummaryValue());
        }

    }

    String getSummaryValue() {
        float res=0;
        for (Object o:buttons) {
            myRunnable mr = (myRunnable) o;
            String vs = String.valueOf(mr.getText());
            res +=Float.valueOf(vs);
        }
        return buttons.size()+" "+ String.valueOf(res);
    }


}
