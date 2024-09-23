package bd.com.ashfaq.apps;

import static bd.com.ashfaq.apps.StaticData.bangladeshDistricts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
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

    private Spinner spinnerFilterDistName;

    private DatabaseReference databaseReference;

    private ProgressBar progressBar;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_list_of_service);

        activity = this;

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("services");

        // Initialize UI elements
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));

        spinnerFilterDistName = findViewById(R.id.spinnerFilterDistName);

        // Set up adapters for service type and district filters
        setupSpinners();

        // Initially load all data
        String serviceType = "POLICE";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(bundle.containsKey("serviceType")) {
                serviceType = bundle.getString("serviceType");
            }
        }

        loadServices(serviceType, null);


    }

    private void setupSpinners() {

        // Spinner for districts in Bangladesh

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
                serviceAdapter = new ServiceAdapter(activity, serviceList);
                recyclerViewServices.setAdapter(serviceAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NumberListOfService.this, "Failed to load services", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
