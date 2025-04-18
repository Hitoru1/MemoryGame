package com.example.memorygame;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private String selectedDifficulty = "";
    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cardCatalogButton = findViewById(R.id.CardCatalog);
        cardCatalogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CardCatalogActivity
                Intent intent = new Intent(MainActivity.this, CardCatalogPage.class);
                startActivity(intent);
            }
        });
        Button exitButton = findViewById(R.id.TotalExit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
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
}
