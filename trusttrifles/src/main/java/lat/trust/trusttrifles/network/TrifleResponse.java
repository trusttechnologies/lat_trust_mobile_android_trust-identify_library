package lat.trust.trusttrifles.network;

import com.google.gson.annotations.SerializedName;

import lat.trust.trusttrifles.model.Audit;

public class TrifleResponse {
    @SerializedName("trifle")
    private Audit audit;

    public Audit getAudit() {
        return audit;
    }
}
