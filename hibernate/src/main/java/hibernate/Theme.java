
package hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Theme implements Serializable
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy ="uuid2")
    private String themeId;
    
    private String themeName;
    
    @ManyToMany(mappedBy="themes")
    private List<User> users = new ArrayList<User>();
    
    private Theme()
    {
        
    }
    
    public Theme(String themeName)
    {
        this.themeName = themeName;
    }
    
    public String getThemeId() {
        return themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
    
    public List<User> getUsers()
    {
        return this.users;
    }
}
