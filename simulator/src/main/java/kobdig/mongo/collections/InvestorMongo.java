package kobdig.mongo.collections;

import kobdig.urbanSimulation.entities.agents.Investor;
import org.springframework.data.annotation.Id;

/**
 * Created by Matthieu on 08/12/2017.
 */
public class InvestorMongo {
    @Id
    private String mongo_id;

    private String id;

    private int step;

    private String householdId;

    private int idSimulation;

    private double purchasingpower;
    private double monthlyIncome;

    public InvestorMongo(){

    }

    public InvestorMongo(int idSimulation, int step, Investor i){
        this.idSimulation = idSimulation;
        this.step = step;
        this.id = i.getId();
        this.purchasingpower = i.getCurrentPurchasingPower();
        this.monthlyIncome = i.getCurrentNetMonthlyIncome();
        try {
            this.householdId = i.getHousehold().getId();
        }
        catch(NullPointerException e){
            this.householdId = "none";
        }
    }

}
