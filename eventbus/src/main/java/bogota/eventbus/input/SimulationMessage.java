package bogota.eventbus.input;

import java.util.ArrayList;
import java.util.List;

public class SimulationMessage {

    private int num;

    private int nbrHousehold;
    private int nbrPromoter;
    private int nbrInvestor;

    private List<Integer> listOfEquipment = new ArrayList<>();
    private List<Integer> listOfTransport = new ArrayList<>();

    private String fileHousehold;
    private String fileInvestor;
    private String filePromoter;

    public int getNbrHousehold() {
        return nbrHousehold;
    }

    public void setNbrHousehold(int nbrHousehold){
        this.nbrHousehold = nbrHousehold;
    }

    public int getNbrPromoter() {
        return nbrPromoter;
    }

    public void setNbrPromoter(int nbrPromoter) {
        this.nbrPromoter = nbrPromoter;
    }

    public int getNbrInvestor() {
        return nbrInvestor;
    }

    public void setNbrInvestor(int nbrInvestor) {
        this.nbrInvestor = nbrInvestor;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Integer> getListOfEquipment(){
        return listOfEquipment;
    }

    public void setListOfEquipment(List<Integer> listOfEquipment) {
        this.listOfEquipment = listOfEquipment;
    }

    public List<Integer> getListOfTransport() {
        return listOfTransport;
    }

    public void setListOfTransport(java.util.List<Integer> listOfTransport) {
        this.listOfTransport = listOfTransport;
    }

    public String getFileHousehold() {
        return fileHousehold;
    }

    public void setFileHousehold(String fileHousehold) {
        this.fileHousehold = fileHousehold;
    }

    public void setFileInvestor(String fileInvestor) {
        this.fileInvestor = fileInvestor;
    }

    public void setFilePromoter(String filePromoter) {
        this.filePromoter = filePromoter;
    }

    public String getFileInvestor() {
        return fileInvestor;
    }

    public String getFilePromoter() {
        return filePromoter;
    }

    @Override
    public String toString() {
        return "{" +
                "\"num\":" + num +
                ", \"nbrHousehold\":" + nbrHousehold +
                ", \"nbrPromoter\":" + nbrPromoter +
                ", \"nbrInvestor\":" + nbrInvestor +
                ", \"listOfEquipment\":" + listOfEquipment +
                ", \"listOfTransport\":" + listOfTransport +
                ", \"fileHousehold\":" + fileHousehold +
                ", \"fileInvestor\":" + fileInvestor +
                ", \"filePromoter\":" + filePromoter +
                '}';
    }
}
