package com.example.memorygame;

import android.os.CountDownTimer;
import android.os.Handler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EasyLevel extends AppCompatActivity {

    private ImageButton[] cards = new ImageButton[12];
    private Integer[] cardImages = {
            R.drawable.img1, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img1, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img6
    };

    private int firstCardIndex = -1;
    private boolean isBusy = false;
    private int flipCount = 0;
    private int matchedPairs = 0;
    private TextView flipCounter;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;
    private long timeLeftInMillis = 30000; // 30 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_level);

        flipCounter = findViewById(R.id.flipCounter);
        timerTextView = findViewById(R.id.timerTextView);

        Collections.shuffle(Arrays.asList(cardImages));

        for (int i = 0; i < cards.length; i++) {
            int resID = getResources().getIdentifier("card_" + i, "id", getPackageName());
            cards[i] = findViewById(resID);
            final int index = i;
            cards[i].setImageResource(R.drawable.card_back);
            cards[i].setOnClickListener(v -> onCardClick(index));
        }

        Intent intent = getIntent();
        String playerName = intent.getStringExtra("playerName");

        TextView nameTextView = findViewById(R.id.playerNameTextView);
        nameTextView.setText(" " + playerName);

        Button bckbutton = findViewById(R.id.backbtn);
        bckbutton.setOnClickListener(v -> {
            Intent mainActivityIntent = new Intent(EasyLevel.this, MainActivity.class);
            startActivity(mainActivityIntent);
        });

        Button pauseButton = findViewById(R.id.pause);
        pauseButton.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            LayoutInflater inflater = LayoutInflater.from(EasyLevel.this);
            View popupView = inflater.inflate(R.layout.popup_pause_easy, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(EasyLevel.this);
            builder.setView(popupView);
            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            Button closeButton = popupView.findViewById(R.id.closepopup);
            closeButton.setOnClickListener(view -> {
                startCountdownTimer();
                dialog.dismiss();
            });

            Button hmbtn = popupView.findViewById(R.id.Home);
            hmbtn.setOnClickListener(v1 -> {
                Intent homeIntent = new Intent(EasyLevel.this, MainActivity.class);
                startActivity(homeIntent);
            });

            Button restartButton = popupView.findViewById(R.id.restartButton);
            restartButton.setOnClickListener(v1 -> {
                resetGame();
                dialog.dismiss();
            });

            Button exitButton = popupView.findViewById(R.id.TotalExit);
            exitButton.setOnClickListener(v1 -> {
                finishAffinity();
                System.exit(0);
            });
        });

        startCountdownTimer();
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int secondsRemaining = (int) millisUntilFinished / 1000;
                int minutes = secondsRemaining / 60;
                int seconds = secondsRemaining % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                showTimeoutDialog();
            }
        }.start();
    }

    private void onCardClick(int index) {
        if (isBusy || cards[index].getTag() != null) return;

        flipCard(cards[index], cardImages[index], true); // flip to front image
        flipCount++;
        flipCounter.setText("" + flipCount);

        if (firstCardIndex < 0) {
            firstCardIndex = index;
        } else {
            isBusy = true;
            if (cardImages[firstCardIndex].equals(cardImages[index])) {
                cards[firstCardIndex].setTag("matched");
                cards[index].setTag("matched");
                matchedPairs++;

                new Handler().postDelayed(this::resetTurn, 400); // let the flip finish before continuing

                if (matchedPairs == 6) {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    showWinDialog();
                }

            } else {
                int previousIndex = firstCardIndex;
                new Handler().postDelayed(() -> {
                    flipCard(cards[previousIndex], R.drawable.card_back, false); // flip back
                    flipCard(cards[index], R.drawable.card_back, false);         // flip back
                    resetTurn();
                }, 800);
            }
        }
    }

    private void resetTurn() {
        firstCardIndex = -1;
        isBusy = false;
    }

    private void flipCard(ImageButton card, int imageResId, boolean showFront) {
        card.animate()
                .rotationY(90)
                .setDuration(100)  // Reduced from 150 to 100 for faster animation
                .withEndAction(() -> {
                    // Change the image halfway through the flip
                    if (showFront) {
                        card.setImageResource(imageResId);
                    } else {
                        card.setImageResource(R.drawable.card_back);
                    }

                    // Complete the flip
                    card.setRotationY(-90);
                    card.animate()
                            .rotationY(0)
                            .setDuration(100)  // Reduced from 150 to 100 for faster animation
                            .start();
                })
                .start();
    }

    private void showTimeoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EasyLevel.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.timeout_popup_easy, null);

        builder.setView(view);
        AlertDialog timeoutDialog = builder.create();
        timeoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeoutDialog.setCancelable(false);

        TextView flipCountTextView = view.findViewById(R.id.flipCountTextView);
        flipCountTextView.setText(" " + flipCount);

        Button closeButton = view.findViewById(R.id.close_timeout);
        closeButton.setOnClickListener(v -> timeoutDialog.dismiss());

        Button restartButton = view.findViewById(R.id.restart_timeout_playagain);
        restartButton.setOnClickListener(v -> {
            timeoutDialog.dismiss();
            resetGame();
        });

        Button exitButton = view.findViewById(R.id.exit_app_timeout);
        exitButton.setOnClickListener(v -> {
            timeoutDialog.dismiss();
            finishAffinity();
            System.exit(0);
        });

        timeoutDialog.show();
    }

    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EasyLevel.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.youwin_popup_easy, null);

        builder.setView(view);
        AlertDialog winDialog = builder.create();
        winDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        winDialog.setCancelable(false);

        TextView flipCountTextView = view.findViewById(R.id.flipCountTextView);
        flipCountTextView.setText(" " + flipCount);

        saveToLeaderboard(getIntent().getStringExtra("playerName"), flipCount, (int) (timeLeftInMillis / 1000));

        Button closebutton = view.findViewById(R.id.close_timeout);
        closebutton.setOnClickListener(v1 -> {
            Intent closeIntent = new Intent(EasyLevel.this, MainActivity.class);
            startActivity(closeIntent);
        });

        Button restartButton = view.findViewById(R.id.restart_timeout_playagain);
        restartButton.setOnClickListener(v -> {
            winDialog.dismiss();
            resetGame();
        });

        Button exitButton = view.findViewById(R.id.exit_app_timeout);
        exitButton.setOnClickListener(v -> {
            winDialog.dismiss();
            finishAffinity();
            System.exit(0);
        });

        winDialog.show();
    }

    private void saveToLeaderboard(String name, int flips, int timeLeft) {
        SharedPreferences prefs = getSharedPreferences("Leaderboard_Easy", MODE_PRIVATE);
        String leaderboardJson = prefs.getString("scores", "[]");

        try {
            JSONArray jsonArray = new JSONArray(leaderboardJson);
            JSONObject newEntry = new JSONObject();
            newEntry.put("name", name);
            newEntry.put("flips", flips);
            newEntry.put("timeLeft", timeLeft);

            jsonArray.put(newEntry);
            prefs.edit().putString("scores", jsonArray.toString()).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void resetGame() {
        flipCount = 0;
        matchedPairs = 0;
        flipCounter.setText("0");

        for (int i = 0; i < cards.length; i++) {
            cards[i].setImageResource(R.drawable.card_back);
            cards[i].setTag(null);
        }

        Collections.shuffle(Arrays.asList(cardImages));

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        timeLeftInMillis = 30000;
        startCountdownTimer();
    }
}