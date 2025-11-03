/**
 * คลาสสำหรับจัดการข้อมูลบ้านพัก
 */
public class House {
    private int houseNumber;
    private boolean isAvailable;
    private double pricePerDay;
    
    /**
     * Constructor สำหรับสร้างบ้านพัก
     * @param houseNumber หมายเลขบ้าน
     * @param pricePerDay ราคาต่อวัน
     */
    public House(int houseNumber, double pricePerDay) {
        this.houseNumber = houseNumber;
        this.pricePerDay = pricePerDay;
        this.isAvailable = true; // เริ่มต้นบ้านว่างทั้งหมด
    }
    
    // Getters
    public int getHouseNumber() {
        return houseNumber;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public double getPricePerDay() {
        return pricePerDay;
    }
    
    // Setters
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    /**
     * แสดงสถานะของบ้านพัก
     */
    public void displayStatus() {
        String status = isAvailable ? "ว่าง" : "ไม่ว่าง";
        System.out.printf("บ้านหมายเลข %d - สถานะ: %s - ราคา: %.2f บาท/วัน\n", 
                         houseNumber, status, pricePerDay);
    }
    
    @Override
    public String toString() {
        return String.format("บ้านหมายเลข %d [%s]", 
                           houseNumber, isAvailable ? "ว่าง" : "ไม่ว่าง");
    }
}
