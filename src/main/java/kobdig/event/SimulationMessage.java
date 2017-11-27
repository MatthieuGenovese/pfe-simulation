package kobdig.event;

public class SimulationMessage {
    private int nbrHousehold;
    private int nbrInvestor;
    private int nbrPromoter;

    private int num;

    public int getNum() {
        return num;
    }

    public int getNbrHousehold() {
        return nbrHousehold;
    }

    public int getNbrInvestor() {
        return nbrInvestor;
    }

    public int getNbrPromoter() {
        return nbrPromoter;
    }

    public SimulationMessage(int nbrHousehold, int nbrInvestor, int nbrPromoter, int num){
        this.nbrHousehold = nbrHousehold;
        this.nbrInvestor = nbrInvestor;
        this.nbrPromoter = nbrPromoter;

        this.num = num;
    }
}
