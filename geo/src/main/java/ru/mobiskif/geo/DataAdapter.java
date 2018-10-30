package ru.mobiskif.geo;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import static java.lang.Math.random;


public class DataAdapter extends BaseAdapter implements android.app.LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    Activity activity;
    int id;
    String action;
    Cursor cursor;
    String TAG=getClass().getSimpleName()+" jop";

    public DataAdapter(Activity activity, String action) {
        this.activity = activity;
        id = (int) (1000*random());
        this.action = action;
        activity.getLoaderManager().initLoader(id, null, this);
    }

    @Override
    public int getCount() {
        if (cursor!=null) return cursor.getCount();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        String[] row = new String[40];
            if (cursor != null) {
                cursor.moveToPosition(position);
                if (!cursor.isAfterLast())
                    for (int i = 0; i < cursor.getColumnCount(); i++)
                        row[i] = cursor.getString(i);
            }
        return row;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String[] item = (String[]) getItem(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int variant=0;
        if (variant==0) {
            TwoLineListItem view = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            TextView v1 = view.getText1();
            v1.setText(item[1] + " (" +item[2]+ ")");
            return view;
        }
        else if (variant==1) {
            TextView view = (TextView) ((convertView == null) ? inflater.inflate(android.R.layout.simple_list_item_1, parent, false) : convertView);
            view.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            view.setText(item[1] + " (" +item[2]+ ")");
            return view;
        }
        else  return null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        MyCursorLoader loader = new MyCursorLoader(activity, action);
        loader.id = id;
        //activity.progressView.setVisibility(View.VISIBLE);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (((MyCursorLoader)loader).id == this.id) {
            cursor = data;
            Log.d(TAG, "onLoadFinished: " + action + " " + id);
            notifyDataSetChanged();
        }
        else Log.d(TAG, "чужой лоадер");
        ((ActivityCustom) activity).callback(this);
    }

    private String toStr(Cursor data) {
        StringBuilder r= new StringBuilder();
        data.moveToFirst();
        while(!data.isAfterLast()) {
            r.append(data.getString(1)).append(" ");
            data.moveToNext();
        }
        data.close();
        return r.toString();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {  }

    public RecyclerView.Adapter getCardAdapter() {
        //if (cardAdapter==null) cardAdapter = new CardAdapter(this, action, this.activity);
        //return cardAdapter;
        return new CardAdapter(this, action, this.activity);
    }

    @Override
    public void onClick(View v) {
        ((ActivityCustom) activity).onClick(v);
    }


    static class MyCursorLoader extends CursorLoader {
        Activity context;
        String action;
        //String param;
        int id;

        public MyCursorLoader(Activity con, String act) {
            super(con);
            context=con;
            action=act;
            //param=con.param;
        }

        @Override
        public Cursor loadInBackground() {
            //Log.d(getClass().getSimpleName()+" jop", "MyCursorLoader.loadInBackground()");
            HubService hs = new HubService(context);
            Cursor cur=null;
            String sw= action;
            switch (sw) {
                case "GetDistrictList": cur=hs.querySOAP("GetDistrictList"); break;
                case "GetLPUList": cur=hs.querySOAP("GetLPUList"); break;
                case "GetSpesialityList": cur=hs.querySOAP("GetSpesialityList"); break;
                case "GetDoctorList": cur=hs.GetDoctorList("GetDoctorList"); break;
                case "GetAvaibleAppointments": cur=hs.querySOAP("GetAvaibleAppointments"); break;
                case "GetOrgList": cur=hs.GetOrgList("GetOrgList"); break;
                case "SearchTop10Patient": cur=hs.SearchTop10Patient("SearchTop10Patient"); break;

                case "CheckPatient": cur=hs.CheckPatient("CheckPatient"); break;
                case "GetPatientHistory": cur=hs.GetPatientHistory("GetPatientHistory"); break;
                case "GetWorkingTime": cur=hs.GetWorkingTime("GetWorkingTime"); break;
                case "SetAppointment": cur=hs.SetAppointment("SetAppointment"); break;
                case "CreateClaimForRefusal": cur=hs.CreateClaimForRefusal("CreateClaimForRefusal"); break;
                default: cur=hs.defaultList();
            }
            //try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {}
            return cur;
        }

    }
}
