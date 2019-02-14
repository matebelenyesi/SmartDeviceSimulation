
package hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Background implements Serializable
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy ="uuid2")
    private String backgroundId;
    
    private String backgroundName;
    
    @ManyToMany(mappedBy="backgrounds")
    private List<User> users = new ArrayList<User>();
    
    private Background()
    {
        
    }
    
    public Background(String backgroundName)
    {
        this.backgroundName = backgroundName;
    }
    
    public String getBackgroundId() {
        return backgroundId;
    }

    public String getBackgroundName() {
        return backgroundName;
    }
    
    public void setBackgroundId(String backgroundId) {
        this.backgroundId = backgroundId;
    }

    public void setBackgroundName(String backgroundName) {
        this.backgroundName = backgroundName;
    }
    
    public List<User> getUsers()
    {
        return users;
    }
}
