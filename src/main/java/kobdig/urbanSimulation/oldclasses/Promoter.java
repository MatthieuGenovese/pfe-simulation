package kobdig.urbanSimulation.oldclasses;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.gui.FactParser;
import kobdig.logic.TruthDegree;
import kobdig.urbanSimulation.Land;

import java.util.ArrayList;

/**
 * Created by Meili on 01/07/16.
 */
public class Promoter {

    public static final String BUY_LAND = "bl";

    public static final String SELL_OFF_PLANS = "sop";
    private String id;
    private Agent agent;
    private ArrayList<Land> purchasableLand;
    private double purchasingPower;
    private double riskAverse;
    public Promoter(String id, Agent agent, double purchasingPower){
        this.id = id;
        this.agent = agent;
        this.purchasableLand = new ArrayList<>();
        this.purchasingPower = purchasingPower;
        this.riskAverse = Math.random();
    }

    public Agent getAgent() {
        return agent;
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

    public void updateBelief(String stringFact){
        FactParser parser = new FactParser(stringFact);
        Fact fact = parser.getFact();
        TruthDegree truthDegree = parser.getTrust();
        agent.updateBeliefs(fact,truthDegree);
        agent.updateDesires();
        agent.updateGoals();
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
