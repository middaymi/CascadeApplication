package controllers.PerformanceEditPage;

import data.Season;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.Performance.PerformanceEditModel;
import models.Performance.PerformanceModel;
import views.Manager;
import views.Performance.PerformanceEditPage;

public class DelSeasonBtnListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        PerformanceEditPage perEditPage = Manager.getManagerInstance().
                                                      getPerEditPage();
        PerformanceEditModel perEditModel = PerformanceEditModel.
                                            getPerformanceEditModelInstance();
        PerformanceModel perModel = PerformanceModel.
                                    getPerformanceModelInstance();
        
        Season season = (Season)perEditPage.getSeasonComboBox().getSelectedItem();
        System.out.println("selected season for delete is " + season);
        
        if (perEditModel.delSeasonFromDB(season.getId())) {
            perEditPage.getSeasonComboBox().removeItem(season);
            
            JOptionPane.showMessageDialog(Manager.getPerPage(),
            "Сезон удален",
            "Инфо", JOptionPane.WARNING_MESSAGE); 
            
            //rewrite seasons array
            perModel.getAllSeasonsFromDB();
            perModel.createComboSeasons();
            perEditModel.setAllSeasonstoCombobox();
            
        } else {return;}      
    }    
}
