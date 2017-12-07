package kobdig.access.sql.tables;


import javax.persistence.*;

@Entity
@Table(name = "sauvegarde")
public class Sauvegarde {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "nbrEtape")
    private int num;

    @Column(name = "nbrHousehold")
    private int nbrHousehold;

    @Column(name = "nbrPromoter")
    private int nbrPromoter;

    @Column(name = "nbrInvestor")
    private int nbrInvestor;

    @Column(name = "idSimulation")
    private int idSimulation;

    public Sauvegarde(){

    }

    public Sauvegarde(int num, int nbrHousehold, int nbrPromoter, int nbrInvestor, int id){
        this.num = num;
        this.nbrHousehold = nbrHousehold;
        this.nbrPromoter = nbrPromoter;
        this.nbrInvestor = nbrInvestor;
        this.idSimulation = id;
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
        return idSimulation;
    }

    public void setId(int id) {
        this.idSimulation = id;
    }

    @Override
    public String toString(){
        return "Sauvegarde{" +
                "id: " + idSimulation + ", " +
                "num" + num + " }";
    }
}
