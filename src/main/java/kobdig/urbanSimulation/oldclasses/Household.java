package kobdig.urbanSimulation.oldclasses;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.gui.FactParser;
import kobdig.logic.TruthDegree;
import kobdig.urbanSimulation.Property;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Meili on 21/06/2016.
 */
public class Household {

    public static final String BUY = "b";
    public static final String RENT = "r";
    public static final String CHANGE = "ch";
    public static final String SELL = "s";
    public static final String LANDLORD = "l";
    public static final String NOT_LANDLORD = "~l";
    public static final String OWNER = "o";
    private String id;
    private Agent agent;
    private String lastname;
    private Property property;
    private ArrayList<Property> purchasableProperties;
    private ArrayList<Property> rentableProperties;
    private boolean ownerOccupied;
    private boolean renting;
    private double previousPurchasingPower;
    private double previousNetMonthlyIncome;
    private double currentPurchasingPower;
    private double currentNetMonthlyIncome;
    private double qualityCoefficient;
    private double centralityCoefficient;
    private double proximityCoefficient;
    private double investDegree;

    public Household(String id, Agent agent, String lastname, double purchasingPower, double netMonthlyIncome) {
        this.id = id;
        this.agent = agent;
        this.property = null;
        this.purchasableProperties = new ArrayList<>();
        this.rentableProperties = new ArrayList<>();
        this.renting = false;
        this.ownerOccupied = false;
        this.lastname = lastname;
        this.previousPurchasingPower = purchasingPower - 100;
        this.previousNetMonthlyIncome = netMonthlyIncome;
        this.currentPurchasingPower = purchasingPower - 100;
        this.currentNetMonthlyIncome = netMonthlyIncome;

        this.qualityCoefficient = Math.random();
        this.centralityCoefficient = Math.random();
        this.proximityCoefficient = Math.random();

        double utility = qualityCoefficient + centralityCoefficient + proximityCoefficient;

        this.qualityCoefficient = qualityCoefficient / utility;
        this.centralityCoefficient = centralityCoefficient / utility;
        this.proximityCoefficient = proximityCoefficient / utility;

        this.investDegree = Math.random();

    }

    // GETTERS AND SETTERS

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public ArrayList<Property> getPurchasableProperties() {
        return purchasableProperties;
    }

    public ArrayList<Property> getRentableProperties() {
        return rentableProperties;
    }

    public boolean isOwnerOccupied() {
        return ownerOccupied;
    }

    public void setOwnerOccupied(boolean ownerOccupied) {
        this.ownerOccupied = ownerOccupied;
    }

    public void setProperty(Property property) {
        this.property = property;
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

    public boolean isRenting() {
        return renting;
    }

    public void setRenting(boolean renting) {
        this.renting = renting;
    }

    public void setPreviousNetMonthlyIncome(double previousNetMonthlyIncome) {
        this.previousNetMonthlyIncome = previousNetMonthlyIncome;
    }

    public void setPreviousPurchasingPower(double previousPurchasingPower) {
        this.previousPurchasingPower = previousPurchasingPower;
    }

    public void setCurrentPurchasingPower(double currentPurchasingPower) {
        this.currentPurchasingPower = currentPurchasingPower;
    }

    public void setCurrentNetMonthlyIncome(double currentNetMonthlyIncome) {
        this.currentNetMonthlyIncome = currentNetMonthlyIncome;
    }

    // METHODS

    /**
     * Clears the purchasable and rentable lists for a new simulation step
     */
    public void clearPurchAndRentLists(){
        this.purchasableProperties = new ArrayList<>();
        this.rentableProperties = new ArrayList<>();
    }

    /**
     * Adds a new purchasable property
     * @param property The property to add
     */
    public void addPurchasableProperty(Property property){
        this.purchasableProperties.add(property);
    }

    /**
     * Adds a new rentable property
     * @param property The property to add
     */
    public void addRentableProperty(Property property){
        this.rentableProperties.add(property);
    }

    /**
     * The satisfaction of the household taking into a account a given quality, centrality and proximity factors
     * @param quality The quality factor
     * @param centrality The centrality factor
     * @param proximity The proximity factor
     * @return
     */
    public double getSatisfaction(double quality, double centrality, double proximity){
        return qualityCoefficient * quality + centralityCoefficient * centrality + proximityCoefficient * proximity;
    }

    /**
     * Updates the agent's beliefs, desires and goals considering a given fact
     * @param stringFact The fact
     */
    public void updateBelief(String stringFact){
        FactParser parser = new FactParser(stringFact);
        Fact fact = parser.getFact();
        TruthDegree truthDegree = parser.getTrust();
        agent.updateBeliefs(fact,truthDegree);
        agent.updateDesires();
        agent.updateGoals();

    }

    /**
     * Rents a all ready aquired property
     * @param property The current property
     */
    public void invest(Property property){
        this.previousPurchasingPower = this.currentPurchasingPower;
        this.currentPurchasingPower = this.previousPurchasingPower - property.getCurrentPrice();
    }

    /**
     * Generates a step in the simulation
     * @param time The time in the simulation
     */
    public void step(int time){
        // TODO: Determine how the purchasing power and net monthly income would evolve
        previousPurchasingPower = currentPurchasingPower;
        previousNetMonthlyIncome = currentNetMonthlyIncome;

        double rnd1 = Math.random();
        double rnd2 = Math.random();

        if (rnd1 < 0.20) {
            currentPurchasingPower = previousPurchasingPower * 1.001;            
        }
        else if (rnd1 < 0.40) {
            currentPurchasingPower = previousPurchasingPower * 0.999;
            currentPurchasingPower = (currentPurchasingPower < 0)? 0: currentPurchasingPower;
        }
        
        if (rnd2 < 0.20) {
            currentNetMonthlyIncome = previousNetMonthlyIncome * 1.001;
        }
        else if (rnd2 < 0.40) {
            currentNetMonthlyIncome = previousNetMonthlyIncome * 0.999;
            currentNetMonthlyIncome = (currentNetMonthlyIncome < 0)? 0: currentNetMonthlyIncome;
        }


        clearPurchAndRentLists();

        //TODO: Determine how these values will be calculated
        // Updates beliefs associated to current home
        if (ownerOccupied) {
            // Updates the satisfied belief
            if(property != null) {
            	double satisfaction = getSatisfaction(property.getUtility(),0,0);
            	if(satisfaction < 0.4) {
            		updateBelief("p:1");
                    // Updates the changing desire
                    updateBelief("ch:1");            		
            	}
            	else updateBelief("not p:1");
            }                        
            else updateBelief("p:1");
            updateBelief("o:1");
            updateBelief("not r:1");
        }
        else if (renting) {
            // Updates the satisfied belief
            //System.out.println(property);
            if(property != null) {
            	double satisfaction = getSatisfaction(property.getUtility(),0,0);
            	//System.out.println(satisfaction);
            	if(satisfaction < 0.4) {
            		// Updates the changing desire
                    updateBelief("ch:" + 1); 
            		updateBelief("p:" + 1);
            	}
            	else updateBelief("not p:1");
            }                        
            else updateBelief("p:1");
            updateBelief("not o:1");
            updateBelief("r:1");
        }
        else {
            updateBelief("not r:1");
            updateBelief("not o:1");
        }
//      System.out.println("_____________________Belief Step___________________");
//      Iterator<Fact> iter = getAgent().beliefs().factIterator();
//      System.out.println("Simulation step: " + time + " Household no." + getId());
//      while (iter.hasNext()){
//          System.out.println(iter.next().formula().toString());
//      }
//      System.out.println("________________________________________")        
    }

    public String toString(){ return id; }
}
