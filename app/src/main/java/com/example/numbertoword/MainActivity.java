package com.example.numbertoword;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    ClipboardManager clipboardManager;
    ClipData clipData;
    EditText numberEditText;
    TabLayout tabs;
    TextView wordTextView;
    TextView numberChanged;
    boolean rial;
    boolean tomaan;
    InputMethodManager imm;
    int size;
    Spinner dropDown;

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
        numberChanged = findViewById(R.id.numChangedView);
        numberChanged.setVisibility(View.INVISIBLE);
        Button copyBtn = findViewById(R.id.copyBtn);
        final Button shareBtn = findViewById(R.id.shareBtn);
        final Button clearBtn = findViewById(R.id.clearBtn);
        dropDown = findViewById(R.id.dropDown);
        dropDown.setSelection(shp.getInt("format_Position",0));

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        final SharedPreferences.Editor shpE = shp.edit();


        tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                convertBtn.callOnClick();

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
                    convertProgress(false,false, dropDown.getSelectedItemPosition());
                    numberChanged.setVisibility(View.INVISIBLE);
                }else if (tabs.getSelectedTabPosition() == 1){
                    convertProgress(false,true, dropDown.getSelectedItemPosition());
                }else if (tabs.getSelectedTabPosition() == 2){
                    convertProgress(true,false, dropDown.getSelectedItemPosition());
                }
            }

        });

        View.OnTouchListener dropdownClicked = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    convertBtn.callOnClick();
                    shpE.putInt("format_Position",dropDown.getSelectedItemPosition());
                    shpE.apply();
                }
                return false;
            }
        };

        dropDown.setOnTouchListener(dropdownClicked);

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
                numberChanged.setText(null);
                numberChanged.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void convertProgress(boolean rial, boolean tomaan, int numFormat){

        if (numberEditText.getText().toString().replace(",","").replace("٬","").length() > 18){
            wordTextView.setText(R.string.bigNumberError);

        }else if (numberEditText.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this,R.string.numberNullError,Toast.LENGTH_SHORT).show();

        } else {
            switch (numFormat){
                case 1:
                    if (tomaan){
                        String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
                        wordTextView.setText(HOROF);
                        numberChanged.setText(numberEditText.getText().toString() + " " + getString(R.string.tomaan));
                        numberChanged.setVisibility(View.VISIBLE);
                    }else if (rial){
                        String checkNumber = numberEditText.getText().toString().replace(",","").replace("٬","")+"0";
                        if (checkNumber.length()>18){
                            wordTextView.setText(getString(R.string.bigNumberError));
                            numberChanged.setText(" ");
                            numberChanged.setVisibility(View.VISIBLE);
                        }else {
                            String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(checkNumber), rial, tomaan);
                            wordTextView.setText(HOROF);
                            numberChanged.setText(changedToRial("0"));
                            numberChanged.setVisibility(View.VISIBLE);
                        }

                    }else {
                        String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
                        wordTextView.setText(HOROF);
                        numberChanged.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 2:
                    if (tomaan){
                        String test = numberEditText.getText().toString().replace(",","").replace("٬","");
                        wordTextView.setText("صفر"+ " " +getString(R.string.tomaan));

                        test = test.substring(0,test.length()-1);
                        if (test.length()<1) {
                            wordTextView.setText("صفر" + " " + getString(R.string.tomaan));
                            numberChanged.setText(changedToTomaan());
                            numberChanged.setVisibility(View.VISIBLE);
                        }else {
                            String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(test), rial, tomaan);
                            wordTextView.setText(HOROF);
                            numberChanged.setText(changedToTomaan());
                            numberChanged.setVisibility(View.VISIBLE);
                        }
                    }else if (rial){
                        String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
                        wordTextView.setText(HOROF);
                        numberChanged.setText(numberEditText.getText().toString() + " " +getString(R.string.rial));
                        numberChanged.setVisibility(View.VISIBLE);
                    }else {
                        String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
                        wordTextView.setText(HOROF);
                        numberChanged.setVisibility(View.INVISIBLE);
                    }
                    break;
                default:
                    String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","").replace("٬","")),rial,tomaan);
                    wordTextView.setText(HOROF);
                    numberChanged.setVisibility(View.INVISIBLE);
            }
        }

    }

    //Changed number To Rial DecimalFormat
    public String changedToRial(String num) {

        String text = numberEditText.getText().toString() + num;
        text = text.replace(",", "").replace("٬","");

        DecimalFormat sdd = new DecimalFormat("#,###");
        Long doublenumber = Long.parseLong(text);
        String format = sdd.format(doublenumber);

        return format + " " +getString(R.string.rial);
    }
    //Changed number To Tomaan DecimalFormat
    public String changedToTomaan() {

        String text = numberEditText.getText().toString();

        if (text.length()<=1){
            return "0" + " " + getString(R.string.tomaan);
        }
        text = text.replace(",", "").replace("٬","");
        text = text.substring(0,text.length()-1);

        DecimalFormat sdd = new DecimalFormat("#,###");
        Long doublenumber = Long.parseLong(text);
        String format = sdd.format(doublenumber);

        return format + " " +getString(R.string.tomaan);
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
                Intent infoIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(infoIntent);
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
        numberChanged.setTextSize(size);
        super.onResumeFragments();
    }
}
