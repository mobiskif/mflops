package ru.mflops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;

public class MainActivity extends Activity implements View.OnClickListener {
    GoogleSpeech tts;
    ArrayList<String> fileContent = new ArrayList<>();

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
        }
        else if (id == R.id.settings) {
            //startActivityForResult(new Intent(this, SettingsActivity.class),0);
            return true;
        }
        else if (id == R.id.load) {
            selectFile("*/*");
            return true;
        }
        else if (id == R.id.save) {
            saveBase();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();

        String[] heads = (fileContent.get(0)).split(",");
        fileContent.remove(0);
        for (String row: fileContent) {
            String[] cols = row.split(",");
            for (int i=0; i<cols.length; i++) {
                user.put(heads[i], cols[i]);
                Log.d("jop",heads[i]+" = "+cols[i]);
            }
            db.collection("users").add(user);
            Log.d("jop", "Отправлена строка: "+row);
        }

        /*
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);
        db.collection("users").add(user);
        */
    }

    private void selectFile(String type) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(type);
        startActivityForResult(intent, 777);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 777 && data != null) {

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
                    saveBase();
                }
                catch (IOException e) {
                    //
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setAdapter(new Adapter_FireBase());

        tts = new GoogleSpeech(this, null);

        /*

        //Pie pie = AnyChart.pie();
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        AnyChartView anyChartView2 = findViewById(R.id.any_chart_view2);

        Venn venn = AnyChart.venn();
        Pie venn2 = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new NameValueDataEntry("A", "Data Science", 100));
        data.add(new NameValueDataEntry("B", "Computer Science", 25));
        data.add(new NameValueDataEntry("C", "Math and Statistics", 25));
        data.add(new NameValueDataEntry("D", "Subject Matter Expertise", 25));
        data.add(new NameValueDataEntry("A&B", "Computer Science", 50));
        data.add(new NameValueDataEntry("A&C", "Math and Statistics", 50));
        data.add(new NameValueDataEntry("A&D", "Subject Matter Expertise", 50));
        data.add(new NameValueDataEntry("B&C", "Machine\\nLearning", 5));
        data.add(new NameValueDataEntry("C&D", "Traditional\\nResearch", 5));
        data.add(new NameValueDataEntry("D&B", "Traditional\\nSoftware", 5));
        data.add(new NameValueDataEntry("B&C&D", "Unicorn", 5));

        venn.setData(data);
        venn2.setData(data);

        venn.setStroke("#FFFFFF", 2d, null, null, null);

        venn.getLabels().setFormat("{%Name}");

        venn.getIntersections().getHovered().setFill("black", 0.25d);

        venn.getIntersections().getLabels().setFontWeight("bold");
        venn.getIntersections().getLabels().setFormat("{%Name}");

        venn.getTooltip().setTitleFormat("{%Name}");

        //anyChartView.setChart(venn);
        //anyChartView2.setChart(venn2);
        */

    }

    @Override
    public void onClick(View v) {
        if (tts.isSpeaking()) tts.stop();
        //tts.speak(((TextView) v).getText(), TextToSpeech.QUEUE_FLUSH, null, "1");
    }
}
