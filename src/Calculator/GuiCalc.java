package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class GuiCalc extends JFrame {
    JPanel upPanel = new JPanel();    //панель для табло
    JPanel downPanel = new JPanel();  //панель для кнопок
    JLabel tablo = new JLabel("0"); //табло калькулятора
    BigDecimal result; // результат вычислений
    String lastCommand = ""; //последняя введенная команда
    int indexOfLC; // индекс lastCommand в tablo.getText()

    //Действие при нажатии кнопки [Лямбда скрывает new ActionListener.actionPerformed(ActionEvent e)]
    ActionListener insert = e -> {
        //проверка табло на наличие ошибки
        if (tablo.getText().equals("деление на ноль")) tablo.setText("0");
        //сравнение выполяемой комманды с именем комманды кнопки
        switch (e.getActionCommand()) {
            case("C"): tablo.setText("0"); lastCommand = ""; indexOfLC = 0;
                break;
            case("CE"): if(lastCommand.equals("")) tablo.setText("0");
                else tablo.setText(tablo.getText().substring(0, indexOfLC+1));
                break;
            case("<<"):
                //стирание последних символов
                if ((tablo.getText().length() == 1)) {
                    tablo.setText("0");
                } else {
                    if (tablo.getText().substring(tablo.getText().length()-1).equals(lastCommand)) {lastCommand = ""; indexOfLC = 0;}//очистка lastCommand при стирании его из табло
                    tablo.setText(tablo.getText().substring(0, tablo.getText().length() - 1)); // уменьшение табло на 1 символ
                }
                break;
            case("*"):
            case("+"):
            case("/"): if(tablo.getText().equals("-")) return;
                if (lastCommand.equals("")) {tablo.setText(tablo.getText()+e.getActionCommand());
                lastCommand = e.getActionCommand();
                indexOfLC = tablo.getText().length()-1;}
                break;
            case("-"): if (tablo.getText().equals("0")){tablo.setText("-"); return;}
                if(tablo.getText().equals("-")) return;
                if (lastCommand.equals("")) {tablo.setText(tablo.getText()+e.getActionCommand());
                lastCommand = e.getActionCommand();
                indexOfLC = tablo.getText().length()-1;}
                break;
            case("n^2"): if (!lastCommand.equals("")) return;
                lastCommand = "n^2";
                indexOfLC = tablo.getText().length(); // для num1 в public void calculate()
                calculate();
                break;
            case("="): calculate();
                break;
            case("."): if (lastCommand.equals("") && tablo.getText().contains(".")) return;
                if (!lastCommand.equals("") && tablo.getText().substring(indexOfLC+1).contains(".")) return;
                if (!lastCommand.equals("") && tablo.getText().substring(indexOfLC+1).equals("")) return;
                tablo.setText(tablo.getText()+e.getActionCommand());
                break;
            //ввод чисел 0-9
            default:
                if (!lastCommand.equals("") && tablo.getText().substring(indexOfLC+1).equals("0")) return; //проерка на 00, 01 после комманды
                if(tablo.getText().equals("0")) tablo.setText(e.getActionCommand());
                else tablo.setText(tablo.getText()+e.getActionCommand());
                break;
        }
    };
    //метод  = или n^2
    public void calculate(){
        if (lastCommand.equals("")) return;
        String num1 = tablo.getText().substring(0,indexOfLC);
        BigDecimal num1BD = new BigDecimal(num1);
        if (lastCommand.equals("n^2")){
            result = num1BD.multiply(num1BD);
            tablo.setText(result.toString());
            lastCommand = "";
            indexOfLC = 0;
            return;
        }
        String num2 = tablo.getText().substring(indexOfLC+1);
        if (num2.equals("")) return; // нет второго числа
        BigDecimal num2BD = new BigDecimal(num2);
        if (lastCommand.equals("+")) result = num1BD.add(num2BD);
        if (lastCommand.equals("-")) result = num1BD.subtract(num2BD);
        if (lastCommand.equals("*")) result = num1BD.multiply(num2BD);
        if (lastCommand.equals("/")) {
            if (num2BD.equals(BigDecimal.ZERO)) {
                tablo.setText("деление на ноль");
                lastCommand = "";
                indexOfLC = 0;
                return;
            } else try {
                result = num1BD.divide(num2BD); //без округления, если нет ArithmeticException
            } catch (ArithmeticException a) {
                result = num1BD.divide(num2BD, 8, BigDecimal.ROUND_HALF_UP); // округление до 8 символов при бесконечном остатке
            }
        }
        tablo.setText(result.toString()); // вывод результата в табло
        lastCommand = "";
        indexOfLC = 0;
    }
    //метод добавления кнопки в Panel (с привязанным ActionListener)
    public JButton addButton(String name, ActionListener insert) {
    JButton button = new JButton(name);
    button.setActionCommand(name);  // добавление имени команды в кнопку для сравнения в ActionListener
    button.addActionListener(insert);
    button.setFont(button.getFont().deriveFont(16f));  //размер шрифта кнопок
    if (name.equals("*")) button.setFont(button.getFont().deriveFont(27f)); // размер шрифта дя "*"
    if (name.equals("-")) button.setFont(button.getFont().deriveFont(23f)); // размер шрифта дя "-"
    return button;
    }
    //конструктор GUI калькулятора
    public GuiCalc(){
        super("Калькулятор v.1.0");
        tablo.setFont(tablo.getFont().deriveFont(40f));  //размер шрифта табло
        upPanel.add(tablo);
        downPanel.setLayout(new GridLayout(5,4));
        //добавление кнопок
        downPanel.add(addButton("CE", insert));
        downPanel.add(addButton("C", insert));
        downPanel.add(addButton("n^2", insert));
        downPanel.add(addButton("<<", insert));
        downPanel.add(addButton("7", insert));
        downPanel.add(addButton("8", insert));
        downPanel.add(addButton("9", insert));
        downPanel.add(addButton("*", insert));
        downPanel.add(addButton("4", insert));
        downPanel.add(addButton("5", insert));
        downPanel.add(addButton("6", insert));
        downPanel.add(addButton("/", insert));
        downPanel.add(addButton("1", insert));
        downPanel.add(addButton("2", insert));
        downPanel.add(addButton("3", insert));
        downPanel.add(addButton("+", insert));
        downPanel.add(addButton("0", insert));
        downPanel.add(addButton(".", insert));
        downPanel.add(addButton("=", insert));
        downPanel.add(addButton("-", insert));
        add(upPanel, BorderLayout.NORTH);   //добавлеие панели в окно
        add(downPanel, BorderLayout.CENTER);//добавлеие панели в окно
    }
}