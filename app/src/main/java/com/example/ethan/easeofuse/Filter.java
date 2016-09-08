package com.example.ethan.easeofuse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

public class Filter extends AppCompatActivity {
    static final int RESULT_GOOD = 879;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        Spinner spinner = (Spinner) findViewById(R.id.types);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void filter(View view){
        Spinner spinner = (Spinner) findViewById(R.id.types);
        String type = spinner.getSelectedItem().toString().toLowerCase();
        RadioButton street = (RadioButton) findViewById(R.id.street);
        boolean streetwear = street.isChecked();
        RadioButton dress = (RadioButton) findViewById(R.id.classy);
        boolean classwear = dress.isChecked();
        String style;
        if(streetwear)
            style = "street";
        else if(classwear)
            style="class";
        else
            style="all";
        Intent i = getIntent();
        i.putExtra("style", style);
        i.putExtra("type", type);
        setResult(RESULT_GOOD, i);
        finish();
    }
}
