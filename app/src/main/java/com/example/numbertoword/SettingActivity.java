package com.example.numbertoword;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    TextView changefontTv;
    Button saveBtn;
    SeekBar seekBar;
    int textSize;
    int seekBarStatus;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);

        changefontTv = findViewById(R.id.changefont);
        textSize = sharedPreferences.getInt("size",16);
        changefontTv.setTextSize(textSize);
        saveBtn = findViewById(R.id.saveBtn);
        seekBar = findViewById(R.id.seekBarSize);
        seekBarStatus = sharedPreferences.getInt("seekStatus",20);
        seekBar.setProgress(seekBarStatus);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changefontTv.setTextSize(progress);
                textSize = progress;
                seekBarStatus = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor shpE = sharedPreferences.edit();
                shpE.putInt("size",textSize);
                shpE.putInt("seekStatus",seekBarStatus);
                shpE.apply();

                finish();
            }
        });

    }
}
