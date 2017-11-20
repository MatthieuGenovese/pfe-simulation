package kobdig.urbanSimulation.entities;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.gui.FactParser;
import kobdig.logic.TruthDegree;

import java.util.ArrayList;

/**
 * Created by Matthieu on 20/11/2017.
 */
public interface IAgent {

    //HOUSEHOLD
    public Agent getAgent();
    public void setAgent(Agent agent);
    public String getId();
    public void setId(String id);
    public void updateBelief(String stringFact);

    /*public Property getProperty();
    public ArrayList<Property> getPurchasableProperties();
    public ArrayList<Property> getRentableProperties();
    public boolean isOwnerOccupied();
    public void setOwnerOccupied(boolean ownerOccupied);
    public void setProperty(Property property);
    public double getCurrentPurchasingPower();
    public double getCurrentNetMonthlyIncome();
    public double getPreviousPurchasingPower();
    public double getPreviousNetMonthlyIncome();
    public boolean isRenting();
    public void setRenting(boolean renting);
    public void setPreviousNetMonthlyIncome(double previousNetMonthlyIncome) ;
    public void setPreviousPurchasingPower(double previousPurchasingPower) ;
    public void setCurrentPurchasingPower(double currentPurchasingPower);
    public void setCurrentNetMonthlyIncome(double currentNetMonthlyIncome);
    public void clearPurchAndRentLists();
    public void addPurchasableProperty(Property property);
    public void addRentableProperty(Property property);
    public double getSatisfaction(double quality, double centrality, double proximity);
    public void invest(Property property);
    public String toString();*/

    //INVESTOR
    /*public Investor(String id, Agent agent, double purchasingPower);
    public Investor(Agent agent, Household household, Property property);
    public Investor(Agent agent, Investor investor, Property property);
    public ArrayList<Property> getPurchasableProperties();
    public void addPurchasableProperty(Property purchasable);
    public Property getProperty();
    public void setProperty(Property property);

    //PROMOTER
    public Promoter(String id, Agent agent, double purchasingPower);
    public double getPurchasingPower();
    public void addPurchasableLand(Land land);
    public ArrayList<Land> getPurchasableLand();
    public void setPurchasingPower(double purchasingPower);*/

}
