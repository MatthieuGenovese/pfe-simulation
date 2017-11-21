package kobdig.urbanSimulation;

public class Launcher {

    private Simulation simulation;

    public void init(){
        simulation = new Simulation();
        simulation.start();
    }

    public static void main(String[] args){
        Launcher launcher = new Launcher();
        launcher.init();
    }
}
