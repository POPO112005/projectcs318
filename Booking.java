import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * คลาสสำหรับจัดการการจองบ้านพัก
 * เก็บข้อมูลการจองแต่ละรายการ รวมถึงข้อมูลบ้าน ลูกค้า วันที่ และราคา
 */
public class Booking {
    // ตัวนับสำหรับสร้างหมายเลขการจองอัตโนมัติ (Auto-increment)
    private static int bookingCounter = 1;
    
    // หมายเลขการจองที่ไม่ซ้ำกัน
    private int bookingId;
    
    // ข้อมูลบ้านที่ถูกจอง
    private House house;
    
    // ข้อมูลลูกค้าที่ทำการจอง
    private Customer customer;
    
    // วันที่เข้าพัก
    private LocalDate checkInDate;
    
    // วันที่คืนบ้าน
    private LocalDate checkOutDate;
    
    // ราคารวมทั้งหมด (คำนวณจากจำนวนวัน x ราคาต่อวัน)
    private double totalPrice;
    
    // สถานะการชำระเงิน: true = ชำระแล้ว, false = ยังไม่ชำระ
    private boolean isPaid;
    
    /**
     * Constructor สำหรับสร้างการจอง
     * สร้างหมายเลขการจองอัตโนมัติและคำนวณราคารวม
     * 
     * @param house บ้านที่ต้องการจอง
     * @param customer ข้อมูลลูกค้า
     * @param checkInDate วันที่เข้าพัก
     * @param checkOutDate วันที่คืนบ้าน
     */
    public Booking(House house, Customer customer, LocalDate checkInDate, LocalDate checkOutDate) {
        // สร้างหมายเลขการจองแบบ Auto-increment
        this.bookingId = bookingCounter++;
        
        this.house = house;
        this.customer = customer;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        
        // เริ่มต้นยังไม่ได้ชำระเงิน
        this.isPaid = false;
        
        // คำนวณราคารวมอัตโนมัติ
        calculateTotalPrice();
    }
    
    /**
     * คำนวณราคารวมโดยอัตโนมัติ
     * สูตร: จำนวนวันที่เข้าพัก x ราคาต่อวัน
     */
    private void calculateTotalPrice() {
        // คำนวณจำนวนวันระหว่างวันเข้าพักและวันคืนบ้าน
        long numberOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        
        // ถ้าน้อยกว่าหรือเท่ากับ 0 ให้นับเป็น 1 วัน
        if (numberOfDays <= 0) {
            numberOfDays = 1; // อย่างน้อย 1 วัน
        }
        
        // คำนวณราคารวม = จำนวนวัน x ราคาต่อวัน
        this.totalPrice = house.getPricePerDay() * numberOfDays;
    }
    
    // === Getters - เมธอดสำหรับดึงข้อมูล ===
    
    /**
     * ดึงหมายเลขการจอง
     * @return หมายเลขการจอง (ไม่ซ้ำกัน)
     */
    public int getBookingId() {
        return bookingId;
    }
    
    /**
     * ดึงข้อมูลบ้านที่ถูกจอง
     * @return object ของ House
     */
    public House getHouse() {
        return house;
    }
    
    /**
     * ดึงข้อมูลลูกค้า
     * @return object ของ Customer
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * ดึงวันที่เข้าพัก
     * @return วันที่เข้าพัก
     */
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    /**
     * ดึงวันที่คืนบ้าน
     * @return วันที่คืนบ้าน
     */
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    /**
     * ดึงราคารวมทั้งหมด
     * @return ราคารวม (บาท)
     */
    public double getTotalPrice() {
        return totalPrice;
    }
    
    /**
     * ตรวจสอบสถานะการชำระเงิน
     * @return true ถ้าชำระแล้ว, false ถ้ายังไม่ชำระ
     */
    public boolean isPaid() {
        return isPaid;
    }
    
    /**
     * ตั้งค่าสถานะการชำระเงิน
     * @param paid true = ชำระแล้ว, false = ยังไม่ชำระ
     */
    public void setPaid(boolean paid) {
        isPaid = paid;
    }
    
    /**
     * คำนวณจำนวนวันที่เข้าพัก
     * @return จำนวนวัน (อย่างน้อย 1 วัน)
     */
    public long getNumberOfDays() {
        // คำนวณจำนวนวันระหว่าง checkInDate และ checkOutDate
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        
        // ถ้าน้อยกว่าหรือเท่ากับ 0 ให้ return 1
        return days <= 0 ? 1 : days;
    }
    
    /**
     * แสดงรายละเอียดการจองแบบสวยงาม
     * พิมพ์ข้อมูลการจองทั้งหมดในรูปแบบที่มีกรอบและอ่านง่าย
     */
    public void displayBookingDetails() {
        // กำหนดรูปแบบวันที่เป็น dd/MM/yyyy (เช่น 15/12/2025)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // แสดงหัวเรื่อง
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         รายละเอียดการจองบ้านพัก                    ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        
        // แสดงข้อมูลการจอง
        System.out.println("หมายเลขการจอง: " + bookingId );
        System.out.println("บ้านหมายเลข: " + house.getHouseNumber());
        System.out.println("วันที่เข้าพัก: " + checkInDate.format(formatter));
        System.out.println("วันที่คืนบ้าน: " + checkOutDate.format(formatter));
        System.out.println("จำนวนวัน: " + getNumberOfDays() + " วัน");
        System.out.println("ราคาต่อวัน: " + String.format("%.2f", house.getPricePerDay()) + " บาท");
        
        // เส้นแบ่ง
        System.out.println("──────────────────────────────────────────────────────");
        System.out.println("ราคารวม: " + String.format("%.2f", totalPrice) + " บาท");
        System.out.println("──────────────────────────────────────────────────────");
        System.out.println();
        
        // แสดงข้อมูลลูกค้า
        customer.displayInfo();
        System.out.println();
        
        // แสดงสถานะการชำระเงิน
        System.out.println("สถานะการชำระเงิน: " + (isPaid ? "ชำระแล้ว ✓" : "ยังไม่ได้ชำระ"));
        System.out.println("══════════════════════════════════════════════════════\n");
    }
}
