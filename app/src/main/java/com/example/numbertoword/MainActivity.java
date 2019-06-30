package com.example.numbertoword;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    ClipboardManager clipboardManager;
    ClipData clipData;
    EditText numberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberEditText = findViewById(R.id.editText);
        numberEditText.addTextChangedListener(new MyNumberWatcher());

//        Toolbar myToolbar = findViewById(R.id.toolbar);
//        getSupportActionBar();
//        ActionBar actionBar = getSupportActionBar();
//

        Button convertBtn = findViewById(R.id.convertBtn);
        final TextView wordTextView = findViewById(R.id.wordView);
        Button copyBtn = findViewById(R.id.copyBtn);
        final Button shareBtn = findViewById(R.id.shareBtn);
        final Button clearBtn = findViewById(R.id.clearBtn);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberEditText.getText().toString().replace(",","").length() > 18){
                    wordTextView.setText(R.string.bigNumberError);

                }else if (numberEditText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,R.string.numberNullError,Toast.LENGTH_SHORT).show();

                } else {
                    String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString().replace(",","")));
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

            String text = numberEditText.getText().toString();

            text = text.replace(",", "");

            if (text.length() > 0){
                DecimalFormat sdd = new DecimalFormat("#,###");
                Double doublenumber = Double.parseDouble(text);

                String format = sdd.format(doublenumber);
                numberEditText.setText(format);
                numberEditText.setSelection(format.length());
            }

            numberEditText.addTextChangedListener(this);
        }
    }
}
