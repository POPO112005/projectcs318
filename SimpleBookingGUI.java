import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GUI แบบเรียบง่าย สำหรับระบบจองบ้านพัก
 * ใช้ Java Swing สร้างหน้าต่างโปรแกรมแบบ Graphical User Interface
 * มีเมนูให้เลือก: ดูบ้าน, จองบ้าน, ดูรายการจอง, ออกจากโปรแกรม
 */
public class SimpleBookingGUI extends JFrame {
    // ระบบจัดการการจองบ้านพัก (Backend)
    private BookingSystem bookingSystem;
    
    // พื้นที่แสดงข้อความในหน้าต่าง (สามารถจัดกลางข้อความได้)
    private JTextPane textPane;
    
    /**
     * Constructor - สร้างหน้าต่าง GUI
     */
    public SimpleBookingGUI() {
        // สร้างระบบจองบ้านพัก
        bookingSystem = new BookingSystem();
        
        // ตั้งค่าหน้าต่าง GUI
        setupUI();
    }
    
    /**
     * ตั้งค่าส่วนประกอบของ GUI
     * สร้างหน้าต่าง, ปุ่มเมนู, และพื้นที่แสดงผล
     */
    private void setupUI() {
        // ตั้งค่าหน้าต่างหลัก
        setTitle("ระบบจองบ้านพัก");          // ชื่อหน้าต่าง
        setSize(800, 600);                    // ขนาดหน้าต่าง (กว้าง x สูง)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ปิดโปรแกรมเมื่อกดปิดหน้าต่าง
        setLocationRelativeTo(null);          // วางหน้าต่างตรงกลางจอ
        setLayout(new BorderLayout());        // ใช้ BorderLayout จัดวาง
        
        // สร้างพื้นที่แสดงผล - ใช้ JTextPane เพื่อจัดกลางข้อความ
        textPane = new JTextPane();
        textPane.setEditable(false);          // ไม่ให้แก้ไขข้อความได้
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 14)); // ฟอนต์แบบ Monospace
        
        // ตั้งค่าให้จัดกลางข้อความ
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        // ใส่ textPane ใน ScrollPane เพื่อให้เลื่อนดูได้
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER); // วางตรงกลาง
        
        // สร้าง Panel สำหรับปุ่มเมนู
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 5, 5)); // 4 แถว, 1 คอลัมน์, ระยะห่าง 5px
        
        // สร้างปุ่มเมนูทั้ง 4 ปุ่ม
        JButton viewHousesBtn = new JButton("1. ดูสถานะบ้านพัก");
        JButton bookBtn = new JButton("2. จองบ้านพัก");
        JButton viewBookingsBtn = new JButton("3. ดูรายการจอง");
        JButton exitBtn = new JButton("4. ออกจากโปรแกรม");
        
        // ผูกฟังก์ชันกับปุ่ม (Event Listener)
        viewHousesBtn.addActionListener(e -> viewHouses());     // เมื่อกดปุ่ม 1
        bookBtn.addActionListener(e -> makeBooking());          // เมื่อกดปุ่ม 2
        viewBookingsBtn.addActionListener(e -> viewBookings()); // เมื่อกดปุ่ม 3
        exitBtn.addActionListener(e -> System.exit(0));         // เมื่อกดปุ่ม 4 - ออกจากโปรแกรม
        
        // เพิ่มปุ่มลงใน Panel
        buttonPanel.add(viewHousesBtn);
        buttonPanel.add(bookBtn);
        buttonPanel.add(viewBookingsBtn);
        buttonPanel.add(exitBtn);
        
        // วาง Panel ปุ่มทางด้านขวา
        add(buttonPanel, BorderLayout.EAST);
        
        // แสดงข้อความต้อนรับเมื่อเปิดโปรแกรม
        displayWelcome();
    }
    
    /**
     * ตั้งค่าข้อความใน textPane และจัดกลางทุกย่อหน้า
     * @param text ข้อความที่ต้องการแสดง
     */
    private void setText(String text) {
        textPane.setText(text);
        
        // จัดกลางทุกย่อหน้า
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }
    
    /**
     * แสดงข้อความต้อนรับเมื่อเปิดโปรแกรม
     */
    private void displayWelcome() {
        setText(
            "═══════════════════════════════════════════════════════\n" +
            "ยินดีต้อนรับสู่ระบบจองบ้านพัก\n" +
            "═══════════════════════════════════════════════════════\n\n" +
            "กรุณาเลือกเมนูทางด้านขวา\n\n" 
         
        );
    }
    
    /**
     * แสดงสถานะบ้านพักทั้งหมด (เมนู 1)
     * ดึงข้อมูลบ้านทั้ง 10 หลังและแสดงสถานะว่าง/ไม่ว่าง พร้อมราคา
     */
    private void viewHouses() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("สถานะบ้านพักทั้งหมด (10 หลัง)\n");
        sb.append("═══════════════════════════════════════════════════════\n\n");
        
        // ดึงรายการบ้านทั้งหมดจากระบบ
        List<House> houses = bookingSystem.getHouses();
        
        // วนลูปแสดงข้อมูลบ้านแต่ละหลัง
        for (House house : houses) {
            String status = house.isAvailable() ? "ว่าง" : "ไม่ว่าง";
            sb.append(String.format("บ้านหมายเลข %2d - สถานะ: %-8s - ราคา: %.2f บาท/วัน\n",
                house.getHouseNumber(), status, house.getPricePerDay()));
        }
        
        setText(sb.toString());
    }
    
    /**
     * ฟังก์ชันจองบ้านพัก (เมนู 2)
     * ขั้นตอน: 1) เลือกบ้าน 2) กรอกข้อมูล 3) ตรวจสอบวันที่ 4) ชำระเงิน
     */
    private void makeBooking() {
        // แสดงสถานะบ้านก่อน เพื่อให้ผู้ใช้เลือก
        viewHouses();
        
        // ถามหมายเลขบ้านที่ต้องการจอง
        String houseNumStr = JOptionPane.showInputDialog(this,
            "พิมพ์หมายเลขบ้านที่ต้องการจอง (1-10):",
            "เลือกบ้าน",
            JOptionPane.QUESTION_MESSAGE);
        
        // ถ้ากด Cancel ให้ออกจากฟังก์ชัน
        if (houseNumStr == null) return;
        
        try {
            // แปลง String เป็นตัวเลข
            int houseNum = Integer.parseInt(houseNumStr);
            
            // ตรวจสอบว่าเลข 1-10 เท่านั้น
            if (houseNum < 1 || houseNum > 10) {
                JOptionPane.showMessageDialog(this, "กรุณาเลือก 1-10 เท่านั้น", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // ค้นหาบ้านจากหมายเลข
            House house = bookingSystem.findHouseByNumber(houseNum);
            if (house == null) {
                JOptionPane.showMessageDialog(this, "ไม่พบบ้านหมายเลข " + houseNum, "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // === สร้างฟอร์มกรอกข้อมูลการจอง ===
            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5)); // 5 แถว, 2 คอลัมน์
            
            // สร้าง TextField สำหรับกรอกข้อมูล (ใส่ค่าเริ่มต้นไว้)
            JTextField checkInField = new JTextField("15/12/2025");
            JTextField checkOutField = new JTextField("18/12/2025");
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField emailField = new JTextField();
            
            // เพิ่ม Label และ TextField ลงใน Panel
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
            
            // แสดง Dialog กรอกข้อมูล
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "กรอกข้อมูลการจอง - บ้านหมายเลข " + houseNum,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            // ถ้ากด Cancel ให้ออกจากฟังก์ชัน
            if (result != JOptionPane.OK_OPTION) return;
            
            // ตรวจสอบว่ากรอกข้อมูลครบถ้วนหรือไม่
            if (nameField.getText().trim().isEmpty() || 
                phoneField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกข้อมูลให้ครบถ้วน", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // แปลงวันที่จาก String เป็น LocalDate
                LocalDate checkIn = BookingSystem.parseDate(checkInField.getText());
                LocalDate checkOut = BookingSystem.parseDate(checkOutField.getText());
                
                // ตรวจสอบว่าวันคืนบ้านต้องมาหลังวันเข้าพัก
                if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                    JOptionPane.showMessageDialog(this, "วันที่คืนบ้านต้องหลังจากวันที่เข้าพัก", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // ตรวจสอบว่าบ้านว่างในช่วงเวลาที่เลือกหรือไม่
                if (!bookingSystem.isHouseAvailableForDates(house, checkIn, checkOut)) {
                    JOptionPane.showMessageDialog(this, "ขออภัย บ้านไม่ว่างในช่วงเวลาที่เลือก", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // สร้าง object ลูกค้า
                Customer customer = new Customer(nameField.getText(), phoneField.getText(), emailField.getText());
                
                // สร้างการจอง (ยังไม่ยืนยัน - รอชำระเงิน)
                Booking booking = bookingSystem.createBooking(house, customer, checkIn, checkOut);
                
                if (booking != null) {
                    // ไปที่ขั้นตอนชำระเงิน
                    processPayment(booking);
                }
                
            } catch (Exception ex) {
                // ถ้าวันที่ไม่ถูกต้อง
                JOptionPane.showMessageDialog(this, 
                    "รูปแบบวันที่ไม่ถูกต้อง\nกรุณาใช้รูปแบบ วว/ดด/ปปปป", 
                    "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            // ถ้ากรอกหมายเลขบ้านไม่ใช่ตัวเลข
            JOptionPane.showMessageDialog(this, "กรุณาใส่ตัวเลข", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * ฟังก์ชันประมวลผลการชำระเงิน
     * มีระบบวนลูปให้กรอกจำนวนเงินซ้ำได้ถ้ากรอกผิด
     * การจองจะถูกบันทึกก็ต่อเมื่อชำระเงินสำเร็จเท่านั้น
     * 
     * @param booking การจองที่ต้องการชำระเงิน
     */
    private void processPayment(Booking booking) {
        // กำหนดรูปแบบวันที่เป็น dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // ตัวแปรเช็คว่าชำระเงินสำเร็จหรือยัง
        boolean paymentSuccess = false;
        
        // วนลูปจนกว่าจะชำระเงินสำเร็จหรือกด Cancel
        while (!paymentSuccess) {
            // สร้างข้อความแสดงข้อมูลการจองและยอดชำระ
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
            
            // แสดง Dialog ให้กรอกจำนวนเงิน
            String paymentStr = JOptionPane.showInputDialog(this, info, "ชำระเงิน", JOptionPane.QUESTION_MESSAGE);
            
            // ถ้ากด Cancel ให้ออกจากลูปและยกเลิกการจอง
            if (paymentStr == null) {
                return; // การจองจะไม่ถูกบันทึก
            }
            
            try {
                // แปลง String เป็นตัวเลข double
                double payment = Double.parseDouble(paymentStr);
                
                // ส่งไปตรวจสอบและประมวลผลการชำระเงิน
                if (bookingSystem.processPayment(booking, payment)) {
                    // === ชำระเงินสำเร็จ ===
                    
                    // สร้างข้อความยืนยันการจอง
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
                    
                    // แสดง Dialog ยืนยันการจอง
                    JOptionPane.showMessageDialog(this, confirmation, "การจองเสร็จสมบูรณ์", JOptionPane.INFORMATION_MESSAGE);
                    
                    // รีเฟรชสถานะบ้าน
                    viewHouses();
                    
                    // ตั้งค่าให้ออกจากลูป
                    paymentSuccess = true;
                    
                } else {
                    // === จำนวนเงินไม่ถูกต้อง ===
                    
                    // แสดงข้อความผิดพลาดและให้กรอกใหม่
                    JOptionPane.showMessageDialog(this,
                        String.format("จำนวนเงินไม่ถูกต้อง!\nต้องชำระ: %.2f บาท\n\nกรุณากรอกจำนวนเงินอีกครั้ง", booking.getTotalPrice()),
                        "ข้อผิดพลาด",
                        JOptionPane.ERROR_MESSAGE);
                    // จะวนลูปกลับไปให้กรอกใหม่อัตโนมัติ
                }
                
            } catch (NumberFormatException ex) {
                // === กรอกไม่ใช่ตัวเลข ===
                
                // แสดงข้อความผิดพลาดและให้กรอกใหม่
                JOptionPane.showMessageDialog(this, 
                    "กรุณาใส่ตัวเลขเท่านั้น!\n\nกรุณากรอกจำนวนเงินอีกครั้ง", 
                    "ข้อผิดพลาด", 
                    JOptionPane.ERROR_MESSAGE);
                // จะวนลูปกลับไปให้กรอกใหม่อัตโนมัติ
            }
        }
    }
    
    /**
     * ดูรายการจองทั้งหมด (เมนู 3)
     * แสดงรายละเอียดการจองทั้งหมดที่มีในระบบ
     */
    private void viewBookings() {
        // ดึงรายการจองทั้งหมดจากระบบ
        List<Booking> bookings = bookingSystem.getBookings();
        
        // ถ้ายังไม่มีการจอง
        if (bookings.isEmpty()) {
            setText(
                "═══════════════════════════════════════════════════════\n" +
                "รายการจองทั้งหมด\n" +
                "═══════════════════════════════════════════════════════\n\n" +
                "ยังไม่มีรายการจอง\n"
            );
            return;
        }
        
        // สร้าง StringBuilder เพื่อรวมข้อความ
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("รายการจองทั้งหมด\n");
        sb.append("═══════════════════════════════════════════════════════\n\n");
        
        // กำหนดรูปแบบวันที่
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // วนลูปแสดงรายละเอียดการจองแต่ละรายการ
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
        
        // แสดงข้อความทั้งหมด
        setText(sb.toString());
    }
    
    /**
     * ฟังก์ชัน main - จุดเริ่มต้นของโปรแกรม
     * สร้างและแสดงหน้าต่าง GUI
     * 
     * @param args อาร์กิวเมนต์จาก command line (ไม่ได้ใช้)
     */
    public static void main(String[] args) {
        try {
            // ใช้ Look and Feel ของระบบปฏิบัติการ (ให้หน้าตาเหมือนโปรแกรมของ OS)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ถ้าเกิดข้อผิดพลาด แสดง Stack Trace
            e.printStackTrace();
        }
        
        // ใช้ SwingUtilities.invokeLater เพื่อรันใน Event Dispatch Thread (EDT)
        // เป็น Best Practice สำหรับ Swing GUI
        SwingUtilities.invokeLater(() -> {
            // สร้าง object ของ GUI
            SimpleBookingGUI gui = new SimpleBookingGUI();
            
            // แสดงหน้าต่าง
            gui.setVisible(true);
        });
    }
}
