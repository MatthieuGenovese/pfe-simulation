package kobdig.urbanSimulation;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.gui.FactParser;
import kobdig.logic.TruthDegree;

import java.util.ArrayList;

/**
 * Created by Meili on 01/07/16.
 */
public class Promoter {

    // CONSTANTS

    /**
     * Buy land
     */
    public static final String BUY_LAND = "bl";

    /**
     * Sell off plans
     */
    public static final String SELL_OFF_PLANS = "sop";

    //ATTRIBUTES

    /**
     * Unique ID for the promoter
     */
    private String id;

    /**
     * The agent associated to the promoter
     */
    private Agent agent;

    /**
     * The purchasable land list
     */
    private ArrayList<Land> purchasableLand;

    /**
     * Purchasing power
     */
    private double purchasingPower;

    /**
     * Degree of risk averse
     */
    private double riskAverse;

    /**
     * The Promoter's constructor
     * @param id The promoter's Id
     * @param agent The promoter's agent
     * @param purchasingPower The purchasing power
     */
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

    // METHODS

    /**
     * Updates the agent's beliefs, desires and goals considering a given fact
     * @param stringFact The fact
     */
    public void updateBelief(String stringFact){
        FactParser parser = new FactParser(stringFact);
        Fact fact = parser.getFact();
        TruthDegree truthDegree = parser.getTrust();
        agent.updateBeliefs(fact,truthDegree);
        agent.updateDesires();
        agent.updateGoals();
    }

    /**
     * Generates a step in the simulation
     */
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
