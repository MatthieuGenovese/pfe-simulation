package kobdig.mongo.collections;

import kobdig.urbanSimulation.entities.environement.Land;
import org.springframework.data.annotation.Id;


/**
 * Created by Matthieu on 08/12/2017.
 */
public class LandMongo {
    @Id
    private String mongo_id;

    private String id;

    private int step;

    private int idSimulation;

    private double latitude;
    private double longitude;

    private double price;
    private String geom;

    private int codigo_upz;

    public LandMongo() {

    }

    public LandMongo(int idSimulation, int step, Land l) {
        this.idSimulation = idSimulation;
        this.latitude = l.getLatitude();
        this.step = step;
        this.longitude = l.getLongitude();
        this.price = l.getPrice();
        this.geom = l.getGeom().toString();
        this.codigo_upz = l.getDivision().getCode();
    }


}