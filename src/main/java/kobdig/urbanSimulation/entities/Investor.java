package kobdig.urbanSimulation.entities;

import kobdig.agent.Agent;
import kobdig.urbanSimulation.Property;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class Investor extends AbstractAgentBuy implements IActionnable {

    public static final String BUY = "b";
    public static final String SELL = "s";
    public static final String NOT_SELL = "~s";
    public static final String LANDLORD = "l";
    private Household household;
    private kobdig.urbanSimulation.oldclasses.Investor investor;
    private double investDegree;
    private double speculate;
    private double currentRent;
    private boolean owner;

    // CONSTRUCTOR

    /**
     * Independent Investor's constructor
     * @param id The id
     * @param agent The BDI agent that represents the investor
     * @param purchasingPower The current purchasing power
     */
    public Investor(String id, Agent agent, double purchasingPower, double netMonthlyIncome){
        super(id, agent, purchasingPower, netMonthlyIncome);
        this.investDegree = Math.random();
        this.speculate = Math.random();
        this.owner = false;
        this.currentRent = 0;
    }

    public Investor(Agent agent, Household household, Property property){
        super(household.getId(), agent, household.getCurrentPurchasingPower(), household.getCurrentNetMonthlyIncome());
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
}
