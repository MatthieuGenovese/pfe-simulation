package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Fact;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.IActionnable;
import kobdig.urbanSimulation.entities.environement.Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public Household(String id, double purchasingPower, double netMonthlyIncome, File file) throws IOException {
        super(id, purchasingPower, netMonthlyIncome, new FileInputStream(file));
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

    @Override
    public Property invest(EntitiesCreator entitiesCreator){
        double maxUtility = 0.0;
        Property selection = null;
        for (Property purchasable : getPurchasableProperties()) {
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = 0.0;
                    double transportUtility = 0.0;

                    equipUtility = purchasable.getLand().getEquipementUtility();

                    transportUtility = purchasable.getLand().getTransportUtility();

                    purchasable.setUtility(0.4*(equipUtility/(double)entitiesCreator.getEquipmentsLength()) + 0.6*(transportUtility/(double)entitiesCreator.getNetworkLength()));
//                    purchasable.setUtility(0.0*(equipUtility/(double)equipmentsLength) + 1.0*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(Math.random());
                    purchasable.setUpdated(true);
                }
                if(purchasable.getUtility() > maxUtility){
                    maxUtility = purchasable.getUtility();
                    selection = purchasable;
                }
        }
        if (selection != null){
            setPreviousPurchasingPower(getCurrentPurchasingPower());
            setCurrentPurchasingPower(getPreviousPurchasingPower() - selection.getCurrentPrice());
        }
        return selection;
    }

    public Property rentProperty(EntitiesCreator entitiesCreator){
        double maxUtility = 0.0;
        Property selection = null;
        for (Property purchasable : getRentableProperties()) {
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = 0.0;
                    double transportUtility = 0.0;

                    equipUtility = purchasable.getLand().getEquipementUtility();

                    transportUtility = purchasable.getLand().getTransportUtility();


                    purchasable.setUtility(0.4*(equipUtility/(double)entitiesCreator.getEquipmentsLength()) + 0.6*(transportUtility/(double)entitiesCreator.getNetworkLength()));
//                    purchasable.setUtility(0.0*(equipUtility/(double)equipmentsLength) + 1.0*(transportUtility/(double)networkLength));
//                    purchasable.setUtility(Math.random());
                    purchasable.setUpdated(true);
                }
                if(purchasable.getUtility() > maxUtility){
                    maxUtility = purchasable.getUtility();
                    selection = purchasable;
                }


        }
        setRenting(selection != null);
        if(isRenting()){
            setPreviousNetMonthlyIncome(getCurrentNetMonthlyIncome());
            setCurrentNetMonthlyIncome(getPreviousNetMonthlyIncome() - selection.getCurrentCapitalizedRent());
        }
        return selection;
    }

    @Override
    public void agentIntentionsStep(EntitiesCreator entitiesCreator) {

        System.out.println("____________________Interntion Step____________________");
        Iterator<Fact> iter = goals().factIterator();
        System.out.println("Simulation step:  Household no." + getId());
        while (iter.hasNext()){
            System.out.println(iter.next().formula().toString());
        }
        System.out.println("________________________________________");

        iter = goals().factIterator();
        while(iter.hasNext()) {
            String goal = iter.next().formula().toString();
            // If the goal is to buy
            if (goal.contains(Household.BUY) && goal.contains(Household.OWNER)){
                Property taken = buyProperty(entitiesCreator);
                if (taken != null) {
                    entitiesCreator.getFreeProperties().remove(taken);
                    entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().remove(taken);
                    taken.setState(Property.OCCUPIED);
                    System.out.println("OCCUPIED");
                    entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().add(taken);
                    setOwnerOccupied(true);
                }
            }

            // If the goal is to invest
            else if (goal.contains(Household.BUY) && !goal.contains(Household.NOT_LANDLORD) && goal.contains(Household.LANDLORD)){
                Property taken = invest(entitiesCreator);
                System.out.println("JE SUIS LA SEEKING !!!");
                if (taken != null) {
                    entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().remove(taken);
                    entitiesCreator.getFreeProperties().remove(taken);
                    taken.setState(Property.SEEKING_TENANT);
                    System.out.println("SEEKING TENANT");
                    entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().add(taken);
                    entitiesCreator.getForRentProperties().add(taken);
                    taken.setUpdated(false);
                    Investor newInvestor = null;
                    try {
                        newInvestor = new Investor(this,taken, entitiesCreator.getInvestorAgentFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    entitiesCreator.getInvestors().add(newInvestor);
                    //entitiesCreator.getAgents().add(newInvestor);
                    setProperty(null);
                }
            }

            // If the goal is to rent
            if (goal.contains(Household.RENT)){
                Property taken = rentProperty(entitiesCreator);

                System.out.println("JE SUIS LA RENTED !!!");
                if (taken != null) {
                    entitiesCreator.getForRentProperties().remove(taken);
                    entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().remove(taken);
                    taken.setState(Property.RENTED);
                    System.out.println("RENTED");
                    entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().add(taken);
                    setProperty(null);
                    setRenting(true);
                }
            }

            // If the goal is to change and either sell or invest
            if (goal.contains(Household.CHANGE)){

                Property taken = buyProperty(entitiesCreator);
                if (taken != null) {
                    entitiesCreator.getFreeProperties().remove(taken);
                    System.out.println("OCCUPIED");
                    entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().remove(taken);
                    taken.setState(Property.OCCUPIED);
                    entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().add(taken);
                }

                if (goal.contains(Household.LANDLORD)){
                    if(getProperty() != null) {
                        invest(getProperty());
                        entitiesCreator.getFreeProperties().remove(getProperty());
                        entitiesCreator.getForRentProperties().add(getProperty());
                        Investor newInvestor = null;
                        try {
                            newInvestor = new Investor(this, getProperty(), entitiesCreator.getInvestorAgentFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        entitiesCreator.getDivisions()[newInvestor.getProperty().getLand().getDivision().getCode()].getProperties().remove(newInvestor.getProperty());
                        newInvestor.getProperty().setState(Property.SEEKING_TENANT);
                        System.out.println("SEEKING TENANT");
                        entitiesCreator.getDivisions()[newInvestor.getProperty().getLand().getDivision().getCode()].getProperties().add(newInvestor.getProperty());
                        entitiesCreator.getInvestors().add(newInvestor);
                        //entitiesCreator.getAgents().add(newInvestor);
                        setProperty(null);
                    }
                }
                else if (goal.contains(Household.SELL)){
                    //TODO: Implement the seller part of the property
                    if (getProperty() != null) {
                        entitiesCreator.getFreeProperties().add(getProperty());
                        entitiesCreator.getDivisions()[getProperty().getLand().getDivision().getCode()].getProperties().remove(getProperty());
                        getProperty().setState(Property.FOR_SALE);
                        System.out.println("FOR SALE");
                        entitiesCreator.getDivisions()[getProperty().getLand().getDivision().getCode()].getProperties().add(getProperty());
                        setProperty(null);
                    }
                }

            }

        }
    }

    /**
     * Purchases a land
     * @return The land purchased
     */
    public Property buyProperty(EntitiesCreator entitiesCreator){
        double maxUtility = 0.0;
        Property selection = null;
        for (Property purchasable : getPurchasableProperties()) {
                double equipUtility = 0.0;
                double transportUtility = 0.0;
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {

                    equipUtility = purchasable.getLand().getEquipementUtility();

                    transportUtility = purchasable.getLand().getTransportUtility();


                    purchasable.setUtility(0.4*(equipUtility/(double)entitiesCreator.getEquipmentsLength()) + 0.6*(transportUtility/(double)entitiesCreator.getNetworkLength()));
//                    purchasable.setUtility(0.0*(equipUtility/(double)equipmentsLength) + 1.0*(transportUtility/(double)networkLength));
//                        purchasable.setUtility(Math.random());
                    purchasable.setUpdated(true);
                }
                if(purchasable.getUtility() > maxUtility){
                    maxUtility = purchasable.getUtility();
                    selection = purchasable;
                }

        }
        setOwnerOccupied(selection != null);
        if(isOwnerOccupied()){
            setProperty(selection);
            setPreviousPurchasingPower(getCurrentPurchasingPower());
            setCurrentPurchasingPower(getPreviousPurchasingPower() - getProperty().getCurrentPrice());
            entitiesCreator.getDivisions()[getProperty().getLand().getDivision().getCode()].getProperties().remove(getProperty());
            getProperty().setState(Property.OCCUPIED);
            entitiesCreator.getDivisions()[getProperty().getLand().getDivision().getCode()].getProperties().add(getProperty());
        }
        return selection;
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

        /*if (rnd1 < 0.20) {
            currentPurchasingPower = previousPurchasingPower * 1.001;
        }
        else if (rnd1 < 0.40) {
            currentPurchasingPower = previousPurchasingPower * 0.999;
            currentPurchasingPower = (currentPurchasingPower < 0)? 0: currentPurchasingPower;
        }*/
        if (rnd1 < 0.60) {
            currentPurchasingPower = currentPurchasingPower;
        }
        else if (rnd1 >= 0.60 && rnd1 < 0.80) {
            currentPurchasingPower = previousPurchasingPower * 1.03;
        }
        else{
            currentPurchasingPower = 0.97 * previousPurchasingPower;
        }

        if (rnd2 < 0.60) {
            currentNetMonthlyIncome = currentNetMonthlyIncome;
        }
        else if (rnd2 >= 0.60 && rnd2 < 0.80) {
            currentNetMonthlyIncome = previousNetMonthlyIncome * 1.03;
        }
        else{
            currentNetMonthlyIncome = previousNetMonthlyIncome * 0.97;
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

    @Override
    public void agentUpdateBeliefs(EntitiesCreator builder, int time) {
//, ArrayList<Property> freeProperties, ArrayList<Property> forRentProperties
        step(time);
        Property cheapestProperty = null;
        double cheapestPrice = Double.POSITIVE_INFINITY;

        // Updates de affordBuying and affordRenting beliefs and gets the cheapest property
        int purchFound = 0;
        int rentFound = 0;

        for (Property property : builder.getFreeProperties()) {
            if (property.getCurrentPrice() < cheapestPrice) {
                cheapestPrice = property.getCurrentPrice();
                cheapestProperty = property;
            }

            if (getCurrentPurchasingPower() >= property.getCurrentPrice()) {
                addPurchasableProperty(property);
                purchFound++;
            }

        }

        for (Property property : builder.getForRentProperties()) {
            if(getCurrentNetMonthlyIncome() >= property.getCurrentCapitalizedRent()){
                addRentableProperty(property);
                rentFound++;
            }
        }
        if(purchFound > 0){
           updateBelief("ab:" + Double.toString(purchFound/(0.0 + builder.getFreeProperties().size())));
        }
        else{
           updateBelief("not ab:1");
        }
        if(rentFound > 0){
            updateBelief("ar:" + Double.toString(rentFound/(0.0 + builder.getForRentProperties().size())));
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
    }



}
