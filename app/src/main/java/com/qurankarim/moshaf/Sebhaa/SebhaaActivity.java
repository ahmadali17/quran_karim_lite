package com.qurankarim.moshaf.Sebhaa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qurankarim.moshaf.R;

public class SebhaaActivity extends AppCompatActivity {

    private TextView sebhaaText;
    private ImageView sebhaaButton;
    private Button resetSebhaaBtn;

    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sebhaa);

        sebhaaText = findViewById(R.id.sebhaa_counter);
        sebhaaButton = findViewById(R.id.sebhaa_click);
        resetSebhaaBtn = findViewById(R.id.reset_sebhaa);

        sebhaaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                sebhaaText.setText(String.valueOf(counter));
            }
        });

        resetSebhaaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                sebhaaText.setText(String.valueOf(counter));
            }
        });
    }

    public void backToMainActivity(View view){
        this.finish();
    }
}