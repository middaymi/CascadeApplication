package utils;


import views.Manager;

public class Layout {

    //standard width with insets
    private static final int INIT_WIDTH = 3168;
    private static final int INIT_HEIGHT = 1657;

    private static final int CURRENT_WIDTH = Manager.getWidthWithInsets();
    private static final int CURRENT_HEIGHT = Manager.getHeightWithInsets();


    public static int calcW(int value) {
        return (int)(value * CURRENT_WIDTH/INIT_WIDTH);
    }

    public static int calcH(int value) {
        return (int)(value * CURRENT_HEIGHT/INIT_HEIGHT);
    }
}
