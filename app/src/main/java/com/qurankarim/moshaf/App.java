package com.qurankarim.moshaf;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class App extends Application {

    public static FirebaseFirestore mDb;
    public static String userDocId;
    private static UserData currentUser = new UserData();

    public static String androidId;

    public static String nativeAdId = "ca-app-pub-2923262614703652/4313460628";
    public static String da3mAdId = "ca-app-pub-2923262614703652/2683031796";
    public static String bannerAdId = "ca-app-pub-2923262614703652/5055696650";
    public static String AppAdId = "ca-app-pub-2923262614703652~7681859993";

    public static String AppId = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxBMOCcyf3A93utc4AUJaGez+NTfQLO0mkvwH4wwPSO9p53TZY52JlFzRIW32ezswBfepwtrADbljkKthgm7hQD2utLM8pTtr1KL2K9iV32E6NmdR7r99apIlQGwgHa3cmJ9SwKofuxS2bA7vbCZHVMjC6eW1u2KLpHew89ntdjn42A70TfyrYREQI2oaMQ6Tn7znaI2RDLVqcvlt+EbY1LsftxfiFGQYqox4y55XIVrD/gkl49mHvmt9bk70QKfanlangK1Aq0/OeYdt5UHTCCGKCXAqHRMsjBAceTMnGyoza4jTt7jWuh1WNhZ0tv2jN/U3+KB//+EBFIIqGxbfBwIDAQAB";
    private DocumentReference userDocumentReference;

    //public static final String nativeAdMobUID = "";

    public static final String CHANNEL_ID = "exampleService";

    public static MediaPlayer mPlayer;
    public static String currentUrl;
    public static String currentPlaySuraNumber;
    public static String currentPlaySuraName;
    public static String currentPlayQariName;
    public static String currentPlayQariPath;
    public static boolean isPlaying = false;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String BOOKMARK_SURA_NUMBER = "bookmarkSuraNumber";
    public static SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();

        mPlayer = new MediaPlayer();
        mPlayer.setLooping(true);
        createNotificationChannel();

        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        try {
            FirebaseApp.initializeApp(this);
            mDb = FirebaseFirestore.getInstance();

            androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            Log.d("kkkkkk", "onCreate: " + androidId);


            mDb.collection("users")
                    .whereEqualTo("userAndroidId", androidId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            if (documents.size() > 0) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    userDocId = documentSnapshot.getId();
                                    DocumentReference ref = documentSnapshot.getReference();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String currentDateTime = dateFormat.format(new Date());
                                    ref.update("lastLogin", currentDateTime);
                                    Log.d("kkkkkk", "onComplete: " + userDocId);
                                }
                            } else {
                                currentUser.setUserAndroidId(androidId);
                                currentUser.setUserName("بدون اسم");
                                currentUser.setUserPoints(0);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateTime = dateFormat.format(new Date());
                                Log.d("ttttt", "onComplete: " + currentDateTime);
                                currentUser.setLoginDate(currentDateTime);
                                userDocumentReference = mDb.collection("users").document();
                                userDocumentReference.set(currentUser);
                                userDocId = userDocumentReference.getId();
                                Log.d("kkkkkk", "onComplete: " + userDocId);
                            }
                        }
                    });

        } catch (Exception ex) {

        }
    }




    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
