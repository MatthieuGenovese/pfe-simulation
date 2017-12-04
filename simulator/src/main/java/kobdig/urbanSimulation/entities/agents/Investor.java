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
import java.util.Iterator;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class Investor extends AbstractAgentBuy implements IActionnable {

    public static final String BUY = "b";
    public static final String SELL = "s";
    public static final String NOT_SELL = "~s";
    public static final String LANDLORD = "l";
    private Household household;
    private double investDegree;
    private double speculate;
    private double currentRent;
    private boolean owner;

    // CONSTRUCTOR

    /**
     * Independent Investor's constructor
     * @param id The id
     * @param purchasingPower The current purchasing power
     */
    public Investor(String id, double purchasingPower, double netMonthlyIncome, File file) throws IOException {
        super(id, purchasingPower, netMonthlyIncome, new FileInputStream(file));
        this.investDegree = Math.random();
        this.speculate = Math.random();
        this.owner = false;
        this.currentRent = 0;
    }

    public Investor(Household household, Property property, File file) throws IOException {
        super(household.getId(), household.getCurrentPurchasingPower(), household.getCurrentNetMonthlyIncome(), new FileInputStream(file));
        setProperty(property);
        this.household = household;
        this.owner = true;
    }

    public void setOwner(boolean owner){
        this.owner = owner;
    }


    public void step(int time){
        Property property = getProperty();
        double currentPurchasingPower = getCurrentPurchasingPower();
        double previousPurchasingPower = getPreviousPurchasingPower();
        double currentNetMonthlyIncome = getCurrentNetMonthlyIncome();

        if (household != null){
            household.setCurrentNetMonthlyIncome(household.getCurrentNetMonthlyIncome() +
                    property.getCurrentCapitalizedRent() - currentRent);
            currentRent = property.getCurrentCapitalizedRent();

            currentPurchasingPower = household.getCurrentPurchasingPower();
            currentNetMonthlyIncome = household.getCurrentNetMonthlyIncome();
        }
        else{
            // TODO: Determine how the purchasing power would evolve
            previousPurchasingPower = currentPurchasingPower;
            double rnd1 = Math.random();

            if (rnd1 < 0.20) currentPurchasingPower = previousPurchasingPower * 1.001;
            else if (rnd1 < 0.4) {
                currentPurchasingPower = previousPurchasingPower * 0.999;
                currentPurchasingPower = (currentPurchasingPower < 0)? 0: currentPurchasingPower;
            }

            if (property != null){
                currentNetMonthlyIncome = currentNetMonthlyIncome + property.getCurrentCapitalizedRent() - currentRent;
                currentRent = property.getCurrentCapitalizedRent();
            }
        }

        clearPurchasableProperties();

        //TODO: Determine how these values will be calculated
        // Updates the speculate belief
        if(speculate > 0.5) {
            updateBelief("sp:" + speculate);
        }
        else{
            updateBelief("not sp:1");
        }

        // Updates the investDegree belief
        updateBelief("i:" + investDegree);

        if (property != null) {
            updateBelief("o:1");
        }
        else {
            updateBelief("not o:1");
        }
        setCurrentPurchasingPower(currentPurchasingPower);
        setCurrentNetMonthlyIncome(currentNetMonthlyIncome);
        setPreviousPurchasingPower(previousPurchasingPower);

    }

    @Override
    public Property invest(EntitiesCreator entitiesCreator){
        double maxUtility = 0.0;
        Property selection = null;
        for (Property purchasable : getPurchasableProperties()) {
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = 0.0;
                    double transportUtility = 0.0;
                    equipUtility = entitiesCreator.equipmentRepository.findById(Integer.parseInt(purchasable.getLand().getId())).size();

                    transportUtility = entitiesCreator.transportNetworkRepository.findById(Integer.parseInt(purchasable.getLand().getId())).size();

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

    @Override
    public void agentUpdateBeliefs(EntitiesCreator entitiesCreator, int time) {

        step(time);
        Property cheapestProperty = null;
        double cheapestPrice = Double.POSITIVE_INFINITY;

        // Updates de affordBuying and affordRenting beliefs and gets the cheapest property
        int purchFound = 0;

        for (Property property : entitiesCreator.getFreeProperties()) {
            if (property.getCurrentPrice() < cheapestPrice) {
                cheapestPrice = property.getCurrentPrice();
                cheapestProperty = property;
            }

            if (getCurrentPurchasingPower() >= property.getCurrentPrice()) {
                addPurchasableProperty(property);
                purchFound++;
            }
        }

        if(purchFound > 0){
            updateBelief("ab:" + Double.toString(purchFound/(0.0 + entitiesCreator.getFreeProperties().size())));
        }

        else updateBelief("not ab:1");

        // Updates the buyingRentable belief
        // TODO: Improve this approach
        if(cheapestPrice < Double.POSITIVE_INFINITY){
            if(getCurrentPurchasingPower() > cheapestPrice){
                updateBelief("br:" + cheapestProperty.getCurrentCapitalizedRent()/(0.0 +
                        cheapestProperty.getCurrentPotentialRent()));
            }
        }


        // Updates the sellingRentable belief
        // TODO: Improve this approach
        if (getProperty() != null) {
            if (cheapestPrice < Double.POSITIVE_INFINITY) {
                if (getCurrentPurchasingPower() > cheapestPrice) {
                    double sellingRentability = getProperty().getCurrentCapitalizedRent() /
                            (0.0 + getProperty().getCurrentCapitalizedRent() +
                                    cheapestProperty.getCurrentCapitalizedRent());

                    updateBelief("sr:" + sellingRentability);
                }
            }
        }
    }

    @Override
    public void agentIntentionsStep(EntitiesCreator entitiesCreator) {

        Iterator<Fact> iter = goals().factIterator();
        while(iter.hasNext()) {
            String goal = iter.next().formula().toString();

            // If the goal is to invest
            if (goal.contains(Investor.BUY) && goal.contains(Investor.LANDLORD)){
                if (getProperty() != null) {
                    Property taken = invest(entitiesCreator);
                    if (taken != null) {
                        entitiesCreator.getFreeProperties().remove(taken);
                        entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().remove(taken);
                        taken.setState(Property.SEEKING_TENANT);
                        entitiesCreator.getForRentProperties().add(taken);
                        entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().add(taken);
                        //Investor newInvestor = new Investor(investorAgent, investor, taken);
                        setProperty(taken);
                        setOwner(true);
                        System.out.println("taille liste investor : " + entitiesCreator.getInvestors().size());
                        entitiesCreator.getInvestors().add(this);
                        System.out.println("taille liste investor après le truc chelou : " + entitiesCreator.getInvestors().size());
                    }
                }
                else{
                    Property taken = invest(entitiesCreator);
                    if (taken != null) {
                        entitiesCreator.getFreeProperties().remove(taken);
                        entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().remove(taken);
                        taken.setState(Property.SEEKING_TENANT);
                        entitiesCreator.getForRentProperties().add(taken);
                        entitiesCreator.getDivisions()[taken.getLand().getDivision().getCode()].getProperties().add(taken);
                    }
                }
            }

            // If the goal is to sell
            // TODO: Improve this approach
//            if (!goal.contains(Investor.BUY) && !goal.contains(Investor.LANDLORD) && !goal.contains(Investor.NOT_SELL)
//                    && goal.contains(Investor.SELL)){
//                if (investor.getProperty() != null) {
//                    freeProperties.add(investor.getProperty());
//                    investor.getProperty().setState(Property.FOR_SALE);
//                }
//            }

        }
    }
}
