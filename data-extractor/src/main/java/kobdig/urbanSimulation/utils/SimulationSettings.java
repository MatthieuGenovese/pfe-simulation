package kobdig.urbanSimulation.utils;

import java.io.*;

/**
 * Created by Matthieu on 01/12/2017.
 */
public class SimulationSettings {

    private int mode;
    private int iterations;
    private int actualIteration;
    private String path;
    private File householdAgentFile;
    private File investorAgentFile;
    private File promoterAgentFile;


    public int getActualIteration() {
        return actualIteration;
    }

    public void setActualIteration(int actualIteration) {
        this.actualIteration = actualIteration;
    }

    public SimulationSettings(){
        mode = 0;
        iterations = 1;
        actualIteration = 0;

    }

    public void parseConfFile(){
        BufferedReader config;
        String line;
        int compteur = 0;
        String[] array;
        try {
            config = new BufferedReader(new FileReader(new File("config.conf")));
            if (config == null) {
                throw new FileNotFoundException("Fichier non trouvé: config.conf");
            }
            do {
                line = config.readLine();
                if (line != null) {
                    array = line.split(" ");
                    if(array[0].equalsIgnoreCase("path") && compteur == 0){
                        try {
                            path = array[1];
                            compteur++;
                        }
                        catch(IndexOutOfBoundsException e){
                            System.out.println("Fichier mal formaté !");
                            return;
                        }
                    }
                    else if(array[0].equalsIgnoreCase("mode") && compteur == 1){
                        try {
                            mode = Integer.valueOf(array[1]);
                            compteur++;
                        }
                        catch(IndexOutOfBoundsException e){
                            System.out.println("Fichier mal formaté !");
                            return;
                        }
                    }
                    else if(array[0].equalsIgnoreCase("iterations") && compteur == 2){
                        try {
                            iterations = Integer.valueOf(array[1]);
                            compteur++;
                        }
                        catch(IndexOutOfBoundsException e){
                            System.out.println("Fichier mal formaté !");
                            return;
                        }
                    }
                }
            } while (line != null);
            config.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public File getHouseholdAgentFile() {
        return householdAgentFile;
    }

    public void setHouseholdAgentFile(File householdAgentFile) {
        this.householdAgentFile = householdAgentFile;
    }

    public File getInvestorAgentFile() {
        return investorAgentFile;
    }

    public void setInvestorAgentFile(File investorAgentFile) {
        this.investorAgentFile = investorAgentFile;
    }

    public File getPromoterAgentFile() {
        return promoterAgentFile;
    }

    public void setPromoterAgentFile(File promoterAgentFile) {
        this.promoterAgentFile = promoterAgentFile;
    }
}
