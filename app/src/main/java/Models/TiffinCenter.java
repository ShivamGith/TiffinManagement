package Models;
import android.location.Location;
import android.media.Rating;


public class TiffinCenter {
    private String name;
    private String centerId;

    private String url;

    private String cuisines;

    private String thumbnail;

    private String menu;

    private String location;

    private String rating;

    public TiffinCenter(String name, String centerId, String url, String cuisines, String thumbnail,
                        String menu, String location, String rating) {
        this.name = name;
        this.centerId = centerId;
        this.url = url;
        this.cuisines = cuisines;
        this.thumbnail = thumbnail;
        this.menu = menu;
        this.location = location;
        this.rating = rating;
    }

    public TiffinCenter() {
    }

    public String getName() {
        return name;
    }

    public String getCenterId() {
        return centerId;
    }

    public String getUrl() {
        return url;
    }

    public String getCuisines() {
        return cuisines;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getMenu() {
        return menu;
    }

    public String getLocation() {
        return location;
    }

    public String getRating() {
        return rating;
    }
}
