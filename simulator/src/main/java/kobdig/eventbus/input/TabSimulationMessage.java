package kobdig.eventbus.input;

import java.util.List;

public class TabSimulationMessage {

    private List<SimulationMessage> simulationMessageList;

    public List<SimulationMessage> getSimulationMessageList() {
        return simulationMessageList;
    }

    public void setSimulationMessageList(List<SimulationMessage> simulationMessageList) {
        this.simulationMessageList = simulationMessageList;
    }

    @Override
    public String toString() {
        String res = "{";
        for(SimulationMessage simulationMessage : simulationMessageList){
            res = res + simulationMessage.toString();
        }
        res = res + "}";

        return res;
    }
}
