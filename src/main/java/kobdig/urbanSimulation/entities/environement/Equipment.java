package kobdig.urbanSimulation.entities.environement;

import org.postgis.PGgeometry;

/**
 * Created by Meili on 7/25/16.
 */
public class Equipment {

    public static final String BIENESTAR = "\"bienestar\"";
    public static final String CULTO = "\"culto\"";
    public static final String CULTURA = "\"cultura\"";
    public static final String CYSF = "\"cysf\"";
    public static final String DEPORTES = "\"deportes\"";
    public static final String EDUCACION = "\"educacion\"";
    public static final String EDUSUP = "\"edusup\"";
    public static final String RECINTOS_FERIALES = "\"recintos_feriales\"";
    public static final String SA = "\"sa\"";
    public static final String SALONES_COMUNALES = "\"salones\"";
    public static final String SALUD = "\"salud\"";
    public static final String SEGURIDAD = "\"seguridad\"";
    private String id;
    private String type;
    private AdministrativeDivision division;
    private PGgeometry geom;
    public Equipment(String id, String type, PGgeometry geom){
        this.id = id;
        this.type = type;
        this.division = null;
        this. geom = geom;
    }

    public AdministrativeDivision getDivision() {
        return division;
    }

    public void setDivision(AdministrativeDivision division) {
        this.division = division;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public PGgeometry getGeom() {
        return geom;
    }
}
