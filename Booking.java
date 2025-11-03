import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * คลาสสำหรับจัดการการจองบ้านพัก
 */
public class Booking {
    private static int bookingCounter = 1;
    
    private int bookingId;
    private House house;
    private Customer customer;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private boolean isPaid;
    
    /**
     * Constructor สำหรับสร้างการจอง
     */
    public Booking(House house, Customer customer, LocalDate checkInDate, LocalDate checkOutDate) {
        this.bookingId = bookingCounter++;
        this.house = house;
        this.customer = customer;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isPaid = false;
        calculateTotalPrice();
    }
    
    /**
     * คำนวณราคารวมโดยอัตโนมัติ
     */
    private void calculateTotalPrice() {
        long numberOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (numberOfDays <= 0) {
            numberOfDays = 1; // อย่างน้อย 1 วัน
        }
        this.totalPrice = house.getPricePerDay() * numberOfDays;
    }
    
    // Getters
    public int getBookingId() {
        return bookingId;
    }
    
    public House getHouse() {
        return house;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
    
    public void setPaid(boolean paid) {
        isPaid = paid;
    }
    
    /**
     * คำนวณจำนวนวันที่เข้าพัก
     */
    public long getNumberOfDays() {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return days <= 0 ? 1 : days;
    }
    
    /**
     * แสดงรายละเอียดการจอง
     */
    public void displayBookingDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         รายละเอียดการจองบ้านพัก                    ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("หมายเลขการจอง: " + bookingId );
        System.out.println("บ้านหมายเลข: " + house.getHouseNumber());
        System.out.println("วันที่เข้าพัก: " + checkInDate.format(formatter));
        System.out.println("วันที่คืนบ้าน: " + checkOutDate.format(formatter));
        System.out.println("จำนวนวัน: " + getNumberOfDays() + " วัน");
        System.out.println("ราคาต่อวัน: " + String.format("%.2f", house.getPricePerDay()) + " บาท");
        System.out.println("──────────────────────────────────────────────────────");
        System.out.println("ราคารวม: " + String.format("%.2f", totalPrice) + " บาท");
        System.out.println("──────────────────────────────────────────────────────");
        System.out.println();
        customer.displayInfo();
        System.out.println();
        System.out.println("สถานะการชำระเงิน: " + (isPaid ? "ชำระแล้ว ✓" : "ยังไม่ได้ชำระ"));
        System.out.println("══════════════════════════════════════════════════════\n");
    }
}
