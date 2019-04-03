package lat.trust.trusttrifles.network.req;

public class SaveDeviceInfoRequest {
   /* {
        "trust_id": "",
            "dni": "",
            "bundle_id": ""
    }*/

    private String trust_id;
    private String dni;
    private String bundle_id;

    public SaveDeviceInfoRequest() {
    }

    public String getTrust_id() {
        return trust_id;
    }

    public void setTrust_id(String trust_id) {
        this.trust_id = trust_id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getBundle_id() {
        return bundle_id;
    }

    public void setBundle_id(String bundle_id) {
        this.bundle_id = bundle_id;
    }
}
