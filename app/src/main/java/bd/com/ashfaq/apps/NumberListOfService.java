package bd.com.ashfaq.apps;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NumberListOfService extends AppCompatActivity {

    private RecyclerView recyclerViewServices;
    private ServiceAdapter serviceAdapter;
    private List<ServiceModel> serviceList = new ArrayList<>();

    private Spinner spinnerFilterServiceType;
    private Spinner spinnerFilterDistName;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_list_of_service);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("services");

        // Initialize UI elements
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));

        spinnerFilterServiceType = findViewById(R.id.spinnerFilterServiceType);
        spinnerFilterDistName = findViewById(R.id.spinnerFilterDistName);

        // Set up adapters for service type and district filters
        setupSpinners();

        // Initially load all data
        loadServices(null, null);

        // Spinner selection listeners for filtering
        spinnerFilterServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedServiceType = spinnerFilterServiceType.getSelectedItem().toString();
                String selectedDistName = spinnerFilterDistName.getSelectedItem().toString();
                loadServices(selectedServiceType, selectedDistName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle when no item is selected
            }
        });

    }

    private void setupSpinners() {
        // Spinner for service types
        String[] serviceTypes = {"All", "POLICE", "AMBULANCE", "RAB", "HOSPITAL", "DOCTORS", "FIRE_SERVICE"};
        ArrayAdapter<String> serviceTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceTypes);
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterServiceType.setAdapter(serviceTypeAdapter);

        // Spinner for districts in Bangladesh
        String[] bangladeshDistricts = {"All", "Dhaka", "Chattogram", "Rajshahi", "Khulna", "Barishal", "Sylhet", "Rangpur", "Mymensingh"};
        ArrayAdapter<String> distAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bangladeshDistricts);
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterDistName.setAdapter(distAdapter);
    }

    private void loadServices(String serviceType, String distName) {
        Query query = databaseReference;

        // Apply filtering if serviceType or distName is selected
        if (serviceType != null && !serviceType.equals("All")) {
            query = query.orderByChild("service_type").equalTo(serviceType);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ServiceModel service = snapshot.getValue(ServiceModel.class);

                    // Filter by district if needed
                    if (distName == null || distName.equals("All") || service.getDist_name().equals(distName)) {
                        serviceList.add(service);
                    }
                }
                serviceAdapter = new ServiceAdapter(serviceList);
                recyclerViewServices.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NumberListOfService.this, "Failed to load services", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
