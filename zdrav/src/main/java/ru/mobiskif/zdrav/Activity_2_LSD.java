package ru.mobiskif.zdrav;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.AdapterView;


public class Activity_2_LSD extends ActivityBase {

    public Activity_2_LSD() {
        super();
        spinner_arr = "GetSpesialityList";
        card_arr = "GetDoctorList";
        list_arr = "GetDoctorList";
    }
    @Override
    void set_Visiblity() {
        super.set_Visiblity();
        findViewById(R.id.label1).setVisibility(View.VISIBLE);
        findViewById(R.id.label2).setVisibility(View.VISIBLE);
        findViewById(R.id.label3).setVisibility(View.VISIBLE);
        findViewById(R.id.button).setVisibility(View.GONE);
        ((AppBarLayout) findViewById(R.id.appbar)).setExpanded(false);
    }
    @Override
    void restore_Values() {
        super.restore_Values();
        label1_text = "Район: "+Storage.restore(this, "GetDistrictList_str");// + " (" + Storage.restore(this, "CheckPatient") +")";
        label2_text = "Специальность (свободных талонов): ";
        label3_text = "Врач (свободных талонов): ";
        textview_text = Storage.restore(this, "GetLPUList_str");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent,view,position,id);
        Intent intent = new Intent(this, Activity_3_DRT.class);
        startActivityForResult(intent, REQUEST_CODE_DRT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE_DRT) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
