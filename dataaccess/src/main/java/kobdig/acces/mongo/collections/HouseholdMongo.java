package kobdig.acces.mongo.collections;


import org.springframework.data.annotation.Id;

/**
 * Created by Matthieu on 07/12/2017.
 */
public class HouseholdMongo  {

    @Id
    private String mongo_id;

    private String id;

    private String lastname;

    private double purchasingpower;
    private double netmonthlyincome;

    public HouseholdMongo(){

    }

    public HouseholdMongo(String id, String lastname, double purchasingpower, double netmonthlyincome){
        this.lastname = lastname;
        this.purchasingpower = purchasingpower;
        this.id = id;
        this.netmonthlyincome = netmonthlyincome;
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
