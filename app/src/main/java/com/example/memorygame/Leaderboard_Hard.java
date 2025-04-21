package com.example.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard_Hard extends AppCompatActivity {

    private LinearLayout leaderboardLayout;

    private static class PlayerScore {
        String name;
        int flips;
        int timeLeft;

        PlayerScore(String name, int flips, int timeLeft) {
            this.name = name;
            this.flips = flips;
            this.timeLeft = timeLeft;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_hard);

        leaderboardLayout = findViewById(R.id.leaderboardLayout);

        List<PlayerScore> leaderboardList = loadLeaderboard();
        Collections.sort(leaderboardList, Comparator.comparingInt(p -> p.flips));

        LayoutInflater inflater = LayoutInflater.from(this);

        for (PlayerScore player : leaderboardList) {
            View itemView = inflater.inflate(R.layout.leaderboard_item, leaderboardLayout, false);

            TextView nameView = itemView.findViewById(R.id.playerName);
            TextView flipsView = itemView.findViewById(R.id.playerFlips);
            TextView timeView = itemView.findViewById(R.id.playerTime);

            nameView.setText(player.name);
            flipsView.setText(player.flips + " flips");
            timeView.setText(player.timeLeft + "s");

            leaderboardLayout.addView(itemView);
        }

        Button hmbtn = findViewById(R.id.backhm);
        hmbtn.setOnClickListener(v1 -> {
            Intent homeIntent = new Intent(Leaderboard_Hard.this, MainActivity.class);
            startActivity(homeIntent);
        });

        // DELETE button logic
        Button deleteBtn = findViewById(R.id.Delete);
        deleteBtn.setOnClickListener(v -> {
            // Clear SharedPreferences data
            SharedPreferences prefs = getSharedPreferences("Leaderboard_Hard", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("scores");
            editor.apply();

            // Remove leaderboard views
            leaderboardLayout.removeAllViews();

            // Show a confirmation message
            Toast.makeText(Leaderboard_Hard.this, "Data deleted", Toast.LENGTH_SHORT).show();
        });
    }

    private List<PlayerScore> loadLeaderboard() {
        SharedPreferences prefs = getSharedPreferences("Leaderboard_Hard", MODE_PRIVATE);
        String leaderboardJson = prefs.getString("scores", "[]");

        List<PlayerScore> scores = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(leaderboardJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("name");
                int flips = obj.getInt("flips");
                int timeLeft = obj.getInt("timeLeft");
                scores.add(new PlayerScore(name, flips, timeLeft));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return scores;
    }
}
