package kobdig.urbanSimulation.entities.agents;

import kobdig.agent.Agent;

/**
 * Created by Matthieu on 20/11/2017.
 */
public interface IAgent {

    public String getId();
    public void setId(String id);
    public void updateBelief(String stringFact);

}
