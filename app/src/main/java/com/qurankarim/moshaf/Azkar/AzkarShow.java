package com.qurankarim.moshaf.Azkar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qurankarim.moshaf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AzkarShow extends AppCompatActivity {

    private RecyclerView azkarRecyclerView;
    public List<Object> viewItems = new ArrayList<>();
    public static ArrayList<String> azkarblessList = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "AzkarActivity";

    private String azkarName;


    private TextView azkarNameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar_show);

        azkarRecyclerView = findViewById(R.id.azkar_recycler_view);

        azkarNameLabel = findViewById(R.id.azkar_name_label);

        azkarRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        azkarRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new AzkarAdapter(this, viewItems);
        azkarRecyclerView.setAdapter(mAdapter);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        azkarName = b.getString("azkarname");

        Log.d(TAG, "onCreate: " + azkarName);

        String fileData = ReadFromFile(azkarName);

        ConvertStringToJSON(fileData);
    }

    private void ConvertStringToJSON(String fileData) {
        //NOTE: For '{' we use JSON Object and for '[' we use JSON Array

        try {
            // Complete Json File
            JSONObject azkar = new JSONObject(fileData);

            // Azkar Name
            String azkarName = azkar.getString("title");

            azkarNameLabel.setText(azkarName);

            // Azkar Content List
            JSONArray azkarList = azkar.getJSONArray("content");

            for (int i = 0; i < azkarList.length(); i++) {
                JSONObject zekr = azkarList.getJSONObject(i);

                azkarblessList.add(zekr.getString("bless"));

                ZekrModel zekrModel = new ZekrModel(zekr.getString("zekr"), zekr.getInt("repeat"),
                        zekr.getString("bless"));



                viewItems.add(zekrModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String ReadFromFile(String fileName) {
        //Creating objects
        StringBuilder returnString = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputSteamReader = null;
        BufferedReader reader = null;
        try {
            inputStream = this.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            inputSteamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputSteamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            //Error Handling
            try {
                if (inputSteamReader != null)
                    inputSteamReader.close();
                if (inputStream != null)
                    inputStream.close();
                if (reader != null)
                    reader.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        //Return the output in string format
        return returnString.toString();
    }

    public void backToAzkarMain(View view) {
        this.finish();
    }

}