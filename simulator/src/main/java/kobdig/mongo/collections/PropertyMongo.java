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

    private double currentPrice;
    private double currentCapitalizedRent;
    private double currentPotentialRent;
    private double currentValue;
    private double previousPrice;
    private double previousCapitalizedRent;
    private double previousPotentialRent;
    private double previousValue;

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

    public double getCurrentValue() {
        return currentValue;
    }

    public double getPreviousPrice() {
        return previousPrice;
    }

    public double getPreviousCapitalizedRent() {
        return previousCapitalizedRent;
    }

    public double getPreviousPotentialRent() {
        return previousPotentialRent;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public double getCurrentPrice() {

        return currentPrice;
    }

    public double getCurrentCapitalizedRent() {
        return currentCapitalizedRent;
    }

    public double getCurrentPotentialRent() {
        return currentPotentialRent;
    }

    public String getState() {
        return state;
    }

    public String getMongo_id() {
        return mongo_id;
    }

    public PropertyMongo(int idSimulation, int step, Property p){
        this.idSimulation = idSimulation;
        this.step = step;
        this.idProperty = p.getId();
        this.currentPrice = p.getCurrentPrice();
        this.previousPrice = p.getPreviousPrice();
        this.currentCapitalizedRent = p.getCurrentCapitalizedRent();
        this.previousCapitalizedRent = p.getPreviousCapitalizedRent();
        this.currentValue = p.getCurrentValue();
        this.previousValue = p.getPreviousValue();
        this.currentPotentialRent = p.getCurrentPotentialRent();
        this.previousPotentialRent = p.getPreviousPotentialRent();

        this.state = p.getState();
    }
}