
package hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class User implements Serializable
{   

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy ="uuid2")
   private String userId;
   
   private String userName;
   
   @OneToOne
   @JoinColumn(name = "fk_activebackground")
   private Background activeBackground;
   
   @OneToOne
   @JoinColumn(name = "fk_activetheme")
   private Theme activeTheme;
   
   @OneToOne
   @JoinColumn(name = "fk_activeapp")
   private App activeApp;
   
   //Menupoints and icons are user specific and editable
   @OneToMany (mappedBy = "user")
   private List<MenuPoint> menuPoints = new ArrayList<MenuPoint>();
   
   @OneToMany (mappedBy = "user")
   private List<Icon> icons = new ArrayList<Icon>();
   
   //Themes, backgrounds and apps are fix, predefined. More user can own them.
   @ManyToMany
   @JoinTable(name="user_theme",
           joinColumns = {@JoinColumn(name = "fk_user")},
           inverseJoinColumns = {@JoinColumn(name = "fk_theme")})
   private List<Theme> themes = new ArrayList<Theme>();
   
   @ManyToMany
   @JoinTable(name="user_background",
           joinColumns = {@JoinColumn(name = "fk_user")},
           inverseJoinColumns = {@JoinColumn(name = "fk_background")})
   private List<Background> backgrounds = new ArrayList<Background>();
     
   @ManyToMany
   @JoinTable(name="user_app",
           joinColumns = {@JoinColumn(name = "fk_user")},
           inverseJoinColumns = {@JoinColumn(name = "fk_app")})
   private List<App> apps = new ArrayList<App>();
  
   private User()
   {
       
   }
       
   public User(String userName)
   {
       this.userName = userName;
   }
   
       public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
    
        public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public List<MenuPoint> getMenuPoints()
    {
        return menuPoints;
    }
        
    public List<Icon> getIcons()
    {
        return icons;
    }
        
    public List<Theme> getThemes()
    {
        return themes;
    }
    
    public List<Background> getBackgrounds()
    {
        return backgrounds;
    }
    
    public List<App> getApps()
    {
        return apps;
    }
    
    public void setActiveApp(App app)
    {
        activeApp = app;
    }
    
    public void setActiveBackground(Background background)
    {
        activeBackground = background;
    }
    
    public void setActiveTheme(Theme theme)
    {
        activeTheme = theme;
    }
    
    public Background getActiveBackground() 
    {
        return activeBackground;
    }

    public Theme getActiveTheme() {
        
        return activeTheme;
    }

    public App getActiveApp() 
    {
        return activeApp;
    }
    
}
