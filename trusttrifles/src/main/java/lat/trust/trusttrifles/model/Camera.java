package lat.trust.trusttrifles.model;

public class Camera {
    private String type;
    private String mega_pixels;
    private String horizontal_view_angle;
    private String vertical_view_angle;
    private String focal_length;
    private String max_exposure_comp;
    private String min_exposure_comp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMega_pixels() {
        return mega_pixels;
    }

    public void setMega_pixels(String mega_pixels) {
        this.mega_pixels = mega_pixels;
    }

    public String getHorizontal_view_angle() {
        return horizontal_view_angle;
    }

    public void setHorizontal_view_angle(String horizontal_view_angle) {
        this.horizontal_view_angle = horizontal_view_angle;
    }

    public String getVertical_view_angle() {
        return vertical_view_angle;
    }

    public void setVertical_view_angle(String vertical_view_angle) {
        this.vertical_view_angle = vertical_view_angle;
    }

    public String getFocal_length() {
        return focal_length;
    }

    public void setFocal_length(String focal_length) {
        this.focal_length = focal_length;
    }

    public String getMax_exposure_comp() {
        return max_exposure_comp;
    }

    public void setMax_exposure_comp(String max_exposure_comp) {
        this.max_exposure_comp = max_exposure_comp;
    }

    public String getMin_exposure_comp() {
        return min_exposure_comp;
    }

    public void setMin_exposure_comp(String min_exposure_comp) {
        this.min_exposure_comp = min_exposure_comp;
    }
    /*
    Type
    MP -> picture-size / 1.000.000
    horizontal-view-angle
    vertical-view-angle
    focal-length
    max-exposure-compensation
    min-exposure-compensation
    */
}
