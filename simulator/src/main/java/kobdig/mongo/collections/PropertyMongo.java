package kobdig.mongo.collections;

import kobdig.urbanSimulation.entities.environement.Property;
import org.springframework.data.annotation.Id;
/**
 * Created by Matthieu on 07/12/2017.
 */
public class PropertyMongo  {

    @Id
    private String mongo_id;

    private int idSimulation;

    private int step;

    private String idProperty;

    private double price;

    private double rent;

    private double value;

    private String state;

    private int codigo_upz;

    private String geom;


    public PropertyMongo(){

    }

    public PropertyMongo(int idSimulation, int step, Property p){
        this.idSimulation = idSimulation;
        this.step = step;
        this.idProperty = p.getId();
        this.price = p.getCurrentPrice();
        this.rent = p.getCurrentCapitalizedRent();
        this.value = p.getCurrentValue();
        this.state = p.getState();
        this.codigo_upz = p.getDivision().getCode();
        this.geom = p.getGeom().toString();
    }
}