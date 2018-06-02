package ru.mobiskif.zdrav;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import static java.lang.Math.random;


public class DataAdapter extends BaseAdapter implements IDataAdapter, android.app.LoaderManager.LoaderCallbacks<Cursor> {
    Activity context;
    int id;
    String action;
    boolean loaded;
    Cursor cursor;
    String TAG=getClass().getSimpleName()+" jop";


    public DataAdapter(Activity c, int r, String a) {
        context = c;
        id = (int) (1000*random());
        action = a;
        loaded=false;

        Log.d(TAG, "Заявка на создание адаптера: " + action + " " + id);
        context.getLoaderManager().initLoader(id, null, this);
    }

    @Override
    public void update() {
        Log.d(TAG, "Заявка на обновление адаптера: " + action + " " + id);
        context.getLoaderManager().restartLoader(id, null, this);
    }

    @Override
    public int getCount() {
        if (cursor!=null) return cursor.getCount();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        String[] row = new String[4];
        if (cursor!=null) {
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
        int variant=1;
        if (variant==0) {
            TwoLineListItem view = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            TextView v1 = view.getText1();
            v1.setText(item[1] + " (" +item[2]+ ")");
            return view;
        }
        else if (variant==1) {
            TextView view = (TextView) ((convertView == null) ? inflater.inflate(android.R.layout.simple_list_item_1, parent, false) : convertView);
            view.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            //if (action.contains("GetLPUList")) view.setText(item[2]); else
                view.setText(item[1] + " (" +item[2]+ ")");
            return view;
        }
        else  {
            return null;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader " + id);
        loaded=false;
        MyCursorLoader loader = new MyCursorLoader(context, action);
        loader.id = id;
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (((MyCursorLoader)loader).id == this.id) {
            loaded = true;
            cursor = data;

            if (action.equals("CheckPatient")) {
                if (cursor != null) {
                    cursor.moveToPosition(0);
                    if (!cursor.isAfterLast()) {
                        String idPat = cursor.getString(0);
                        Storage.store(context, "CheckPatient", idPat);
                    }
                }
            }
            Log.d(TAG, "onLoadFinished: " + action);
            notifyDataSetChanged();
        }
        else {
            Log.d(TAG, "чужой лоадер");
        }
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

    static class MyCursorLoader extends CursorLoader {
        Context context;
        String action;
        int id;

        public MyCursorLoader(Context con, String act) {
            super(con);
            context=con;
            action=act;
        }

        @Override
        public Cursor loadInBackground() {
            Log.d("jop", "MyCursorLoader.loadInBackground()");
            HubService hs = new HubService(context);
            Cursor cur=null;
            String sw= action;
            //sw="qweqwe";
            switch (sw) {
                case "GetLPUList": cur=hs.GetLPUList("GetLPUList"); break;
                case "GetOrgList": cur=hs.GetOrgList("GetOrgList"); break;
                case "GetDistrictList": cur=hs.GetDistrictList("GetDistrictList"); break;
                case "CheckPatient": cur=hs.CheckPatient("CheckPatient"); break;
                case "GetPatientHistory": cur=hs.GetPatientHistory("GetPatientHistory"); break;
                case "GetSpesialityList": cur=hs.GetSpesialityList("GetSpesialityList"); break;
                case "GetDoctorList": cur=hs.GetDoctorList("GetDoctorList"); break;
                case "GetAvaibleAppointments": cur=hs.GetAvaibleAppointments("GetAvaibleAppointments"); break;
                case "GetWorkingTime": cur=hs.GetWorkingTime("GetWorkingTime"); break;
                case "SetAppointment": cur=hs.SetAppointment("SetAppointment"); break;
                case "CreateClaimForRefusal": cur=hs.CreateClaimForRefusal("CreateClaimForRefusal"); break;
                case "SearchTop10Patient": cur=hs.SearchTop10Patient("SearchTop10Patient"); break;
                default: cur=hs.defaultList();
            }
            //try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {}
            return cur;
        }

    }
}
