package ru.mflops;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

class Adapter_FireBase extends RecyclerView.Adapter {
    private final ArrayList datas = new ArrayList();
    private final String TAG = "jop";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public Adapter_FireBase() {
        //mRecyclerView = d;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //db.collection("users")
        db.collection("spb")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            datas.add(document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                })
                //.addOnSuccessListener(documentSnapshots -> mRecyclerView.getAdapter().notifyDataSetChanged());
                .addOnSuccessListener(documentSnapshots -> notifyDataSetChanged());
/*
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
*/

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder, parent, false);
        //((LinearLayout) v.findViewById(R.id.llv)).addView(new myRunnable(parent.getContext()));
        ((LinearLayout) v.findViewById(R.id.llv)).addView(new Button(parent.getContext()));
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Map m = (Map) datas.get(position);
        //((TextView) holder.itemView.findViewById(R.id.f1)).setText(m.get("born").toString());
        //((TextView) holder.itemView.findViewById(R.id.f2)).setText(m.get("first").toString());
        //((TextView) holder.itemView.findViewById(R.id.f3)).setText(m.get("last").toString());
        ((TextView) holder.itemView.findViewById(R.id.f1)).setText(m.get("name").toString());
        ((TextView) holder.itemView.findViewById(R.id.f2)).setText(m.get("txt").toString());
        ((TextView) holder.itemView.findViewById(R.id.f3)).setText(m.get("url").toString());

        new DownloadImageTask((ImageView) holder.itemView.findViewById(R.id.imageView)).execute(m.get("url").toString());
        /*
        try {
            URL murl = new URL(m.get("url").toString());
            Bitmap mIcon_val = BitmapFactory.decodeStream(murl.openConnection().getInputStream());
            //((ImageView) holder.itemView.findViewById(R.id.imageView)).setImageURI(Uri.parse(m.get("url").toString()));
            ((ImageView) holder.itemView.findViewById(R.id.imageView)).setImageBitmap(mIcon_val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}