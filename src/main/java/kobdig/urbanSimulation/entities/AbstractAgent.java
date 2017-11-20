package kobdig.urbanSimulation.entities;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.gui.FactParser;
import kobdig.logic.TruthDegree;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class AbstractAgent implements IAgent {

    private Agent agent;
    private String id;

    public AbstractAgent(String id, Agent agent){
        this.id=id;
        this.agent = agent;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void updateBelief(String stringFact){
        FactParser parser = new FactParser(stringFact);
        Fact fact = parser.getFact();
        TruthDegree truthDegree = parser.getTrust();
        agent.updateBeliefs(fact,truthDegree);
        agent.updateDesires();
        agent.updateGoals();

    }

}
