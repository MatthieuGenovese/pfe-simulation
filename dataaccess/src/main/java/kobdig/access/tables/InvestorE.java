package kobdig.access.tables;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "investors")
public class InvestorE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "purchasingpower")
    private int purchasingpower;

    public InvestorE(){

    }

    public InvestorE(int purchasingpower){
        this.purchasingpower = purchasingpower;
    }

    public int getId() {
        return id;
    }

    public int getPurchasingpower() {
        return purchasingpower;
    }
}
