package kobdig.urbanSimulation.entities.agents;



import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.environement.Property;

import java.util.ArrayList;

/**
 * Created by Matthieu on 20/11/2017.
 */
public interface IAgentBuy {

    public double getCurrentPurchasingPower();
    public double getCurrentNetMonthlyIncome();
    public double getPreviousPurchasingPower();
    public double getPreviousNetMonthlyIncome();
    public void setPreviousNetMonthlyIncome(double previousNetMonthlyIncome) ;
    public void setPreviousPurchasingPower(double previousPurchasingPower) ;
    public void setCurrentPurchasingPower(double currentPurchasingPower);
    public void setCurrentNetMonthlyIncome(double netMonthlyIncome);
    public ArrayList<Property> getPurchasableProperties();
    public void addPurchasableProperty(Property purchasable);
    public Property getProperty();
    public void setProperty(Property property);
    public Property invest(EntitiesCreator builder);
}
