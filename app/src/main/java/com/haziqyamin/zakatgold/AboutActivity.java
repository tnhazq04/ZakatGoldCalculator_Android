package com.haziqyamin.zakatgold;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    TextView tvGithubLink;
    String githubUrl = "https://github.com/tnhazq04/ZakatGoldCalculator_Android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();

        tvGithubLink = findViewById(R.id.tvGithubLink);
        tvGithubLink.setText(githubUrl);
        tvGithubLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl));
            startActivity(intent);
        });
    }

    public void visitWebsite(android.view.View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}