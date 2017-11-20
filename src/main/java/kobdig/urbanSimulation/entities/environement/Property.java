package kobdig.urbanSimulation.entities.environement;

import kobdig.urbanSimulation.entities.IActionnable;
import org.postgis.PGgeometry;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class Property extends AbstractEnvironment implements IActionnable {

    public static final String RENTED = "Rented";
    public static final String SEEKING_TENANT = "Seeking tenant";
    public static final String OCCUPIED = "Occupied";
    public static final String FOR_SALE = "For sale";
    public final static double ABANDONMENT_FACTOR = 0.0125;
    public final static double TENANT_MOBILITY = 0.0561;
    public final static double DEPRECIATION_RATE = 0.0028;
    private String state;
    private double currentPrice;
    private double currentCapitalizedRent;
    private double currentPotentialRent;
    private double currentValue;
    private double previousPrice;
    private double previousCapitalizedRent;
    private double previousPotentialRent;
    private double previousValue;
    private Land land;
    public Property(String id, double latitude, double longitude, double price, double rent,
                    double value, PGgeometry geom, Land land){
        super(id,latitude,longitude,geom);
        this.currentPrice = price;
        this.currentCapitalizedRent = rent;
        this.currentPotentialRent = rent;
        this.currentValue = value;
        this.previousPrice = price;
        this.previousCapitalizedRent = rent;
        this.previousPotentialRent = rent;
        this.previousValue = value;
        this.land = land;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public double getCurrentValue() {
        return currentValue;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }
    public void step(int time){

        previousPrice = currentPrice;
        previousCapitalizedRent = currentCapitalizedRent;
        previousPotentialRent = currentPotentialRent;
        previousValue = currentValue;

        currentPrice = (previousPrice - Math.exp(time) < 0)? 0.0: previousPrice - Math.exp(time);
        currentPotentialRent = (previousPotentialRent + Math.log(time + 1) < 0)? 0.0: previousPotentialRent +
                Math.log(time + 1);
        currentCapitalizedRent = (previousCapitalizedRent - Math.log(time + 1) <= 0.1)? 0.1: previousCapitalizedRent -
                Math.log(time + 1);
        currentValue = (previousValue - Math.exp(time) < 0)? 0.0: previousValue - Math.exp(time);

    }
}
