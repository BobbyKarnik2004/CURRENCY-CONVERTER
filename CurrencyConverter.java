package Project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class CurrencyConverter extends JFrame {

    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JTextField amountField;
    private JLabel resultLabel;

    // Conversion rates (Base: 1 INR)
    private final HashMap<String, Double> rates = new HashMap<>();

    public CurrencyConverter() {
        setTitle("💰 Currency Converter");
        setSize(600, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(30, 30, 60));

        // Add currency rates
        rates.put("INR", 1.0);
        rates.put("USD", 0.012);
        rates.put("EUR", 0.011);
        rates.put("GBP", 0.0095);
        rates.put("JPY", 1.69);
        rates.put("AUD", 0.018);
        rates.put("CAD", 0.016);
        rates.put("CNY", 0.086);
        rates.put("SGD", 0.016);
        rates.put("RUB", 1.05);

        JLabel titleLabel = new JLabel("Currency Converter");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setBounds(150, 10, 350, 40);
        add(titleLabel);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setBounds(50, 70, 100, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setFont(new Font("Consolas", Font.PLAIN, 16));
        amountField.setBounds(130, 70, 150, 30);
        add(amountField);

        String[] currencies = rates.keySet().toArray(new String[0]);

        fromCurrency = new JComboBox<>(currencies);
        fromCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        fromCurrency.setBounds(300, 70, 100, 30);
        add(fromCurrency);

        JLabel toLabel = new JLabel("To");
        toLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        toLabel.setForeground(Color.WHITE);
        toLabel.setBounds(410, 70, 30, 30);
        add(toLabel);

        toCurrency = new JComboBox<>(currencies);
        toCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        toCurrency.setBounds(440, 70, 100, 30);
        add(toCurrency);

        JButton convertButton = new JButton("Convert");
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        convertButton.setBackground(new Color(0, 153, 76));
        convertButton.setForeground(Color.WHITE);
        convertButton.setBounds(220, 130, 140, 40);
        add(convertButton);

        resultLabel = new JLabel("Converted Amount: ");
        resultLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        resultLabel.setForeground(new Color(173, 216, 230));
        resultLabel.setBounds(160, 200, 400, 40);
        add(resultLabel);

        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
    }

    private void convertCurrency() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();

            double inrValue = amount / rates.get(from);
            double result = inrValue * rates.get(to);

            resultLabel.setText("Converted Amount: " + String.format("%.2f", result) + " " + to);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CurrencyConverter().setVisible(true);
        });
    }
}

