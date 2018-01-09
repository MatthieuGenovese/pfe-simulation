package kobdig.urbanSimulation.entities.environement;

import org.postgis.PGgeometry;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class AbstractEnvironment implements IEnvironment {
    private String id;
    private double latitude;
    private double longitude;
    private AdministrativeDivision division;
    private double utility;
    private PGgeometry geom;
    private boolean updated;

    public AbstractEnvironment(String id, double latitude, double longitude, PGgeometry geom) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.division = null;
        this.utility = Double.NEGATIVE_INFINITY;
        this.geom = geom;
        this.updated = false;
    }



    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public AdministrativeDivision getDivision(){
        return division;
    }
    public void setDivision(AdministrativeDivision division){
        this.division = division;
    }
    public double getUtility(){
        return utility;
    }
    public void setUtility(double utility){
        this.utility = utility;
    }
    public PGgeometry getGeom(){
        return geom;
    }
    public boolean isUpdated(){
        return updated;
    }
    public void setUpdated(boolean updated){
        this.updated = updated;
    }
}
