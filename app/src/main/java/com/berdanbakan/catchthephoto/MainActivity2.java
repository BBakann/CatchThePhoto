package com.berdanbakan.catchthephoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    TextView maxScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        maxScoreText = findViewById(R.id.maxScoreText);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.berdanbakan.catchthephoto", Context.MODE_PRIVATE);
        int maxScore = sharedPreferences.getInt("maxScore", 0);
        maxScoreText.setText("Max Skor: " + maxScore);

        // Toast message for "Made by Berdan Bakan"
        Toast.makeText(this, "Made by Berdan Bakan", Toast.LENGTH_SHORT).show();
    }

    public void startActivity(View view) {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }
}
