/*VALUES OF THE 'CURRENT PANEL':
0 - START PAGE
10 - ORGANIZATION PAGE
20 - EMPLOYEE PAGE
30 - ATHLETE PAGE
40 - PERFORMANE PAGE
50 - COMPETITION PAGE
60 - TESTING PAGE
*/

package cascadeapp;

import javax.swing.SwingUtilities;
import views.Manager;

public class CascadeApp {
    static Manager manager;
       
    public static void main(String[] args) {
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {                
                manager = Manager.getManagerInstance();
                System.out.println("WIDTH: " + Manager.getWidthWithInsets());
                System.out.println("HEIGHT: " + Manager.getHeightWithInsets());
                dataBase.DataBaseConnection.getInstanceDataBase(); 
            }
        });     
    }   
}    




    
    

