import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * คลาสระบบจัดการการจองบ้านพัก
 */
public class BookingSystem {
    private List<House> houses;
    private List<Booking> bookings;
    private static final int TOTAL_HOUSES = 10;
    
    /**
     * Constructor - สร้างระบบจองบ้านพักพร้อมบ้าน 10 หลัง
     */
    public BookingSystem() {
        houses = new ArrayList<>();
        bookings = new ArrayList<>();
        initializeHouses();
    }
    
    /**
     * สร้างบ้านพัก 10 หลัง พร้อมกำหนดราคา
     */
    private void initializeHouses() {
        // สร้างบ้านพัก 10 หลัง (ราคาสามารถปรับได้ตามต้องการ)
        houses.add(new House(1, 1000.0));
        houses.add(new House(2, 1000.0));
        houses.add(new House(3, 1200.0));
        houses.add(new House(4, 1200.0));
        houses.add(new House(5, 1500.0));
        houses.add(new House(6, 1500.0));
        houses.add(new House(7, 1800.0));
        houses.add(new House(8, 1800.0));
        houses.add(new House(9, 2000.0));
        houses.add(new House(10, 2000.0));
    }
    
    /**
     * แสดงสถานะของบ้านพักทั้งหมด
     */
    public void displayAllHousesStatus() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         สถานะบ้านพักทั้งหมด (10 หลัง)              ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
        
        for (House house : houses) {
            house.displayStatus();
        }
        System.out.println();
    }
    
    /**
     * ค้นหาบ้านพักจากหมายเลข
     */
    public House findHouseByNumber(int houseNumber) {
        for (House house : houses) {
            if (house.getHouseNumber() == houseNumber) {
                return house;
            }
        }
        return null;
    }
    
    /**
     * ตรวจสอบว่าบ้านว่างในช่วงวันที่ต้องการหรือไม่
     */
    public boolean isHouseAvailableForDates(House house, LocalDate checkIn, LocalDate checkOut) {
        if (!house.isAvailable()) {
            return false;
        }
        
        // ตรวจสอบว่ามีการจองที่ทับซ้อนหรือไม่
        for (Booking booking : bookings) {
            if (booking.getHouse().getHouseNumber() == house.getHouseNumber()) {
                // ตรวจสอบว่าวันที่ทับซ้อนกันหรือไม่
                if (!(checkOut.isBefore(booking.getCheckInDate()) || 
                      checkIn.isAfter(booking.getCheckOutDate()))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * สร้างการจองใหม่
     */
    public Booking createBooking(House house, Customer customer, 
                                 LocalDate checkInDate, LocalDate checkOutDate) {
        if (!isHouseAvailableForDates(house, checkInDate, checkOutDate)) {
            System.out.println("ขออภัย บ้านหมายเลข " + house.getHouseNumber() + 
                             " ไม่ว่างในช่วงเวลาที่เลือก");
            return null;
        }
        
        Booking booking = new Booking(house, customer, checkInDate, checkOutDate);
        bookings.add(booking);
        return booking;
    }
    
    /**
     * ประมวลผลการชำระเงิน
     */
    public boolean processPayment(Booking booking, double paymentAmount) {
        if (booking == null) {
            System.out.println("ไม่พบข้อมูลการจอง");
            return false;
        }
        
        if (paymentAmount == booking.getTotalPrice()) {
            booking.setPaid(true);
            // อัพเดทสถานะบ้าน
            booking.getHouse().setAvailable(false);
            System.out.println("\n✓ ชำระเงินสำเร็จ!");
            return true;
        } else {
            System.out.println("\n✗ จำนวนเงินไม่ถูกต้อง");
            System.out.printf("ต้องชำระ: %.2f บาท แต่ได้รับ: %.2f บาท\n", 
                            booking.getTotalPrice(), paymentAmount);
            return false;
        }
    }
    
    /**
     * แสดงรายการจองทั้งหมด
     */
    public void displayAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("\nยังไม่มีการจอง\n");
            return;
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         รายการจองทั้งหมด                            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
        
        for (Booking booking : bookings) {
            booking.displayBookingDetails();
        }
    }
    
    /**
     * แปลงวันที่จาก String เป็น LocalDate
     */
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }
    
    public List<House> getHouses() {
        return houses;
    }
    
    public List<Booking> getBookings() {
        return bookings;
    }
}
