package com.haziqyamin.zakatgold;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etPrice;
    RadioGroup rgGoldType;
    RadioButton rbKeep, rbWear;
    Button btnCalculate, btnClear;
    CardView cardResults;
    TextView tvTotalValue, tvZakatPayable, tvTotalZakat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set ActionBar title
        getSupportActionBar().setTitle("Zakat Gold Calculator");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round);

        etWeight = findViewById(R.id.etWeight);
        etPrice = findViewById(R.id.etPrice);
        rgGoldType = findViewById(R.id.rgGoldType);
        rbKeep = findViewById(R.id.rbKeep);
        rbWear = findViewById(R.id.rbWear);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        cardResults = findViewById(R.id.cardResults);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvZakatPayable = findViewById(R.id.tvZakatPayable);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);

        btnCalculate.setOnClickListener(v -> calculateZakat());
        btnClear.setOnClickListener(v -> clearFields());

        rbKeep.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rbKeep.setTextColor(isChecked ? 0xFFFFFFFF : 0xFF555555);
        });

        rbWear.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rbWear.setTextColor(isChecked ? 0xFFFFFFFF : 0xFF555555);
        });

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);

        SwitchCompat switchDarkMode = findViewById(R.id.switchDarkMode);
        switchDarkMode.setChecked(isDark);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void calculateZakat() {
        // --- VALIDATION ---
        String weightStr = etWeight.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (weightStr.isEmpty()) {
            etWeight.setError("Weight is required");
            return;
        }
        if (rgGoldType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a gold type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (priceStr.isEmpty()) {
            etPrice.setError("Price is required");
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double price = Double.parseDouble(priceStr);

        if (weight <= 0 || price <= 0) {
            Toast.makeText(this, "Values must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- CALCULATION ---
        int nisab = rbKeep.isChecked() ? 85 : 200;
        double totalGoldValue = weight * price;
        double zakatableWeight = weight - nisab;
        double zakatPayableValue = zakatableWeight > 0 ? zakatableWeight * price : 0;
        double totalZakat = zakatPayableValue * 0.025;

        // --- DISPLAY ---
        cardResults.setVisibility(View.VISIBLE);
        tvTotalValue.setText(String.format("RM %.2f", totalGoldValue));

        if (zakatableWeight <= 0) {
            tvZakatPayable.setText("Zakat Payable Value: RM 0.00\n(Gold does not reach nisab threshold)");
            tvTotalZakat.setText("Total Zakat: RM 0.00 — No zakat required");
        } else {
            tvZakatPayable.setText(String.format("RM %.2f", zakatPayableValue));
            tvTotalZakat.setText(String.format("RM %.2f", totalZakat));
        }
    }

    private void clearFields() {
        etWeight.setText("");
        etPrice.setText("");
        rgGoldType.clearCheck();
        cardResults.setVisibility(View.GONE);
    }

    // --- MENU with Share button ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareApp();
            return true;
        }
        if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out ZakatGold – a gold zakat calculator! https://github.com/yourusername/zakatgold");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}