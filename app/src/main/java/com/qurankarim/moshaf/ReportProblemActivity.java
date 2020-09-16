package com.qurankarim.moshaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReportProblemActivity extends AppCompatActivity {

    private EditText emailEditText, reportEditText;
    private Button reportButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, String> problem = new HashMap<>();

    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);

        emailEditText = findViewById(R.id.email_text);
        reportEditText = findViewById(R.id.report_text);
        reportButton = findViewById(R.id.report_button);
        adView = findViewById(R.id.adViewMain);

        MobileAds.initialize(this,App.AppAdId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean connected;
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }

                if (!connected) {
                    Toast.makeText(ReportProblemActivity.this, "Please Connect to internet !!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!reportEditText.getText().toString().equals("")) {
                        problem.put("email", emailEditText.getText().toString());
                        problem.put("problem", reportEditText.getText().toString());
                        db.collection("problems").document().set(problem);
                        Toast.makeText(ReportProblemActivity.this, "تم الابلاغ و شكرا لك", Toast.LENGTH_SHORT).show();
                        reportEditText.setText("");
                        reportEditText.setHint("برجاء شرح المشكلة");
                        emailEditText.setText("");
                        emailEditText.setHint("برجاء كتابة البريد الخاص بكم (غير ضروري)");
                    } else {
                        Toast.makeText(ReportProblemActivity.this, "برجاء كتابة المشكلة", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    public void goBackToMainActivity(View view) {
        this.finish();
    }
}