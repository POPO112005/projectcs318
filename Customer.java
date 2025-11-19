/**
 * คลาสสำหรับเก็บข้อมูลลูกค้า
 * เก็บข้อมูลส่วนตัวของลูกค้าที่ทำการจองบ้านพัก
 */
public class Customer {
    // ชื่อเต็มของลูกค้า
    private String fullName;
    
    // เบอร์โทรศัพท์ติดต่อ
    private String phoneNumber;
    
    // อีเมลสำหรับติดต่อ
    private String email;
    
    /**
     * Constructor สำหรับสร้างข้อมูลลูกค้า
     * @param fullName ชื่อ-นามสกุลของลูกค้า
     * @param phoneNumber เบอร์โทรศัพท์ติดต่อ
     * @param email อีเมลสำหรับติดต่อ
     */
    public Customer(String fullName, String phoneNumber, String email) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    // === Getters - เมธอดสำหรับดึงข้อมูล ===
    
    /**
     * ดึงชื่อเต็มของลูกค้า
     * @return ชื่อ-นามสกุล
     */
    public String getFullName() {
        return fullName;
    }
    
    /**
     * ดึงเบอร์โทรศัพท์
     * @return เบอร์โทรศัพท์
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * ดึงอีเมล
     * @return อีเมล
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * แสดงข้อมูลลูกค้า
     * พิมพ์ข้อมูลส่วนตัวของลูกค้าในรูปแบบที่อ่านง่าย
     */
    public void displayInfo() {
        System.out.println("ข้อมูลลูกค้า:");
        System.out.println("  ชื่อ: " + fullName);
        System.out.println("  เบอร์โทร: " + phoneNumber);
        System.out.println("  อีเมล: " + email);
    }
    
    /**
     * แปลงข้อมูลลูกค้าเป็น String
     * @return ข้อมูลลูกค้าในรูปแบบ String (ชื่อ, เบอร์, อีเมล)
     */
    @Override
    public String toString() {
        return String.format("%s (%s, %s)", fullName, phoneNumber, email);
    }
}
