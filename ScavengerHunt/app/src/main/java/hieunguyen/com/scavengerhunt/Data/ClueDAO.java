package hieunguyen.com.scavengerhunt.Data;

/**
 * Created by hieunguyen on 10/17/15.
 */
public class ClueDAO {

    private int id;
    private double latitude;
    private double longitude;
    private String s3id;
    private boolean isActive;

    public ClueDAO(int id, double latitude, double longitude, String s3id, boolean isActive) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.s3id = s3id;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getS3id() {
        return s3id;
    }

    @Override
    public String toString() {
        return "ClueDAO{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", s3id='" + s3id + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
