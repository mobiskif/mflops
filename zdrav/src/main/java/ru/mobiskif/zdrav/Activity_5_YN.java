package ru.mobiskif.zdrav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ru.healthy.R;

public class Activity_5_YN extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yesno);

        ((TextView) findViewById(R.id.textView)).setText(getIntent().getStringExtra("message"));
        findViewById(R.id.Yes).setOnClickListener(this);
        findViewById(R.id.No).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId()==R.id.Yes) {
            setResult(RESULT_OK, intent);
            intent.putExtra("result", getString(R.string.yes));
        }
        else {
            setResult(RESULT_CANCELED, intent);
            intent.putExtra("result", getString(R.string.no));
        }
        finish();
    }
}
