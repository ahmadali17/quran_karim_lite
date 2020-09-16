package com.qurankarim.moshaf.QuranAudio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qurankarim.moshaf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class ShowQuranAndAudio extends AppCompatActivity {

    private TextView suraText, basmalaText, qariAndSuraLabel;

    private TextView startTime, suraTime;

    private String suraNumberString;
    private int suraNumber;

    private ImageButton forwardbtn, backwardbtn, pausebtn, playbtn;
    private MediaPlayer mPlayer;
    private SeekBar suraPrgs;
    private static int oTime = 0, sTime = 0, eTime = 0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quran_and_audio);

        suraText = findViewById(R.id.surah_text);
        basmalaText = findViewById(R.id.basmala_text);
        qariAndSuraLabel = findViewById(R.id.qari_and_sura_label);

        playbtn = (ImageButton) findViewById(R.id.btnPlay);
        pausebtn = (ImageButton) findViewById(R.id.btnPause);
        pausebtn.setEnabled(false);

        suraPrgs = (SeekBar) findViewById(R.id.sBar);
        suraPrgs = (SeekBar) findViewById(R.id.sBar);
        suraPrgs.setClickable(false);

        startTime = (TextView) findViewById(R.id.txtStartTime);
        suraTime = (TextView) findViewById(R.id.txtSongTime);

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        suraNumberString = b.getString("suraposition");
        suraNumber = Integer.parseInt(suraNumberString);

        qariAndSuraLabel.setText(b.getString("qariname") + " ( " + b.getString("suraname") + " ) ");

        final String fileData = ReadFromFile("quran.json");

        ConvertStringToJSON(fileData);

        if (suraNumber >= 1 && suraNumber <= 9) {
            suraNumberString = "00" + suraNumberString;
        }
        if (suraNumber >= 10 && suraNumber <= 99) {
            suraNumberString = "0" + suraNumberString;
        }

        url = "https://download.quranicaudio.com/quran/" + b.getString("qaripath") + suraNumberString + ".mp3";
        Log.d("url", "onCreate: "+url);

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowQuranAndAudio.this, "Playing Audio", Toast.LENGTH_SHORT).show();
                mPlayer.start();
                eTime = mPlayer.getDuration();
                sTime = mPlayer.getCurrentPosition();
                if (oTime == 0) {
                    suraPrgs.setMax(eTime);
                    //oTime = 1;
                }
                suraTime.setText(String.format("%d m, %d s", TimeUnit.MILLISECONDS.toMinutes(eTime),
                        TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));

                startTime.setText(String.format("%d m, %d s", TimeUnit.MILLISECONDS.toMinutes(sTime),
                        TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));

                hdlr.postDelayed(UpdateSuraTime, 100);
                pausebtn.setEnabled(true);
                playbtn.setEnabled(false);
            }
        });

        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
                pausebtn.setEnabled(false);
                playbtn.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPlayer.stop();
        mPlayer = new MediaPlayer();
        sTime = 0;
        eTime = 0;
    }

    private Runnable UpdateSuraTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
            startTime.setText(String.format("%d m, %d s", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
            suraPrgs.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };

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

            if (suraNumberString.equals("9")) {
                basmalaText.setVisibility(View.GONE);
            }

            try {
                //suraNameText.setText(suraName);
                //suraNumber = Integer.parseInt(suraNumberString);

                Log.d("llll", "ConvertStringToJSON: " + suraNumber);

                for (int i = 0; i < quran.length(); i++) {
                    JSONObject surah = quran.getJSONObject(i);
                    if (surah.getInt("surah_number") == suraNumber) {
                        suraText.append(surah.getString("content") + " (" + surah.getString("verse_number") + ") ");
                    }
                }
            } catch (Exception e) {
                Log.d("jjj", e.getMessage());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void backToQuranIndexAudio(View view) {
        mPlayer.stop();
        mPlayer = new MediaPlayer();
        sTime = 0;
        eTime = 0;
        this.finish();
    }
}