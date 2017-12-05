package kobdig.access.tables;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "promoters")
public class PromoterE implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gid")
    private int id;

    @Column(name = "purchasingpower")
    private int purchasingpower;

    public PromoterE(){

    }

    public PromoterE(int purchasingpower){
        this.purchasingpower = purchasingpower;
    }

    public int getId() {
        return id;
    }

    public int getPurchasingpower() {
        return purchasingpower;
    }
}
