package main.controllers.eventbus;

public class EventRessource<T> implements IEventRessource {

    private String type;
    private T value;

    public EventRessource() {
    }

    public EventRessource(T value) {
        this.value = value;
    }

    public EventRessource(String type, T value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
