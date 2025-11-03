/**
 * คลาสสำหรับเก็บข้อมูลลูกค้า
 */
public class Customer {
    private String fullName;
    private String phoneNumber;
    private String email;
    
    /**
     * Constructor สำหรับสร้างข้อมูลลูกค้า
     * @param fullName ชื่อจริง
     * @param phoneNumber เบอร์โทรศัพท์
     * @param email อีเมล
     */
    public Customer(String fullName, String phoneNumber, String email) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    // Getters
    public String getFullName() {
        return fullName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    /**
     * แสดงข้อมูลลูกค้า
     */
    public void displayInfo() {
        System.out.println("ข้อมูลลูกค้า:");
        System.out.println("  ชื่อ: " + fullName);
        System.out.println("  เบอร์โทร: " + phoneNumber);
        System.out.println("  อีเมล: " + email);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s, %s)", fullName, phoneNumber, email);
    }
}
