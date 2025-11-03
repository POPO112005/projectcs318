import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * โปรแกรมหลักสำหรับระบบจองบ้านพัก
 */
public class HouseBookingApp {
    private static BookingSystem bookingSystem = new BookingSystem();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║     ยินดีต้อนรับสู่ระบบจองบ้านพัก                  ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
        
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = getIntInput("เลือกเมนู: ");
            
            switch (choice) {
                case 1:
                    viewHouseStatus();
                    break;
                case 2:
                    makeBooking();
                    break;
                case 3:
                    viewAllBookings();
                    break;
                case 4:
                    System.out.println("\nขอบคุณที่ใช้บริการ!\n");
                    running = false;
                    break;
                default:
                    System.out.println("\n✗ กรุณาเลือกเมนู 1-4 เท่านั้น\n");
            }
        }
        
        scanner.close();
    }
    
    /**
     * แสดงเมนูหลัก
     */
    private static void displayMenu() {
        System.out.println("┌──────────────────────────────────────────────────────┐");
        System.out.println("│ เมนูหลัก                                            │");
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.println("│ 1. ดูสถานะบ้านพัก                                   │");
        System.out.println("│ 2. จองบ้านพัก                                       │");
        System.out.println("│ 3. ดูรายการจองทั้งหมด                               │");
        System.out.println("│ 4. ออกจากโปรแกรม                                    │");
        System.out.println("└──────────────────────────────────────────────────────┘");
    }
    
    /**
     * แสดงสถานะบ้านพัก
     */
    private static void viewHouseStatus() {
        bookingSystem.displayAllHousesStatus();
        System.out.println("กด Enter เพื่อกลับไปเมนูหลัก...");
        scanner.nextLine();
        scanner.nextLine();
    }
    
    /**
     * ขั้นตอนการจองบ้านพัก
     */
    private static void makeBooking() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         ขั้นตอนการจองบ้านพัก                       ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
        
        // แสดงสถานะบ้านพักก่อน
        bookingSystem.displayAllHousesStatus();
        
        // 1. เลือกหมายเลขบ้าน
        int houseNumber = getIntInput("พิมพ์หมายเลขบ้านที่ต้องการจอง (1-10): ");
        
        if (houseNumber < 1 || houseNumber > 10) {
            System.out.println("\n✗ หมายเลขบ้านไม่ถูกต้อง กรุณาเลือก 1-10 เท่านั้น\n");
            return;
        }
        
        House selectedHouse = bookingSystem.findHouseByNumber(houseNumber);
        if (selectedHouse == null) {
            System.out.println("\n✗ ไม่พบบ้านหมายเลข " + houseNumber + "\n");
            return;
        }
        
        // 2. กรอกวันที่เข้าพัก
        System.out.println("\n--- กรอกวันที่เข้าพัก ---");
        System.out.println("ตัวอย่าง: 15/12/2025");
        scanner.nextLine(); // consume newline
        
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;
        
        try {
            System.out.print("วันที่เข้าพัก (วัน/เดือน/ปี): ");
            String checkInStr = scanner.nextLine();
            checkInDate = BookingSystem.parseDate(checkInStr);
            
            System.out.print("วันที่คืนบ้าน (วัน/เดือน/ปี): ");
            String checkOutStr = scanner.nextLine();
            checkOutDate = BookingSystem.parseDate(checkOutStr);
            
            // ตรวจสอบความถูกต้องของวันที่
            if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
                System.out.println("\n✗ วันที่คืนบ้านต้องหลังจากวันที่เข้าพัก\n");
                return;
            }
            
        } catch (DateTimeParseException e) {
            System.out.println("\n✗ รูปแบบวันที่ไม่ถูกต้อง กรุณาใช้รูปแบบ วัน/เดือน/ปี (เช่น 15/12/2025)\n");
            return;
        }
        
        // ตรวจสอบว่าบ้านว่างหรือไม่
        if (!bookingSystem.isHouseAvailableForDates(selectedHouse, checkInDate, checkOutDate)) {
            System.out.println("\n✗ ขออภัย บ้านหมายเลข " + houseNumber + 
                             " ไม่ว่างในช่วงวันที่ที่เลือก\n");
            return;
        }
        
        // 3. กรอกข้อมูลติดต่อ
        System.out.println("\n--- กรอกข้อมูลติดต่อ ---");
        System.out.print("ชื่อจริง: ");
        String fullName = scanner.nextLine();
        
        System.out.print("เบอร์โทร: ");
        String phoneNumber = scanner.nextLine();
        
        System.out.print("อีเมล: ");
        String email = scanner.nextLine();
        
        // สร้างข้อมูลลูกค้า
        Customer customer = new Customer(fullName, phoneNumber, email);
        
        // สร้างการจอง
        Booking booking = bookingSystem.createBooking(selectedHouse, customer, checkInDate, checkOutDate);
        
        if (booking == null) {
            System.out.println("\n✗ ไม่สามารถสร้างการจองได้\n");
            return;
        }
        
        // แสดงข้อมูลการจองและราคา
        System.out.println("\n--- ระบบคำนวณราคารวมโดยอัตโนมัติ ---");
        System.out.printf("จำนวนวัน: %d วัน\n", booking.getNumberOfDays());
        System.out.printf("ราคาต่อวัน: %.2f บาท\n", selectedHouse.getPricePerDay());
        System.out.printf("ราคารวม: %.2f บาท\n", booking.getTotalPrice());
        
        // 4. ชำระเงิน
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         การชำระเงิน                                 ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.printf("ยอดที่ต้องชำระ: %.2f บาท\n", booking.getTotalPrice());
        System.out.println("กรุณาพิมพ์จำนวนเงินที่ชำระตามจำนวนที่แสดง");
        
        double paymentAmount = getDoubleInput("จำนวนเงินที่ชำระ: ");
        
        // ประมวลผลการชำระเงิน
        boolean paymentSuccess = bookingSystem.processPayment(booking, paymentAmount);
        
        if (paymentSuccess) {
            // แสดงรายละเอียดการจอง
            booking.displayBookingDetails();
            System.out.println("✓ การจองเสร็จสมบูรณ์!\n");
        } else {
            System.out.println("✗ การชำระเงินไม่สำเร็จ กรุณาลองใหม่อีกครั้ง\n");
        }
    }
    
    /**
     * แสดงรายการจองทั้งหมด
     */
    private static void viewAllBookings() {
        bookingSystem.displayAllBookings();
        System.out.println("กด Enter เพื่อกลับไปเมนูหลัก...");
        scanner.nextLine();
        scanner.nextLine();
    }
    
    /**
     * รับค่า Integer จากผู้ใช้
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("✗ กรุณาใส่ตัวเลขเท่านั้น");
            }
        }
    }
    
    /**
     * รับค่า Double จากผู้ใช้
     */
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("✗ กรุณาใส่ตัวเลขที่ถูกต้อง");
            }
        }
    }
}
