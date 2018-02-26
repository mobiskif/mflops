package ru.mflops;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements View.OnClickListener {
    LinearLayout lay;
    ArrayList buttons;
    String TAG ="jop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lay = findViewById(R.id.lay);
        buttons = new ArrayList();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnplus) {
            myRunnable mr = new myRunnable(this);
            buttons.add(mr);
            lay.addView(mr);
        }
        if (view.getId() == R.id.btnminus && buttons.size() > 0) {
            ((myRunnable) buttons.get(buttons.size() - 1)).stop();
            lay.removeViewAt(buttons.size() - 1);
            buttons.remove(buttons.size() - 1);
        }
        if (view.getId() == R.id.btnn) {
            ((TextView) findViewById(R.id.tvInfo)).setText(getSummaryValue());
            firestore();
        }

    }

    String getSummaryValue() {
        float res = 0;
        for (Object o : buttons) {
            String vs = String.valueOf(((myRunnable) o).getText());
            res += Float.valueOf(vs);
        }
        return buttons.size() + ": " + String.valueOf(res);
    }

    void firestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        // Create a new user with a first, middle, and last name
        //Map<String, Object>
                user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);

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
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
