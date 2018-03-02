package views;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

public class CommonSettings {
    
    private static LineBorder  lightGrayLineBorder = null; 
    private static LineBorder  grayLineBorder = null; 
    private static LineBorder  narrowLightGrayBorder = null;
    private static Font font30 = null;
    private static Font fontBold30 = null;
    private static Font font50 = null; 
        
    //COMMON_SETTINGS_FOR_PANELS (USUAL AND SCROLL)*****************************
    public static <T extends JComponent> void panelSettings(T obj) {
        obj.setLayout(null);
        obj.setSize(Manager.getWidthWithInsets(), 
                    Manager.getHeightWithInsets());
        obj.setOpaque(false);              
    }
    
    //FONTS*********************************************************************
    public static <T extends JComponent> void settingFont50 (T obj) {
        obj.setFont(font50Instance());
    } 
    public static <T extends JComponent> void settingFont30 (T obj) {
        obj.setFont(font30Instance());
    }
    public static <T extends JComponent> void settingFontBold30 (T obj) {
        obj.setFont(font30BoldInstance());
    }     
    
    //BORDERS*******************************************************************
    public static <T extends JComponent> void settingLightGrayBorder (T obj) {        
        obj.setBorder(lightGrayLineBorderInstance());
    }
    public static <T extends JComponent> void settingGrayBorder (T obj) {        
        obj.setBorder(grayLineBorderInstance());
    }
    public static <T extends JComponent> void settingNarrowLightGrayBorder (T obj) {        
        obj.setBorder(narrowLightGrayBorder());
    }
     
    //PRIVATE_FUNCTIONS*********************************************************
    //set grayLineBoard
    private static LineBorder lightGrayLineBorderInstance() {
        if (lightGrayLineBorder == null)
            lightGrayLineBorder = new LineBorder(Color.LIGHT_GRAY, 
                                      (int)(0.00125 * Manager.getWidth()));
        return lightGrayLineBorder;        
    }
    private static LineBorder grayLineBorderInstance() {
        if (grayLineBorder == null)
            grayLineBorder = new LineBorder(Color.GRAY, 
                                 (int)(0.00125 * Manager.getWidth()));
        return grayLineBorder;        
    }
    
    private static LineBorder narrowLightGrayBorder() {
        if (narrowLightGrayBorder == null)
            narrowLightGrayBorder = new LineBorder(Color.LIGHT_GRAY, 
                                 (int)(Manager.getWidthWithInsets())/1584);
        return narrowLightGrayBorder;        
    }
    
    private static Font font30Instance() {
        if (font30 == null)
            font30 = new Font("TimesNewRoman", Font.PLAIN, 
                             (3*Manager.getWidth()/320));
        return font30;
    }
     private static Font font30BoldInstance() {
        if (fontBold30 == null)
            fontBold30 = new Font("TimesNewRoman", Font.BOLD, 
                             (3*Manager.getWidth()/320));
        return fontBold30;
    }
    private static Font font50Instance() {
        if (font50 == null)
            font50 = new Font("TimesNewRoman", Font.PLAIN, 
                             (Manager.getWidth()/64));
        return font50;
    }
}
