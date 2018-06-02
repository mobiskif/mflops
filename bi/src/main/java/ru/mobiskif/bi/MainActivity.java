package ru.mobiskif.bi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.NameValueDataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.Venn;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    //GoogleSpeech tts;
    ArrayList fileContent = new ArrayList();
    ArrayList datas = new ArrayList();
    String headers = "A,B,C";
    String baseName = "bi";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.exit) {
            finish();
            return true;
        } else if (id == R.id.settings) {
            //startActivityForResult(new Intent(this, SettingsActivity.class),0);
            return true;
        } else if (id == R.id.load) {
            loadFromFile("*/*");
            return true;
        } else if (id == R.id.save) {
            saveToBase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveToBase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();

        String[] heads = headers.split(",");
        for (Object row : fileContent) {
            String[] cols = (row.toString()).split(",");
            for (int i = 0; i < cols.length; i++) {
                user.put(heads[i], cols[i]);
                Log.d("jop", heads[i] + " = " + cols[i]);
            }
            db.collection(baseName).add(user);
            Log.d("jop", "Отправлена строка: " + row);
        }

    }

    void loadFromBase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //db.collection("users")
        db.collection(baseName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d("jop", String.valueOf(document.getData().toString()));
                            datas.add(document.getData());
                        }
                    } else {
                        Log.d("jop", "Error getting documents.", task.getException());
                    }
                })
                //.addOnSuccessListener(documentSnapshots -> mRecyclerView.getAdapter().notifyDataSetChanged());
                .addOnSuccessListener(documentSnapshots -> prepareCharts());

    }

    private void loadFromFile(String type) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(type);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 && data != null) {
                fileContent = new ArrayList<>();

                try {
                    FileDescriptor fd = this
                            .getContentResolver()
                            .openFileDescriptor(data.getData(), "r")
                            .getFileDescriptor();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(fd)
                            )
                    );
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            fileContent.add(line);
                        }
                    } catch (IOException e) {
                        // log error
                    }
                    headers = fileContent.get(0).toString();
                    fileContent.remove(0);
                    prepareCharts();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    List getData(ArrayList<String> content) {
        List<DataEntry> data = new ArrayList<>();
        for (Object row : content) {
            String[] cells = (row.toString()).split(",");
            Log.d("jop", cells[1]);
            data.add(new NameValueDataEntry(cells[0], cells[1], Integer.valueOf(cells[2])));
        }
        return data;
    }

    List getData2(ArrayList content) {
        List<DataEntry> data = new ArrayList<>();
        for (Object row : content) {
            //String[] cells = (row.toString()).split(",");
            Log.d("jop", row.toString());
            Map<String, Object> user = (Map<String, Object>) row;
            Log.d("jop", user.get("last").toString());

            data.add(new NameValueDataEntry( user.get("first").toString(), user.get("last").toString(), Integer.valueOf(user.get("born").toString()) ));
        }
        return data;
    }

    void prepareCharts() {
        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view1);
        AnyChartView anyChartView2 = findViewById(R.id.any_chart_view2);
        Venn chart1 = AnyChart.venn();
        Pie chart2 = AnyChart.pie();
        //List<DataEntry> data = getData(fileContent);
        List<DataEntry> data = getData2(datas);
        chart1.setData(data);
        chart2.setData(data);
        anyChartView1.setChart(chart1);
        anyChartView2.setChart(chart2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFromBase();
        //prepareCharts();
    }

}
