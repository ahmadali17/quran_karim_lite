package com.qurankarim.moshaf.Quran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qurankarim.moshaf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SurahShow extends AppCompatActivity {

    TextView suraView, suraNameText, basmalaText;
    ImageView backButton;

    // This array list will be used to store verses data temporarily and then pass that to listview.
    ArrayList<String> versesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sura_show);

        suraView = findViewById(R.id.sura_text);
        backButton = findViewById(R.id.back_btn);
        suraNameText = findViewById(R.id.sura_name_label);
        basmalaText = findViewById(R.id.basmala_text);

        String fileData = ReadFromFile("quran.json");

        ConvertStringToJSON(fileData);

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

    private void ConvertStringToJSON(String fileData) {
        //NOTE: For '{' we use JSON Object and for '[' we use JSON Array

        try {
            // Complete Json File
            JSONArray quran = new JSONArray(fileData);
            Intent iin = getIntent();
            Bundle b = iin.getExtras();

            String suraNumberString = b.getString("suranumber");
            String suraName = b.getString("suraname");

            if(suraNumberString.equals("9")){
                basmalaText.setVisibility(View.GONE);
            }

            try {
                suraNameText.setText(suraName);
                int suraNumber = Integer.parseInt(suraNumberString);
                Log.d("llll", "ConvertStringToJSON: " + suraNumber);

                for (int i = 0; i < quran.length(); i++) {
                    JSONObject surah = quran.getJSONObject(i);
                    if (surah.getInt("surah_number") == suraNumber) {
                        suraView.append(surah.getString("content") + " (" + surah.getString("verse_number") + ") ");
                    }
                }
            } catch (Exception e) {
                Log.d("jjj", e.getMessage());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void backToQuranIndex(View view) {
        this.finish();
    }
}