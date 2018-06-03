package ru.mobiskif.bi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAlign;
import com.anychart.anychart.LegendLayout;
import com.anychart.anychart.NameValueDataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.Sunburst;
import com.anychart.anychart.TreeDataEntry;
import com.anychart.anychart.TreeFillingMethod;
import com.anychart.anychart.Venn;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    //GoogleSpeech tts;
    List<DataEntry> contentOfCSVFile;
    String[] headersOfCSVFile = {"first","last","born"};
    String BASE_NAME = "bi";
    int FILE_DIALOG = 1;
    private String CSV_SEPARATOR = ",";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.loadfromfile) {
            startSelectFileDialog("*/*");
            return true;
        } else if (item.getItemId() == R.id.loadfrombase) {
            contentOfCSVFile = loadFromFireBase(BASE_NAME);
            return true;
        } else if (item.getItemId() == R.id.save) {
            saveToFireBase(BASE_NAME);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void saveToFireBase(String basename) {
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();

        String[] heads = headersOfCSVFile.split(",");
        for (Object row : fileContent) {
            String[] cols = (row.toString()).split(",");
            for (int i = 0; i < cols.length; i++) {
                user.put(heads[i], cols[i]);
                Log.d("jop", heads[i] + " = " + cols[i]);
            }
            db.collection(BASE_NAME).add(user);
            Log.d("jop", "Отправлена строка: " + row);
        }
*/
    }

    List<DataEntry> loadFromFireBase(String baseName) {
        List<DataEntry> result = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(baseName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d("jop", String.valueOf(document.getData().toString()));
                            Map<String, Object> row = document.getData();
                            NameValueDataEntry dataEntry = new NameValueDataEntry(row.get(headersOfCSVFile[0]).toString(), row.get(headersOfCSVFile[1]).toString(), Integer.valueOf(row.get(headersOfCSVFile[2]).toString()));
                            result.add(dataEntry);
                        }
                    } else {
                        Log.d("jop", "Error getting documents.", task.getException());
                    }
                })
                //.addOnSuccessListener(documentSnapshots -> mRecyclerView.getAdapter().notifyDataSetChanged())
                .addOnSuccessListener(documentSnapshots -> prepareCharts());
        return result;

    }

    void startSelectFileDialog(String type) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(type);
        startActivityForResult(intent, FILE_DIALOG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_DIALOG && data != null) {
                contentOfCSVFile = loadFromCSVFile(data);
                prepareCharts();
            }
        }
    }

    List<DataEntry> loadFromCSVFile(Intent intent) {
        List<DataEntry> result = new ArrayList<>();
        try {
            FileDescriptor fd = this
                    .getContentResolver()
                    .openFileDescriptor(intent.getData(), "r")
                    .getFileDescriptor();
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(fd)
                            )
                    );
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(CSV_SEPARATOR);
                NameValueDataEntry dataEntry = new NameValueDataEntry(cols[0], cols[1], Integer.valueOf(cols[2]));
                result.add(dataEntry);
            }
        }
        catch (IOException e) {}
        return result;

    }


    void prepareCharts() {

        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view1);
        Venn venn = AnyChart.venn();
        venn.setData(contentOfCSVFile);
        venn.setTitle("Доходы");
        venn.getLegend().setEnabled(false);
        anyChartView1.setChart(venn);

        AnyChartView anyChartView2 = findViewById(R.id.any_chart_view2);
        Pie pie = AnyChart.pie();
        //Sunburst pie = AnyChart.sunburst();
        pie.setData(contentOfCSVFile);
        pie.setTitle("Расходы");
        pie.getLegend().setEnabled(true);
        pie.getLabels().setPosition("outside");
        anyChartView2.setChart(pie);

        /*
        pie.getLegend().getTitle().setEnabled(true);
        pie.getLegend().getTitle()
                .setText("Retail channels")
                .setPadding(0d, 0d, 10d, 0d);

        pie.getLegend()
                .setPosition("center-bottom")
                .setItemsLayout(LegendLayout.HORIZONTAL)
                .setAlign(EnumsAlign.CENTER)
                .setEnabled(false);
        //pie.getBackground().fill("black",1);
        */

        /*
        List<DataEntry> data = new ArrayList<>();
        data.add(new TreeDataEntry("root",null, 0));
        data.add(new TreeDataEntry("child1", null, 0));
        data.add(new TreeDataEntry("child2", "root", 0));

        TreeDataEntry tde1 = new TreeDataEntry("1", "child2", 1);
        tde1.setValue("1", "111");
        data.add(tde1);

        TreeDataEntry tde2 = new TreeDataEntry("2", "child2", 2);
        tde2.setValue("2", "222");
        data.add(tde2);

        TreeDataEntry tde3 = new TreeDataEntry("3", "child2", 3);
        tde3.setValue("child2","qweqwe");
        data.add(tde3);

        pie.setData(data, TreeFillingMethod.AS_TABLE);
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentOfCSVFile = loadFromFireBase(BASE_NAME);
    }

}
