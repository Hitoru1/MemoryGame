package com.example.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String selectedDifficulty = "";
    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button leaderbtn = findViewById(R.id.Leaderboard);
        leaderbtn.setOnClickListener(v -> showLeaderboardPopup()); // now shows a popup instead of new activity

        Button cardCatalogButton = findViewById(R.id.CardCatalog);
        cardCatalogButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CardCatalogPage.class);
            startActivity(intent);
        });

        Button exitButton = findViewById(R.id.TotalExit);
        exitButton.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });

        Button optionsButton = findViewById(R.id.options);
        Button startMatchButton = findViewById(R.id.startmatch);

        optionsButton.setOnClickListener(v -> showOptionsPopup());

        startMatchButton.setOnClickListener(v -> {
            if (selectedDifficulty.isEmpty() || playerName.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter name and select difficulty first in option button.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent;
            switch (selectedDifficulty) {
                case "Easy":
                    intent = new Intent(MainActivity.this, EasyLevel.class);
                    break;
                case "Medium":
                    intent = new Intent(MainActivity.this, MediumLevel.class);
                    break;
                case "Hard":
                    intent = new Intent(MainActivity.this, HardLevel.class);
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Invalid difficulty", Toast.LENGTH_SHORT).show();
                    return;
            }

            intent.putExtra("playerName", playerName);
            intent.putExtra("difficulty", selectedDifficulty);
            startActivity(intent);
        });
    }

    private void showOptionsPopup() {
        View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_options, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextInputEditText nameInput = popupView.findViewById(R.id.inputtedname);
        Button easyBtn = popupView.findViewById(R.id.easybutton);
        Button mediumBtn = popupView.findViewById(R.id.mediumbutton);
        Button hardBtn = popupView.findViewById(R.id.hardbutton);
        Button closeBtn = popupView.findViewById(R.id.closepopup);

        View.OnClickListener difficultyClickListener = view -> {
            playerName = nameInput.getText().toString().trim();
            if (playerName.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if player name exists in SharedPreferences (Leaderboard)
            if (isNameDuplicate(playerName)) {
                Toast.makeText(MainActivity.this, "Name already exists. Please enter a different name.", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = view.getId();
            if (id == R.id.easybutton) {
                selectedDifficulty = "Easy";
            } else if (id == R.id.mediumbutton) {
                selectedDifficulty = "Medium";
            } else if (id == R.id.hardbutton) {
                selectedDifficulty = "Hard";
            }

            Toast.makeText(MainActivity.this, "Selected: " + selectedDifficulty, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        };

        easyBtn.setOnClickListener(difficultyClickListener);
        mediumBtn.setOnClickListener(difficultyClickListener);
        hardBtn.setOnClickListener(difficultyClickListener);

        closeBtn.setOnClickListener(view -> dialog.dismiss());
    }

    private boolean isNameDuplicate(String playerName) {
        // Check if the player name exists in SharedPreferences (Leaderboard_Easy)
        SharedPreferences prefs = getSharedPreferences("Leaderboard_Easy", MODE_PRIVATE);
        String leaderboardJson = prefs.getString("scores", "[]");

        try {
            JSONArray jsonArray = new JSONArray(leaderboardJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject leaderboardEntry = jsonArray.getJSONObject(i);
                String name = leaderboardEntry.getString("name");
                if (name.equals(playerName)) {
                    return true; // Name is a duplicate
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showLeaderboardPopup() {
        View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_leaderboard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button closeBtn = popupView.findViewById(R.id.closeLeaderboardPopup);
        Button easyBtn = popupView.findViewById(R.id.easy);
        Button mediumBtn = popupView.findViewById(R.id.medium);
        Button hardBtn = popupView.findViewById(R.id.hard);

        closeBtn.setOnClickListener(view -> dialog.dismiss());

        easyBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Leaderboard_Easy.class);
            startActivity(intent);
            dialog.dismiss();
        });

        mediumBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Leaderboard_Medium.class);
            startActivity(intent);
            dialog.dismiss();
        });

        hardBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Leaderboard_Hard.class);
            startActivity(intent);
            dialog.dismiss();
        });
    }
}
