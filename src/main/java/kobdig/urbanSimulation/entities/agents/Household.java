package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Agent;
import kobdig.urbanSimulation.entities.IActionnable;
import kobdig.urbanSimulation.entities.environement.Property;

import java.util.ArrayList;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class Household extends AbstractAgentBuy implements IActionnable {
    public static final String BUY = "b";
    public static final String RENT = "r";
    public static final String CHANGE = "ch";
    public static final String SELL = "s";
    public static final String LANDLORD = "l";
    public static final String NOT_LANDLORD = "~l";
    public static final String OWNER = "o";
    private ArrayList<Property> rentableProperties;
    private boolean ownerOccupied;
    private boolean renting;
    private double qualityCoefficient;
    private double centralityCoefficient;
    private double proximityCoefficient;
    private double investDegree;

    public Household(String id, Agent agent, double purchasingPower, double netMonthlyIncome) {
        super(id, agent, purchasingPower, netMonthlyIncome);
        this.rentableProperties = new ArrayList<>();
        this.renting = false;
        this.ownerOccupied = false;
        this.qualityCoefficient = Math.random();
        this.centralityCoefficient = Math.random();
        this.proximityCoefficient = Math.random();
        double utility = qualityCoefficient + centralityCoefficient + proximityCoefficient;
        this.qualityCoefficient = qualityCoefficient / utility;
        this.centralityCoefficient = centralityCoefficient / utility;
        this.proximityCoefficient = proximityCoefficient / utility;
        this.investDegree = Math.random();

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

    public boolean isRenting() {
        return renting;
    }

    public void setRenting(boolean renting) {
        this.renting = renting;
    }

    public void clearPurchAndRentLists(){
        clearPurchasableProperties();
        this.rentableProperties = new ArrayList<>();
    }


    public void addRentableProperty(Property property){
        this.rentableProperties.add(property);
    }

    public double getSatisfaction(double quality, double centrality, double proximity){
        return qualityCoefficient * quality + centralityCoefficient * centrality + proximityCoefficient * proximity;
    }

    public void invest(Property property){
        setPreviousPurchasingPower(getCurrentPurchasingPower());
        setCurrentPurchasingPower(getPreviousPurchasingPower() - property.getCurrentPrice());
    }

    public void step(int time){
        // TODO: Determine how the purchasing power and net monthly income would evolve
        double currentPurchasingPower = getCurrentPurchasingPower();
        double previousPurchasingPower = getPreviousPurchasingPower();
        double currentNetMonthlyIncome = getCurrentNetMonthlyIncome();
        double previousNetMonthlyIncome = getPreviousNetMonthlyIncome();
        Property property = getProperty();

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
        setPreviousNetMonthlyIncome(previousNetMonthlyIncome);
        setPreviousPurchasingPower(previousPurchasingPower);
        setCurrentPurchasingPower(currentPurchasingPower);
        setCurrentNetMonthlyIncome(currentNetMonthlyIncome);
    }


}
