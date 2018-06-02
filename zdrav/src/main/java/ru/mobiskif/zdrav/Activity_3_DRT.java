package ru.mobiskif.zdrav;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.AdapterView;

import ru.healthy.R;

public class Activity_3_DRT extends ActivityBase {

    public Activity_3_DRT() {
        super();
        spinner_arr = "GetWorkingTime";
        card_arr = "GetAvaibleAppointments";
        list_arr = "GetAvaibleAppointments";
        button_text = "Взять";
    }

    @Override
    void set_Visiblity() {
        super.set_Visiblity();
        findViewById(R.id.button).setVisibility(View.GONE);
        findViewById(R.id.label2).setVisibility(View.VISIBLE);
        findViewById(R.id.label3).setVisibility(View.VISIBLE);
        ((AppBarLayout) findViewById(R.id.appbar)).setExpanded(false);

    }

    @Override
    void restore_Values() {
        super.restore_Values();
        label1_text = Storage.restore(this, "GetSpesialityList_str");
        textview_text = Storage.restore(this, "GetDoctorList_str");
        label2_text = "Расписание: ";
        label3_text = "Свободные талоны: ";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE_YN) {
            if (resultCode == RESULT_OK) {
                DataAdapter adapter = new DataAdapter(this, requestCode, "SetAppointment");
                adapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        setResult(RESULT_OK);
                        finish();
                    }
                });

            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent,view,position,id);
        Intent intent = new Intent(this, Activity_5_YN.class);
        intent.putExtra("message", getString(R.string.confirm_talon));
        startActivityForResult(intent, REQUEST_CODE_YN);
    }


}
