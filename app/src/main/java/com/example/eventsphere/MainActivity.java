package com.example.eventsphere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView; // Importer RecyclerView

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.example.eventsphere.entity.Event;
import com.example.eventsphere.entity.EventAdapter; // Importer l'adaptateur
import com.example.eventsphere.entity.DatabaseHelper;

import java.util.ArrayList; // Importer ArrayList
import java.util.List; // Importer List

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextName, editTextDate, editTextLocation, editTextDescription;
    private Button buttonAdd;
    private RecyclerView recyclerView; // Déclaration du RecyclerView
    private EventAdapter eventAdapter; // Adaptateur pour la liste des événements
    private List<Event> eventList; // Liste des événements

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les vues
        editTextName = findViewById(R.id.editTextName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAdd = findViewById(R.id.buttonAdd);

        // Initialiser le RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Définir le LayoutManager
        eventList = new ArrayList<>(); // Initialiser la liste des événements
        eventAdapter = new EventAdapter(eventList, this); // Créer l'adaptateur
        recyclerView.setAdapter(eventAdapter); // Définir l'adaptateur pour le RecyclerView

        // Initialiser l'assistant de base de données
        databaseHelper = new DatabaseHelper(this);

        // Ajouter un écouteur de clic pour le bouton
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        // Charger les événements dès que l'activité est créée
        loadEvents();
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

        // Effacer les champs de saisie
        editTextName.setText("");
        editTextDate.setText("");
        editTextLocation.setText("");
        editTextDescription.setText("");

        Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();

        // Charger les événements
        loadEvents(); // Mettre à jour la liste après ajout
    }

    // Méthode pour supprimer un événement
    public void deleteEvent(int eventId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("events", "id = ?", new String[]{String.valueOf(eventId)});
        db.close();
        Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
        loadEvents(); // Recharger les événements après suppression
    }

    // Méthode pour mettre à jour un événement
    public void updateEvent(Event event) {
        Intent intent = new Intent(this, UpdateEventActivity.class);
        intent.putExtra("eventId", event.getId());
        intent.putExtra("eventName", event.getName());
        intent.putExtra("eventDate", event.getDate());
        intent.putExtra("eventLocation", event.getLocation());
        intent.putExtra("eventDescription", event.getDescription());
        startActivity(intent);
    }

    private void loadEvents() {
        eventList.clear(); // Vider la liste avant de charger
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM events", null); // Charger les événements

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0); // Récupérer l'ID
                String name = cursor.getString(1); // Récupérer le nom
                String date = cursor.getString(2); // Récupérer la date
                String location = cursor.getString(3); // Récupérer la localisation
                String description = cursor.getString(4); // Récupérer la description

                // Ajouter un nouvel événement à la liste
                eventList.add(new Event(id, name, date, location, description));
            } while (cursor.moveToNext()); // Continuer jusqu'à ce qu'il n'y ait plus d'événements
        }

        cursor.close(); // Fermer le curseur
        db.close(); // Fermer la base de données
        eventAdapter.notifyDataSetChanged(); // Notifier l'adaptateur que la liste a changé
    }
}
