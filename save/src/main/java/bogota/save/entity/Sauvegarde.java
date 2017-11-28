package bogota.save.entity;

public class Sauvegarde {

    private int num;

    private int nbrHousehold;
    private int nbrPromoter;
    private int nbrInvestor;

    private int id;

    public Sauvegarde(int num, int nbrHousehold, int nbrPromoter, int nbrInvestor, int id){
        this.num = num;
        this.nbrHousehold = nbrHousehold;
        this.nbrPromoter = nbrPromoter;
        this.nbrInvestor = nbrInvestor;
        this.id = id;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return "Sauvegarde{" +
                "id: " + id + ", " +
                "num" + num + " }";
    }
}
