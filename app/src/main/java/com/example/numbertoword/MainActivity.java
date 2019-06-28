package com.example.numbertoword;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        Button convertBtn = findViewById(R.id.convertBtn);
        final TextView wordTextView = findViewById(R.id.wordView);
        Button copyBtn = findViewById(R.id.copyBtn);
        final Button addBtn = findViewById(R.id.addBtn);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberEditText.getText().length() > 18){
                    wordTextView.setText(R.string.bigNumberError);
                }else {
                    String HOROF = NumAdapter.convertNumberToWords(Long.parseLong(numberEditText.getText().toString()));
                    wordTextView.setText(HOROF);
                }
            }

        });

        copyBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String text = wordTextView.getText().toString();
                clipData = clipData.newPlainText("text",text);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this,R.string.copy_Toast,Toast.LENGTH_LONG).show();

            }
        });

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
