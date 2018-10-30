package ru.mobiskif.zdrav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements IDataAdapter{
    private String[] mDataset;
    private  String tag;
    Activity context;
    String action;

    public CardAdapter(String card_arr, Activity c, String t) {
        //mDataset = new Storage(context).getStringArray(card_arr);
        context = c;
        //button_text = ((ActivityBase)a).button_text;
        tag = t;
        action = card_arr;
        update();
    }

    @Override
    public void update() {
        //Log.d("jop","======"+action);
        //((BaseAdapter) ((ListView) findViewById(R.id.list)).getAdapter()).notifyDataSetChanged();
        //Log.e("jop", "==========");
        //mDataset = new Storage(activity).getStringArray(action);
        mDataset = context.getResources().getStringArray(R.array.def_arr);

        notifyDataSetChanged();
        //Log.d("jop","===Load Fin==="+action);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(String[] myDataset, Activity a, String t) {
        mDataset = myDataset;
        context = a;
        //button_text = ((ActivityBase)a).button_text;
        tag = t;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        Button btn = v.findViewById(R.id.action_button);
        btn.setText(tag);
        v.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, Activity_5_YN.class);
                //intent.putExtra(ActivityBase.EXTRA_POSITION, getAdapterPosition());
                intent.putExtra("message", context.getString(R.string.confirm_text));
                ((Activity)context).startActivityForResult(intent, 1);

            }
        });
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.get
        ((TextView) holder.cardView.findViewById(R.id.card_text)).setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
