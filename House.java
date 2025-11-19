/**
 * คลาสสำหรับจัดการข้อมูลบ้านพัก
 * เก็บข้อมูลของบ้านแต่ละหลัง รวมถึงหมายเลขบ้าน สถานะว่าง และราคา
 */
public class House {
    // หมายเลขบ้าน (1-10)
    private int houseNumber;
    
    // สถานะว่าง: true = ว่าง, false = ไม่ว่าง/ถูกจอง
    private boolean isAvailable;
    
    // ราคาต่อวัน (บาท)
    private double pricePerDay;
    
    /**
     * Constructor สำหรับสร้างบ้านพัก
     * @param houseNumber หมายเลขบ้าน (เช่น 1, 2, 3, ...)
     * @param pricePerDay ราคาต่อวัน (บาท)
     */
    public House(int houseNumber, double pricePerDay) {
        this.houseNumber = houseNumber;
        this.pricePerDay = pricePerDay;
        this.isAvailable = true; // เริ่มต้นบ้านว่างทั้งหมด
    }
    
    // === Getters - เมธอดสำหรับดึงข้อมูล ===
    
    /**
     * ดึงหมายเลขบ้าน
     * @return หมายเลขบ้าน
     */
    public int getHouseNumber() {
        return houseNumber;
    }
    
    /**
     * ตรวจสอบสถานะว่างของบ้าน
     * @return true ถ้าบ้านว่าง, false ถ้าบ้านไม่ว่าง
     */
    public boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * ดึงราคาต่อวัน
     * @return ราคาต่อวัน (บาท)
     */
    public double getPricePerDay() {
        return pricePerDay;
    }
    
    // === Setters - เมธอดสำหรับตั้งค่าข้อมูล ===
    
    /**
     * ตั้งค่าสถานะว่างของบ้าน
     * @param available true = บ้านว่าง, false = บ้านไม่ว่าง
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    /**
     * แสดงสถานะของบ้านพัก
     * พิมพ์ข้อมูลบ้าน รวมหมายเลข สถานะ และราคาต่อวัน
     */
    public void displayStatus() {
        // แปลงสถานะเป็นภาษาไทย
        String status = isAvailable ? "ว่าง" : "ไม่ว่าง";
        
        // แสดงข้อมูลในรูปแบบที่อ่านง่าย
        System.out.printf("บ้านหมายเลข %d - สถานะ: %s - ราคา: %.2f บาท/วัน\n", 
                         houseNumber, status, pricePerDay);
    }
    
    /**
     * แปลงข้อมูลบ้านเป็น String
     * @return ข้อมูลบ้านในรูปแบบ String
     */
    @Override
    public String toString() {
        return String.format("บ้านหมายเลข %d [%s]", 
                           houseNumber, isAvailable ? "ว่าง" : "ไม่ว่าง");
    }
}
