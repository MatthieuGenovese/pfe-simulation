package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Agent;
import kobdig.agent.Fact;
import kobdig.gui.FactParser;
import kobdig.logic.TruthDegree;
import kobdig.urbanSimulation.EntitiesCreator;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Matthieu on 20/11/2017.
 */
public class AbstractAgent extends Agent implements IAgent {

    private String id;

    public AbstractAgent(String id, InputStream is) throws IOException {
        super(is);
        this.id=id;
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
        updateBeliefs(fact,truthDegree);
        updateDesires();
        updateGoals();

    }

    public void agentUpdateBeliefs(EntitiesCreator builder, int time){

    }

    public void agentIntentionsStep(EntitiesCreator builder){

    }
}
