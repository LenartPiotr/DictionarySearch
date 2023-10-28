package programy.piotr.lenart.com.dictionarysearch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    HashMap<Integer, Boolean> selectedDictionaries;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlexboxLayout optionsLayout = findViewById(R.id.optionsLayout);
        EditText editText = findViewById(R.id.inputRegex);
        Button countButton = findViewById(R.id.btCount);
        TextView resultTextView = findViewById(R.id.results);

        selectedDictionaries = new HashMap<>();

        // Load all dictionaries from assets
        Pair<Integer, String>[] assets = new Pair[] {
                new Pair<>(R.raw.wszystkie_slowa, "Wszystkie słowa"),
                new Pair<>(R.raw.panstwa, "Państwa"),
                new Pair<>(R.raw.miasta_polskie, "Polskie miasta")
        };
        for (int i = 0; i < assets.length; i++) {
            Pair<Integer, String> asset = assets[i];
            selectedDictionaries.put(asset.first, false);
            CheckBox checkBox = new CheckBox(MainActivity.this);
            checkBox.setText(asset.second);
            checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
                if (isChecked) selectedDictionaries.put(asset.first, true);
                else selectedDictionaries.put(asset.first, false);
            });
            optionsLayout.addView(checkBox);
        }

        countButton.setOnClickListener(v -> {
            resultTextView.setText("All matching words: " + countAllWords(editText.getText().toString()));
        });
    }

    int countAllWords(String regexText) {
        Pattern pattern = Pattern.compile(regexText, Pattern.CASE_INSENSITIVE);
        int matchWords = 0;
        for (Map.Entry<Integer, Boolean> entry : selectedDictionaries.entrySet()) {
            if (!entry.getValue()) continue;
            InputStream is = getResources().openRawResource(entry.getKey());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    if (pattern.matcher(line).find())
                        matchWords++;
                }
            } catch (IOException ignore) {}
        }
        return matchWords;
    }
}
