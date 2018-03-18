package ru.mflops;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        //mAdapter = new Adapter_StringArray(getResources().getStringArray(R.array.areas));
        RecyclerView.Adapter mAdapter = new Adapter_FireBase(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {
    }


}
