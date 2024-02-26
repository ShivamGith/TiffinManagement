package com.svd.tiffinmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Models.TiffinCenter;

public class MainActivity extends AppCompatActivity {

    private List<TiffinCenter> tiffinCenterList;
    private TiffinCenterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tiffinCenterList = new ArrayList<>();
        adapter = new TiffinCenterAdapter(this, tiffinCenterList);
        recyclerView.setAdapter(adapter);
        fetchTiffinCenters();
    }

    private void fetchTiffinCenters() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TiffinCenters");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tiffinCenterList.clear();

                // Iterate through each child node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract individual properties
                    String name = snapshot.child("name").getValue(String.class);
                    String centerId = snapshot.child("centerId").getValue(String.class);
                    String url = snapshot.child("url").getValue(String.class);
                    GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {};
                    String cuisines = TextUtils.join(", ", Objects.requireNonNull(snapshot.child("cuisines").getValue(genericTypeIndicator)));
                    String thumbnail = snapshot.child("thumbnail").getValue(String.class);
                    String menu = snapshot.child("menu").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String rating = snapshot.child("rating").getValue(String.class);

                    // Create a new TiffinCenter object with extracted data
                    TiffinCenter tiffinCenter = new TiffinCenter(name, centerId, url, cuisines, thumbnail, menu, location, rating);

                    // Add the TiffinCenter to the list
                    tiffinCenterList.add(tiffinCenter);
                }


                // Notify the adapter of the data change
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Failed to fetch Tiffin Centers: " + databaseError.getMessage());
                Log.e("Firebase", "Failed to fetch Tiffin Centers", databaseError.toException());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}