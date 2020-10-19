package lat.trust.trusttrifles.model;

import java.util.ArrayList;

public class FileTrustId {
    private String trustId;
    private String createAt;
    private String score;
    private String type;
    private ArrayList<FileAppTrustId> trustIdApps;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrustId() {
        return trustId;
    }

    public void setTrustId(String trustId) {
        this.trustId = trustId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ArrayList<FileAppTrustId> getTrustIdApps() {
        return trustIdApps;
    }

    public void setTrustIdApps(ArrayList<FileAppTrustId> trustIdApps) {
        this.trustIdApps = trustIdApps;
    }
}
