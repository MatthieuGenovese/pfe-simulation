package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Fact;
import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.IActionnable;
import kobdig.urbanSimulation.entities.environement.Land;
import kobdig.urbanSimulation.entities.environement.Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

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
        System.out.println(getPurchasableLand().size());
        for (Land purchasable : getPurchasableLand()) {
            System.out.println(purchasable.getId());
            try {
                if (purchasable.getDivision() != null && !purchasable.isUpdated()) {
                    double equipUtility = 0.0;
                    double transportUtility = 0.0;

                    Statement s1 = entitiesCreator.getConn().createStatement();
                    String query_equipments = "SELECT COUNT(a.*) FROM " + entitiesCreator.getFilteredEquipments() + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getId();
                    ResultSet r1 = s1.executeQuery(query_equipments);
                    if(r1.next()) {
                        equipUtility = r1.getInt(1);
                    }
                    s1.close();
                    r1.close();


                    Statement s2 = entitiesCreator.getConn().createStatement();
                    String query_transport = "SELECT COUNT(a.*) FROM " + entitiesCreator.getFilteredNetwork() + ")) a INNER JOIN buffer b ON ST_Intersects(a.geom, b.geom) WHERE b.id_land = " + purchasable.getId();
                    ResultSet r2 = s2.executeQuery(query_transport);
                    if(r2.next()) {
                        transportUtility = r2.getInt(1);
                    }
                    s2.close();
                    r2.close();
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
            catch (SQLException e){
                e.printStackTrace();
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

        if (rnd1 < 0.20) {
            purchasingPower = purchasingPower * 1.001;
        }
        else if (rnd1 < 0.40) {
            purchasingPower = purchasingPower * 0.999;
            purchasingPower = (purchasingPower < 0)? 0: purchasingPower;
        }

        // Updates the investDegree belief
        double rnd2 = Math.random();

        if (rnd2 > 0.5) updateBelief("ra:" + 1);
        else updateBelief("not ra:" + 1);
    }
}
