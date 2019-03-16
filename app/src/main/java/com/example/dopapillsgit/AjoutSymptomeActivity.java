package com.example.dopapillsgit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AjoutSymptomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_symptome);

        String date_n = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(new Date());

        TextView tv_date = findViewById(R.id.date);
        tv_date.setText(date_n);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String heure = format.format(calendar.getTime());
        TextView textView = findViewById(R.id.heure);
        textView.setText(heure);
    }
}
