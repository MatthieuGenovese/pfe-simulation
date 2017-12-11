package bogota.eventbus.input;

/**
 * Created by Matthieu on 11/12/2017.
 */
public class ExtractDataMessage {
    private String entity;
    private int idSimulation;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getIdSimulation() {
        return idSimulation;
    }

    public void setIdSimulation(int idSimulation) {
        this.idSimulation = idSimulation;
    }
}
