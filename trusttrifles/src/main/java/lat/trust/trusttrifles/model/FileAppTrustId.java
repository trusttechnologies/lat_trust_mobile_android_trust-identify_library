package lat.trust.trusttrifles.model;

public class FileAppTrustId {

    private String trustId;
    private String createAt;
    private String bundleId;
    private String score;
    private String type;

    public String getTrustId() {
        return trustId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
