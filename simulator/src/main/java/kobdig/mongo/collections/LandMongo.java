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

    private double price;

    private double utility;

    public LandMongo() {

    }

    public String getId() {
        return id;
    }

    public int getStep() {
        return step;
    }

    public int getIdSimulation() {
        return idSimulation;
    }

    public double getPrice() {
        return price;
    }

    public double getUtility() {
        return utility;
    }

    public LandMongo(int idSimulation, int step, Land l) {
        this.idSimulation = idSimulation;
        this.utility = l.getUtility();

        this.step = step;
        this.price = l.getPrice();
    }


}