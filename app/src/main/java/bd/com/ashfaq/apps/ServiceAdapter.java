package bd.com.ashfaq.apps;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<ServiceModel> serviceList;
    private Activity activity;

    public ServiceAdapter(Activity activity, List<ServiceModel> serviceList) {
        this.serviceList = serviceList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceModel service = serviceList.get(position);
//        holder.textViewServiceType.setText(service.getService_type());
        holder.textViewDistName.setText(service.getDist_name());
        holder.textViewOrgName.setText(service.getOrganization_name());
        holder.textViewPhoneNumber.setText(service.getPhone_number());


        // Set click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+service.getPhone_number()));
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView textViewServiceType, textViewDistName, textViewOrgName, textViewPhoneNumber;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
//            textViewServiceType = itemView.findViewById(R.id.textViewServiceType);
            textViewDistName = itemView.findViewById(R.id.textViewDistName);
            textViewOrgName = itemView.findViewById(R.id.textViewOrgName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
        }
    }
}
