package Project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CurrencyConverter extends JFrame {

    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JTextField amountField;
    private JLabel resultLabel;
    private JTextArea historyArea;

    // Conversion rates (Base: 1 INR)
    private final HashMap<String, Double> rates = new HashMap<>();

    // Store conversion history
    private final ArrayList<String> history = new ArrayList<>();

    public CurrencyConverter() {
        setTitle("💰 Currency Converter");
        setSize(750, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(25, 25, 50));

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

        // Title
        JLabel titleLabel = new JLabel("Currency Converter");
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 30));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setBounds(220, 10, 400, 40);
        add(titleLabel);

        // Amount Label
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setBounds(60, 80, 100, 25);
        add(amountLabel);

        // Amount Field
        amountField = new JTextField();
        amountField.setFont(new Font("Consolas", Font.PLAIN, 16));
        amountField.setBounds(150, 80, 150, 30);
        amountField.setToolTipText("Enter amount to convert");
        add(amountField);

        // Currency Dropdowns
        String[] currencies = rates.keySet().toArray(new String[0]);

        fromCurrency = new JComboBox<>(currencies);
        fromCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        fromCurrency.setBounds(320, 80, 100, 30);
        add(fromCurrency);

        JLabel toLabel = new JLabel("To");
        toLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        toLabel.setForeground(Color.WHITE);
        toLabel.setBounds(430, 80, 30, 30);
        add(toLabel);

        toCurrency = new JComboBox<>(currencies);
        toCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        toCurrency.setBounds(470, 80, 100, 30);
        add(toCurrency);

        // Convert Button
        JButton convertButton = new JButton("Convert");
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        convertButton.setBackground(new Color(0, 153, 76));
        convertButton.setForeground(Color.WHITE);
        convertButton.setBounds(220, 140, 120, 40);
        convertButton.setFocusPainted(false);
        add(convertButton);

        // Clear Button
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearButton.setBackground(new Color(200, 50, 50));
        clearButton.setForeground(Color.WHITE);
        clearButton.setBounds(360, 140, 120, 40);
        clearButton.setFocusPainted(false);
        add(clearButton);

        // Result Label
        resultLabel = new JLabel("Converted Amount: ");
        resultLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        resultLabel.setForeground(new Color(173, 216, 230));
        resultLabel.setBounds(200, 200, 400, 40);
        add(resultLabel);

        // History Area
        JLabel historyLabel = new JLabel("Conversion History:");
        historyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyLabel.setForeground(Color.WHITE);
        historyLabel.setBounds(60, 260, 200, 30);
        add(historyLabel);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        historyArea.setBackground(new Color(40, 40, 70));
        historyArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBounds(60, 290, 600, 100);
        add(scrollPane);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem backItem = new JMenuItem("Back");
        JMenuItem exitItem = new JMenuItem("Exit");

        menu.add(backItem);
        menu.add(aboutItem);
        menu.add(exitItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Action Listeners
        convertButton.addActionListener(e -> convertCurrency());
        clearButton.addActionListener(e -> clearFields());
        exitItem.addActionListener(e -> System.exit(0));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "💰 Currency Converter v2.0\nCreated by Bobby\nJava Swing Project",
                "About", JOptionPane.INFORMATION_MESSAGE));
        backItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Back functionality can be linked to main menu (not implemented).",
                "Back", JOptionPane.INFORMATION_MESSAGE));
    }

    private void convertCurrency() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();

            double inrValue = amount / rates.get(from);
            double result = inrValue * rates.get(to);

            String output = String.format("%.2f %s = %.2f %s", amount, from, result, to);
            resultLabel.setText("Converted Amount: " + String.format("%.2f", result) + " " + to);

            // Save to history
            history.add(output);
            historyArea.append(output + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        amountField.setText("");
        resultLabel.setText("Converted Amount: ");
        historyArea.setText("");
        history.clear();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverter().setVisible(true));
    }
}

       
