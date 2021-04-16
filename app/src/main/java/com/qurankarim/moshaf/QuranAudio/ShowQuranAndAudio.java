package com.qurankarim.moshaf.QuranAudio;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qurankarim.moshaf.App;
import com.qurankarim.moshaf.CreateNotification;
import com.qurankarim.moshaf.NotificationActionService;
import com.qurankarim.moshaf.NotificationService;
import com.qurankarim.moshaf.Playable;
import com.qurankarim.moshaf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static android.app.Service.STOP_FOREGROUND_DETACH;
import static com.qurankarim.moshaf.App.CHANNEL_ID;
import static com.qurankarim.moshaf.App.mPlayer;

public class ShowQuranAndAudio extends AppCompatActivity implements Playable {

    private static final String TAG = "ShowQuranAndAudio";
    private TextView suraText, basmalaText, qariAndSuraLabel;

    private TextView startTime, suraTime;

    private String suraNumberString;
    private int suraNumber;

    private ImageView forwardbtn, backwardbtn, pausebtn, playbtn;

    private SeekBar suraPrgs;
    private static int oTime = 0, sTime = 0, eTime = 0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    private String url;

    private String qariName, surahName, qariPath, suraPosition;

    NotificationManager notificationManager;
    boolean isPlaying = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quran_and_audio);

        // start notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), NotificationService.class));
        }

        suraText = findViewById(R.id.surah_text);
        basmalaText = findViewById(R.id.basmala_text);
        qariAndSuraLabel = findViewById(R.id.qari_and_sura_label);
        playbtn = findViewById(R.id.btnPlay);
        pausebtn = findViewById(R.id.btnPause);
        forwardbtn = findViewById(R.id.btn_frd);
        backwardbtn = findViewById(R.id.btn_back);
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
        qariName = b.getString("qariname");
        surahName = b.getString("suraname");
        qariPath = b.getString("qaripath");
        suraPosition = b.getString("suraposition");

        final String fileData = ReadFromFile("quran.json");

        ConvertStringToJSON(fileData);


        if (suraNumber >= 1 && suraNumber <= 9) {
            suraNumberString = "00" + suraNumberString;
        }
        if (suraNumber >= 10 && suraNumber <= 99) {
            suraNumberString = "0" + suraNumberString;
        }

        url = "https://download.quranicaudio.com/quran/" + b.getString("qaripath") + suraNumberString + ".mp3";
        Log.d(TAG, "onCreate: sura url" + url);
        Log.d(TAG, "onCreate: current url" + App.currentUrl);

        if (url.equals(App.currentUrl)) {
            Log.d(TAG, "onCreate: same sura from start");
            if (mPlayer.isPlaying()) {
                playbtn.setVisibility(View.GONE);
                pausebtn.setVisibility(View.VISIBLE);
            } else {
                playbtn.setVisibility(View.VISIBLE);
                pausebtn.setVisibility(View.GONE);
            }
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
        }

        playbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if (mPlayer.isPlaying()) {
                    Log.d(TAG, "onClick: mPlayer is playing");
                    if (!url.equals(App.currentUrl)) {
                        Log.d(TAG, "onClick: new sura");
                        mPlayer.stop();
                        mPlayer.reset();
                        App.currentUrl = url;

                        //TODO add sura and qari
                        App.currentPlaySuraNumber = suraPosition;
                        App.currentPlayQariName = qariName;
                        App.currentPlaySuraName = surahName;
                        App.currentPlayQariPath = qariPath;
                        //mPlayer = new MediaPlayer();
                        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mPlayer.reset();
                            mPlayer.setDataSource(url);
                            mPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        onPlay();
                    } else {
                        Log.d(TAG, "onClick: same sura");
                        startPlay();
                    }
                } else {
                    Log.d(TAG, "onClick: mPlayer not playing");
                    if (!url.equals(App.currentUrl)) {
                        Log.d(TAG, "onClick: new sura");
                        mPlayer.stop();
                        mPlayer.reset();
                        App.currentUrl = url;
                        //TODO add sura and qari
                        App.currentPlaySuraNumber = suraPosition;
                        App.currentPlayQariName = qariName;
                        App.currentPlaySuraName = surahName;
                        App.currentPlayQariPath = qariPath;
                        //mPlayer = new MediaPlayer();
                        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mPlayer.reset();
                            mPlayer.setDataSource(url);
                            mPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        onPlay();

                    } else {
                        Log.d(TAG, "onClick: same sura");
                        startPlay();
                    }
                }
            }
        });

        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseSura();
            }
        });

        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mPlayer.getCurrentPosition();

                int duration = mPlayer.getDuration();

                if (mPlayer.isPlaying() && duration != currentPosition) {
                    currentPosition += 5000;
                    eTime = currentPosition;
                    mPlayer.seekTo(currentPosition);
                }
            }
        });

        backwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mPlayer.getCurrentPosition();

                int duration = mPlayer.getDuration();

                if (mPlayer.isPlaying() && currentPosition > 5000) {
                    currentPosition -= 5000;
                    eTime = currentPosition;
                    mPlayer.seekTo(currentPosition);
                }
            }
        });

        suraPrgs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(progress);
                }
                eTime = mPlayer.getCurrentPosition();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                mPlayer.seekTo(0);
                mPlayer.start();
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action) {
                case CreateNotification.ACTION_PLAY:
                    if (isPlaying) {
                        onPauseSura();
                    } else {
                        onPlay();
                    }
                    break;
            }
        }
    };

    public void startPlay() {
        Log.d(TAG, "startPlay: " + surahName + " " + qariName);
        CreateNotification.createNotification(ShowQuranAndAudio.this, App.currentPlayQariName, App.currentPlaySuraName,
                R.drawable.ic_baseline_pause, 0, 0,true);
        playbtn.setVisibility(View.GONE);
        pausebtn.setVisibility(View.VISIBLE);
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
        isPlaying = true;
    }

    public void stopService() {
        CreateNotification.createNotification(ShowQuranAndAudio.this, App.currentPlayQariName, App.currentPlaySuraName,
                R.drawable.ic_baseline_play, 0, 0,false);
        pausebtn.setVisibility(View.GONE);
        playbtn.setVisibility(View.VISIBLE);
        mPlayer.pause();
        hdlr.removeCallbacks(UpdateSuraTime);
        isPlaying = false;
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
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPlay() {
        startPlay();
    }

    @Override
    public void onPauseSura() {

        stopService();
    }
}