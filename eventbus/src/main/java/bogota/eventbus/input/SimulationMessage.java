package bogota.eventbus.input;

public class SimulationMessage {

    private int num;

    private int nbrHousehold;
    private int nbrPromoter;
    private int nbrInvestor;

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

    @Override
    public String toString() {
        return "{" +
                "\"num\":" + num +
                ", \"nbrHousehold\":" + nbrHousehold +
                ", \"nbrPromoter\":" + nbrPromoter +
                ", \"nbrInvestor\":" + nbrInvestor +
                '}';
    }
}
