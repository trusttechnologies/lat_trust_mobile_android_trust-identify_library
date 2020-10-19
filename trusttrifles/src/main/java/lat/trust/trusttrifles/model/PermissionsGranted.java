package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class PermissionsGranted {

    @SerializedName("read_phone_state")
    private boolean readPhoneState;

    @SerializedName("read_external_storage")
    private boolean readExternalStorage;

    @SerializedName("write_external_storage")
    private boolean writeExternalStorage;

    @SerializedName("access_coarse_location")
    private boolean accessCoarseLocation;

    @SerializedName("access_fine_location")
    private boolean accessFineLocation;

    public PermissionsGranted() {
    }

    public boolean isReadPhoneState() {
        return readPhoneState;
    }

    public void setReadPhoneState(boolean readPhoneState) {
        this.readPhoneState = readPhoneState;
    }

    public boolean isReadExternalStorage() {
        return readExternalStorage;
    }

    public void setReadExternalStorage(boolean readExternalStorage) {
        this.readExternalStorage = readExternalStorage;
    }

    public boolean isWriteExternalStorage() {
        return writeExternalStorage;
    }

    public void setWriteExternalStorage(boolean writeExternalStorage) {
        this.writeExternalStorage = writeExternalStorage;
    }

    public boolean isAccessCoarseLocation() {
        return accessCoarseLocation;
    }

    public void setAccessCoarseLocation(boolean accessCoarseLocation) {
        this.accessCoarseLocation = accessCoarseLocation;
    }

    public boolean isAccessFineLocation() {
        return accessFineLocation;
    }

    public void setAccessFineLocation(boolean accessFineLocation) {
        this.accessFineLocation = accessFineLocation;
    }
}
