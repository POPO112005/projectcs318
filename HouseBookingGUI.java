import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GUI Application สำหรับระบบจองบ้านพัก
 */
public class HouseBookingGUI extends JFrame {
    private BookingSystem bookingSystem;
    private JPanel mainPanel;
    private JPanel housesPanel;
    private JPanel bookingsPanel;
    private CardLayout cardLayout;
    
    // สีธีม
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);
    private static final Color DARK_GRAY = new Color(52, 73, 94);
    
    public HouseBookingGUI() {
        bookingSystem = new BookingSystem();
        setupUI();
    }
    
    private void setupUI() {
        setTitle("ระบบจองบ้านพัก - House Booking System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // สร้าง Menu Bar
        createMenuBar();
        
        // สร้าง Main Panel พร้อม CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // สร้างหน้าต่างๆ
        mainPanel.add(createWelcomePanel(), "welcome");
        mainPanel.add(createHousesPanel(), "houses");
        mainPanel.add(createBookingsPanel(), "bookings");
        
        add(mainPanel);
        
        // แสดงหน้าต้อนรับ
        cardLayout.show(mainPanel, "welcome");
    }
    
    /**
     * สร้าง Menu Bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(PRIMARY_COLOR);
        
        // เมนู หน้าหลัก
        JMenu homeMenu = new JMenu("หน้าหลัก");
        homeMenu.setForeground(Color.WHITE);
        homeMenu.setFont(new Font("Tahoma", Font.BOLD, 14));
        JMenuItem welcomeItem = new JMenuItem("ต้อนรับ");
        welcomeItem.addActionListener(e -> cardLayout.show(mainPanel, "welcome"));
        homeMenu.add(welcomeItem);
        
        // เมนู บ้านพัก
        JMenu housesMenu = new JMenu("บ้านพัก");
        housesMenu.setForeground(Color.WHITE);
        housesMenu.setFont(new Font("Tahoma", Font.BOLD, 14));
        JMenuItem viewHousesItem = new JMenuItem("ดูสถานะบ้านพัก");
        viewHousesItem.addActionListener(e -> {
            refreshHousesPanel();
            cardLayout.show(mainPanel, "houses");
        });
        housesMenu.add(viewHousesItem);
        
        // เมนู การจอง
        JMenu bookingsMenu = new JMenu("การจอง");
        bookingsMenu.setForeground(Color.WHITE);
        bookingsMenu.setFont(new Font("Tahoma", Font.BOLD, 14));
        JMenuItem viewBookingsItem = new JMenuItem("ดูรายการจองทั้งหมด");
        viewBookingsItem.addActionListener(e -> {
            refreshBookingsPanel();
            cardLayout.show(mainPanel, "bookings");
        });
        bookingsMenu.add(viewBookingsItem);
        
        // เมนู ออก
        JMenu exitMenu = new JMenu("ออก");
        exitMenu.setForeground(Color.WHITE);
        exitMenu.setFont(new Font("Tahoma", Font.BOLD, 14));
        JMenuItem exitItem = new JMenuItem("ออกจากโปรแกรม");
        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "ต้องการออกจากโปรแกรมหรือไม่?",
                "ยืนยันการออก",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        exitMenu.add(exitItem);
        
        menuBar.add(homeMenu);
        menuBar.add(housesMenu);
        menuBar.add(bookingsMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(exitMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * สร้างหน้าต้อนรับ
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_GRAY);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 100));
        JLabel titleLabel = new JLabel("ระบบจองบ้านพัก");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Center - ปุ่มเมนูหลัก
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        
        // ปุ่มดูสถานะบ้านพัก
        gbc.gridy = 0;
        JButton viewHousesBtn = createBigButton("🏠 ดูสถานะบ้านพัก", PRIMARY_COLOR);
        viewHousesBtn.addActionListener(e -> {
            refreshHousesPanel();
            cardLayout.show(mainPanel, "houses");
        });
        centerPanel.add(viewHousesBtn, gbc);
        
        // ปุ่มดูรายการจอง
        gbc.gridy = 1;
        JButton viewBookingsBtn = createBigButton("📋 ดูรายการจอง", SUCCESS_COLOR);
        viewBookingsBtn.addActionListener(e -> {
            refreshBookingsPanel();
            cardLayout.show(mainPanel, "bookings");
        });
        centerPanel.add(viewBookingsBtn, gbc);
        
        // ปุ่มออก
        gbc.gridy = 2;
        JButton exitBtn = createBigButton("🚪 ออกจากโปรแกรม", DANGER_COLOR);
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "ต้องการออกจากโปรแกรมหรือไม่?",
                "ยืนยันการออก",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        centerPanel.add(exitBtn, gbc);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(DARK_GRAY);
        footerPanel.setPreferredSize(new Dimension(0, 50));
        JLabel footerLabel = new JLabel("House Booking System © 2025 - CS318");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        panel.add(footerPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * สร้างหน้าแสดงบ้านพัก
     */
    private JPanel createHousesPanel() {
        housesPanel = new JPanel(new BorderLayout());
        housesPanel.setBackground(LIGHT_GRAY);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        JLabel titleLabel = new JLabel("สถานะบ้านพักทั้งหมด (10 หลัง)");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        housesPanel.add(headerPanel, BorderLayout.NORTH);
        
        return housesPanel;
    }
    
    /**
     * รีเฟรชหน้าบ้านพัก
     */
    private void refreshHousesPanel() {
        // ลบ component เก่า (ยกเว้น header)
        Component[] components = housesPanel.getComponents();
        for (Component comp : components) {
            if (comp != components[0]) { // เก็บ header ไว้
                housesPanel.remove(comp);
            }
        }
        
        // สร้าง Grid Panel สำหรับแสดงบ้าน
        JPanel gridPanel = new JPanel(new GridLayout(2, 5, 15, 15));
        gridPanel.setBackground(LIGHT_GRAY);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        List<House> houses = bookingSystem.getHouses();
        for (House house : houses) {
            gridPanel.add(createHouseCard(house));
        }
        
        housesPanel.add(gridPanel, BorderLayout.CENTER);
        housesPanel.revalidate();
        housesPanel.repaint();
    }
    
    /**
     * สร้างการ์ดบ้าน
     */
    private JPanel createHouseCard(House house) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(house.isAvailable() ? SUCCESS_COLOR : DANGER_COLOR, 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // ข้อมูลบ้าน
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel houseLabel = new JLabel("บ้านหมายเลข " + house.getHouseNumber(), SwingConstants.CENTER);
        houseLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        
        JLabel statusLabel = new JLabel(
            house.isAvailable() ? "✓ ว่าง" : "✗ ไม่ว่าง",
            SwingConstants.CENTER
        );
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        statusLabel.setForeground(house.isAvailable() ? SUCCESS_COLOR : DANGER_COLOR);
        
        JLabel priceLabel = new JLabel(
            String.format("%.0f บาท/วัน", house.getPricePerDay()),
            SwingConstants.CENTER
        );
        priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        infoPanel.add(houseLabel);
        infoPanel.add(statusLabel);
        infoPanel.add(priceLabel);
        
        // ปุ่มจอง
        JButton bookBtn = new JButton("จองเลย");
        bookBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        bookBtn.setBackground(PRIMARY_COLOR);
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFocusPainted(false);
        bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (!house.isAvailable()) {
            bookBtn.setEnabled(false);
            bookBtn.setBackground(Color.GRAY);
        }
        
        bookBtn.addActionListener(e -> showBookingDialog(house));
        
        infoPanel.add(bookBtn);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * แสดง Dialog สำหรับจองบ้าน
     */
    private void showBookingDialog(House house) {
        JDialog dialog = new JDialog(this, "จองบ้านหมายเลข " + house.getHouseNumber(), true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("กรอกข้อมูลการจอง");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        dialog.add(headerPanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        Font labelFont = new Font("Tahoma", Font.BOLD, 14);
        Font fieldFont = new Font("Tahoma", Font.PLAIN, 14);
        
        // วันที่เข้าพัก
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel checkInLabel = new JLabel("วันที่เข้าพัก (วว/ดด/ปปปป):");
        checkInLabel.setFont(labelFont);
        formPanel.add(checkInLabel, gbc);
        
        gbc.gridx = 1;
        JTextField checkInField = new JTextField(15);
        checkInField.setFont(fieldFont);
        checkInField.setText("15/12/2025"); // ตัวอย่าง
        formPanel.add(checkInField, gbc);
        
        // วันที่คืนบ้าน
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel checkOutLabel = new JLabel("วันที่คืนบ้าน (วว/ดด/ปปปป):");
        checkOutLabel.setFont(labelFont);
        formPanel.add(checkOutLabel, gbc);
        
        gbc.gridx = 1;
        JTextField checkOutField = new JTextField(15);
        checkOutField.setFont(fieldFont);
        checkOutField.setText("18/12/2025"); // ตัวอย่าง
        formPanel.add(checkOutField, gbc);
        
        // ชื่อจริง
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel nameLabel = new JLabel("ชื่อจริง:");
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        nameField.setFont(fieldFont);
        formPanel.add(nameField, gbc);
        
        // เบอร์โทร
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel phoneLabel = new JLabel("เบอร์โทร:");
        phoneLabel.setFont(labelFont);
        formPanel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(15);
        phoneField.setFont(fieldFont);
        formPanel.add(phoneField, gbc);
        
        // อีเมล
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel emailLabel = new JLabel("อีเมล:");
        emailLabel.setFont(labelFont);
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        emailField.setFont(fieldFont);
        formPanel.add(emailField, gbc);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        
        // ปุ่ม
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton confirmBtn = new JButton("ยืนยันการจอง");
        confirmBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        confirmBtn.setBackground(SUCCESS_COLOR);
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setPreferredSize(new Dimension(150, 40));
        
        JButton cancelBtn = new JButton("ยกเลิก");
        cancelBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        cancelBtn.setBackground(DANGER_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(150, 40));
        
        confirmBtn.addActionListener(e -> {
            processBooking(house, checkInField.getText(), checkOutField.getText(),
                         nameField.getText(), phoneField.getText(), emailField.getText(), dialog);
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * ประมวลผลการจอง
     */
    private void processBooking(House house, String checkInStr, String checkOutStr,
                               String name, String phone, String email, JDialog dialog) {
        try {
            // ตรวจสอบข้อมูล
            if (name.trim().isEmpty() || phone.trim().isEmpty() || email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "กรุณากรอกข้อมูลให้ครบถ้วน",
                    "ข้อผิดพลาด",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // แปลงวันที่
            LocalDate checkIn = BookingSystem.parseDate(checkInStr);
            LocalDate checkOut = BookingSystem.parseDate(checkOutStr);
            
            // ตรวจสอบวันที่
            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                JOptionPane.showMessageDialog(dialog,
                    "วันที่คืนบ้านต้องหลังจากวันที่เข้าพัก",
                    "ข้อผิดพลาด",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // ตรวจสอบว่าบ้านว่าง
            if (!bookingSystem.isHouseAvailableForDates(house, checkIn, checkOut)) {
                JOptionPane.showMessageDialog(dialog,
                    "ขออภัย บ้านไม่ว่างในช่วงเวลาที่เลือก",
                    "ข้อผิดพลาด",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // สร้างลูกค้า
            Customer customer = new Customer(name, phone, email);
            
            // สร้างการจอง
            Booking booking = bookingSystem.createBooking(house, customer, checkIn, checkOut);
            
            if (booking != null) {
                dialog.dispose();
                showPaymentDialog(booking);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                "รูปแบบวันที่ไม่ถูกต้อง\nกรุณาใช้รูปแบบ วว/ดด/ปปปป",
                "ข้อผิดพลาด",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * แสดง Dialog สำหรับชำระเงิน
     */
    private void showPaymentDialog(Booking booking) {
        JDialog dialog = new JDialog(this, "ชำระเงิน", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("ข้อมูลการจองและการชำระเงิน");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        dialog.add(headerPanel, BorderLayout.NORTH);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        Font labelFont = new Font("Tahoma", Font.PLAIN, 14);
        Font boldFont = new Font("Tahoma", Font.BOLD, 16);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        infoPanel.add(new JLabel("บ้านหมายเลข: " + booking.getHouse().getHouseNumber(), SwingConstants.CENTER));
        infoPanel.add(new JLabel("วันที่: " + booking.getCheckInDate().format(formatter) + 
                                " ถึง " + booking.getCheckOutDate().format(formatter), SwingConstants.CENTER));
        infoPanel.add(new JLabel("จำนวนวัน: " + booking.getNumberOfDays() + " วัน", SwingConstants.CENTER));
        infoPanel.add(new JLabel("ราคาต่อวัน: " + String.format("%.2f", booking.getHouse().getPricePerDay()) + 
                                " บาท", SwingConstants.CENTER));
        
        JLabel totalLabel = new JLabel("ยอดชำระทั้งหมด: " + String.format("%.2f", booking.getTotalPrice()) + 
                                      " บาท", SwingConstants.CENTER);
        totalLabel.setFont(boldFont);
        totalLabel.setForeground(DANGER_COLOR);
        infoPanel.add(totalLabel);
        
        for (Component comp : infoPanel.getComponents()) {
            if (comp instanceof JLabel && comp != totalLabel) {
                ((JLabel) comp).setFont(labelFont);
            }
        }
        
        // Payment Panel
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        paymentPanel.setBackground(Color.WHITE);
        
        JLabel payLabel = new JLabel("จำนวนเงินที่ชำระ:");
        payLabel.setFont(labelFont);
        
        JTextField paymentField = new JTextField(10);
        paymentField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        paymentPanel.add(payLabel);
        paymentPanel.add(paymentField);
        
        infoPanel.add(paymentPanel);
        
        dialog.add(infoPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton payBtn = new JButton("ชำระเงิน");
        payBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        payBtn.setBackground(SUCCESS_COLOR);
        payBtn.setForeground(Color.WHITE);
        payBtn.setFocusPainted(false);
        payBtn.setPreferredSize(new Dimension(120, 40));
        
        JButton cancelBtn = new JButton("ยกเลิก");
        cancelBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        cancelBtn.setBackground(DANGER_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(120, 40));
        
        payBtn.addActionListener(e -> {
            try {
                double payment = Double.parseDouble(paymentField.getText());
                if (bookingSystem.processPayment(booking, payment)) {
                    dialog.dispose();
                    showBookingConfirmation(booking);
                    refreshHousesPanel();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        String.format("จำนวนเงินไม่ถูกต้อง\nต้องชำระ: %.2f บาท", booking.getTotalPrice()),
                        "ข้อผิดพลาด",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "กรุณาใส่ตัวเลขที่ถูกต้อง",
                    "ข้อผิดพลาด",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(payBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    /**
     * แสดงยืนยันการจอง
     */
    private void showBookingConfirmation(Booking booking) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String message = String.format(
            "<html><div style='text-align: center; padding: 10px;'>" +
            "<h2 style='color: #27ae60;'>✓ จองสำเร็จ!</h2>" +
            "<p><b>หมายเลขการจอง:</b> %d</p>" +
            "<p><b>บ้านหมายเลข:</b> %d</p>" +
            "<p><b>ชื่อผู้จอง:</b> %s</p>" +
            "<p><b>วันที่:</b> %s ถึง %s</p>" +
            "<p><b>จำนวนวัน:</b> %d วัน</p>" +
            "<p style='font-size: 16px; color: #e74c3c;'><b>ยอดชำระ:</b> %.2f บาท</p>" +
            "</div></html>",
            booking.getBookingId(),
            booking.getHouse().getHouseNumber(),
            booking.getCustomer().getFullName(),
            booking.getCheckInDate().format(formatter),
            booking.getCheckOutDate().format(formatter),
            booking.getNumberOfDays(),
            booking.getTotalPrice()
        );
        
        JOptionPane.showMessageDialog(this,
            message,
            "การจองเสร็จสมบูรณ์",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * สร้างหน้าแสดงรายการจอง
     */
    private JPanel createBookingsPanel() {
        bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBackground(LIGHT_GRAY);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        JLabel titleLabel = new JLabel("รายการจองทั้งหมด");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        bookingsPanel.add(headerPanel, BorderLayout.NORTH);
        
        return bookingsPanel;
    }
    
    /**
     * รีเฟรชหน้ารายการจอง
     */
    private void refreshBookingsPanel() {
        // ลบ component เก่า (ยกเว้น header)
        Component[] components = bookingsPanel.getComponents();
        for (Component comp : components) {
            if (comp != components[0]) { // เก็บ header ไว้
                bookingsPanel.remove(comp);
            }
        }
        
        List<Booking> bookings = bookingSystem.getBookings();
        
        if (bookings.isEmpty()) {
            JLabel noBookingLabel = new JLabel("ยังไม่มีรายการจอง", SwingConstants.CENTER);
            noBookingLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
            noBookingLabel.setForeground(Color.GRAY);
            bookingsPanel.add(noBookingLabel, BorderLayout.CENTER);
        } else {
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setBackground(LIGHT_GRAY);
            listPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Booking booking : bookings) {
                JPanel bookingCard = new JPanel(new GridLayout(7, 1, 5, 5));
                bookingCard.setBackground(Color.WHITE);
                bookingCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                bookingCard.setMaximumSize(new Dimension(900, 250));
                
                Font normalFont = new Font("Tahoma", Font.PLAIN, 14);
                Font boldFont = new Font("Tahoma", Font.BOLD, 14);
                
                JLabel idLabel = new JLabel("หมายเลขการจอง: " + booking.getBookingId());
                idLabel.setFont(boldFont);
                
                JLabel houseLabel = new JLabel("บ้านหมายเลข: " + booking.getHouse().getHouseNumber());
                houseLabel.setFont(normalFont);
                
                JLabel customerLabel = new JLabel("ชื่อผู้จอง: " + booking.getCustomer().getFullName());
                customerLabel.setFont(normalFont);
                
                JLabel dateLabel = new JLabel("วันที่: " + booking.getCheckInDate().format(formatter) + 
                                             " ถึง " + booking.getCheckOutDate().format(formatter));
                dateLabel.setFont(normalFont);
                
                JLabel daysLabel = new JLabel("จำนวนวัน: " + booking.getNumberOfDays() + " วัน");
                daysLabel.setFont(normalFont);
                
                JLabel priceLabel = new JLabel("ราคารวม: " + String.format("%.2f", booking.getTotalPrice()) + " บาท");
                priceLabel.setFont(boldFont);
                priceLabel.setForeground(DANGER_COLOR);
                
                JLabel statusLabel = new JLabel("สถานะ: " + (booking.isPaid() ? "✓ ชำระแล้ว" : "✗ ยังไม่ชำระ"));
                statusLabel.setFont(boldFont);
                statusLabel.setForeground(booking.isPaid() ? SUCCESS_COLOR : DANGER_COLOR);
                
                bookingCard.add(idLabel);
                bookingCard.add(houseLabel);
                bookingCard.add(customerLabel);
                bookingCard.add(dateLabel);
                bookingCard.add(daysLabel);
                bookingCard.add(priceLabel);
                bookingCard.add(statusLabel);
                
                listPanel.add(bookingCard);
                listPanel.add(Box.createVerticalStrut(10));
            }
            
            JScrollPane scrollPane = new JScrollPane(listPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            bookingsPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        bookingsPanel.revalidate();
        bookingsPanel.repaint();
    }
    
    /**
     * สร้างปุ่มใหญ่สำหรับหน้าต้อนรับ
     */
    private JButton createBigButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(400, 60));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    public static void main(String[] args) {
        // ใช้ Look and Feel ของระบบ
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            HouseBookingGUI gui = new HouseBookingGUI();
            gui.setVisible(true);
        });
    }
}
