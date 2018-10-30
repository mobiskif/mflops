package ru.mobiskif.geo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityCustom extends AppCompatActivity implements View.OnClickListener {
    String action = "";
    String prev_action="";
    Class nextClass = null;
    RecyclerView recyclerView;
    ProgressBar progressView;
    @LayoutRes int contentView;
    String TAG=getClass().getSimpleName()+" jop";

    public ActivityCustom() {
        contentView = R.layout.activity_custom;
    }

    boolean isLogged() {
        if (
                Storage.restore(this, "Surname").toString().length()>1 &&
                        Storage.restore(this, "Name").toString().length()>1 &&
                        Storage.restore(this, "Birthday").toString().length()>6
                ) return true;
        else return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!isLogged()) startActivity(new Intent(this, ActivityChangeUser.class));
        else {
            ((TextView) findViewById(R.id.textViewInfo)).setText(Storage.restore(this, prev_action + "_info"));
            recyclerView = findViewById(R.id.recyclerview);
            progressView = findViewById(R.id.progressView);

            int spanCount = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 2 : 1;
            if (recyclerView != null)
                recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

            startAdapter(action);
        }
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Hello Snackbar!",
                        Snackbar.LENGTH_LONG).show();
            }
        });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.getItem(3).setEnabled(action.equals("GetLPUList"));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle()!=null) {
            if (item.getTitle().equals("Выйти")) finishAffinity();
            else if (item.getTitle().equals("Выбрать пациента")) startActivity(new Intent(this, ActivityChangeUser.class));
            else if (item.getTitle().equals("Выбрать район")) startActivity(new Intent(this, ActivityDistrict.class));
            else if (item.getTitle().equals("Как это работает?")) startActivity(new Intent(this, ActivityHelp.class));
            else if (item.getTitle().equals("Карта")) startActivity(new Intent(this, ActivityMap.class));
        }
        //Storage.store(this, "action_info", "");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.card_view) {
            Storage.store(this, action, ((TextView) v.findViewById(R.id.textView0)).getText().toString());
            Storage.store(this, action+"_info", ((TextView) v.findViewById(R.id.textView2)).getText().toString());
            if (nextClass!=null) startActivity(new Intent(this, nextClass));
            else finish();
            /*
             if (v.getId() == R.id.imageViewInfo) {
            nextClass = ActivityMap.class;
            CardView cv = (CardView) v.getParent().getParent().getParent().getParent().getParent();
            super.onClick(cv);
            }
            */
        }
    }

    void callback(DataAdapter dataAdapter) {
        progressView.setVisibility(View.GONE);
        if (recyclerView!=null) recyclerView.setAdapter(dataAdapter.getCardAdapter());
    }

    void startAdapter(String action) {
        new DataAdapter(this, action);
    }
}
