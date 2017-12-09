package kobdig.urbanSimulation.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 09/12/2017.
 */
public class SimulationLogging {

    public SimulationLogging(){

    }

    public void writeData(String content){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(new File ("log.log"), true));
            writer.write(content + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeList(String content, List<Integer> list){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(new File ("log.log"), true));
            writer.write(content);
            writer.write("[ ");
            for(Integer i : list){
                writer.write(i.toString()+ " ");
            }
            writer.write("]");
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFileInput(String path){
        BufferedReader config;
        BufferedWriter writer;
        String pwd = new File("").getAbsolutePath();
        pwd = pwd + "/docs/";
        String line;
        try {
            config = new BufferedReader(new FileReader(new File(pwd+path)));
            writer = new BufferedWriter(new FileWriter(new File ("log.log"), true));
            if (config == null) {
                throw new FileNotFoundException("Fichier non trouvé: " + path);
            }
            do {
                line = config.readLine();
                if (line != null) {
                    writer.write(line + "\n");
                }
            } while (line != null);
            config.close();
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
