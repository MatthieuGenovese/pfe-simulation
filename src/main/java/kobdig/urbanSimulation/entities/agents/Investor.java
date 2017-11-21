package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Agent;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.IActionnable;
import kobdig.urbanSimulation.entities.environement.Property;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public Investor(EntitiesCreator entitiesCreator, String id, double purchasingPower, double netMonthlyIncome) throws IOException {
        super(entitiesCreator, id, purchasingPower, netMonthlyIncome, new FileInputStream(entitiesCreator.getInvestorAgentFile()));
        this.investDegree = Math.random();
        this.speculate = Math.random();
        this.owner = false;
        this.currentRent = 0;
    }

    public Investor(EntitiesCreator entitiesCreator, Household household, Property property) throws IOException {
        super(entitiesCreator, household.getId(), household.getCurrentPurchasingPower(), household.getCurrentNetMonthlyIncome(), new FileInputStream(entitiesCreator.getInvestorAgentFile()));
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
