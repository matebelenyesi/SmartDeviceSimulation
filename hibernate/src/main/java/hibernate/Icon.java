
package hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Icon implements Serializable
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy ="uuid2")
    private String iconId;
    
    private String iconName;
    
    @OneToOne
    @JoinColumn(name = "fk_affectedapp")
    private App affectedApp;
    
    @ManyToOne
    @JoinColumn(name= "fk_user")
    User user;
    
    private Icon()
    {
        
    }
    
    public Icon(String iconName)
    {
        this.iconName = iconName;
    }
    
    public String getIconId() {
        return iconId;
    }

    public String getIconName() {
        return iconName;
    }

    public App getAffectedApp() {
        return affectedApp;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setAffectedApp(App affectedApp) {
        this.affectedApp = affectedApp;
    }
    
    public void setUser(User user)
    {
        this.user =user;
    }
    
}
