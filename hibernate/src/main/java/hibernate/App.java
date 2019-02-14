
package hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;


@Entity
public class App implements Serializable
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy ="uuid2")
    private String appId;
    
    private String appName;
    
    @ManyToMany(mappedBy="apps")
    private List<User> users = new ArrayList<User>();
    
    private App()
    {
        
    }
    
    public App(String appName)
    {
        this.appName = appName;
    }
    
    public String getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
    
    public List<User> getUsers()
    {
        return users;
    }
    
}
