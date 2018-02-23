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
    int count=0;
    LinearLayout lay;
    ArrayList buttons;
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lay = findViewById(R.id.lay);
        tvInfo = findViewById(R.id.tvInfo);
        buttons = new ArrayList();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnplus) {
            Log.d("jop",view.getId()+"");
            myRunnable mr = new myRunnable(this, ++count);
            buttons.add(mr);
            lay.addView(mr);
        }
        if (view.getId()==R.id.btnminus) {
            int last = buttons.size()-1;
            if (last>=0) {
                myRunnable mr = (myRunnable) buttons.get(last);
                mr.onClick(null);
                lay.removeViewAt(last);
                buttons.remove(last);
            }
        }
        if (view.getId()==R.id.btnn) {
            tvInfo.setText(getSummaryValue());
        }

    }

    String getSummaryValue() {
        float res=0;
        for (Object o:buttons) {
            myRunnable mrr = (myRunnable) o;
            String vs = String.valueOf(mrr.getText());
            float vf= Float.valueOf(vs);
            res +=vf;
        }
        return buttons.size()+" "+ String.valueOf(res);
    }


}
