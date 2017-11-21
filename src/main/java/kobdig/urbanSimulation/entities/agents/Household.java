package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Agent;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.IActionnable;
import kobdig.urbanSimulation.entities.environement.Property;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public Household(EntitiesCreator entitiesCreator, String id, double purchasingPower, double netMonthlyIncome) throws IOException {
        super(entitiesCreator, id, purchasingPower, netMonthlyIncome, new FileInputStream(entitiesCreator.getHouseholdAgentFile()));
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

    public void householdUpdateBeliefs(int time, ArrayList<Property> freeProperties, ArrayList<Property> forRentProperties) {

        step(time);
        Property cheapestProperty = null;
        double cheapestPrice = Double.POSITIVE_INFINITY;

        // Updates de affordBuying and affordRenting beliefs and gets the cheapest property
        int purchFound = 0;
        int rentFound = 0;

        for (Property property : freeProperties) {
            if (property.getCurrentPrice() < cheapestPrice) {
                cheapestPrice = property.getCurrentPrice();
                cheapestProperty = property;
            }

            if (getCurrentPurchasingPower() >= property.getCurrentPrice()) {
                addPurchasableProperty(property);
                purchFound++;
            }

        }

        for (Property property : forRentProperties) {
            if(getCurrentNetMonthlyIncome() >= property.getCurrentCapitalizedRent()){
                addRentableProperty(property);
                rentFound++;
            }
        }
        if(purchFound > 0){
           updateBelief("ab:" + Double.toString(purchFound/(0.0 + freeProperties.size())));
        }
        else{
           updateBelief("not ab:1");
        }
        if(rentFound > 0){
            updateBelief("ar:" + Double.toString(rentFound/(0.0 + forRentProperties.size())));
        }
        else{
            updateBelief("not ar:1");
        }

        // Updates the buyingRentable belief
        // TODO: Improve this approach
        if(cheapestPrice < Double.POSITIVE_INFINITY){
            if(getCurrentPurchasingPower() > cheapestPrice){
                double rnd = Math.random();
                if (rnd < 0.5){
                    updateBelief("br:" + cheapestProperty.getCurrentCapitalizedRent()/(0.0 +
                            cheapestProperty.getCurrentPotentialRent()));
                }
                else updateBelief("not br:1");
            }
        }
//        System.out.println("_____________________Belief Step___________________");
//        Iterator<Fact> iter = household.getAgent().beliefs().factIterator();
//        System.out.println("Simulation step: " + time + " Household no." + household.getId());
//        while (iter.hasNext()){
//            System.out.println(iter.next().formula().toString());
//        }
//        System.out.println("________________________________________");
    }



}
