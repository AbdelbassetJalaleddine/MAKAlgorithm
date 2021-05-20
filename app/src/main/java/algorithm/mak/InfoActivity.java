package algorithm.mak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    TextView textViewPatch,textViewEditScript;
    EditText editTextEditDistance,editTextSimilarity,editTextRNA1,editTextRNA2;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        textViewPatch = findViewById(R.id.textViewPatch);

        textViewEditScript = findViewById(R.id.textViewEditScript);

        editTextEditDistance = findViewById(R.id.editTextEditDistance);
        editTextSimilarity = findViewById(R.id.editTextSimilarity);
        editTextRNA1 = findViewById(R.id.editTextRNA1);
        editTextRNA2 = findViewById(R.id.editTextRNA2);

        double editDist = getIntent().getDoubleExtra("editDistance",0.0);


        String editScript = getIntent().getStringExtra("editScript");

        String patchString = getIntent().getStringExtra("patch");

        double similarity = getIntent().getDoubleExtra("similarity",0.0);

        String str1 = getIntent().getStringExtra("str1");

        String str2 = getIntent().getStringExtra("str2");

        textViewPatch.setText("Patch: \n" + patchString);
        textViewEditScript.setText("Edit Script: \n" + editScript);


        editTextEditDistance.setText(editDist + "");
        editTextEditDistance.setEnabled(false);
        editTextSimilarity.setText(similarity + "");
        editTextSimilarity.setEnabled(false);
        editTextRNA1.setText(str1);
        editTextRNA1.setEnabled(false);
        editTextRNA2.setText(str2);
        editTextRNA2.setEnabled(false);

    }
}