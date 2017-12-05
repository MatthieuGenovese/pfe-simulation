package kobdig.access.tables;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "households")
public class HouseholdE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "purchasingpower")
    private int purchasingpower;

    @Column(name = "netmonthlyincome")
    private int netmonthlyincome;

    public HouseholdE(){

    }

    public HouseholdE(String lastname, int purchasingpower, int netmonthlyincome){
        this.lastname = lastname;
        this.purchasingpower = purchasingpower;
        this.netmonthlyincome = netmonthlyincome;
    }

    public int getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public int getPurchasingpower() {
        return purchasingpower;
    }

    public int getNetmonthlyincome() {
        return netmonthlyincome;
    }
}
