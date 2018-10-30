package ru.mobiskif.geo;

import android.view.View;
import android.widget.TextView;

public class ActivityDoctor extends ActivityCustom {

    public ActivityDoctor() {
        action="GetDoctorList";
        prev_action="GetSpesialityList";
        nextClass = ActivityAvailable.class;
    }

}
