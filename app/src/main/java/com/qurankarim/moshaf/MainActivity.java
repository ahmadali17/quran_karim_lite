package com.qurankarim.moshaf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.qurankarim.moshaf.Adiah.AdiahActivity;
import com.qurankarim.moshaf.Azkar.AzkarMain;
import com.qurankarim.moshaf.HesnElmoslem.HesnIndexActivity;
import com.qurankarim.moshaf.Quran.QuranIndexActivity;
import com.qurankarim.moshaf.QuranAudio.QuranAudioMain;
import com.qurankarim.moshaf.Sebhaa.SebhaaActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    CardView quranCard;
    private Toolbar toolbar;


    private InterstitialAd interstitialAd;
    private AdView adViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //adViewMain = findViewById(R.id.adViewMain);
        MobileAds.initialize(this,"ca-app-pub-2923262614703652~2588674782");
        AdRequest adRequest = new AdRequest.Builder().build();
        //adViewMain.loadAd(adRequest);


        /*interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.show();*/

        toolbar = (Toolbar) findViewById(R.id.tool_bar_home);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.report_item) {
            startActivity(new Intent(this, ReportProblemActivity.class));
            return true;
        }

        if (id == R.id.info_item) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        if (id == R.id.evaluation_item) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.da3m_item) {
            startActivity(new Intent(this, Da3mActivity.class));
            return true;
        }

        if (id == R.id.share_item) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quran Lite");
                String shareMessage= "ادعوك لتحمبل (تطبيق القرآن الكريم lite) "+"\n" + "يمكنك من خلاله قراءة القرآن الكريم كاملا مع الاستماع لأكثر من 50 مقراء وتلاوت نادرة كما يوجد في التطبيق أذكار وأدعية وكتاب حصن المسلم كاملا وسبحة"+"\n"+ "حمل التطبيق الان من خلال الرابط : "+"\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.qurankarim.moshaf";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void goToQuran(View view) {

        startActivity(new Intent(this, QuranIndexActivity.class));
    }

    public void goToDoaa(View view) {
        startActivity(new Intent(this, AdiahActivity.class));
    }

    public void goToSebhaa(View view) {
        startActivity(new Intent(this, SebhaaActivity.class));
    }

    public void goToAzkar(View view) {
        startActivity(new Intent(this, AzkarMain.class));
    }

    public void goToQuranAudio(View view) {
        startActivity(new Intent(this, QuranAudioMain.class));
    }

    public void goToHesn(View view) {
        startActivity(new Intent(this, HesnIndexActivity.class));
    }

    public void goToDa3m(View view){
        startActivity(new Intent(this, Da3mActivity.class));
    }
}