package ru.mobiskif.zdrav;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import static java.lang.Math.random;

public class ActivityBase extends AppCompatActivity implements  AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnTouchListener {
    int content_view = R.layout.activity_base;
    int spinner_pos = 0;
    String label1_text = "*";
    String label2_text = "*";
    String label3_text = "*";
    String textview_text = "*";
    String text_text = "*";
    String button_text = "*";
    String spinner_arr = "GetDistrictList";
    String card_arr = "ca";
    String list_arr = "GetLPUList";
    boolean error=false;
    String TAG=getClass().getSimpleName()+" jop";
    int REQUEST_CODE_YN = 555;
    int REQUEST_CODE_HELP = 666;
    int REQUEST_CODE_LSD = 222;
    int REQUEST_CODE_UA = 0;
    int REQUEST_CODE_DRT = 333;

    void config_ToolbarAndMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_6_help.class));
                //Snackbar.make(v, "Нажата FloatingActionButton", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    void set_Visiblity() {
        findViewById(R.id.label1).setVisibility(View.VISIBLE);
        findViewById(R.id.label2).setVisibility(View.GONE);
        findViewById(R.id.label3).setVisibility(View.GONE);
        findViewById(R.id.text).setVisibility(View.GONE);
        findViewById(R.id.recycler).setVisibility(View.GONE);
        findViewById(R.id.tv).setVisibility(View.GONE);

        findViewById(R.id.textview).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.tv).setOnClickListener(this);

        findViewById(R.id.spinner).setOnTouchListener(this);
        findViewById(R.id.list).setOnTouchListener(this);

    }

    void attach_Adapters() {
        int rand;
        final Spinner spinner = findViewById(R.id.spinner);
        if (spinner.getVisibility()==View.VISIBLE) {
            spinner.setOnItemSelectedListener(this);
            rand = (int) (random()*10000);
            DataAdapter spinner_adapter = new DataAdapter (this, rand, spinner_arr);
            spinner.setAdapter(spinner_adapter);

            //только когда собственные данные готовы (асинхронно !!!)
            //можно восстанавливать сохраненную позицию
            //связанный список обновится далее по onItemSelection()
            spinner_adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    spinner.setSelection(spinner_pos);
                }
            });
        }

        final ListView listView = findViewById(R.id.list);
        if (listView.getVisibility()==View.VISIBLE) {
            listView.setOnItemClickListener(this);
            rand = (int) (random()*10000);
            DataAdapter list_adapter = new DataAdapter(this, rand, list_arr);
            listView.setAdapter(list_adapter);
            //list_adapter.update();
        }

        RecyclerView mRecyclerView = findViewById(R.id.recycler);
        if (mRecyclerView.getVisibility()==View.VISIBLE) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            CardAdapter card_adapter = new CardAdapter(card_arr, this, button_text);
            mRecyclerView.setAdapter(card_adapter);
        }
    }

    void restore_Values() {
        label2_text = Storage.restore(this, "CheckPatient");
        label1_text = Storage.restore(this, spinner_arr+"_str");
        textview_text = Storage.restore(this, spinner_arr+"_str");
        text_text = Storage.restore(this, "FIO"); if (text_text.length()<2) text_text="";
        spinner_pos = Integer.valueOf(Storage.restore(this, spinner_arr+"_pos"));
    }

    void show_Values() {
        ((TextView) findViewById(R.id.label1)).setText(label1_text);
        ((TextView) findViewById(R.id.label2)).setText(label2_text);
        ((TextView) findViewById(R.id.label3)).setText(label3_text);
        ((TextView) findViewById(R.id.text)).setText(text_text);
        ((TextView) findViewById(R.id.textview)).setText(textview_text);
        ((Button) findViewById(R.id.button)).setText(button_text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(this, "onCreate()" + " " + this.getLocalClassName(), Toast.LENGTH_LONG).show();

        setContentView(content_view);
        config_ToolbarAndMenu();

        set_Visiblity();
        attach_Adapters();
        restore_Values();
        show_Values();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        //Toast.makeText(this, "onStart()" + " " + this.getLocalClassName(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        //Toast.makeText(this, "onResume()" + " " + this.getLocalClassName(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        //Toast.makeText(this, "onPause()" + " " + this.getLocalClassName(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        //Toast.makeText(this, "onStop()" + " " + this.getLocalClassName(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        //Toast.makeText(this, "onDestroy()" + " " + this.getLocalClassName(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "onActivityResult()" + " " + this.getLocalClassName() + " " + data.getStringExtra("result") + " request=" + requestCode + " result=" + resultCode, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataAdapter adapter = (DataAdapter) parent.getAdapter();
        if (adapter.loaded) {
            String[] row = (String[]) adapter.getItem(position);
            Storage.store(this, list_arr, row[0]);
            Storage.store(this, list_arr + "_str", row[1]);
            Storage.store(this, list_arr + "_pos", String.valueOf(position));
            Log.d(TAG, "onItemClick() сохранено в SharedPref: " + row[0] + " " + row[1] + " " + row[2] + " ");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DataAdapter adapter = (DataAdapter) parent.getAdapter();
        if (adapter.loaded) {
            String [] row = (String[]) adapter.getItem(position);
            Storage.store(this, spinner_arr, row[0]);
            Storage.store(this, spinner_arr+"_str", row[1]);
            Storage.store(this, spinner_arr + "_pos", String.valueOf(position));
            //Log.d(TAG, "onItemSelected() сохранено в SharedPref: "+row[0] +" "+row[1] +" "+row[2] +" ");

            if (spinner_arr.equals("GetLPUList") || spinner_arr.equals("GetDistrictList") ) {
                DataAdapter adapter1 = new DataAdapter(this, (int) (random()*10000),"CheckPatient");
                adapter1.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        refresh();
                    }
                });
            }
            else {
                ListView listView = findViewById(R.id.list);
                if (listView.getVisibility() == View.VISIBLE) {
                    DataAdapter adapter2 = (DataAdapter) listView.getAdapter();
                    adapter2.update();
                }
                //RecyclerView mRecyclerView = findViewById(R.id.recycler); if (mRecyclerView.getVisibility()==View.VISIBLE) updateObserver((AdapterView) mRecyclerView);
            }
            restore_Values();
            show_Values();
        }
        else {
            Log.d(TAG,"onItemSelected, но адаптер спинера еще не обновился");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {  }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.spinner || v.getId() == R.id.list ) ((AppBarLayout) findViewById(R.id.appbar)).setExpanded(false);
        return false;
    }

    void refresh(){
        DataAdapter adapter = new DataAdapter(this, (int) (random() * 10000), "CheckPatient");
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                restore_Values();
                show_Values();

                Spinner spinner = findViewById(R.id.spinner);
                if (spinner.getVisibility()==View.VISIBLE) {
                    ((DataAdapter) spinner.getAdapter()).update();
                    spinner.setSelection(spinner_pos);
                }

                ListView listView = findViewById(R.id.list);
                if (listView.getVisibility()==View.VISIBLE) {
                    ((DataAdapter) listView.getAdapter()).update();
                }

            }
        });
    }

}
