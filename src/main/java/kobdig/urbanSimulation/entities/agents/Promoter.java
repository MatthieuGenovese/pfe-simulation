package kobdig.urbanSimulation.entities.agents;

import kobdig.urbanSimulation.EntitiesCreator;
import kobdig.urbanSimulation.entities.IActionnable;
import kobdig.urbanSimulation.entities.environement.Land;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class Promoter extends AbstractAgent implements IActionnable {

    public static final String BUY_LAND = "bl";
    public static final String SELL_OFF_PLANS = "sop";
    private ArrayList<Land> purchasableLand;
    private double purchasingPower;
    private double riskAverse;

    public Promoter(EntitiesCreator entitiesCreator, String id, double purchasingPower, InputStream is) throws IOException {
        super(entitiesCreator, id, new FileInputStream(entitiesCreator.getPromoterAgentFile()));
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
