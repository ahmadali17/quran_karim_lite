package com.qurankarim.moshaf.Quran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qurankarim.moshaf.DatabaseHelper;
import com.qurankarim.moshaf.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuranIndexActivity extends AppCompatActivity implements Adapter.onSurahListener, FavAdapter.OnFavSurahListener {

    private static final String TAG = "QuranIndexActivity";

    private ArrayList<SurahModel> surahModelList = new ArrayList<>();

    private LinearLayout favBtn, allBtn;

    private RecyclerView surahsRecycler;

    private List<FavSurahModel> favSurahsList = new ArrayList<>();
    private FavAdapter favAdapter;
    private DatabaseHelper favDb;

    private Context mContext = this;

    private SearchView searchView;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_index2);

        searchView = findViewById(R.id.search_action);
        surahsRecycler = findViewById(R.id.recycler_quran_index_surahs);
        surahsRecycler.setLayoutManager(new LinearLayoutManager(this));
        surahsRecycler.setHasFixedSize(true);

        allBtn = findViewById(R.id.all_btn);

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
                surahsRecycler.setAdapter(mAdapter);
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        favBtn = findViewById(R.id.fav_btn);
        String fileData = ReadFromFile("surahs.json");

        ConvertStringToJSON(fileData);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surahsRecycler.setAdapter(mAdapter);
            }
        });
        favDb = new DatabaseHelper(this);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: linear");
                surahsRecycler.setHasFixedSize(true);
                //surahsRecycler.setAdapter(new Adapter(surahModelList, this));
                surahsRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                loadFavData();
            }
        });

    }

    public void loadFavData() {
        if (favSurahsList != null) {
            favSurahsList.clear();
        }
        SQLiteDatabase db = favDb.getReadableDatabase();
        Cursor cursor = favDb.select_all_favorite_list_surah();
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(favDb.SURAH_TITLE));
                String id = cursor.getString(cursor.getColumnIndex(favDb.KEY_ID));
                String surahType = cursor.getString(cursor.getColumnIndex(favDb.SURAH_REVELTION_TYPE));
                String surahVersesNum = cursor.getString(cursor.getColumnIndex(favDb.SURAH_VERSES_NUM));
                FavSurahModel favSurah = new FavSurahModel(title, id, Integer.parseInt(surahVersesNum), surahType);
                favSurahsList.add(favSurah);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        favAdapter = new FavAdapter(this, favSurahsList, this);

        surahsRecycler.setAdapter(favAdapter);
    }

    private void ConvertStringToJSON(String fileData) {
        //NOTE: For '{' we use JSON Object and for '[' we use JSON Array

        try {
            // Complete Json File
            JSONArray quranSurahs = new JSONArray(fileData);
            Log.d(TAG, "ConvertStringToJSON: " + quranSurahs.length());
            for (int i = 0; i < quranSurahs.length(); i++) {
                JSONObject surah = quranSurahs.getJSONObject(i);
                SurahModel surahModel = new SurahModel(surah.getString("name"), surah.getInt("number"),
                        surah.getInt("total_verses"), surah.getString("revelation_type"), "0");
                surahModelList.add(surahModel);
            }
            mAdapter = new Adapter(surahModelList, this, this);
            surahsRecycler.setAdapter(mAdapter);

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

    public void backToMainActivity(View view) {
        this.finish();
    }

    @Override
    public void onSurahClick(String position, String selectedSurahName) {
        Intent i = new Intent(QuranIndexActivity.this, SurahShow.class);
        i.putExtra("suranumber", position);
        i.putExtra("suraname", selectedSurahName);
        startActivity(i);
    }

    @Override
    public void onFavSurahClick(String position, String selectedSurahName) {
        Intent i = new Intent(QuranIndexActivity.this, SurahShow.class);
        i.putExtra("suranumber", position);
        i.putExtra("suraname", selectedSurahName);
        startActivity(i);
    }
}