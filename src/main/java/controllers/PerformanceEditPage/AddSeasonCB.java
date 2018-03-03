package controllers.PerformanceEditPage;

import data.Season;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import models.Performance.PerformanceEditModel;
import models.Performance.PerformanceModel;
import views.Manager;
import views.Performance.PerformanceEditPage;

public class AddSeasonCB implements ActionListener {
    
    public void actionPerformed(ActionEvent e) {  
        
        PerformanceEditPage perEditPage = Manager.getManagerInstance().
                                                      getPerEditPage();
        PerformanceEditModel perEditModel = PerformanceEditModel.
                                            getPerformanceEditModelInstance();
        PerformanceModel perModel = PerformanceModel.
                                    getPerformanceModelInstance();
        
        String str = perEditPage.getTextField().getText();
        
        // if clear string
        if (str.equals("")) {
            JOptionPane.showMessageDialog(Manager.getPerPage(),
                "Нельзя создать пустой сезон",
                "Ошибка", JOptionPane.WARNING_MESSAGE);
            perEditPage.getTextField().setVisible(false);
            return;
        }
               
        JComboBox sc = perEditPage.getSeasonComboBox();               
        
        //check at equals values in combobox
        for (int i = 0; i < sc.getItemCount(); i++) {
            if (str.equals(((Season)sc.getItemAt(i)).getPeriod())) {
                JOptionPane.showMessageDialog(Manager.getPerPage(),
                "Добавлен существующий сезон",
                "Ошибка", JOptionPane.WARNING_MESSAGE);               
                perEditPage.getTextField().setVisible(false);
                return;
            }
        }
        Season newSeason = new Season();       
        newSeason.setId(perEditModel.getNewSeasonID());
        newSeason.setPeriod(str);
        sc.addItem(newSeason);
        
        JOptionPane.showMessageDialog(Manager.getPerPage(),
                "Сезон добавлен!",
                "Инфо", JOptionPane.WARNING_MESSAGE);   
        perEditPage.getTextField().setVisible(false); 
        
        //add to db new season 
        perEditModel.addSeasonToDB(str);
        perEditPage.getTextField().setText("");
        
        //rewrite seasons array
        perModel.getAllSeasonsFromDB();
        perModel.createComboSeasons();
        perEditModel.setAllSeasonstoCombobox();
    }
}
    

