package com.example.memorygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ClovePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clove_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton brimBtn = findViewById(R.id.brimcatalog);
        brimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, CardCatalogPage.class);
                startActivity(intent);
            }
        });
        ImageButton chamBtn = findViewById(R.id.chamcatalog);
        chamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, ChamPage.class);
                startActivity(intent);
            }
        });
        ImageButton gekkoBtn = findViewById(R.id.gekkocatalog);
        gekkoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, GekkoPage.class);
                startActivity(intent);
            }
        });
        ImageButton isoBtn = findViewById(R.id.isocatalog);
        isoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, IsoPage.class);
                startActivity(intent);
            }
        });
        ImageButton jettBtn = findViewById(R.id.jettcatalog);
        jettBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, JettPage.class);
                startActivity(intent);
            }
        });
        ImageButton omenBtn = findViewById(R.id.omencatalog);
        omenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, OmenPage.class);
                startActivity(intent);
            }
        });
        ImageButton sageBtn = findViewById(R.id.sagecatalog);
        sageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, SagePage.class);
                startActivity(intent);
            }
        });
        ImageButton skyeBtn = findViewById(R.id.skyecatalog);
        skyeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, SkyePage.class);
                startActivity(intent);
            }
        });
        ImageButton sovaBtn = findViewById(R.id.sovacatalog);
        sovaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, SovaPage.class);
                startActivity(intent);
            }
        });
        Button homebutton = findViewById(R.id.HomeBtn);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClovePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}