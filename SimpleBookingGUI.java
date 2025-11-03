import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GUI แบบเรียบง่าย สำหรับระบบจองบ้านพัก (จัดกลาง)
 */
public class SimpleBookingGUI extends JFrame {
    private BookingSystem bookingSystem;
    private JTextPane textPane;
    
    public SimpleBookingGUI() {
        bookingSystem = new BookingSystem();
        setupUI();
    }
    
    private void setupUI() {
        setTitle("ระบบจองบ้านพัก");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // พื้นที่แสดงผล - ใช้ JTextPane เพื่อจัดกลาง
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        // ตั้งค่าให้จัดกลาง
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel ปุ่ม
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 5, 5));
        
        JButton viewHousesBtn = new JButton("1. ดูสถานะบ้านพัก");
        JButton bookBtn = new JButton("2. จองบ้านพัก");
        JButton viewBookingsBtn = new JButton("3. ดูรายการจอง");
        JButton exitBtn = new JButton("4. ออกจากโปรแกรม");
        
        viewHousesBtn.addActionListener(e -> viewHouses());
        bookBtn.addActionListener(e -> makeBooking());
        viewBookingsBtn.addActionListener(e -> viewBookings());
        exitBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(viewHousesBtn);
        buttonPanel.add(bookBtn);
        buttonPanel.add(viewBookingsBtn);
        buttonPanel.add(exitBtn);
        
        add(buttonPanel, BorderLayout.EAST);
        
        // แสดงข้อความต้อนรับ
        displayWelcome();
    }
    
    private void setText(String text) {
        textPane.setText(text);
        // จัดกลางทุกย่อหน้า
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }
    
    private void displayWelcome() {
        setText(
            "═══════════════════════════════════════════════════════\n" +
            "ยินดีต้อนรับสู่ระบบจองบ้านพัก\n" +
            "═══════════════════════════════════════════════════════\n\n" +
            "กรุณาเลือกเมนูทางด้านขวา\n\n" 
         
        );
    }
    
    private void viewHouses() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("สถานะบ้านพักทั้งหมด (10 หลัง)\n");
        sb.append("═══════════════════════════════════════════════════════\n\n");
        
        List<House> houses = bookingSystem.getHouses();
        for (House house : houses) {
            String status = house.isAvailable() ? "ว่าง" : "ไม่ว่าง";
            sb.append(String.format("บ้านหมายเลข %2d - สถานะ: %-8s - ราคา: %.2f บาท/วัน\n",
                house.getHouseNumber(), status, house.getPricePerDay()));
        }
        
        setText(sb.toString());
    }
    
    private void makeBooking() {
        // แสดงบ้านก่อน
        viewHouses();
        
        // ถามหมายเลขบ้าน
        String houseNumStr = JOptionPane.showInputDialog(this,
            "พิมพ์หมายเลขบ้านที่ต้องการจอง (1-10):",
            "เลือกบ้าน",
            JOptionPane.QUESTION_MESSAGE);
        
        if (houseNumStr == null) return;
        
        try {
            int houseNum = Integer.parseInt(houseNumStr);
            if (houseNum < 1 || houseNum > 10) {
                JOptionPane.showMessageDialog(this, "กรุณาเลือก 1-10 เท่านั้น", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            House house = bookingSystem.findHouseByNumber(houseNum);
            if (house == null) {
                JOptionPane.showMessageDialog(this, "ไม่พบบ้านหมายเลข " + houseNum, "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // สร้างฟอร์มกรอกข้อมูล
            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
            
            JTextField checkInField = new JTextField("15/12/2025");
            JTextField checkOutField = new JTextField("18/12/2025");
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField emailField = new JTextField();
            
            panel.add(new JLabel("วันที่เข้าพัก (วว/ดด/ปปปป):"));
            panel.add(checkInField);
            panel.add(new JLabel("วันที่คืนบ้าน (วว/ดด/ปปปป):"));
            panel.add(checkOutField);
            panel.add(new JLabel("ชื่อจริง:"));
            panel.add(nameField);
            panel.add(new JLabel("เบอร์โทร:"));
            panel.add(phoneField);
            panel.add(new JLabel("อีเมล:"));
            panel.add(emailField);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "กรอกข้อมูลการจอง - บ้านหมายเลข " + houseNum,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result != JOptionPane.OK_OPTION) return;
            
            // ตรวจสอบข้อมูล
            if (nameField.getText().trim().isEmpty() || 
                phoneField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกข้อมูลให้ครบถ้วน", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                LocalDate checkIn = BookingSystem.parseDate(checkInField.getText());
                LocalDate checkOut = BookingSystem.parseDate(checkOutField.getText());
                
                if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                    JOptionPane.showMessageDialog(this, "วันที่คืนบ้านต้องหลังจากวันที่เข้าพัก", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!bookingSystem.isHouseAvailableForDates(house, checkIn, checkOut)) {
                    JOptionPane.showMessageDialog(this, "ขออภัย บ้านไม่ว่างในช่วงเวลาที่เลือก", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Customer customer = new Customer(nameField.getText(), phoneField.getText(), emailField.getText());
                Booking booking = bookingSystem.createBooking(house, customer, checkIn, checkOut);
                
                if (booking != null) {
                    // แสดงข้อมูลและชำระเงิน
                    processPayment(booking);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "รูปแบบวันที่ไม่ถูกต้อง\nกรุณาใช้รูปแบบ วว/ดด/ปปปป", 
                    "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "กรุณาใส่ตัวเลข", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void processPayment(Booking booking) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String info = String.format(
            "ข้อมูลการจอง:\n\n" +
            "บ้านหมายเลข: %d\n" +
            "วันที่: %s ถึง %s\n" +
            "จำนวนวัน: %d วัน\n" +
            "ราคาต่อวัน: %.2f บาท\n\n" +
            "ยอดชำระทั้งหมด: %.2f บาท\n\n" +
            "กรุณาพิมพ์จำนวนเงินที่ชำระ:",
            booking.getHouse().getHouseNumber(),
            booking.getCheckInDate().format(formatter),
            booking.getCheckOutDate().format(formatter),
            booking.getNumberOfDays(),
            booking.getHouse().getPricePerDay(),
            booking.getTotalPrice()
        );
        
        String paymentStr = JOptionPane.showInputDialog(this, info, "ชำระเงิน", JOptionPane.QUESTION_MESSAGE);
        
        if (paymentStr == null) return;
        
        try {
            double payment = Double.parseDouble(paymentStr);
            if (bookingSystem.processPayment(booking, payment)) {
                // แสดงยืนยัน
                String confirmation = String.format(
                    "จองสำเร็จ!\n\n" +
                    "หมายเลขการจอง: %d\n" +
                    "บ้านหมายเลข: %d\n" +
                    "ชื่อผู้จอง: %s\n" +
                    "วันที่: %s ถึง %s\n" +
                    "จำนวนวัน: %d วัน\n" +
                    "ยอดชำระ: %.2f บาท",
                    booking.getBookingId(),
                    booking.getHouse().getHouseNumber(),
                    booking.getCustomer().getFullName(),
                    booking.getCheckInDate().format(formatter),
                    booking.getCheckOutDate().format(formatter),
                    booking.getNumberOfDays(),
                    booking.getTotalPrice()
                );
                
                JOptionPane.showMessageDialog(this, confirmation, "การจองเสร็จสมบูรณ์", JOptionPane.INFORMATION_MESSAGE);
                viewHouses(); // รีเฟรชสถานะ
            } else {
                JOptionPane.showMessageDialog(this,
                    String.format("จำนวนเงินไม่ถูกต้อง\nต้องชำระ: %.2f บาท", booking.getTotalPrice()),
                    "ข้อผิดพลาด",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "กรุณาใส่ตัวเลข", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewBookings() {
        List<Booking> bookings = bookingSystem.getBookings();
        
        if (bookings.isEmpty()) {
            setText(
                "═══════════════════════════════════════════════════════\n" +
                "รายการจองทั้งหมด\n" +
                "═══════════════════════════════════════════════════════\n\n" +
                "ยังไม่มีรายการจอง\n"
            );
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("รายการจองทั้งหมด\n");
        sb.append("═══════════════════════════════════════════════════════\n\n");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Booking booking : bookings) {
            sb.append(String.format("หมายเลขการจอง: %d\n", booking.getBookingId()));
            sb.append(String.format("บ้านหมายเลข: %d\n", booking.getHouse().getHouseNumber()));
            sb.append(String.format("ชื่อผู้จอง: %s\n", booking.getCustomer().getFullName()));
            sb.append(String.format("เบอร์โทร: %s\n", booking.getCustomer().getPhoneNumber()));
            sb.append(String.format("วันที่: %s ถึง %s\n", 
                booking.getCheckInDate().format(formatter),
                booking.getCheckOutDate().format(formatter)));
            sb.append(String.format("จำนวนวัน: %d วัน\n", booking.getNumberOfDays()));
            sb.append(String.format("ราคารวม: %.2f บาท\n", booking.getTotalPrice()));
            sb.append(String.format("สถานะ: %s\n", booking.isPaid() ? "ชำระแล้ว" : "ยังไม่ชำระ"));
            sb.append("───────────────────────────────────────────────────────\n");
        }
        
        setText(sb.toString());
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            SimpleBookingGUI gui = new SimpleBookingGUI();
            gui.setVisible(true);
        });
    }
}
