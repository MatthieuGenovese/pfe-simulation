package kobdig.mongo.collections;


import kobdig.urbanSimulation.entities.agents.Household;
import kobdig.urbanSimulation.entities.environement.Property;
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

    private ArrayList<String> rentableProperties;

    private String lastname;

    private double purchasingpower;
    private double netmonthlyincome;

    public HouseholdMongo(){

    }

    public HouseholdMongo(int idSimulation, int step, Household h) {
        rentableProperties = new ArrayList<>();
        try {
            this.propertyId = h.getProperty().getId();
        } catch (NullPointerException e) {
            this.propertyId = "none";
        }
        this.lastname = h.name();
        this.step = step;
        this.purchasingpower = h.getCurrentPurchasingPower();
        this.id = h.getId();
        this.netmonthlyincome = h.getCurrentNetMonthlyIncome();
        this.idSimulation = idSimulation;
        for (Property p : h.getRentableProperties()) {
            rentableProperties.add(p.getId());
        }
    }


    public String getLastname() {
        return lastname;
    }

    public double getPurchasingpower() {
        return purchasingpower;
    }

    public double getNetmonthlyincome() {
        return netmonthlyincome;
    }
}
