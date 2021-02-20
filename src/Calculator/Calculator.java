package Calculator;

import javax.swing.*;

public class Calculator {

    public static void main(String[] args) {
        // создание окна JFrame
        GuiCalc calc = new GuiCalc();
        calc.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        calc.setSize(400,320);
        calc.setLocationRelativeTo(null); //появление окна в центре экрана
        calc.setVisible(true);
    }

}
