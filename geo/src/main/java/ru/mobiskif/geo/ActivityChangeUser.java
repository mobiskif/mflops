package ru.mobiskif.geo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityChangeUser extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);

        RadioGroup radioButtonGroup = findViewById(R.id.rgroup);
        radioButtonGroup.setOnCheckedChangeListener(this);
        int idx = Integer.valueOf(Storage.getCurrentUser(this));
        if (idx<0 || idx >4) idx=0;
        View btn = radioButtonGroup.getChildAt(idx);
        radioButtonGroup.check(btn.getId());


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_distr) {
            String psurname = ((EditText) findViewById(R.id.editText1)).getText().toString();
            String pname = ((EditText) findViewById(R.id.editText2)).getText().toString();
            String psecond = ((EditText) findViewById(R.id.editText3)).getText().toString();
            String pbirst = ((EditText) findViewById(R.id.editText4)).getText().toString();
            Storage.store(this, "Surname", psurname);
            Storage.store(this, "Name", pname);
            Storage.store(this, "SecondName", psecond);
            Storage.store(this, "Birthday", pbirst);
            Intent intent = new Intent(this, ActivityDistrict.class);
            startActivityForResult(intent,0);
            //finish();
        }
        if (v.getId() == R.id.button_save) {
            String psurname = ((EditText) findViewById(R.id.editText1)).getText().toString();
            String pname = ((EditText) findViewById(R.id.editText2)).getText().toString();
            String psecond = ((EditText) findViewById(R.id.editText3)).getText().toString();
            String pbirst = ((EditText) findViewById(R.id.editText4)).getText().toString();
            Storage.store(this, "Surname", psurname);
            Storage.store(this, "Name", pname);
            Storage.store(this, "SecondName", psecond);
            Storage.store(this, "Birthday", pbirst);
            Intent intent = new Intent(this, ActivityLPU.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        View radioButton = group.findViewById(checkedId);
        int idx = group.indexOfChild(radioButton);
        Storage.setCurrentUser(this, "" + idx);
        init();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();
    }

    void init() {
        ((EditText) findViewById(R.id.editText1)).setText(Storage.restore(this, "Surname"));
        ((EditText) findViewById(R.id.editText2)).setText(Storage.restore(this, "Name"));
        ((EditText) findViewById(R.id.editText3)).setText(Storage.restore(this, "SecondName"));
        ((EditText) findViewById(R.id.editText4)).setText(Storage.restore(this, "Birthday"));

        ((TextView) findViewById(R.id.textViewDistrict)).setText(Storage.restore(this, "GetDistrictList_info"));
        if (((TextView) findViewById(R.id.textViewDistrict)).getText().length()<=1) ((TextView) findViewById(R.id.textViewDistrict)).setText("Адмиралтейский");
    }
}
