package hibernate;

import java.util.ArrayList;
import org.hibernate.Session;

import java.util.List;
import org.hibernate.Transaction;

public class SmartDevice 
{

    private Session session = HibernateUtil.getSession();
    
    public static void main(String[] args) 
    {
      SmartDevice smartDevice= new SmartDevice();
        
      //Init database
      smartDevice.session.beginTransaction();
      
      ArrayList<Background> availableBackgrounds = new ArrayList<Background>();
      availableBackgrounds.add(new Background("Nyári háttér"));
      availableBackgrounds.add(new Background("Őszi háttér"));
      availableBackgrounds.add(new Background("Téli háttér"));
      availableBackgrounds.add(new Background("Tavaszi háttér"));
      
      for(Background b : availableBackgrounds)
      {
          smartDevice.session.save(b);
      }
      
      ArrayList<Theme> availableThemes = new ArrayList<Theme>();
      availableThemes.add(new Theme("Piros arculat"));
      availableThemes.add(new Theme("Sárga arculat"));
      availableThemes.add(new Theme("Kék arculat"));
      availableThemes.add(new Theme("Zöld arculat"));
      
      for(Theme t : availableThemes)
      {
          smartDevice.session.save(t);
      }
      
      ArrayList<App> availableApps = new ArrayList<App>();
      availableApps.add(new App("Recept alkalmazás"));
      availableApps.add(new App("GPS alkalmazás"));
      availableApps.add(new App("Autós alkalmazás"));
      availableApps.add(new App("Tetris alkalmazás"));
      
      for(App a : availableApps)
      {
          smartDevice.session.save(a);
      }
      
      smartDevice.session.getTransaction().commit();
      
      
      //Random Simulation
      ArrayList<String> possibleUserNamesForRandomSimulation = new ArrayList<String>();
      possibleUserNamesForRandomSimulation.add("Aladár");
      possibleUserNamesForRandomSimulation.add("Béla");
      possibleUserNamesForRandomSimulation.add("Cecília");
      possibleUserNamesForRandomSimulation.add("Dénes");
      possibleUserNamesForRandomSimulation.add("Elemér");
      possibleUserNamesForRandomSimulation.add("Ferenc");
      possibleUserNamesForRandomSimulation.add("Géza");
      
      for(int i=0; i<4; ++i)
      {
          int nameIndex = (int) (Math.random() *possibleUserNamesForRandomSimulation.size());
          User user = smartDevice.addUser(possibleUserNamesForRandomSimulation.get(nameIndex));
          possibleUserNamesForRandomSimulation.remove(nameIndex);
          
          ArrayList<Background> possibleBackgrounds = new ArrayList<>(availableBackgrounds);
          for(int j =0; j<3; ++j)
          {
                int backgroundIndex = (int) (Math.random() *possibleBackgrounds.size());
                smartDevice.addBackground(user, possibleBackgrounds.get(backgroundIndex));         
                possibleBackgrounds.remove(backgroundIndex);
          }
          
          int activeBackgroundIndex = (int) (Math.random() *user.getBackgrounds().size());
          smartDevice.setBackground(user, activeBackgroundIndex);
          
          
          ArrayList<Theme> possibleThemes = new ArrayList<>(availableThemes);
          for(int j =0; j<3; ++j)
          {
                int themeIndex = (int) (Math.random() *possibleThemes.size());
                smartDevice.addTheme(user, possibleThemes.get(themeIndex));         
                possibleThemes.remove(themeIndex);
          }
          
          int activeThemeIndex = (int) (Math.random() *user.getThemes().size());
          smartDevice.setTheme(user, activeThemeIndex);
          
          ArrayList<App> possibleApps = new ArrayList<>(availableApps);
          for(int j =0; j<3; ++j)
          {
                int appIndex = (int) (Math.random() *possibleApps.size());
                smartDevice.addApp(user, possibleApps.get(appIndex));         
                possibleApps.remove(appIndex);
          }
          
          int activeAppIndex = (int) (Math.random() *user.getApps().size());
          smartDevice.startApp(user, activeAppIndex);
                  
      }
        
      List<User> createdUsers = smartDevice.session.createQuery("FROM User").getResultList();
      
      System.out.println();
      
      System.out.format("%20s %30s %20s %20s %60s","Felhasználók","Elindított alkalamazás","Háttérkép","Aktív arculat","Telepített alkalamzások");
      System.out.println();
      
      
      for(User u : createdUsers)
      {
          System.out.format("%20s %30s %20s %20s",u.getUserName(), u.getActiveApp().getAppName(),  u.getActiveBackground().getBackgroundName(), u.getActiveTheme().getThemeName());
          for(int i=0; i<u.getApps().size(); ++i)
          {
              System.out.format("%20s",u.getApps().get(i).getAppName());
          }
          System.out.println();
      }
      
    }
     
    public User addUser(String userName)
    {
        Transaction tx = null;
        
        User user = new User(userName);   
        try
        {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            
        }
        catch(Exception e)
        {
            System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
        return user;
    }
    
    public void removeUser(User user)
    {
        Transaction tx = null;
        
        try
        {
            tx = session.beginTransaction();
            if (!isUserExist(user))
            {
                throw new IllegalArgumentException();       
            }
            else
            {
                User u = (User) session.find(User.class,user.getUserId());
                session.remove(u);
                tx.commit();
            }
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void modifyUser(User user, String newName)
    {
        Transaction tx =null;
        try
        {
            tx = session.beginTransaction();
            if (!isUserExist(user))
            {
                throw new IllegalArgumentException();       
            }
            User u = (User) session.find(User.class,user.getUserId());
            u.setUserName(newName);
            tx.commit();
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public boolean isUserExist(User user)
    {
        return session.contains(user);
    }
    
    public void addMenuPoint(User user, String menuPointName)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            if (!isUserExist(user))
            {
                throw new IllegalArgumentException();       
            }
            MenuPoint m = new MenuPoint(menuPointName);
            session.save(m);
            m.setUser(user);
            user.getMenuPoints().add(m);
            session.getTransaction().commit();
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }   
    }
    
    public void removeMenuPoint(User user, int menuPointIndex)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            if (!isUserExist(user))
            {
                throw new IllegalArgumentException();       
            }
            if(menuPointIndex >= user.getMenuPoints().size())
            {
                throw new IndexOutOfBoundsException();
            }
            MenuPoint m = (MenuPoint)session.find(MenuPoint.class,user.getMenuPoints().get(menuPointIndex).getMenuPointId());
            
            List<SubMenuPoint> sms = m.getSubMenuPoints();
            
            for(int i=0; i< sms.size(); ++i)
            {
                removeSubMenuPoint(user, menuPointIndex, i);
            }
            
            session.delete(m);
            user.getMenuPoints().remove(m);
            tx.commit();
        }
        catch(Exception e)
        {
            if (e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű menüpont nem létezik.");
            else
                System.out.println("Hiba a tranzakció során");
            
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void modifyMenuPoint(User user, int menuPointIndex, String newName)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            MenuPoint m = (MenuPoint)session.find(MenuPoint.class,user.getMenuPoints().get(menuPointIndex).getMenuPointId());
            m.setMenuPointName(newName);
            tx.commit();
        }
        catch(Exception e)
        {
            if (e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű menüpont nem létezik.");
            else
                System.out.println("Hiba a tranzakció során");
            
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void addIcon(User user, String iconName)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            User u = (User) session.find(User.class,user.getUserId());
            Icon i = new Icon(iconName);
            session.save(i);
            i.setUser(user);
            user.getIcons().add(i);
            tx.commit();
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }  
    }
    
    public void removeIcon(User user, int iconIndex)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            Icon i = (Icon)session.find(Icon.class,user.getIcons().get(iconIndex).getIconId());
            session.delete(i);
            user.getIcons().remove(i);
            tx.commit();
        }
        catch(Exception e)
        {
            if (e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű ikon nem létezik.");
            else
                System.out.println("Hiba a tranzakció során");
            
            if(tx != null)
                tx.rollback();
        }  
    }
    
    public void modifyIcon(User user, int iconIndex, String newName)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            Icon i = (Icon)session.find(Icon.class,user.getIcons().get(iconIndex).getIconId());
            i.setIconName(newName);
            tx.commit();
        }
        catch(Exception e)
        {
            if (e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű ikon nem létezik.");
            else
                System.out.println("Hiba a tranzakció során");
            
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void addBackground(User user, Background background)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            user.getBackgrounds().add(background);
            background.getUsers().add(user);
            tx.commit();
            
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        } 
    }
    
    public void addTheme(User user, Theme theme)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            user.getThemes().add(theme);
            theme.getUsers().add(user);
            tx.commit();
            
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        } 
    }
    
    public void addApp(User user, App app)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            user.getApps().add(app);
            app.getUsers().add(user);
            tx.commit();
            
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        } 
    }
    
    public void setAppToMenuPoint(User user, int appIndex, int menuPointIndex)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            user.getMenuPoints().get(menuPointIndex).setAffectedApp(user.getApps().get(appIndex));
            tx.commit();
            
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű menüpont nem létezik.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void setAppToIcon(User user, int appIndex, int iconIndex)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            user.getIcons().get(iconIndex).setAffectedApp(user.getApps().get(appIndex));
            tx.commit();
            
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű ikon nem létezik.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void startApp(User user, int appIndex)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            user.setActiveApp(user.getApps().get(appIndex));
            tx.commit();
            
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű app nem létezik.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void setBackground(User user, int backgroundIndex)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            user.setActiveBackground(user.getBackgrounds().get(backgroundIndex));
            tx.commit();
            
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű háttér nem létezik.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void setTheme(User user, int themeIndex)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            user.setActiveTheme(user.getThemes().get(themeIndex));
            tx.commit();
            
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");          
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű arculat nem létezik.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void addSubMenuPoint(User user, int menuPointIndex, String subMenuPointName)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            MenuPoint m = user.getMenuPoints().get(menuPointIndex);
            SubMenuPoint sm = new SubMenuPoint(subMenuPointName);
            session.save(sm);
            m.getSubMenuPoints().add(sm);
            sm.setMenuPoint(m);
            tx.commit();
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű menüpont nem létezik.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void removeSubMenuPoint(User user, int menuPointIndex, int subMenuPointIndex)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            MenuPoint m = user.getMenuPoints().get(menuPointIndex);
            SubMenuPoint sm = m.getSubMenuPoints().get(subMenuPointIndex);
            session.remove(sm);
            m.getSubMenuPoints().remove(sm);
            tx.commit();
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű menüpont nem létezik.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
    
    public void modifySubMenuPoint(User user, int menuPointIndex, int subMenuPointIndex, String newName)
    {
        Transaction tx = null;
        try
        {
            tx = session.beginTransaction();
            MenuPoint m = user.getMenuPoints().get(menuPointIndex);
            SubMenuPoint sm = m.getSubMenuPoints().get(subMenuPointIndex);
            sm.setSubMenuPointName(newName);
            tx.commit();
        }
        catch(Exception e)
        {
            if(e instanceof IllegalArgumentException)
                System.out.println("A megadott felhasználó nem létezik a rendszerben.");
            else if(e instanceof IndexOutOfBoundsException)
                System.out.println("A megadott indexű menüpont nem létezik.");
            else
                System.out.println("Hiba a tranzakció során.");
            if(tx != null)
                tx.rollback();
        }
    }
       
}

