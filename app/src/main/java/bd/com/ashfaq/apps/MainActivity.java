package bd.com.ashfaq.apps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<Map<String, String>> serviceList;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;


        gridView = findViewById(R.id.gridView);

        // Initialize the list
        serviceList = new ArrayList<>();

        // Add icons and titles
        addService(1,"icon_police", "Police");
        addService(2, "icon_ambulance", "Ambulance");
        addService(3, "icon_doctor", "Doctor");
        addService(4, "icon_hospital", "Hospital");
        addService(5, "icon_fire", "Fire Service");
        addService(6, "icon_rab", "RAB");
        addService(7, "icon_national_emergency", "Call 999");

        // Set up the adapter
        ServiceAdapter adapter = new ServiceAdapter(serviceList);
        gridView.setAdapter(adapter);

        activity.findViewById(R.id.titleBar).setOnLongClickListener(v -> {
            activity.startActivity(new Intent(activity, AdminPanel.class));
            return true;
        });

    }



    // Method to add service data to the list
    private void addService(@NonNull Integer idX, String iconName, String title) {
        Map<String, String> service = new HashMap<>();
        service.put("id", String.valueOf(idX));
        service.put("icon", iconName);
        service.put("title", title);
        serviceList.add(service);
    }

    public class ServiceAdapter extends BaseAdapter {
        private final ArrayList<Map<String, String>> serviceList;

        public ServiceAdapter(ArrayList<Map<String, String>> serviceList) {
            this.serviceList = serviceList;
        }

        @Override
        public int getCount() {
            return serviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return serviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_services, parent, false);
            }

            ImageView icon = convertView.findViewById(R.id.icon);
            TextView title = convertView.findViewById(R.id.title);

            // Get the service item from the list
            Map<String, String> service = serviceList.get(position);

            // Set the title
            title.setText(service.get("title"));

            // Set the icon dynamically based on the icon name
            String iconName = service.get("icon");
            int iconResId = getResources().getIdentifier(iconName, "drawable", getPackageName());
            icon.setImageResource(iconResId);

            convertView.setOnClickListener(v -> {
                Intent intent = null;
                if(Objects.equals(service.get("id"), "7")) {
                    intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:999"));
                }else{
                    intent = new Intent(activity, NumberListOfService.class);
                    intent.putExtra("id", service.get("id"));
                }

                activity.startActivity(intent);
            });

            return convertView;
        }
    }
}
