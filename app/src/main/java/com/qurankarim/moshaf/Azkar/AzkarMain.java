package com.qurankarim.moshaf.Azkar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qurankarim.moshaf.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AzkarMain extends AppCompatActivity {

    private Button azkarSabah, azkarMasaa, azkarPray;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar_main);

        azkarSabah = findViewById(R.id.azkar_sabah);
        azkarMasaa = findViewById(R.id.azkar_masaa);
        azkarPray = findViewById(R.id.azkar_pray);
        adView = findViewById(R.id.adViewMain);

        MobileAds.initialize(this,"ca-app-pub-2923262614703652~9605870668");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        azkarSabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AzkarMain.this,AzkarShow.class);
                i.putExtra("azkarname","azkarasbah.json");
                startActivity(i);
            }
        });

        azkarMasaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AzkarMain.this,AzkarShow.class);
                i.putExtra("azkarname","azkarmasaa.json");
                startActivity(i);
            }
        });

        azkarPray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AzkarMain.this,AzkarShow.class);
                i.putExtra("azkarname","azkarafterpray.json");
                startActivity(i);
            }
        });

    }


    public void backToMainActivity(View view){
        this.finish();
    }
}