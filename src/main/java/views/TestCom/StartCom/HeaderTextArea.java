package views.TestCom.StartCom;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextArea;
import views.CommonSettings;

import static utils.Layout.calcH;
import static utils.Layout.calcW;

public class HeaderTextArea extends JTextArea {  
    
    private Dimension sizeRow = new Dimension(calcW(250), calcH(100));
    private Dimension sizeCol = new Dimension(calcW(400), calcH(100));
    private int top = 0;
    private boolean rowOrCol;
    private int serial;
    private String text;
    
    //row = elements = false, column = FIOs = true
    //count start one
    public HeaderTextArea(boolean rowOrCol, int serial, String text) {        
        this.rowOrCol = rowOrCol;
        this.serial = serial;     
        this.text = text;
        calculate();
    }   
    
    public HeaderTextArea(boolean rowOrCol, int serial, String text, Dimension sizeRow) {        
        this(rowOrCol, serial, text);
        this.sizeRow = sizeRow;
        calculate();
    } 
    
    public HeaderTextArea(boolean rowOrCol, int serial, String text, int top, boolean halfOrNot) {        
        this(rowOrCol, serial, text);
        if (halfOrNot) {
            this.top = top;
            this.sizeRow = new Dimension((int)sizeRow.getWidth()/2, (int)sizeRow.getHeight());
            calculate();
        }
    }
    
    public HeaderTextArea(boolean rowOrCol, int serial, String text, int top) {        
        this(rowOrCol, serial, text);
        this.top = top;
        calculate();
    } 

    private void calculate() {        
        //FIOs
        if (rowOrCol) {
            this.setSize(sizeCol);
            this.setLocation(0, sizeCol.height * serial);            
        } 
        //elements
        else {
            this.setSize(sizeRow);
            this.setLocation(calcW(400) + sizeRow.width * serial, top);
            
        }        
        this.setText(text);        
        this.setVisible(true);
        this.setOpaque(true);
        this.setBackground(Color.LIGHT_GRAY);
        CommonSettings.settingFontBold30(this);
        CommonSettings.settingGrayBorder(this);        
        this.setEditable(false);
        this.setWrapStyleWord(true);
        this.setLineWrap(true);
    }
}
