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

    private double investDegree;

    private double speculate;

    private double currentrent;

    public String getId() {
        return id;
    }

    public int getStep() {
        return step;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public int getIdSimulation() {
        return idSimulation;
    }

    public double getPurchasingpower() {
        return purchasingpower;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public double getInvestDegree() {
        return investDegree;
    }

    public double getSpeculate() {
        return speculate;
    }

    public double getCurrentrent() {
        return currentrent;
    }

    public InvestorMongo(){

    }

    public InvestorMongo(int idSimulation, int step, Investor i){
        this.idSimulation = idSimulation;
        this.step = step;
        this.id = i.getId();
        this.purchasingpower = i.getCurrentPurchasingPower();
        this.monthlyIncome = i.getCurrentNetMonthlyIncome();
        this.speculate = i.getSpeculate();
        this.currentrent = i.getCurrentRent();
        this.investDegree = i .getInvestDegree();
        try {
            this.householdId = i.getHousehold().getId();
        }
        catch(NullPointerException e){
            this.householdId = "none";
        }
    }

}
