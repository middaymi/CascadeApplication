package controllers.TestComPage;

import data.Competition;
import models.TestCom.StartCom.IsuComModel;
import models.TestCom.StartCom.StComModel;
import models.TestCom.TestComModel;
import views.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartCom implements ActionListener {
    private Manager manager;
    private TestComModel tcModel;
    private StComModel model;

    Competition competition;
    int sel;
    int kindId;

    public void actionPerformed(ActionEvent e) {
        manager = Manager.getManagerInstance();
        tcModel = TestComModel.getTestComModelInstance();
        competition = tcModel.getCompetitions().get(tcModel.selRow());

        try {
            sel = tcModel.selRow();
            kindId = competition.getKind().getId();

            switch (kindId) {
                case (1):
                case (2):
                case (3):
                    manager.choosePanel(61);
                    model = StComModel.getStComModelInstance();
                    model.setTextToWelcomeLbl(
                            tcModel.getValueAt(sel, 0) + ". " +
                                    tcModel.getValueAt(sel, 3) + ".   " +
                                    tcModel.getValueAt(sel, 5));
                    model.setAllData();
                    break;

                //SINGLE
                case (4):
                    model = IsuComModel.getModelInstance();

                    if (competition.getRankId() == null) {
                        JOptionPane.showMessageDialog(Manager.getTestCompPage(),
                                "Установите разряд в форме редактирования для проведения соревнования!",
                                "Ошибка", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    manager.choosePanel(62);

                    //competition is finished
                    if (competition.isFinished()) {
                        model.setAllDataForFinishedCompetition();
                        return;
                    }

                    model.setAllData();
                    break;

                //SPBALET
                case (5):
                    JOptionPane.showMessageDialog(Manager.getTestCompPage(),
                            "Проведение соревнования по спортивному балету недоступно!",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                    break;
            }
            kindId = 0;
            sel = 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return;
        }
    }
}
