package bd.com.ashfaq.apps;

import static bd.com.ashfaq.apps.StaticData.bangladeshDistricts;
import static bd.com.ashfaq.apps.StaticData.getMyAppServices;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminPanel extends AppCompatActivity {
    private Spinner spinnerServiceType;
    private Spinner spinnerDistName;
    private EditText editTextOrganizationName, editTextPhoneNumber;
    private Button buttonSubmit;

    // Firebase Database references
    private DatabaseReference databaseReference;
    private DatabaseReference counterReference;
    private ProgressBar progressBar;
    private long serviceCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("services");
        counterReference = FirebaseDatabase.getInstance().getReference("services_counter"); // Initialize the counter reference

        // Initialize UI elements
        spinnerServiceType = findViewById(R.id.spinnerServiceType);
        spinnerDistName = findViewById(R.id.spinnerDistName);
        editTextOrganizationName = findViewById(R.id.editTextOrganizationName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        progressBar = findViewById(R.id.progressBar);

        // Set up spinner for service types
        ArrayList<String>  serviceType = new ArrayList<>();
        for(Map<String, String> serviceList : getMyAppServices()){
            if (!serviceList.get("id").equals("0")){
                serviceType.add(serviceList.get("id"));
            }
        }
        ArrayAdapter serviceTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceType);
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(serviceTypeAdapter);

        // Set up spinner for Bangladesh
        ArrayAdapter<String> distAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bangladeshDistricts);
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistName.setAdapter(distAdapter);

        // Submit button click listener
        buttonSubmit.setOnClickListener(v -> {
            if(editTextPhoneNumber.length() < 3 || editTextPhoneNumber.length() > 12){
                Toast.makeText(AdminPanel.this, "Phone number must be 11 digits", Toast.LENGTH_SHORT).show();
            }else{
                progressBar.setVisibility(View.VISIBLE);
                submitData();
            }
        });
    }

    private void submitData() {
        // Initialize counterReference to keep track of the service counter
        counterReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Long currentCounter = currentData.getValue(Long.class);
                if (currentCounter == null) {
                    currentCounter = 0L;  // Initialize the counter if it doesn't exist
                }

                // Increment the counter
                serviceCounter = currentCounter + 1;
                currentData.setValue(serviceCounter);  // Update the counter in Firebase
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (committed) {
                    // Get user input
                    String serviceType = spinnerServiceType.getSelectedItem().toString();
                    String distName = spinnerDistName.getSelectedItem().toString();
                    String organizationName = editTextOrganizationName.getText().toString();
                    String phoneNumber = editTextPhoneNumber.getText().toString();

                    // Create a data map
                    Map<String, String> serviceData = new HashMap<>();
                    serviceData.put("service_type", serviceType);
                    serviceData.put("organization_name", organizationName);
                    serviceData.put("dist_name", distName);
                    serviceData.put("phone_number", phoneNumber);
                    serviceData.put("id", String.valueOf(serviceCounter));  // Use the counter as the ID

                    // Push data to Firebase Realtime Database using the counter as the unique ID
                    databaseReference.child(String.valueOf(serviceCounter)).setValue(serviceData)
                            .addOnSuccessListener(aVoid -> {
                                // Clear the input fields after successful submission
//                                editTextOrganizationName.setText("");
                                editTextPhoneNumber.setText("");
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AdminPanel.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Toast.makeText(AdminPanel.this, "Failed to submit data: " + e.getMessage(), Toast.LENGTH_LONG).show());
                } else {
                    if (error != null) {
                        Toast.makeText(AdminPanel.this, "Transaction failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AdminPanel.this, "Transaction was not committed.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
