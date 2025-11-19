import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * คลาสระบบจัดการการจองบ้านพัก (Main System)
 * จัดการบ้านพักทั้งหมดและรายการจองทั้งหมด
 * เป็นศูนย์กลางในการประมวลผลการจองและการชำระเงิน
 */
public class BookingSystem {
    // รายการบ้านพักทั้งหมด (10 หลัง)
    private List<House> houses;
    
    // รายการการจองทั้งหมด
    private List<Booking> bookings;
    
    // จำนวนบ้านทั้งหมดในระบบ
    private static final int TOTAL_HOUSES = 10;
    
    /**
     * Constructor - สร้างระบบจองบ้านพัก
     * เริ่มต้นระบบด้วยการสร้างบ้าน 10 หลังพร้อมราคา
     */
    public BookingSystem() {
        // สร้าง ArrayList เปล่าสำหรับเก็บบ้าน
        houses = new ArrayList<>();
        
        // สร้าง ArrayList เปล่าสำหรับเก็บการจอง
        bookings = new ArrayList<>();
        
        // เรียกเมธอดสร้างบ้าน 10 หลัง
        initializeHouses();
    }
    
    /**
     * สร้างบ้านพัก 10 หลัง พร้อมกำหนดราคาแต่ละหลัง
     * ราคาจะแตกต่างกันตามหมายเลขบ้าน
     */
    private void initializeHouses() {
        // สร้างบ้านพัก 10 หลัง (ราคาสามารถปรับได้ตามต้องการ)
        houses.add(new House(1, 1000.0));  // บ้านหมายเลข 1: 1000 บาท/วัน
        houses.add(new House(2, 1000.0));  // บ้านหมายเลข 2: 1000 บาท/วัน
        houses.add(new House(3, 1200.0));  // บ้านหมายเลข 3: 1200 บาท/วัน
        houses.add(new House(4, 1200.0));  // บ้านหมายเลข 4: 1200 บาท/วัน
        houses.add(new House(5, 1500.0));  // บ้านหมายเลข 5: 1500 บาท/วัน
        houses.add(new House(6, 1500.0));  // บ้านหมายเลข 6: 1500 บาท/วัน
        houses.add(new House(7, 1800.0));  // บ้านหมายเลข 7: 1800 บาท/วัน
        houses.add(new House(8, 1800.0));  // บ้านหมายเลข 8: 1800 บาท/วัน
        houses.add(new House(9, 2000.0));  // บ้านหมายเลข 9: 2000 บาท/วัน
        houses.add(new House(10, 2000.0)); // บ้านหมายเลข 10: 2000 บาท/วัน
    }
    
    /**
     * แสดงสถานะของบ้านพักทั้งหมด
     * พิมพ์รายการบ้านทั้ง 10 หลัง พร้อมสถานะว่างและราคา
     */
    public void displayAllHousesStatus() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         สถานะบ้านพักทั้งหมด (10 หลัง)              ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
        
        // วนลูปแสดงสถานะบ้านแต่ละหลัง
        for (House house : houses) {
            house.displayStatus();
        }
        System.out.println();
    }
    
    /**
     * ค้นหาบ้านพักจากหมายเลข
     * @param houseNumber หมายเลขบ้านที่ต้องการค้นหา (1-10)
     * @return object ของ House ถ้าพบ, null ถ้าไม่พบ
     */
    public House findHouseByNumber(int houseNumber) {
        // วนลูปค้นหาบ้านที่มีหมายเลขตรงกัน
        for (House house : houses) {
            if (house.getHouseNumber() == houseNumber) {
                return house; // เจอแล้ว return ทันที
            }
        }
        return null; // ไม่เจอ return null
    }
    
    /**
     * ตรวจสอบว่าบ้านว่างในช่วงวันที่ต้องการหรือไม่
     * ตรวจสอบทั้งสถานะบ้านและการจองที่ทับซ้อนกัน
     * 
     * @param house บ้านที่ต้องการตรวจสอบ
     * @param checkIn วันที่เข้าพัก
     * @param checkOut วันที่คืนบ้าน
     * @return true ถ้าบ้านว่าง, false ถ้าบ้านไม่ว่างหรือมีการจองทับซ้อน
     */
    public boolean isHouseAvailableForDates(House house, LocalDate checkIn, LocalDate checkOut) {
        // ตรวจสอบสถานะบ้านก่อน
        if (!house.isAvailable()) {
            return false; // บ้านไม่ว่าง
        }
        
        // ตรวจสอบว่ามีการจองที่ทับซ้อนกับวันที่ต้องการหรือไม่
        for (Booking booking : bookings) {
            // ตรวจสอบเฉพาะการจองของบ้านหลังเดียวกัน
            if (booking.getHouse().getHouseNumber() == house.getHouseNumber()) {
                // ตรวจสอบว่าวันที่ทับซ้อนกันหรือไม่
                // วันที่ไม่ทับซ้อนเมื่อ: checkOut มาก่อน booking.checkIn หรือ checkIn มาหลัง booking.checkOut
                if (!(checkOut.isBefore(booking.getCheckInDate()) || 
                      checkIn.isAfter(booking.getCheckOutDate()))) {
                    return false; // วันที่ทับซ้อนกัน
                }
            }
        }
        return true; // บ้านว่างและไม่มีการจองทับซ้อน
    }
    
    /**
     * สร้างการจองใหม่ (ยังไม่ยืนยัน - รอชำระเงิน)
     * สร้าง Booking object แต่ยังไม่เพิ่มเข้ารายการจอง
     * การจองจะถูกยืนยันก็ต่อเมื่อชำระเงินสำเร็จแล้ว
     * 
     * @param house บ้านที่ต้องการจอง
     * @param customer ข้อมูลลูกค้า
     * @param checkInDate วันที่เข้าพัก
     * @param checkOutDate วันที่คืนบ้าน
     * @return Booking object ถ้าสามารถจองได้, null ถ้าบ้านไม่ว่าง
     */
    public Booking createBooking(House house, Customer customer, 
                                 LocalDate checkInDate, LocalDate checkOutDate) {
        // ตรวจสอบว่าบ้านว่างในช่วงวันที่ต้องการหรือไม่
        if (!isHouseAvailableForDates(house, checkInDate, checkOutDate)) {
            System.out.println("ขออภัย บ้านหมายเลข " + house.getHouseNumber() + 
                             " ไม่ว่างในช่วงเวลาที่เลือก");
            return null;
        }
        
        // สร้าง Booking object แต่ยังไม่เพิ่มเข้า list (รอชำระเงินก่อน)
        Booking booking = new Booking(house, customer, checkInDate, checkOutDate);
        return booking;
    }
    
    /**
     * ยืนยันการจอง (เรียกหลังชำระเงินสำเร็จแล้ว)
     * เพิ่มการจองเข้ารายการจองในระบบ
     * 
     * @param booking การจองที่ต้องการยืนยัน
     */
    public void confirmBooking(Booking booking) {
        // ตรวจสอบว่ามี booking และยังไม่ได้อยู่ใน list แล้ว
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking); // เพิ่มเข้ารายการจอง
        }
    }
    
    /**
     * ประมวลผลการชำระเงิน
     * ตรวจสอบจำนวนเงินและยืนยันการจองถ้าชำระถูกต้อง
     * 
     * @param booking การจองที่ต้องการชำระเงิน
     * @param paymentAmount จำนวนเงินที่ชำระ
     * @return true ถ้าชำระเงินสำเร็จ, false ถ้าจำนวนเงินไม่ถูกต้อง
     */
    public boolean processPayment(Booking booking, double paymentAmount) {
        // ตรวจสอบว่ามีการจองหรือไม่
        if (booking == null) {
            System.out.println("ไม่พบข้อมูลการจอง");
            return false;
        }
        
        // ตรวจสอบว่าจำนวนเงินตรงกับราคารวมหรือไม่
        if (paymentAmount == booking.getTotalPrice()) {
            // ชำระเงินถูกต้อง
            booking.setPaid(true); // ตั้งสถานะเป็นชำระแล้ว
            
            // อัพเดทสถานะบ้านเป็นไม่ว่าง
            booking.getHouse().setAvailable(false);
            
            // ยืนยันการจอง - เพิ่มเข้ารายการจอง
            confirmBooking(booking);
            
            System.out.println("\n✓ ชำระเงินสำเร็จ!");
            return true;
        } else {
            // จำนวนเงินไม่ถูกต้อง
            System.out.println("\n✗ จำนวนเงินไม่ถูกต้อง");
            System.out.printf("ต้องชำระ: %.2f บาท แต่ได้รับ: %.2f บาท\n", 
                            booking.getTotalPrice(), paymentAmount);
            return false;
        }
    }
    
    /**
     * แสดงรายการจองทั้งหมด
     * พิมพ์รายละเอียดการจองทั้งหมดที่มีในระบบ
     */
    public void displayAllBookings() {
        // ตรวจสอบว่ามีการจองหรือไม่
        if (bookings.isEmpty()) {
            System.out.println("\nยังไม่มีการจอง\n");
            return;
        }
        
        // แสดงหัวเรื่อง
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         รายการจองทั้งหมด                            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
        
        // วนลูปแสดงรายละเอียดการจองแต่ละรายการ
        for (Booking booking : bookings) {
            booking.displayBookingDetails();
        }
    }
    
    /**
     * แปลงวันที่จาก String เป็น LocalDate
     * รองรับรูปแบบ dd/MM/yyyy (เช่น 15/12/2025)
     * 
     * @param dateStr วันที่ในรูปแบบ String (dd/MM/yyyy)
     * @return LocalDate object
     * @throws DateTimeParseException ถ้ารูปแบบวันที่ไม่ถูกต้อง
     */
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        // กำหนดรูปแบบวันที่เป็น dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // แปลง String เป็น LocalDate
        return LocalDate.parse(dateStr, formatter);
    }
    
    // === Getters - เมธอดสำหรับดึงข้อมูล ===
    
    /**
     * ดึงรายการบ้านทั้งหมด
     * @return List ของบ้านทั้งหมด
     */
    public List<House> getHouses() {
        return houses;
    }
    
    /**
     * ดึงรายการจองทั้งหมด
     * @return List ของการจองทั้งหมด
     */
    public List<Booking> getBookings() {
        return bookings;
    }
}
