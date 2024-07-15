package com.berdanbakan.catchthephoto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView timeText;
    TextView scoreText;
    Button startButton;
    int score;
    int maxScore;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;

    ImageView[] imageArray;
    Handler handler1;
    Runnable runnable;

    int level;
    int initialTime = 10000; // başlangıç zamanı 10 saniye
    int scoreThreshold; // her seviyede geçmek için gereken skor
    int hideDelay; // Görsellerin çıkma hızı

    boolean isClickable; // Görsele sadece bir kez tıklanabileceğini kontrol eder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        startButton = findViewById(R.id.button2); // Start button

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);

        imageArray = new ImageView[]{imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9};

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.berdanbakan.catchthephoto", Context.MODE_PRIVATE);
        maxScore = sharedPreferences.getInt("maxScore", 0);

        resetGame();
    }

    private void resetGame() {
        score = 0;
        level = 1;
        scoreThreshold = 10; // her seviye için gereken skor
        hideDelay = 900; // ilk seviyede görsellerin çıkma hızı
        scoreText.setText("Skor: " + score);
        timeText.setText("Kalan Süre: 10");
        startButton.setVisibility(View.VISIBLE);
        for (ImageView image : imageArray) {
            image.setVisibility(View.INVISIBLE);
        }
    }

    public void startGame(View view) {
        startButton.setVisibility(View.INVISIBLE);
        playLevel(initialTime);
    }

    private void playLevel(int millisInFuture) {
        updateScoreAndTime(); // Skor ve süreyi güncelle

        HideImages();
        new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("Kalan Süre: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timeText.setText("Süre Bitti!");
                handler1.removeCallbacks(runnable); // runnable'ı durdurur
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE); // tüm görüntüleri gizler
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                if (score >= scoreThreshold) {
                    SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.berdanbakan.catchthephoto", Context.MODE_PRIVATE);
                    if (score > maxScore) {
                        maxScore = score;
                        sharedPreferences.edit().putInt("maxScore", maxScore).apply();
                        Toast.makeText(MainActivity.this, "Yeni Rekor: " + maxScore, Toast.LENGTH_LONG).show();
                    }

                    if (level < 3) {
                        alert.setTitle("Seviye Tamamlandı!");
                        alert.setMessage("Sonraki seviyeye geçmek ister misiniz?");
                        alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                level++;
                                hideDelay -= 250; // her seviyede görsellerin çıkma hızını azalt
                                scoreThreshold += 3; // her seviyede gereken skoru azaltır zorlaştığı için
                                playLevel(initialTime + level * 1000); // her seviyede süreyi arttır
                            }
                        });
                        alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Oyun Bitti!", Toast.LENGTH_SHORT).show();
                                resetGame();
                            }
                        });
                    } else {
                        alert.setTitle("Oyun Bitti!");
                        alert.setMessage("Tüm seviyeleri tamamladınız! Tekrar oynamak ister misiniz?");
                        alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetGame();
                            }
                        });
                        alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Oyun Bitti!", Toast.LENGTH_SHORT).show();
                                resetGame();
                            }
                        });
                    }
                } else {
                    alert.setTitle("Oyun Bitti!");
                    alert.setMessage("Tekrar oynamak ister misiniz?");
                    alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetGame();
                        }
                    });
                    alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Oyun Bitti!", Toast.LENGTH_SHORT).show();
                            resetGame();
                        }
                    });
                }
                alert.show();
            }
        }.start();
    }

    public void backActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        startActivity(intent);
    }

    public void increaseScore(View view) {
        if (isClickable) {
            score++;
            scoreText.setText("Skor: " + score);
            isClickable = false; // Görsele bir kez tıklandığını işaretler
        }
    }

    public void HideImages() {
        handler1 = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                Random random = new Random();
                int i = random.nextInt(9); // 0 ile 8 arasında bir sayı üretir
                imageArray[i].setVisibility(View.VISIBLE); // Rastgele bir resmi görünür yapar
                isClickable = true; // Görsele tıklanabilir olduğunu işaretler

                handler1.postDelayed(this, hideDelay); // her seviyede görsellerin çıkma hızını ayarlar
            }
        };
        handler1.post(runnable); // runnable'ı başlatır
    }

    private void updateScoreAndTime() {
        score = 0; // Her seviyede skoru sıfırlar
        scoreText.setText("Skor: " + score);
        timeText.setText("Kalan Süre: " + initialTime / 1000);
    }
}
