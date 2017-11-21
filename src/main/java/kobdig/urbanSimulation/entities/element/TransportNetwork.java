package kobdig.urbanSimulation.entities.environement;

import org.postgis.PGgeometry;

/**
 * Created by Meili on 7/26/16.
 */
public class TransportNetwork {

    private String id;
    private String level;
    private AdministrativeDivision division;
    private PGgeometry geom;
    public TransportNetwork(String id, String level, PGgeometry geom){
        this.id = id;
        this.level = level;
        this. geom = geom;
    }

    public String getId() {
        return id;
    }

    public AdministrativeDivision getDivision() {
        return division;
    }

    public void setDivision(AdministrativeDivision division) {
        this.division = division;
    }
}
