package bd.com.ashfaq.apps;

import static bd.com.ashfaq.apps.CustomTools.log;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminPanel extends Activity {

    private TextView tvPersonSpecialization;
    private Spinner spinnerServiceType, spinnerDistName, spinnerPersonSpecialization;
    private EditText editTextOrganizationName, editTextPhoneNumber, editTextPersonName, editTextServiceArea;
    private Button buttonSubmit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Initialize UI components
        spinnerServiceType = findViewById(R.id.spinnerServiceType);
        spinnerDistName = findViewById(R.id.spinnerDistName);
        spinnerPersonSpecialization = findViewById(R.id.spinnerPersonSpecialization); // Spinner for specialization
        tvPersonSpecialization = findViewById(R.id.tvPersonSpecialization); // Spinner for specialization
        editTextOrganizationName = findViewById(R.id.editTextOrganizationName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextServiceArea = findViewById(R.id.editTextServiceArea);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        progressBar = findViewById(R.id.progressBar);

        // Populate the spinners with data
        populateServiceTypeSpinner();
        populateDistrictSpinner();

        // Set service type change listener
        spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateSpecializationSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        //
        final Integer[] x = {3};
        ((EditText)findViewById(R.id.et_passcode)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 12){
                    TextView tvResultPass = findViewById(R.id.tvResultPass);
                    if (String.valueOf(s).equalsIgnoreCase("324462324462")){
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        findViewById(R.id.et_passcode).clearFocus();
                        findViewById(R.id.rlCheckPassCode).setVisibility(View.GONE);
                        // Set the button click listener
                        buttonSubmit.setOnClickListener(v -> submitData());
                    }else{
                        x[0]--;
                        tvResultPass.setText(x[0] + " attempt left");
                        if (x[0] == 0){
                            finishAffinity();
                            System.exit(0);
                        }
                    }
                    s.clear();
                }
            }
        });

    }

    private void populateServiceTypeSpinner() {
        // Retrieve the service types from StaticData
        ArrayList<Map<String, String>> services = StaticData.getMyAppServices();
        ArrayList<String> serviceTitles = new ArrayList<>();

        // Extract only the titles for the spinner
        for (Map<String, String> service : services) {
            if (!Objects.equals(service.get("id"), "0")) {
                serviceTitles.add(service.get("title"));
            }
        }

        // Create an ArrayAdapter for the service spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(adapter);
    }

    private void populateDistrictSpinner() {
        // Retrieve the district names from StaticData
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, StaticData.bangladeshDistricts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistName.setAdapter(adapter);
    }

    private void updateSpecializationSpinner() {
        String selectedService = spinnerServiceType.getSelectedItem().toString();
        String[] specializationOptions;

        // Check which service type is selected and populate specialization accordingly
        switch (selectedService) {
            case "Doctor":
                specializationOptions = StaticData.doctorsSpecialist;
                break;
            case "Police":
                specializationOptions = StaticData.policeDesignations;
                break;
            case "RAB":
                specializationOptions = StaticData.rabDesignations;
                break;
            case "Fire Service":
                specializationOptions = StaticData.fireServiceDesignations;
                break;
            default:
                specializationOptions = new String[]{};
                break;
        }

        // If there are no specializations for the selected service type, hide the spinner
        if (specializationOptions.length == 0) {
            spinnerPersonSpecialization.setVisibility(View.GONE);
            tvPersonSpecialization.setVisibility(View.GONE);
            editTextPersonName.setVisibility(View.GONE);
        } else {
            spinnerPersonSpecialization.setVisibility(View.VISIBLE);
            tvPersonSpecialization.setVisibility(View.VISIBLE);
            editTextPersonName.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specializationOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPersonSpecialization.setAdapter(adapter);
        }
    }

    private void submitData() {
        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Prepare data to send
        ArrayList<Map<String, String>> services = StaticData.getMyAppServices();
        String serviceType = null;
        // Extract only the titles for the spinner
        for (Map<String, String> service : services) {
            if (Objects.equals(service.get("title"), spinnerServiceType.getSelectedItem().toString())) {
                serviceType = service.get("id");
                break;
            }
        }

        String districtName = spinnerDistName.getSelectedItem().toString();
        String organizationName = editTextOrganizationName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String personName = editTextPersonName.getText().toString();
        String serviceArea = editTextServiceArea.getText().toString();
        String personSpecialization = "";

        // If the specialization spinner is visible, get the selected specialization
        if (spinnerPersonSpecialization.getVisibility() == View.VISIBLE) {
            personSpecialization = spinnerPersonSpecialization.getSelectedItem().toString();
        }

        // Validate required fields
        if (phoneNumber.isEmpty()) {
            CustomTools.toast(this, "Please fill in all required fields.");
            progressBar.setVisibility(View.GONE);
            return;
        }

        // Create a map to hold the form data
        Map<String, String> formData = new HashMap<>();
        formData.put("service_type", serviceType);
        formData.put("service_city", districtName);
        formData.put("organization_name", organizationName);
        formData.put("phone_number", phoneNumber);
        formData.put("person_name", personName);
        formData.put("service_area", serviceArea);
        formData.put("person_specialization", personSpecialization); // Add specialization to form data
        formData.put("add_es", "1");

        // Call the Internet3 class to handle the network request
        new Internet3(this, CustomTools.url("es.php"), formData, (code, result) -> {
            // Hide the progress bar
            progressBar.setVisibility(View.GONE);

            // Handle the result (success or error)
            if (code == 200) {
                CustomTools.toast(AdminPanel.this, "Data submitted successfully!");
                log(result);
            } else {
                CustomTools.toast(AdminPanel.this, "Failed to submit data.");
            }
        }).connect();
    }
}
