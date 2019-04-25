package lat.trust.trusttrifles.network.req;

public class SaveDeviceNotificationRequest {
     /*
    "trust_id": "ee708c21-b844-4eee-90ec-fe005353b2b6",
    "firebase_token": "fY1seSvJ3pc:APA91bE2CAkwiKdyu9dyymeDaQq9zLoRRw1Geh3dwOaDaBKQh9d0ocE01mT96SLuo-dtQygSUjepKvmfFCLKM9Fl69zDl4i0qYeKrhSyAAdrfGZ2UC608MmshsrSSIqacpMpFVakEksw",
    "application_name": "enrollment",
    "plataform": "Android"*/

    private String trust_id;
    private String firebase_token;
    private String bundle_id;
    private String platform;

    public SaveDeviceNotificationRequest() {
    }

    public void setTrust_id(String trust_id) {
        this.trust_id = trust_id;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    public void setBundle_id(String bundle_id) {
        this.bundle_id = bundle_id;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
