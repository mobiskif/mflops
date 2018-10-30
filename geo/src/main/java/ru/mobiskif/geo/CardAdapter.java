package ru.mobiskif.geo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.v4.content.ContextCompat.startActivity;

public class CardAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    public final BaseAdapter dataAdapter;
    private final String action;
    Context context;

    public CardAdapter(BaseAdapter dataAdapter, String a, Context c) {
        this.dataAdapter = dataAdapter;
        this.action = a;
        context = c;
    }

    @Override
    public void onClick(View v) {
        Storage.store(context, action, ((TextView) v.findViewById(R.id.textView0)).getText().toString());
        Storage.store(context, "action_info", ((TextView) v.findViewById(R.id.textView2)).getText().toString());
        ((ActivityCustom) context).onClick(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (action.contains("GetLPUList")) v = LayoutInflater.from(parent.getContext()).inflate(R.layout.district_view, parent, false);
        else v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        if (action.contains("GetAvaibleAppointments") || action.contains("GetPatientHistory")) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f);
            v.findViewById(R.id.textView2).setLayoutParams(params);
        }
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String[] item = (String[]) dataAdapter.getItem(position);
        if (item[0]!=null) {
            ((TextView) holder.itemView.findViewById(R.id.textView0)).setText(item[0].toString());
            ((TextView) holder.itemView.findViewById(R.id.textView1)).setText(item[1].toString());
            ((TextView) holder.itemView.findViewById(R.id.textView2)).setText(item[2].toString());
            ((TextView) holder.itemView.findViewById(R.id.textView3)).setText(item[3].toString());
            if (item[4] != null)
                ((TextView) holder.itemView.findViewById(R.id.textView4)).setText(item[4].toString());

            switch (action) {
                case "GetDoctorList":
                    holder.itemView.findViewById(R.id.circleImageView).setBackgroundResource(R.drawable.doctor_72x72);
                    break;
                case "GetDistrictList":
                    holder.itemView.findViewById(R.id.circleImageView).setVisibility(View.GONE);
                    holder.itemView.findViewById(R.id.imageViewInfo).setOnClickListener(this);
                    break;
                case "GetAvaibleAppointments":
                    //holder.itemView.findViewById(R.id.circleImageView).setBackgroundResource(R.drawable.round);
                    break;
                case "GetPatientHistory":
                    holder.itemView.findViewById(R.id.circleImageView).setBackgroundResource(R.drawable.ticket_56x56);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] ss = (String[]) dataAdapter.getItem(position);
                            new AlertDialog.Builder(context)
                                    .setTitle("Отменить талончик?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            ((ActivitySpeciality) context).callRefuse(ss[0]);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();
                        }
                    });
                    break;
                case "GetLPUList":
                    String[] ss = (String[]) dataAdapter.getItem(position);
                    Storage.store(context, "lpu_" + ss[0], "1");
                    holder.itemView.findViewById(R.id.imageView_minus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId() == R.id.imageView_minus) {
                                //Toast.makeText(context, "" + v.getId(), Toast.LENGTH_SHORT).show();
                                //holder.itemView.findViewById(R.id.imageView_minus).setVisibility(View.INVISIBLE);
                                holder.itemView.setVisibility(View.GONE);
                                Storage.store(context, "show_all","0");
                                Storage.store(context, "lpu_" + ss[0], "0");
                            }
                        }
                    });
                    break;

                default:
                    if (holder.itemView.findViewById(R.id.circleImageView) != null)
                        holder.itemView.findViewById(R.id.circleImageView).setVisibility(View.GONE);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataAdapter.getCount();
    }

}

