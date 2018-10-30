package ru.mobiskif.geo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class ActivityLPU extends ActivityCustom {

    public ActivityLPU() {
        action="GetLPUList";
        prev_action="GetDistrictList";
        nextClass = ActivitySpeciality.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((android.support.v7.widget.Toolbar)findViewById(R.id.toolbar)).setNavigationIcon(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Показать скрытое")) {
            item.setChecked(!item.isChecked());
            if (item.isChecked()) Storage.store(this, "show_all","1");
            else Storage.store(this, "show_all","0");
            startAdapter(action);
        }
        return super.onOptionsItemSelected(item);
    }

}
