package bd.com.ashfaq.apps;

public class ServiceModel {
    private String service_type;
    private String dist_name;
    private String organization_name;
    private String phone_number;

    public ServiceModel() {
        // Default constructor required for calls to DataSnapshot.getValue(ServiceModel.class)
    }

    public ServiceModel(String service_type, String dist_name, String organization_name, String phone_number) {
        this.service_type = service_type;
        this.dist_name = dist_name;
        this.organization_name = organization_name;
        this.phone_number = phone_number;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getDist_name() {
        return dist_name;
    }

    public void setDist_name(String dist_name) {
        this.dist_name = dist_name;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
