package com.qurankarim.moshaf.QuranAudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.qurankarim.moshaf.App;
import com.qurankarim.moshaf.DatabaseHelper;
import com.qurankarim.moshaf.Quran.FavAdapter;
import com.qurankarim.moshaf.Quran.FavSurahModel;
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

public class QuranAudioMain extends AppCompatActivity {

    RecyclerView qarisRecyclerView;
    public List<QariModel> viewItems = new ArrayList<>();
    private List<FavQariModel> favQariModelList = new ArrayList<>();


    private QariAdapterNew mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "QarisActivity";

    private FavQariAdapter favAdapter;

    private boolean connected = false;

    private TextView connectiontext;

    private LinearLayout allBtn, favBtn;

    private SearchView searchView;

    private DatabaseHelper favQariDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_audio_main);

        qarisRecyclerView = findViewById(R.id.qaris_recycler_view);
        connectiontext = findViewById(R.id.connection_text);
        qarisRecyclerView.setHasFixedSize(true);

        allBtn = findViewById(R.id.all_btn);
        favBtn = findViewById(R.id.fav_btn);

        favQariDb = new DatabaseHelper(this);

        String fileData = ReadFromFile("qaris.json");

        ConvertStringToJSON(fileData);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qarisRecyclerView.setAdapter(mAdapter);
            }
        });

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFavData();
            }
        });

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
                qarisRecyclerView.setAdapter(mAdapter);
                return false;
            }
        });

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        qarisRecyclerView.setLayoutManager(layoutManager);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        if (!connected) {
            connectiontext.setVisibility(View.VISIBLE);
            qarisRecyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "Please Connect to internet !!", Toast.LENGTH_SHORT).show();
        } else {
            connectiontext.setVisibility(View.GONE);
            qarisRecyclerView.setVisibility(View.VISIBLE);
        }


    }

    private void ConvertStringToJSON(String fileData) {
        //NOTE: For '{' we use JSON Object and for '[' we use JSON Array

        try {
            // Complete Json File
            JSONArray qarisList = new JSONArray(fileData);

            for (int i = 0; i < qarisList.length(); i++) {
                // qari object
                JSONObject qari = qarisList.getJSONObject(i);

                QariModel qariModel = new QariModel(qari.getInt("id"), qari.getString("arabic_name")
                        , qari.getString("relative_path"), "0");

                viewItems.add(qariModel);
            }
            mAdapter = new QariAdapterNew(this, viewItems);
            qarisRecyclerView.setAdapter(mAdapter);

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


    public void loadFavData() {
        if (favQariModelList != null) {
            favQariModelList.clear();
        }
        SQLiteDatabase db = favQariDb.getReadableDatabase();
        Cursor cursor = favQariDb.select_all_favorite_list_qari();
        try {
            while (cursor.moveToNext()) {
                String qariName = cursor.getString(cursor.getColumnIndex(favQariDb.QARI_NAME));
                String id = cursor.getString(cursor.getColumnIndex(favQariDb.KEY_ID1));
                String qariPath = cursor.getString(cursor.getColumnIndex(favQariDb.QARI_RELATIVE_PATH));
                String qariFav = cursor.getString(cursor.getColumnIndex(favQariDb.FAVORITE_STATUS1));
                FavQariModel favQariModel = new FavQariModel(Integer.parseInt(id), qariName, qariPath, qariFav);
                favQariModelList.add(favQariModel);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        favAdapter = new FavQariAdapter(this,favQariModelList);

        qarisRecyclerView.setAdapter(favAdapter);
    }

    public void backToMainActivity(View view) {
        this.finish();
    }
   /* @Override
    public void onQariClick(int position, String qariName, String qariPath) {
        Intent i = new Intent(QuranAudioMain.this, QuranIndexAudio.class);
        i.putExtra("qariposition", position);
        i.putExtra("qariname", qariName);
        i.putExtra("qaripath", qariPath);
        startActivity(i);
    }*/
}