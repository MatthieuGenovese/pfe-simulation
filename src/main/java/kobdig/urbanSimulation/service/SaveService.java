package kobdig.urbanSimulation.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaveService {

    private static List<Sauvegarde> sims = new ArrayList<>();

    public void add(Sauvegarde sauvegarde){
        sims.add(sauvegarde);
    }

    public List<Sauvegarde> retrieveAllSauvegarde() {
        return sims;
    }


}
