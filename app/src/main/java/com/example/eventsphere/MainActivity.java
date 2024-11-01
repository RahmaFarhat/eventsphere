package com.example.eventsphere;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.example.eventsphere.entity.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextName, editTextDate, editTextLocation, editTextDescription;
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAdd = findViewById(R.id.buttonAdd);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Add button click listener
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });
    }

    private void addEvent() {
        String name = editTextName.getText().toString();
        String date = editTextDate.getText().toString();
        String location = editTextLocation.getText().toString();
        String description = editTextDescription.getText().toString();

        if (name.isEmpty() || date.isEmpty() || location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("date", date);
        values.put("location", location);
        values.put("description", description);
        db.insert("events", null, values);
        db.close();

        // Clear input fields
        editTextName.setText("");
        editTextDate.setText("");
        editTextLocation.setText("");
        editTextDescription.setText("");

        Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();

        // Navigate to EventListActivity
        Intent intent = new Intent(MainActivity.this, EventListActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();
    }
}
