package com.qurankarim.moshaf.Quran;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qurankarim.moshaf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JuzShowActivity extends AppCompatActivity {

    TextView juzNumber, juzText, surahName;
    ImageView bookmarkBtn;
    String juzNum;

    LinearLayout juzContent;
    // This array list will be used to store verses data temporarily and then pass that to list view.
    ArrayList<String> versesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juz_show);

        juzNumber = findViewById(R.id.juz_name_label);
        juzText = findViewById(R.id.juz_text);
        //suraName = findViewById(R.id.sura_name);
        //basmalaText = findViewById(R.id.basmala_text);
        juzContent = findViewById(R.id.juz_content);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        juzNum = b.getString("juzNumber");
        Log.d("juz", "onCreate: " + b.getString("juzNumber"));
        juzNumber.setText("الجزء " + juzNum);

        String fileData = ReadFromFile("quran_juz.json");

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

    @SuppressLint("SetTextI18n")
    private void ConvertStringToJSON(String fileData) {
        //NOTE: For '{' we use JSON Object and for '[' we use JSON Array

        try {
            JSONObject allData = new JSONObject(fileData);
            JSONObject data = allData.getJSONObject("data");

            JSONArray surahs = data.getJSONArray("surahs");

            for (int i = 0; i < surahs.length(); i++) {
                JSONObject surah = surahs.getJSONObject(i);
                JSONArray ayahs = surah.getJSONArray("ayahs");
                for (int j = 0; j < ayahs.length(); j++) {
                    JSONObject ayah = ayahs.getJSONObject(j);
                    if (ayah.getInt("juz") == Integer.parseInt(juzNum)) {
                        if (ayah.getInt("numberInSurah") == 1) {
                            SpannableString suraName = new SpannableString("\n" + surah.getString("name") + "\n");
                            ForegroundColorSpan fSpan = new ForegroundColorSpan(Color.WHITE);
                            BackgroundColorSpan bSpan = new BackgroundColorSpan(Color.parseColor("#325E4D"));

                            suraName.setSpan(fSpan, 0, suraName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            suraName.setSpan(bSpan, 0, suraName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            juzText.append(suraName);
                        }
                        juzText.append(ayah.getString("text") + " (" + ayah.getString("numberInSurah") + ") ");

                    }
                }

            }

            // Complete Json File
            try {
//
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