package com.qurankarim.moshaf.HesnElmoslem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;


import com.qurankarim.moshaf.App;
import com.qurankarim.moshaf.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HesnIndexActivity extends AppCompatActivity implements HesnIndexAdapter.onChapterListener {

    RecyclerView hesnIndexRecycler;
    public List<HesnIndexModel> viewItems = new ArrayList<>();

    private HesnIndexAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "HesnIndexActivity";

    private AdView adView;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesn_index);

        adView = findViewById(R.id.adViewMain);
        MobileAds.initialize(this, App.AppAdId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        hesnIndexRecycler = findViewById(R.id.hesn_index_recycler);
        hesnIndexRecycler.setHasFixedSize(true);

        searchView = findViewById(R.id.search_action);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("kkk", "onQueryTextChange: " + newText);
                mAdapter.getFilter().filter(newText);
                mAdapter.notifyDataSetChanged();
                hesnIndexRecycler.setAdapter(mAdapter);
                return false;
            }
        });

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        hesnIndexRecycler.setLayoutManager(layoutManager);


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

                HesnIndexModel hesnIndexModel = new HesnIndexModel(chapter.getString("title"));

                viewItems.add(hesnIndexModel);
            }

            mAdapter = new HesnIndexAdapter(this, viewItems, this);
            hesnIndexRecycler.setAdapter(mAdapter);

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

    public void goToMainActivity(View view) {
        this.finish();
    }

    @Override
    public void onChapterClick(int position, String chapterTitle) {
        Intent i = new Intent(HesnIndexActivity.this, HesnChapterViewActivity.class);
        i.putExtra("chaptertitle", chapterTitle);
        i.putExtra("chapterposition", position);
        startActivity(i);
    }
}