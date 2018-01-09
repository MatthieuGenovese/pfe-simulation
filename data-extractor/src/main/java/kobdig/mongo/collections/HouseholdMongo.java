package kobdig.mongo.collections;


import kobdig.urbanSimulation.entities.agents.Household;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

/**
 * Created by Matthieu on 07/12/2017.
 */
public class HouseholdMongo  {

    @Id
    private String mongo_id;

    private int step;

    private int idSimulation;

    private String id;

    private String propertyId;

    private int rentableProperties;

    private double purchasingpower;
    private double netmonthlyincome;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getIdSimulation() {
        return idSimulation;
    }

    public void setIdSimulation(int idSimulation) {
        this.idSimulation = idSimulation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public int getRentableProperties() {
        return rentableProperties;
    }

    public void setRentableProperties(int rentableProperties) {
        this.rentableProperties = rentableProperties;
    }

    public void setPurchasingpower(double purchasingpower) {
        this.purchasingpower = purchasingpower;
    }

    public void setNetmonthlyincome(double netmonthlyincome) {
        this.netmonthlyincome = netmonthlyincome;
    }

    public HouseholdMongo(){

    }

    public HouseholdMongo(int idSimulation, int step, Household h) {
        try {
            this.propertyId = h.getProperty().getId();
        } catch (NullPointerException e) {
            this.propertyId = "none";
        }
        this.step = step;
        this.purchasingpower = h.getCurrentPurchasingPower();
        this.id = h.getId();
        this.netmonthlyincome = h.getCurrentNetMonthlyIncome();
        this.idSimulation = idSimulation;
        this.rentableProperties = h.getRentableProperties().size();
    }


    public double getPurchasingpower() {
        return purchasingpower;
    }

    public double getNetmonthlyincome() {
        return netmonthlyincome;
    }
}
