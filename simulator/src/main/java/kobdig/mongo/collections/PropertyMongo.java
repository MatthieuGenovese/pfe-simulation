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

    private double capitalizedRent;

    private double value;

    private String state;


    public PropertyMongo(){

    }

    public int getIdSimulation() {
        return idSimulation;
    }

    public int getStep() {
        return step;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public double getPrice() {
        return price;
    }

    public double getCapitalizedRent() {
        return capitalizedRent;
    }

    public double getValue() {
        return value;
    }

    public String getState() {
        return state;
    }

    public PropertyMongo(int idSimulation, int step, Property p){
        this.idSimulation = idSimulation;
        this.step = step;
        this.idProperty = p.getId();
        this.price = p.getCurrentPrice();

        this.capitalizedRent = p.getCurrentCapitalizedRent();
        this.value = p.getCurrentValue();
        this.state = p.getState();
    }
}