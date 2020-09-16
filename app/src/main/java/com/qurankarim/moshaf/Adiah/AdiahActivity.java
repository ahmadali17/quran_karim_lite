package com.qurankarim.moshaf.Adiah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qurankarim.moshaf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdiahActivity extends AppCompatActivity {
    private RecyclerView adiahRecyclerView;

    private List<Object> viewItems = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageView copyDoaa;

    private static final String TAG = "AdiahActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiah);
        adiahRecyclerView = findViewById(R.id.recycler_adiah_view);
        copyDoaa = findViewById(R.id.copy_doaa);

        adiahRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        adiahRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new AdiahAdapter(this, viewItems);
        adiahRecyclerView.setAdapter(mAdapter);

        String fileData = ReadFromFile("adiah.json");

        ConvertStringToJSON(fileData);
    }

    private void ConvertStringToJSON(String fileData) {
        //NOTE: For '{' we use JSON Object and for '[' we use JSON Array

        try {
            // Complete Json File
            JSONArray adiahList = new JSONArray(fileData);
            Log.d(TAG, "ConvertStringToJSON: " + adiahList.length());
            for (int i = 0; i < adiahList.length(); i++) {
                JSONObject doaa = adiahList.getJSONObject(i);

                DoaaModel doaaModel = new DoaaModel(doaa.getString("id"), doaa.getString("dua"),
                        doaa.getString("reference"));
                viewItems.add(doaaModel);
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
    public void backToMainActivity(View view){
        this.finish();
    }
}