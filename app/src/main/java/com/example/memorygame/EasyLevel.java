package com.example.memorygame;

import android.os.Handler;
import android.content.Intent;
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
    private TextView flipCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_level);

        flipCounter = findViewById(R.id.flipCounter);

        // Shuffle images
        Collections.shuffle(Arrays.asList(cardImages));

        // Init cards
        for (int i = 0; i < cards.length; i++) {
            int resID = getResources().getIdentifier("card_" + i, "id", getPackageName());
            cards[i] = findViewById(resID);
            final int index = i;
            cards[i].setImageResource(R.drawable.card_back);
            cards[i].setOnClickListener(v -> onCardClick(index));
        }

        // Get the player name and difficulty from the intent
        Intent intent = getIntent();
        String playerName = intent.getStringExtra("playerName");
        String difficulty = intent.getStringExtra("difficulty");

        // Display the player's name and selected difficulty on the screen
        TextView nameTextView = findViewById(R.id.playerNameTextView);
        TextView difficultyTextView = findViewById(R.id.difficultyTextView);

        nameTextView.setText(" " + playerName);

        Button bckbutton = findViewById(R.id.backbtn);
        bckbutton.setOnClickListener(v -> {
            Intent mainActivityIntent = new Intent(EasyLevel.this, MainActivity.class);
            startActivity(mainActivityIntent);
        });


        Button pauseButton = findViewById(R.id.pause);
        pauseButton.setOnClickListener(v -> {
            // Inflate the pause popup layout
            LayoutInflater inflater = LayoutInflater.from(EasyLevel.this);
            View popupView = inflater.inflate(R.layout.popup_pause_easy, null);

            // Create and show the pause dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(EasyLevel.this);
            builder.setView(popupView);
            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            // Handle close button click
            Button closeButton = popupView.findViewById(R.id.closepopup);
            closeButton.setOnClickListener(view -> dialog.dismiss());

            // Handle Home button click
            Button hmbtn = popupView.findViewById(R.id.Home);
            hmbtn.setOnClickListener(v1 -> {
                Intent homeIntent = new Intent(EasyLevel.this, MainActivity.class);
                startActivity(homeIntent);
            });

            // Handle Exit button click
            Button exitButton = popupView.findViewById(R.id.TotalExit);
            exitButton.setOnClickListener(v1 -> {
                finishAffinity();
                System.exit(0);
            });
            Button restartButton = popupView.findViewById(R.id.restartButton);
                                                                                                                                                                   restartButton.setOnClickListener(v1 -> {
                // Reset the flip count
                flipCount = 0;
                flipCounter.setText("0");

                // Reset all card images to card_back
                for (int i = 0; i < cards.length; i++) {
                    cards[i].setImageResource(R.drawable.card_back);
                    cards[i].setTag(null);  // Clear any tag associated with the cards
                }

                // Shuffle the cards again (optional, based on your design)
                Collections.shuffle(Arrays.asList(cardImages));

                // Close the dialog after the reset
                dialog.dismiss();
            });
        });
    }

    private void onCardClick(int index) {
        if (isBusy || cards[index].getTag() != null) return;

        cards[index].setImageResource(cardImages[index]);
        flipCount++;
        flipCounter.setText("" + flipCount);

        if (firstCardIndex < 0) {
            firstCardIndex = index;
        } else {
            isBusy = true;
            if (cardImages[firstCardIndex].equals(cardImages[index])) {
                // Match
                cards[firstCardIndex].setTag("matched");
                cards[index].setTag("matched");
                resetTurn();
            } else {
                // No match
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    cards[firstCardIndex].setImageResource(R.drawable.card_back);
                    cards[index].setImageResource(R.drawable.card_back);
                    resetTurn();
                }, 1000);
            }
        }
    }

    private void resetTurn() {
        firstCardIndex = -1;
        isBusy = false;
    }
}
