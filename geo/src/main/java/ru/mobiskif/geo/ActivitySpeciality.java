package ru.mobiskif.geo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitySpeciality extends ActivityCustom {

    public ActivitySpeciality() {
        action = "GetSpesialityList";
        prev_action = "GetLPUList";
        nextClass = ActivityDoctor.class;
        //contentView = R.layout.activity_speciality;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.recyclerview2).setVisibility(View.VISIBLE);
        startAdapter("CheckPatient");
    }


    @Override
    void callback(DataAdapter dataAdapter) {
        if (dataAdapter.action.equals(action)) super.callback(dataAdapter);
        else {
            if (dataAdapter.action.contains("CheckPatient")) {
                progressView.setVisibility(View.GONE);
                if (dataAdapter.cursor != null) {
                    dataAdapter.cursor.moveToPosition(0);
                    if (!dataAdapter.cursor.isAfterLast())
                        if (dataAdapter.cursor.getString(0).length() >= 3) {
                            Storage.store(this, "CheckPatient", dataAdapter.cursor.getString(0));
                            String fio, id, info;
                            fio = Storage.restore(this, "Surname") + " " + Storage.restore(this, "Name");
                            id = dataAdapter.cursor.getString(0);
                            info = Storage.restore(this, prev_action+"_info");
                            //((TextView) findViewById(R.id.textViewInfo)).setText(info + "  " + fio + " " + id);
                            ((TextView) findViewById(R.id.textViewInfo)).setText(info + "  " + fio);
                        }
                        else {
                            String info = Storage.restore(this, prev_action+"_info");
                            ((TextView) findViewById(R.id.textViewInfo)).setText(info + "  (пациента нет в базе)");
                        }
                }
                startAdapter("GetPatientHistory");
            }
            else if (dataAdapter.action.contains("GetPatientHistory")) {
                progressView.setVisibility(View.GONE);
                if (dataAdapter.getCount() > 0) {
                    RecyclerView recyclerView2 = findViewById(R.id.recyclerview2);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView2.setAdapter(dataAdapter.getCardAdapter());
                    focusOnView(recyclerView2);
                } else findViewById(R.id.recyclerview2).setVisibility(View.GONE);
            }
            else if (dataAdapter.action.contains("CreateClaimForRefusal")) {
                progressView.setVisibility(View.GONE);
                if (dataAdapter.cursor != null) {
                    dataAdapter.cursor.moveToPosition(0);
                    if (!dataAdapter.cursor.isAfterLast())
                        if (dataAdapter.cursor.getString(2).equals("Не отменен")) {
                            String error_msg = dataAdapter.cursor.getString(1);
                            Toast.makeText(this, error_msg, Toast.LENGTH_LONG).show();
                        } else {
                            Storage.store(this, prev_action+"_info", "Талон отменен");
                            ((TextView) findViewById(R.id.textViewInfo)).setText(Storage.restore(this, prev_action + "_info"));
                            startAdapter("GetPatientHistory");
                        }
                }
            }
        }
    }

    private final void focusOnView(RecyclerView rv) {
        rv.post(new Runnable() {
            @Override
            public void run() {
                rv.smoothScrollBy(105, 0);
            }
        });
    }

    void callRefuse(String id) {
        Storage.store(this, "GetPatientHistory", id);
        startAdapter("CreateClaimForRefusal");
    }

}
