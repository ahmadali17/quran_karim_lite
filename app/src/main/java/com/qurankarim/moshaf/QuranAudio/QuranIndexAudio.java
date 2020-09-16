package com.qurankarim.moshaf.QuranAudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.qurankarim.moshaf.DatabaseHelper;
import com.qurankarim.moshaf.Quran.Adapter;
import com.qurankarim.moshaf.Quran.FavAdapter;
import com.qurankarim.moshaf.Quran.FavSurahModel;
import com.qurankarim.moshaf.Quran.SurahModel;
import com.qurankarim.moshaf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuranIndexAudio extends AppCompatActivity implements Adapter.onSurahListener, FavAdapter.OnFavSurahListener {

    private RecyclerView surahsRecycler;
    private ArrayList<SurahModel> viewItems = new ArrayList<>();
    private ArrayList<FavSurahModel> favSurahsList = new ArrayList<>();
    private DatabaseHelper favDb;

    private RecyclerView.LayoutManager layoutManager;

    private TextView qariNameLabel;

    private static final String TAG = "QuranIndexAudioActivity";

    private String qariName, qariPath, qariPosition;

    private SearchView searchView;

    private Adapter mAdapter;
    private FavAdapter mFavAdapter;

    private LinearLayout favBtn;
    private LinearLayout allBtn;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_index_audio);

        qariNameLabel = findViewById(R.id.qari_name_label);

        searchView = findViewById(R.id.search_action);
        favBtn = findViewById(R.id.fav_btn);
        allBtn = findViewById(R.id.all_btn);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surahsRecycler.setAdapter(mAdapter);
            }
        });

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

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

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        qariName = b.getString("qariname");
        qariPath = b.getString("qaripath");
        qariPosition = b.getString("qariposition");

        qariNameLabel.setText(qariName);

        surahsRecycler = findViewById(R.id.recycler_quran_surahs_audio);
        surahsRecycler.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        surahsRecycler.setLayoutManager(layoutManager);

        favDb = new DatabaseHelper(this);

        String fileData = ReadFromFile("surahs.json");
        ConvertStringToJSON(fileData);
    }

    private void loadData() {
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

        mFavAdapter = new FavAdapter(this, favSurahsList, this);

        surahsRecycler.setAdapter(mFavAdapter);
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
                viewItems.add(surahModel);
            }
            mAdapter = new Adapter(viewItems, this, this);
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

    public void backToQuranAudioMain(View view) {
        this.finish();
    }

    @Override
    public void onSurahClick(String position, String selectedSurahName) {
        Intent i = new Intent(QuranIndexAudio.this, ShowQuranAndAudio.class);
        i.putExtra("suraname", selectedSurahName);
        i.putExtra("suraposition", position);
        i.putExtra("qariposition", qariPosition);
        i.putExtra("qariname", qariName);
        i.putExtra("qaripath", qariPath);
        startActivity(i);
    }

    @Override
    public void onFavSurahClick(String position, String selectedSurahName) {
        Intent i = new Intent(QuranIndexAudio.this, ShowQuranAndAudio.class);
        i.putExtra("suraname", selectedSurahName);
        i.putExtra("suraposition", position);
        i.putExtra("qariposition", qariPosition);
        i.putExtra("qariname", qariName);
        i.putExtra("qaripath", qariPath);
        startActivity(i);
    }
}