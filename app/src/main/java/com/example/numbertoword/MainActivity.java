package com.example.numbertoword;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    ClipboardManager clipboardManager;
    ClipData clipData;
    EditText numberEditText;
    TabLayout tabs;
    TextView wordTextView;
    boolean rial;
    boolean tomaan;
    InputMethodManager imm;
    int size;

    public String convertToEnglishDigits(String value)
    {
        String newValue = value.replace("١", "1").replace("٢", "2").replace("٣", "3").replace("٤", "4").replace("٥", "5")
                .replace("٦", "6").replace("٧", "7").replace("٨", "8").replace("٩", "9").replace("٠", "0")
                .replace("۱", "1").replace("۲", "2").replace("۳", "3").replace("۴", "4").replace("۵", "5")
                .replace("۶", "6").replace("۷", "7").replace("۸", "8").replace("۹", "9").replace("۰", "0")
                .replace("٬","");

        return newValue;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final SharedPreferences shp = getSharedPreferences("setting",MODE_PRIVATE);
        size = shp.getInt("size",22);

        numberEditText = findViewById(R.id.editText);
        numberEditText.addTextChangedListener(new MyNumberWatcher());
        tabs = findViewById(R.id.tabss);
        tabs.getTabAt(shp.getInt("position",0)).select();

        final Button convertBtn = findViewById(R.id.convertBtn);
        wordTextView = findViewById(R.id.wordView);
        wordTextView.setTextSize(size);
        Button copyBtn = findViewById(R.id.copyBtn);
        final Button shareBtn = findViewById(R.id.shareBtn);
        final Button clearBtn = findViewById(R.id.clearBtn);


        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    convertProgress(false,false);
                    imm.hideSoftInputFromWindow(numberEditText.getWindowToken(),0);
                }else if (tab.getPosition() == 1){
                    tomaan = true;
                    rial = false;
                    convertProgress(rial,tomaan);
                    imm.hideSoftInputFromWindow(numberEditText.getWindowToken(),0);

                }else if(tab.getPosition() == 2){
                    rial = true;
                    tomaan = false;
                    convertProgress(rial,tomaan);
                    imm.hideSoftInputFromWindow(numberEditText.getWindowToken(),0);
                }
                SharedPreferences.Editor shpE = shp.edit();
                shpE.putInt("position",tabs.getSelectedTabPosition());
                shpE.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(numberEditText.getWindowToken(),0);
                if (tabs.getSelectedTabPosition() == 0){
                    convertProgress(false,false);
                }else if (tabs.getSelectedTabPosition() == 1){
                    convertProgress(false,true);
                }else if (tabs.getSelectedTabPosition() == 2){
                    convertProgress(true,false);
                }
            }

        });

        copyBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (wordTextView.getText() == ""){
                    Toast.makeText(MainActivity.this,R.string.copy_Error,Toast.LENGTH_SHORT).show();

                }else {
                    String text = wordTextView.getText().toString();
                    clipData = clipData.newPlainText("text", text);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(MainActivity.this, R.string.copy_Toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wordTextView.getText() == ""){
                    Toast.makeText(MainActivity.this,R.string.share_Toast,Toast.LENGTH_SHORT).show();
                }else {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareContent = wordTextView.getText().toString();
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "تبدیل عدد به حروف");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                    startActivity(Intent.createChooser(shareIntent, "اشتراک در "));
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberEditText.setText(null);
                wordTextView.setText(null);
            }
        });
    }

    public void convertProgress(boolean rial,boolean tomaan){

        if (numberEditText.getText().toString().replace(",","").replace("٬","").length() > 18){
            wordTextView.setText(R.string.bigNumberError);

        }else if (numberEditText.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this,R.string.numberNullError,Toast.LENGTH_SHORT).show();

        } else {
            String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
            wordTextView.setText(HOROF);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.infobar:
                Intent ontent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(ontent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public class MyNumberWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            numberEditText.removeTextChangedListener(this);

            String text = convertToEnglishDigits(numberEditText.getText().toString());

            text = text.replace(",", "").replace("٬","");


            if (text.length() > 0){
                DecimalFormat sdd = new DecimalFormat("#,###");

                    Long doublenumber = Long.parseLong(text);

                String format = sdd.format(doublenumber);
                numberEditText.setText(format);
                numberEditText.setSelection(format.length());
            }

            numberEditText.addTextChangedListener(this);
        }

    }

    @Override
    protected void onResumeFragments() {
        SharedPreferences shp = getSharedPreferences("setting",MODE_PRIVATE);
        size = shp.getInt("size",16);
        wordTextView.setTextSize(size);
        super.onResumeFragments();
    }
}
