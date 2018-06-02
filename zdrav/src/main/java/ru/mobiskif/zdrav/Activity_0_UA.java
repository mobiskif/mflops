package ru.mobiskif.zdrav;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import ru.healthy.R;

public class Activity_0_UA extends ActivityBase {

    public Activity_0_UA() {
        super();
        spinner_arr = "GetDistrictList";
        card_arr = "GetLPUList";
        list_arr = "GetLPUList";
    }

    @Override
    void set_Visiblity() {
        super.set_Visiblity();
        findViewById(R.id.label1).setVisibility(View.GONE);
        findViewById(R.id.text).setVisibility(View.VISIBLE);
        findViewById(R.id.textview).setVisibility(View.GONE);
        findViewById(R.id.list).setVisibility(View.GONE);
        findViewById(R.id.tv).setVisibility(View.VISIBLE);
        findViewById(R.id.spinner).setVisibility(View.VISIBLE);
        button_text = getString(R.string.save);
    }

    void storeFIO(String s) {
        s=s.trim();
        Storage.store(this, "FIO", s);
        Storage.store(this, "CheckPatient", "нет такого");
        String[] ar = s.split(" ");
        if (ar.length==4) {
            Storage.store(this, "Surname", ar[0]);
            Storage.store(this, "Name", ar[1]);
            Storage.store(this, "Secondname", ar[2]);
            Storage.store(this, "Birstdate", ar[3]);
            error=false;
        }
        else {
            Storage.store(this, "Surname", "Фамилия");
            Storage.store(this, "Name", "Имя");
            Storage.store(this, "Secondname", "Отчество");
            Storage.store(this, "Birstdate", "2001-11-23");
            error=true;
        }
        if (error) Toast.makeText(this, "Заполните ФИО и дату точно, как в примере", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String fio = ((TextView) findViewById(R.id.text)).getText().toString();
        storeFIO(fio);
        super.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            storeFIO(((TextView) findViewById(R.id.text)).getText().toString());
            if (!error) {
                setResult(RESULT_OK);
                finish();
            }
        }
        else if (v.getId() == R.id.tv) startActivity(new Intent(getApplicationContext(), Activity_4_MAP.class));
    }

}
