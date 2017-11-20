package kobdig.urbanSimulation.entities;

import kobdig.agent.Agent;
import kobdig.urbanSimulation.Property;

import java.util.ArrayList;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class AbstractAgentBuy extends AbstractAgent implements IAgentBuy {
    private double currentPurchasingPower;
    private double currentNetMonthlyIncome;
    private double previousPurchasingPower;
    private double previousNetMonthlyIncome;
    private Property property;
    private ArrayList<Property> purchasableProperties;

    public AbstractAgentBuy(String id, Agent agent, double purchasingPower, double netMonthlyIncome){
        super(id, agent);
        this.previousPurchasingPower = purchasingPower - 100;
        this.previousNetMonthlyIncome = netMonthlyIncome;
        this.currentPurchasingPower = purchasingPower - 100;
        this.currentNetMonthlyIncome = netMonthlyIncome;
        this.property = null;
        this.purchasableProperties = new ArrayList<>();
    }



    public double getCurrentPurchasingPower() {
        return currentPurchasingPower;
    }

    public double getCurrentNetMonthlyIncome() {
        return currentNetMonthlyIncome;
    }

    public double getPreviousPurchasingPower() {
        return previousPurchasingPower;
    }

    public double getPreviousNetMonthlyIncome() {
        return previousNetMonthlyIncome;
    }

    public void setPreviousNetMonthlyIncome(double previousNetMonthlyIncome){
        this.previousNetMonthlyIncome = previousNetMonthlyIncome;
    }
    public void setPreviousPurchasingPower(double previousPurchasingPower){
        this.previousPurchasingPower = previousPurchasingPower;
    }
    public void setCurrentPurchasingPower(double currentPurchasingPower){
        this.currentPurchasingPower = currentPurchasingPower;
    }

    public void setCurrentNetMonthlyIncome(double currentNetMonthlyIncome){
        this.currentNetMonthlyIncome = currentNetMonthlyIncome;
    }

    public ArrayList<Property> getPurchasableProperties(){
        return purchasableProperties;
    }
    public void addPurchasableProperty(Property purchasable){
        this.purchasableProperties.add(purchasable);
    }
    public Property getProperty(){
        return property;
    }
    public void setProperty(Property property){
        this.property = property;
    }

    public void clearPurchasableProperties(){
        purchasableProperties = new ArrayList<>();
    }
}
