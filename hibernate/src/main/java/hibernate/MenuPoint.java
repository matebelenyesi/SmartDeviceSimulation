
package hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class MenuPoint implements Serializable
{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy ="uuid2")
    private String menuPointId;
    
    private String menuPointName;
    
    @OneToOne
    @JoinColumn(name = "fk_affectedapp")
    private App affectedApp;
    
    @OneToMany (mappedBy = "menuPoint")
    private List<SubMenuPoint> subMenuPoints = new ArrayList<SubMenuPoint>();
    
    @ManyToOne
    @JoinColumn(name= "fk_user")
    private User user;
    
    private MenuPoint()
    {
        
    }
    
    public MenuPoint(String menuPointName)
    {
        this.menuPointName = menuPointName;
    }
    
     public String getMenuPointId() {
        return menuPointId;
    }

    public String getMenuPointName() {
        return menuPointName;
    }

    public App getAffectedApp() {
        return affectedApp;
    }
    
    public String getUserId()
    {
        return user.getUserId();
    }

    public void setMenuPointId(String menuPointId) {
        this.menuPointId = menuPointId;
    }

    public void setMenuPointName(String menuPointName) {
        this.menuPointName = menuPointName;
    }

    public void setAffectedApp(App affectedApp) {
        this.affectedApp = affectedApp;
    }
    
    public void setUser(User user)
    {
        this.user = user;
    }
    
    public List<SubMenuPoint> getSubMenuPoints()
    {
        return subMenuPoints;
    }
}
