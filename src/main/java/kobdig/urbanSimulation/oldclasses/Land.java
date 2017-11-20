package kobdig.urbanSimulation.oldclasses;

import kobdig.urbanSimulation.entities.environement.AdministrativeDivision;
import org.postgis.PGgeometry;

/**
 * Created by Meili on 7/19/16.
 */
public class Land {
    private String id;
    private double latitude;
    private double longitude;
    private AdministrativeDivision division;
    private double price;
    private double utility;
    private PGgeometry geom;
    private boolean updated;

    public Land(String id, double latitude, double longitude, double price, PGgeometry geom) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.division = null;
        this.utility = Double.NEGATIVE_INFINITY;
        this.geom = geom;
        this.updated = false;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public AdministrativeDivision getDivision() {
        return division;
    }

    public void setDivision(AdministrativeDivision division) {
        this.division = division;
    }

    public double getUtility() {
        return utility;
    }

    public void setUtility(Double utility) {
        this.utility = utility;
    }

    public PGgeometry getGeom() {
        return geom;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    // METHODS

    public void step(int time) {
        price = (price - Math.exp(time) < 0) ? 0.0 : price - Math.exp(time);
    }

}
