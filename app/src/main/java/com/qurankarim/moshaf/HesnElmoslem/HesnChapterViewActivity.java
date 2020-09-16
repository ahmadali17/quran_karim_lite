package com.qurankarim.moshaf.HesnElmoslem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class HesnChapterViewActivity extends AppCompatActivity {

    private RecyclerView chapterViewRecycler;

    public List<Object> viewItems = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "HesnChapterViewActivity";

    private TextView hesnChapterLabel;

    private String chapterTitle, chapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesn_chapter_view);

        chapterViewRecycler = findViewById(R.id.hesn_chapter_recycler);
        hesnChapterLabel = findViewById(R.id.hesn_chapter_name_label);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        hesnChapterLabel.setText(b.getString("chaptertitle"));

        chapterTitle = b.getString("chaptertitle");
        chapterPosition = b.getString("chapterposition");

        chapterViewRecycler.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        chapterViewRecycler.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new HesnChapterAdapter(this, viewItems);
        chapterViewRecycler.setAdapter(mAdapter);

        String fileData = ReadFromFile("hesn_elmoslm.json");

        ConvertStringToJSON(fileData);

    }

    private void ConvertStringToJSON(String fileData) {
        //NOTE: For '{' we use JSON Object and for '[' we use JSON Array

        try {
            // Complete Json File
            JSONArray chaptersList = new JSONArray(fileData);

            for (int i = 0; i < chaptersList.length(); i++) {
                JSONObject chapter = chaptersList.getJSONObject(i);

                if (chapterTitle.equals(chapter.getString("title"))) {
                    JSONArray chapterContentArray = chapter.getJSONArray("content");

                    for (int j = 0; j < chapterContentArray.length(); j++) {
                        JSONObject content = chapterContentArray.getJSONObject(j);

                        HesnChapterModel hesnChapterModel = new HesnChapterModel(content.getString("text")
                                , content.getString("source"));

                        viewItems.add(hesnChapterModel);
                    }
                }

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

    public void goToHesnIndex(View view) {
        this.finish();
    }
}