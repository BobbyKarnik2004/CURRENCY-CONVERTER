package project2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class CurrencyConverterPro extends JFrame {
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JTextField amountField;
    private JLabel resultLabel;
    private JLabel lastUpdatedLabel;
    private JLabel exchangeRateLabel;
    private JButton swapButton;
    private JLabel timeSeriesChart;
    private JLabel fromFlagLabel;
    private JLabel toFlagLabel;
    private JLabel timeSeriesLabel;
    private JTextArea newsFeedArea;
    private JLabel popularConversionsLabel;
    
    // Conversion rates (Base: 1 INR)
    private final HashMap<String, Double> rates = new HashMap<>();
    private final HashMap<String, String> currencySymbols = new HashMap<>();
    private final HashMap<String, String> currencyNames = new HashMap<>();
    private final HashMap<String, ImageIcon> flagIcons = new HashMap<>();
    private final List<ConversionRecord> conversionHistory = new ArrayList<>();
    private final HashMap<String, Double> popularConversions = new HashMap<>();
    
    // News headlines for the ticker
    private final String[] newsHeadlines = {
        "USD reaches 6-month high against EUR",
        "Bitcoin surges past $60,000 mark",
        "RBI keeps interest rates unchanged",
        "Asian markets show mixed response to Fed announcement",
        "Oil prices impact CAD and RUB currencies",
        "New trade agreement strengthens AUD and NZD",
        "Digital Yuan expansion affects Asian currency markets"
    };
    
    private int currentNewsIndex = 0;

    public CurrencyConverterPro() {
        setTitle("💱 Currency Converter");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));
        
        initializeCurrencyData();
        createHeaderPanel();
        createMainPanel();
        createFooterPanel();
        createRightPanel();
        
        // Start news ticker
        startNewsTicker();
        
        setVisible(true);
    }
    
    private void initializeCurrencyData() {
        // Add currency rates (example rates - in real app, fetch from API)
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
        rates.put("CHF", 0.0107);
        rates.put("AED", 0.044);
        rates.put("BTC", 0.00000018);
        rates.put("NZD", 0.017);
        rates.put("MXN", 0.20);
        rates.put("KRW", 15.50);
        
        // Currency symbols
        currencySymbols.put("INR", "₹");
        currencySymbols.put("USD", "$");
        currencySymbols.put("EUR", "€");
        currencySymbols.put("GBP", "£");
        currencySymbols.put("JPY", "¥");
        currencySymbols.put("AUD", "A$");
        currencySymbols.put("CAD", "C$");
        currencySymbols.put("CNY", "¥");
        currencySymbols.put("SGD", "S$");
        currencySymbols.put("RUB", "₽");
        currencySymbols.put("CHF", "CHF");
        currencySymbols.put("AED", "AED");
        currencySymbols.put("BTC", "₿");
        currencySymbols.put("NZD", "NZ$");
        currencySymbols.put("MXN", "Mex$");
        currencySymbols.put("KRW", "₩");
        
        // Currency names
        currencyNames.put("INR", "Indian Rupee");
        currencyNames.put("USD", "US Dollar");
        currencyNames.put("EUR", "Euro");
        currencyNames.put("GBP", "British Pound");
        currencyNames.put("JPY", "Japanese Yen");
        currencyNames.put("AUD", "Australian Dollar");
        currencyNames.put("CAD", "Canadian Dollar");
        currencyNames.put("CNY", "Chinese Yuan");
        currencyNames.put("SGD", "Singapore Dollar");
        currencyNames.put("RUB", "Russian Ruble");
        currencyNames.put("CHF", "Swiss Franc");
        currencyNames.put("AED", "UAE Dirham");
        currencyNames.put("BTC", "Bitcoin");
        currencyNames.put("NZD", "New Zealand Dollar");
        currencyNames.put("MXN", "Mexican Peso");
        currencyNames.put("KRW", "South Korean Won");
        
        // Create colored circles as flag placeholders
        createFlagPlaceholders();
        
        // Initialize popular conversions
        popularConversions.put("USD to EUR", 0.92);
        popularConversions.put("EUR to GBP", 0.86);
        popularConversions.put("USD to INR", 83.25);
        popularConversions.put("GBP to USD", 1.27);
        popularConversions.put("USD to JPY", 147.65);
    }
    
    private void createFlagPlaceholders() {
        // Create colored circle icons for each currency
        flagIcons.put("INR", createColoredCircleIcon(new Color(255, 153, 51), "INR"));
        flagIcons.put("USD", createColoredCircleIcon(new Color(60, 59, 110), "USD"));
        flagIcons.put("EUR", createColoredCircleIcon(new Color(0, 51, 153), "EUR"));
        flagIcons.put("GBP", createColoredCircleIcon(new Color(200, 16, 46), "GBP"));
        flagIcons.put("JPY", createColoredCircleIcon(Color.RED, "JPY"));
        flagIcons.put("AUD", createColoredCircleIcon(new Color(0, 102, 0), "AUD"));
        flagIcons.put("CAD", createColoredCircleIcon(new Color(255, 0, 0), "CAD"));
        flagIcons.put("CNY", createColoredCircleIcon(new Color(255, 0, 0), "CNY"));
        flagIcons.put("SGD", createColoredCircleIcon(new Color(255, 0, 0), "SGD"));
        flagIcons.put("RUB", createColoredCircleIcon(new Color(255, 0, 0), "RUB"));
        flagIcons.put("CHF", createColoredCircleIcon(new Color(255, 0, 0), "CHF"));
        flagIcons.put("AED", createColoredCircleIcon(new Color(0, 102, 0), "AED"));
        flagIcons.put("BTC", createColoredCircleIcon(new Color(242, 169, 0), "BTC"));
        flagIcons.put("NZD", createColoredCircleIcon(new Color(0, 51, 102), "NZD"));
        flagIcons.put("MXN", createColoredCircleIcon(new Color(0, 102, 51), "MXN"));
        flagIcons.put("KRW", createColoredCircleIcon(new Color(0, 71, 160), "KRW"));
    }
    
    private ImageIcon createColoredCircleIcon(Color color, String text) {
        int width = 30;
        int height = 30;
        
        // Create an image for the circle
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Enable anti-aliasing for smoother circles
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the circle
        g2d.setColor(color);
        g2d.fillOval(0, 0, width, height);
        
        // Draw a border
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawOval(0, 0, width-1, height-1);
        
        // Draw the currency code
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 9));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, (width - textWidth) / 2, height/2 + 3);
        
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(30, 30, 60));
        headerPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Ultimate Currency Converter");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Real-time exchange rates with advanced features");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Amount input
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(amountLabel, gbc);
        
        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setPreferredSize(new Dimension(200, 40));
        amountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                convertCurrency();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                convertCurrency();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                convertCurrency();
            }
        });
        gbc.gridx = 1; gbc.gridy = 0;
        mainPanel.add(amountField, gbc);
        
        // From currency flag
        fromFlagLabel = new JLabel(flagIcons.get("INR"));
        gbc.gridx = 2; gbc.gridy = 0;
        mainPanel.add(fromFlagLabel, gbc);
        
        // From currency
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(fromLabel, gbc);
        
        String[] currencies = rates.keySet().toArray(new String[0]);
        fromCurrency = new JComboBox<>(currencies);
        customizeComboBox(fromCurrency);
        fromCurrency.addActionListener(e -> {
            updateFlagIcon((String) fromCurrency.getSelectedItem(), fromFlagLabel);
            convertCurrency();
        });
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(fromCurrency, gbc);
        
        // Swap button
        swapButton = new JButton("⇄ Swap");
        swapButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        swapButton.setBackground(new Color(70, 130, 180));
        swapButton.setForeground(Color.WHITE);
        swapButton.setFocusPainted(false);
        swapButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        swapButton.addActionListener(e -> swapCurrencies());
        gbc.gridx = 2; gbc.gridy = 1;
        mainPanel.add(swapButton, gbc);
        
        // To currency flag
        toFlagLabel = new JLabel(flagIcons.get("USD"));
        gbc.gridx = 2; gbc.gridy = 2;
        mainPanel.add(toFlagLabel, gbc);
        
        // To currency
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(toLabel, gbc);
        
        toCurrency = new JComboBox<>(currencies);
        toCurrency.setSelectedItem("USD");
        customizeComboBox(toCurrency);
        toCurrency.addActionListener(e -> {
            updateFlagIcon((String) toCurrency.getSelectedItem(), toFlagLabel);
            convertCurrency();
        });
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(toCurrency, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));
        
        // Convert button
        JButton convertButton = new JButton("Convert");
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        convertButton.setBackground(new Color(0, 153, 76));
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        convertButton.addActionListener(e -> convertCurrency());
        buttonPanel.add(convertButton);
        
        // History button
        JButton historyButton = new JButton("View History");
        historyButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyButton.setBackground(new Color(102, 102, 102));
        historyButton.setForeground(Color.WHITE);
        historyButton.setFocusPainted(false);
        historyButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        historyButton.addActionListener(e -> showConversionHistory());
        buttonPanel.add(historyButton);
        
        // Calculator button
        JButton calculatorButton = new JButton("Calculator");
        calculatorButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        calculatorButton.setBackground(new Color(70, 130, 180));
        calculatorButton.setForeground(Color.WHITE);
        calculatorButton.setFocusPainted(false);
        calculatorButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        calculatorButton.addActionListener(e -> showCalculator());
        buttonPanel.add(calculatorButton);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(buttonPanel, gbc);
        
        // Result display
        resultLabel = new JLabel("Converted Amount: ");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resultLabel.setForeground(new Color(30, 30, 60));
        resultLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(resultLabel, gbc);
        
        // Exchange rate info
        exchangeRateLabel = new JLabel("");
        exchangeRateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exchangeRateLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 3;
        mainPanel.add(exchangeRateLabel, gbc);
        
        // Popular conversions
        popularConversionsLabel = new JLabel("Popular Conversions: ");
        popularConversionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        popularConversionsLabel.setForeground(new Color(30, 30, 60));
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 3;
        mainPanel.add(popularConversionsLabel, gbc);
        
        updatePopularConversions();
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(300, 0));
        rightPanel.setBorder(new EmptyBorder(20, 10, 20, 20));
        
        // News feed label
        JLabel newsLabel = new JLabel("Financial News", SwingConstants.CENTER);
        newsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        newsLabel.setForeground(new Color(30, 30, 60));
        newsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        rightPanel.add(newsLabel, BorderLayout.NORTH);
        
        // News feed area
        newsFeedArea = new JTextArea();
        newsFeedArea.setEditable(false);
        newsFeedArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        newsFeedArea.setLineWrap(true);
        newsFeedArea.setWrapStyleWord(true);
        newsFeedArea.setBackground(new Color(240, 240, 240));
        newsFeedArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane newsScrollPane = new JScrollPane(newsFeedArea);
        newsScrollPane.setPreferredSize(new Dimension(280, 150));
        rightPanel.add(newsScrollPane, BorderLayout.CENTER);
        
        // Time series chart placeholder
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        timeSeriesLabel = new JLabel("Exchange Rate Trends", SwingConstants.CENTER);
        timeSeriesLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timeSeriesLabel.setForeground(new Color(30, 30, 60));
        chartPanel.add(timeSeriesLabel, BorderLayout.NORTH);
        
        timeSeriesChart = new JLabel("📈 Chart visualization would appear here", SwingConstants.CENTER);
        timeSeriesChart.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeSeriesChart.setForeground(Color.GRAY);
        timeSeriesChart.setPreferredSize(new Dimension(280, 200));
        timeSeriesChart.setBorder(new EmptyBorder(20, 0, 20, 0));
        chartPanel.add(timeSeriesChart, BorderLayout.CENTER);
        
        rightPanel.add(chartPanel, BorderLayout.SOUTH);
        
        add(rightPanel, BorderLayout.EAST);
    }
    
    private void createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(30, 30, 60));
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Last updated label
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        lastUpdatedLabel = new JLabel("Last updated: " + sdf.format(new Date()));
        lastUpdatedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lastUpdatedLabel.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(lastUpdatedLabel, BorderLayout.WEST);
        
        // Add a refresh button
        JButton refreshButton = new JButton("🔄 Refresh Rates");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        refreshButton.addActionListener(e -> refreshRates());
        footerPanel.add(refreshButton, BorderLayout.EAST);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void customizeComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    String code = value.toString();
                    label.setText(code + " - " + currencyNames.get(code));
                    label.setIcon(flagIcons.get(code));
                    label.setIconTextGap(10);
                }
                return label;
            }
        });
        comboBox.setPreferredSize(new Dimension(250, 40));
    }
    
    private void updateFlagIcon(String currencyCode, JLabel flagLabel) {
        ImageIcon flagIcon = flagIcons.get(currencyCode);
        flagLabel.setIcon(flagIcon);
    }
    
    private void convertCurrency() {
        try {
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                resultLabel.setText("Converted Amount: ");
                exchangeRateLabel.setText("");
                return;
            }
            
            double amount = Double.parseDouble(amountText);
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();
            
            if (from.equals(to)) {
                resultLabel.setText("Converted Amount: " + amount + " " + to);
                exchangeRateLabel.setText("1 " + from + " = 1 " + to);
                return;
            }
            
            double inrValue = amount / rates.get(from);
            double result = inrValue * rates.get(to);
            double rate = rates.get(to) / rates.get(from);
            
            String fromSymbol = currencySymbols.getOrDefault(from, "");
            String toSymbol = currencySymbols.getOrDefault(to, "");
            
            resultLabel.setText("Converted Amount: " + toSymbol + String.format("%,.2f", result) + " (" + to + ")");
            exchangeRateLabel.setText(String.format("1 %s = %s %,.4f %s", from, toSymbol, rate, to));
            
            // Add to history
            conversionHistory.add(new ConversionRecord(
                new Date(), from, to, amount, result, rate
            ));
            
            // Update chart text
            timeSeriesChart.setText("📈 Showing exchange rate trend for " + from + " to " + to);
            
        } catch (Exception ex) {
            resultLabel.setText("Converted Amount: ");
            exchangeRateLabel.setText("");
        }
    }
    
    private void swapCurrencies() {
        String from = (String) fromCurrency.getSelectedItem();
        String to = (String) toCurrency.getSelectedItem();
        
        fromCurrency.setSelectedItem(to);
        toCurrency.setSelectedItem(from);
        
        // Convert immediately after swapping
        convertCurrency();
    }
    
    private void refreshRates() {
        // Simulate rate changes
        Random rand = new Random();
        for (String currency : rates.keySet()) {
            if (!currency.equals("INR")) {
                double change = (rand.nextDouble() * 0.1) - 0.05; // -5% to +5%
                rates.put(currency, rates.get(currency) * (1 + change));
            }
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        lastUpdatedLabel.setText("Last updated: " + sdf.format(new Date()));
        
        JOptionPane.showMessageDialog(this, 
            "Rates refreshed successfully!\n\nNote: In a production application, this would fetch real-time data from a currency exchange API.",
            "Refresh Complete", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Update the conversion if there's an amount
        convertCurrency();
        updatePopularConversions();
    }
    
    private void updatePopularConversions() {
        StringBuilder popularText = new StringBuilder("<html><b>Popular Conversions: </b>");
        int count = 0;
        for (Map.Entry<String, Double> entry : popularConversions.entrySet()) {
            if (count > 0) popularText.append(" | ");
            popularText.append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue()));
            count++;
            if (count >= 3) break; // Show only 3 popular conversions
        }
        popularText.append("</html>");
        popularConversionsLabel.setText(popularText.toString());
    }
    
    private void showConversionHistory() {
        JDialog historyDialog = new JDialog(this, "Conversion History", true);
        historyDialog.setSize(600, 400);
        historyDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {"Date", "From", "To", "Amount", "Result", "Rate"};
        Object[][] data = new Object[conversionHistory.size()][6];
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i = 0; i < conversionHistory.size(); i++) {
            ConversionRecord record = conversionHistory.get(i);
            data[i][0] = sdf.format(record.date);
            data[i][1] = record.fromCurrency;
            data[i][2] = record.toCurrency;
            data[i][3] = String.format("%,.2f", record.amount);
            data[i][4] = String.format("%,.2f", record.result);
            data[i][5] = String.format("%.4f", record.rate);
        }
        
        JTable historyTable = new JTable(data, columnNames);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        historyTable.setEnabled(false);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> historyDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        historyDialog.add(panel);
        historyDialog.setVisible(true);
    }
    
    private void showCalculator() {
        JDialog calculatorDialog = new JDialog(this, "Calculator", true);
        calculatorDialog.setSize(300, 400);
        calculatorDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextField calcDisplay = new JTextField("0");
        calcDisplay.setFont(new Font("Arial", Font.BOLD, 20));
        calcDisplay.setHorizontalAlignment(JTextField.RIGHT);
        calcDisplay.setEditable(false);
        panel.add(calcDisplay, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "←", "(", ")"
        };
        
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.addActionListener(e -> {
                String currentText = calcDisplay.getText();
                if (text.equals("=")) {
                    try {
                        // Very simple calculation evaluation
                        if (currentText.contains("+")) {
                            String[] parts = currentText.split("\\+");
                            double result = Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
                            calcDisplay.setText(String.valueOf(result));
                        } else if (currentText.contains("-")) {
                            String[] parts = currentText.split("-");
                            double result = Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
                            calcDisplay.setText(String.valueOf(result));
                        } else if (currentText.contains("*")) {
                            String[] parts = currentText.split("\\*");
                            double result = Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
                            calcDisplay.setText(String.valueOf(result));
                        } else if (currentText.contains("/")) {
                            String[] parts = currentText.split("/");
                            double result = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
                            calcDisplay.setText(String.valueOf(result));
                        }
                    } catch (Exception ex) {
                        calcDisplay.setText("Error");
                    }
                } else if (text.equals("C")) {
                    calcDisplay.setText("0");
                } else if (text.equals("←")) {
                    if (currentText.length() > 1) {
                        calcDisplay.setText(currentText.substring(0, currentText.length() - 1));
                    } else {
                        calcDisplay.setText("0");
                    }
                } else {
                    if (currentText.equals("0") || currentText.equals("Error")) {
                        calcDisplay.setText(text);
                    } else {
                        calcDisplay.setText(currentText + text);
                    }
                }
            });
            buttonPanel.add(button);
        }
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> calculatorDialog.dispose());
        panel.add(closeButton, BorderLayout.SOUTH);
        
        calculatorDialog.add(panel);
        calculatorDialog.setVisible(true);
    }
    
    private void startNewsTicker() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    newsFeedArea.setText(newsHeadlines[currentNewsIndex]);
                    currentNewsIndex = (currentNewsIndex + 1) % newsHeadlines.length;
                });
            }
        }, 0, 5000); // Update every 5 seconds
    }
    
    // Inner class to store conversion history
    private static class ConversionRecord {
        Date date;
        String fromCurrency;
        String toCurrency;
        double amount;
        double result;
        double rate;
        
        ConversionRecord(Date date, String fromCurrency, String toCurrency, double amount, double result, double rate) {
            this.date = date;
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.amount = amount;
            this.result = result;
            this.rate = rate;
        }
    }
    
    public static void main(String[] args) {
        try {
            // Set native look and feel for better appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new CurrencyConverterPro().setVisible(true);
        });
    }
}
