package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Agent;
import kobdig.urbanSimulation.EntitiesCreator;

/**
 * Created by Matthieu on 20/11/2017.
 */
public interface IAgent {

    public String getId();
    public void setId(String id);
    public void updateBelief(String stringFact);
    public void agentUpdateBeliefs(EntitiesCreator builder, int time);
    public void agentIntentionsStep(EntitiesCreator builder);

}
