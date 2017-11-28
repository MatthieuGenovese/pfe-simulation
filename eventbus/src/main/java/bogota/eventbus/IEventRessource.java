package bogota.eventbus;

public interface IEventRessource {

    String getType();
    Object getValue();
    void setType(String type);
}
