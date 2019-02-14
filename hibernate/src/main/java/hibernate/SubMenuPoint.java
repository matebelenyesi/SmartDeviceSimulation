
package hibernate;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class SubMenuPoint implements Serializable
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy ="uuid2")
    private String subMenuPointId;
    
    private String subMenuPointName;
    
    @OneToOne
    @JoinColumn(name = "fk_affectedapp")
    private App affectedApp;
    
    @ManyToOne
    @JoinColumn(name= "fk_menupoint")
    private MenuPoint menuPoint;
    
    private SubMenuPoint()
    {
        
    }
    
    public SubMenuPoint(String subMenuPointName)
    {
        this.subMenuPointName = subMenuPointName;
    }
    
    public String getSubMenuPointId() {
        return subMenuPointId;
    }

    public String getSubMenuPointName() {
        return subMenuPointName;
    }

    public App getAffectedApp() {
        return affectedApp;
    }

    public void setSubMenuPointId(String subMenuPointId) {
        this.subMenuPointId = subMenuPointId;
    }

    public void setSubMenuPointName(String subMenuPointName) {
        this.subMenuPointName = subMenuPointName;
    }

    public void setAffectedApp(App affectedApp) {
        this.affectedApp = affectedApp;
    }
    
    public void setMenuPoint(MenuPoint menuPoint)
    {
        this.menuPoint = menuPoint;
    }
}
