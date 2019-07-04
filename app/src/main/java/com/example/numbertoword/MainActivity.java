package com.example.numbertoword;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    ClipboardManager clipboardManager;
    ClipData clipData;
    EditText numberEditText;
    RadioButton RialRadioBtn;
    RadioButton TomaanRadioBtn;
    TabLayout tabs;
    boolean rial;
    boolean tomaan;

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

        numberEditText = findViewById(R.id.editText);
        numberEditText.addTextChangedListener(new MyNumberWatcher());
        RialRadioBtn = findViewById(R.id.rialRadioBtn);
        TomaanRadioBtn = findViewById(R.id.tomaanRadioBtn);
        tabs = findViewById(R.id.tabss);

        final Button convertBtn = findViewById(R.id.convertBtn);
        final TextView wordTextView = findViewById(R.id.wordView);
        final TextView TomaanTV = findViewById(R.id.tomaantv);
        final TextView RialTV = findViewById(R.id.rialtv);
        Button copyBtn = findViewById(R.id.copyBtn);
        final Button shareBtn = findViewById(R.id.shareBtn);
        final Button clearBtn = findViewById(R.id.clearBtn);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    wordTextView.setVisibility(View.VISIBLE);
                    RialTV.setVisibility(View.INVISIBLE);
                    TomaanTV.setVisibility(View.INVISIBLE);

                }else if (tab.getPosition() == 1){
                    wordTextView.setVisibility(View.INVISIBLE);
                    RialTV.setVisibility(View.INVISIBLE);
                    TomaanTV.setVisibility(View.VISIBLE);

                }else if(tab.getPosition() == 2){
                    wordTextView.setVisibility(View.INVISIBLE);
                    RialTV.setVisibility(View.VISIBLE);
                    TomaanTV.setVisibility(View.INVISIBLE);
                }
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
                if (numberEditText.getText().toString().replace(",","").replace("٬","").length() > 18){
                    wordTextView.setText(R.string.bigNumberError);

                }else if (numberEditText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,R.string.numberNullError,Toast.LENGTH_SHORT).show();

                } else {
                    if (RialRadioBtn.isChecked()){
                        rial = true;
                        tomaan = false;
                        TomaanTV.setVisibility(View.INVISIBLE);
                        RialTV.setVisibility(View.VISIBLE);
                        String RIAL = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
                        RialTV.setText(RIAL);

                    }else if (TomaanRadioBtn.isChecked()){
                        tomaan = true;
                        rial = false;
                        RialTV.setVisibility(View.INVISIBLE);
                        TomaanTV.setVisibility(View.VISIBLE);
                        String  TOMAAN= NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
                        TomaanTV.setText(TOMAAN);
                    }else {
                        rial = false;
                        tomaan = false;
                        RialTV.setVisibility(View.INVISIBLE);
                        TomaanTV.setVisibility(View.INVISIBLE);
                    }

                    String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
                    wordTextView.setText(HOROF);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu , menu);
        return true;
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
}
