package ru.mobiskif.geo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAvailable extends ActivityCustom {
    View tmp;

    public ActivityAvailable() {
        action = "GetAvaibleAppointments";
        prev_action="GetDoctorList";
        nextClass = ActivitySpeciality.class;
    }

    @Override
    public void onClick(View v) {
        ActivityAvailable av = this;
        tmp = v;
        new AlertDialog.Builder(this)
                .setTitle("Взять талончик?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Storage.store(av, action, ((TextView) v.findViewById(R.id.textView0)).getText().toString());
                        Storage.store(av, "action_info", ((TextView) v.findViewById(R.id.textView2)).getText().toString());
                        startAdapter("SetAppointment");
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    void callback(DataAdapter dataAdapter) {
        if (dataAdapter.action.equals(action)) super.callback(dataAdapter);
        else {
            if (dataAdapter.action.contains("SetAppointment")) {
                progressView.setVisibility(View.GONE);
                if (dataAdapter.cursor != null) {
                    dataAdapter.cursor.moveToPosition(0);
                    if (!dataAdapter.cursor.isAfterLast()) {
                        if (dataAdapter.cursor.getString(2).equals("Ошибка")) {
                            String error_msg = "";
                            if (dataAdapter.cursor.getString(3).contains("недопустимый")) {
                                error_msg = "В записи отказано: пациента нет в базе. Зайдите в регистратуру или проверьте ФИО в Меню->Пациенты";
                            } else if (dataAdapter.cursor.getString(3).contains("имеет предстоящую запись")) {
                                error_msg = "В записи отказано: пациент имеет талон к врачу этой специальности";
                            } else {
                                error_msg = "В записи отказано регистратурой поликлиники";
                            }
                            Toast.makeText(this, error_msg, Toast.LENGTH_LONG).show();
                        } else {
                            Storage.store(this, "GetLPUList_info", "Талон отложен");
                            //((TextView) tmp.findViewById(R.id.textView2)).setText("Талончик отложен");
                            super.onClick(tmp);
                        }
                    }
                }
            }
        }
    }
}
