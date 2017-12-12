package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Fact;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.IActionnable;
import kobdig.urbanSimulation.entities.environement.Land;
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
public class Promoter extends AbstractAgent implements IActionnable {

    public static final String BUY_LAND = "bl";
    public static final String SELL_OFF_PLANS = "sop";
    private ArrayList<Land> purchasableLand;
    private double purchasingPower;
    private double riskAverse;

    public Promoter(String id, double purchasingPower, File file) throws IOException {
        super(id, new FileInputStream(file));
        this.purchasableLand = new ArrayList<>();
        this.purchasingPower = purchasingPower;
        this.riskAverse = Math.random();
    }

    public double getPurchasingPower() {
        return purchasingPower;
    }

    public void addPurchasableLand(Land land){
        purchasableLand.add(land);
    }

    public ArrayList<Land> getPurchasableLand() {
        return purchasableLand;
    }

    public void setPurchasingPower(double purchasingPower) {
        this.purchasingPower = purchasingPower;
    }

    @Override
    public void agentUpdateBeliefs(EntitiesCreator entitiesCreator, int time) {

        step(time);
        int purchFound = 0;

        // Updates affordBuyingLand
        for (Land land : entitiesCreator.getForSaleLand()) {
            if(getPurchasingPower() >= land.getPrice()){
                addPurchasableLand(land);
                purchFound++;
            }
        }

        if(purchFound > 0) updateBelief("abl:" + Double.toString(purchFound/(0.0 + entitiesCreator.getForSaleLand().size())));

        else updateBelief("not abl:1");

        // Updates ac
        // TODO: Improve this approach

        double rnd = Math.random();
        if (rnd < 0.5) updateBelief("ac:" + 1);
        else updateBelief("not ac:" + 1);
    }


    @Override
    public void agentIntentionsStep(EntitiesCreator entitiesCreator) {
        Iterator<Fact> iter = goals().factIterator();
        while(iter.hasNext()) {
            String goal = iter.next().formula().toString();
            //if (goal.contains(Promoter.BUY_LAND) && goal.contains(Promoter.SELL_OFF_PLANS)){
            if (goal.contains(Promoter.BUY_LAND)){
                Land taken = buyLand(entitiesCreator);
                if (taken != null){
                    entitiesCreator.getForSaleLand().remove(taken);
                    Property construction = null;
                    try {
                        int id = entitiesCreator.getIdManager()[0]++;
                        construction = new Property(Integer.toString(id),taken.getLatitude(),
                                taken.getLongitude(),(taken.getPrice() + 150), taken.getPrice()/10, taken.getPrice(),
                                taken.getGeom(), taken);
                        construction.setDivision(taken.getDivision());
                        taken.getDivision().addProperty(construction);
                        construction.setState(Property.FOR_SALE);
                        entitiesCreator.getFreeProperties().add(construction);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Purchases a land
     * @return The land purchased
     */
    public Land buyLand(EntitiesCreator entitiesCreator){
        double maxUtility = 0.0;
        Land selection = null;
        for (Land purchasable : getPurchasableLand()) {
            //System.out.println(purchasable.getId());
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = purchasable.getEquipementUtility();
                    double transportUtility = purchasable.getTransportUtility();
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
            setPurchasingPower(getPurchasingPower() - selection.getPrice());
        }
        return selection;
    }

    public void step(int time){

        double rnd1 = Math.random();

        purchasableLand = new ArrayList<>();

        if (rnd1 < 0.60) {
            purchasingPower = purchasingPower;
        }
        else if (rnd1 >= 0.60 && rnd1 < 0.80) {
            purchasingPower = purchasingPower * 1.03;
        }
        else{
            purchasingPower = 0.97 * purchasingPower;
        }

        // Updates the investDegree belief
        double rnd2 = Math.random();

        if (rnd2 > 0.5) updateBelief("ra:" + 1);
        else updateBelief("not ra:" + 1);
    }
}
