# ระบบจองบ้านพัก (House Booking System)

## 📋 ภาพรวมของโปรเจค

โปรเจคนี้เป็นระบบจองบ้านพักที่พัฒนาด้วยภาษา Java โดยใช้ Java Swing สร้าง GUI (Graphical User Interface) สำหรับผู้ใช้งาน ระบบสามารถจัดการบ้านพัก 10 หลัง รองรับการจองพร้อมระบบชำระเงิน และเก็บข้อมูลลูกค้าและการจองทั้งหมด

---

## 🏗️ สถาปัตยกรรมการออกแบบ (Architecture Design)

โปรเจคนี้ใช้หลักการ **Object-Oriented Programming (OOP)** ในการออกแบบ โดยแบ่งระบบออกเป็น 5 คลาสหลัก ดังนี้:

```
┌─────────────────────────┐
│   SimpleBookingGUI      │  ← GUI Layer (Presentation)
│  (Java Swing)           │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│   BookingSystem         │  ← Business Logic Layer
│  (Main Controller)      │
└───────────┬─────────────┘
            │
            ├──────────┬──────────┬──────────┐
            ▼          ▼          ▼          ▼
      ┌─────────┐┌─────────┐┌──────────┐┌──────────┐
      │  House  ││Customer ││ Booking  ││ Booking  │  ← Data Models
      │         ││         ││          ││          │
      └─────────┘└─────────┘└──────────┘└──────────┘
           ...        ...        ...         ...
```

### Pattern ที่ใช้:
- **MVC Pattern** (แบบง่าย):
  - **Model**: `House`, `Customer`, `Booking`
  - **Controller**: `BookingSystem`
  - **View**: `SimpleBookingGUI`

---

## 📦 คลาสทั้งหมดในระบบ

### 1️⃣ คลาส `House` - คลาสข้อมูลบ้านพัก

**หน้าที่หลัก:** เก็บข้อมูลและสถานะของบ้านพักแต่ละหลัง

#### 🔹 Attributes (คุณสมบัติ)
| ชื่อ Attribute | ชนิดข้อมูล | Access Modifier | คำอธิบาย |
|---------------|-----------|-----------------|----------|
| `houseNumber` | `int` | `private` | หมายเลขบ้าน (1-10) |
| `isAvailable` | `boolean` | `private` | สถานะว่างของบ้าน (true = ว่าง, false = ไม่ว่าง) |
| `pricePerDay` | `double` | `private` | ราคาเช่าต่อวัน (บาท) |

#### 🔹 Methods (เมธอด)
| ชื่อ Method | Return Type | คำอธิบาย |
|------------|-------------|----------|
| `House(int, double)` | Constructor | สร้างบ้านพักพร้อมกำหนดหมายเลขและราคา |
| `getHouseNumber()` | `int` | ดึงหมายเลขบ้าน |
| `isAvailable()` | `boolean` | ตรวจสอบสถานะว่าง |
| `getPricePerDay()` | `double` | ดึงราคาต่อวัน |
| `setAvailable(boolean)` | `void` | ตั้งค่าสถานะว่าง |
| `displayStatus()` | `void` | แสดงข้อมูลบ้านในคอนโซล |
| `toString()` | `String` | แปลงข้อมูลเป็น String |

#### 💡 จุดเด่นของการออกแบบ:
- ใช้ **Encapsulation**: ซ่อนข้อมูลด้วย `private` และเข้าถึงผ่าน getter/setter เท่านั้น
- **Immutable `houseNumber`**: หมายเลขบ้านไม่สามารถเปลี่ยนแปลงได้หลังสร้าง (ไม่มี setter)
- **Default Available**: บ้านทุกหลังเริ่มต้นเป็นว่าง (`isAvailable = true`)

---

### 2️⃣ คลาส `Customer` - คลาสข้อมูลลูกค้า

**หน้าที่หลัก:** เก็บข้อมูลส่วนตัวของลูกค้าที่ทำการจอง

#### 🔹 Attributes (คุณสมบัติ)
| ชื่อ Attribute | ชนิดข้อมูล | Access Modifier | คำอธิบาย |
|---------------|-----------|-----------------|----------|
| `fullName` | `String` | `private` | ชื่อ-นามสกุลของลูกค้า |
| `phoneNumber` | `String` | `private` | เบอร์โทรศัพท์ติดต่อ |
| `email` | `String` | `private` | อีเมลสำหรับติดต่อ |

#### 🔹 Methods (เมธอด)
| ชื่อ Method | Return Type | คำอธิบาย |
|------------|-------------|----------|
| `Customer(String, String, String)` | Constructor | สร้างข้อมูลลูกค้า |
| `getFullName()` | `String` | ดึงชื่อเต็ม |
| `getPhoneNumber()` | `String` | ดึงเบอร์โทร |
| `getEmail()` | `String` | ดึงอีเมล |
| `displayInfo()` | `void` | แสดงข้อมูลลูกค้าในคอนโซล |
| `toString()` | `String` | แปลงข้อมูลเป็น String |

#### 💡 จุดเด่นของการออกแบบ:
- **Value Object Pattern**: คลาสนี้เป็น Value Object ที่เก็บข้อมูลอย่างเดียว ไม่มี business logic
- **Immutable**: ข้อมูลลูกค้าไม่สามารถเปลี่ยนแปลงได้หลังสร้าง (ไม่มี setter)
- **Simple and Clean**: เน้นความเรียบง่ายและชัดเจน

---

### 3️⃣ คลาส `Booking` - คลาสข้อมูลการจอง

**หน้าที่หลัก:** เก็บข้อมูลการจองแต่ละรายการ พร้อมคำนวณราคาอัตโนมัติ

#### 🔹 Static Attributes
| ชื่อ Attribute | ชนิดข้อมูล | คำอธิบาย |
|---------------|-----------|----------|
| `bookingCounter` | `int` | ตัวนับสำหรับสร้างหมายเลขการจองอัตโนมัติ (Auto-increment) |

#### 🔹 Instance Attributes
| ชื่อ Attribute | ชนิดข้อมูล | Access Modifier | คำอธิบาย |
|---------------|-----------|-----------------|----------|
| `bookingId` | `int` | `private` | หมายเลขการจองที่ไม่ซ้ำกัน |
| `house` | `House` | `private` | บ้านที่ถูกจอง (Object Reference) |
| `customer` | `Customer` | `private` | ลูกค้าที่ทำการจอง (Object Reference) |
| `checkInDate` | `LocalDate` | `private` | วันที่เข้าพัก |
| `checkOutDate` | `LocalDate` | `private` | วันที่คืนบ้าน |
| `totalPrice` | `double` | `private` | ราคารวมทั้งหมด (คำนวณอัตโนมัติ) |
| `isPaid` | `boolean` | `private` | สถานะการชำระเงิน (true = ชำระแล้ว) |

#### 🔹 Methods (เมธอด)
| ชื่อ Method | Return Type | คำอธิบาย |
|------------|-------------|----------|
| `Booking(House, Customer, LocalDate, LocalDate)` | Constructor | สร้างการจองและคำนวณราคาอัตโนมัติ |
| `calculateTotalPrice()` | `void` | คำนวณราคารวม = จำนวนวัน × ราคาต่อวัน |
| `getBookingId()` | `int` | ดึงหมายเลขการจอง |
| `getHouse()` | `House` | ดึงข้อมูลบ้าน |
| `getCustomer()` | `Customer` | ดึงข้อมูลลูกค้า |
| `getCheckInDate()` | `LocalDate` | ดึงวันที่เข้าพัก |
| `getCheckOutDate()` | `LocalDate` | ดึงวันที่คืนบ้าน |
| `getTotalPrice()` | `double` | ดึงราคารวม |
| `isPaid()` | `boolean` | ตรวจสอบสถานะชำระเงิน |
| `setPaid(boolean)` | `void` | ตั้งค่าสถานะชำระเงิน |
| `getNumberOfDays()` | `long` | คำนวณจำนวนวันที่เข้าพัก |
| `displayBookingDetails()` | `void` | แสดงรายละเอียดการจองแบบสวยงาม |

#### 💡 จุดเด่นของการออกแบบ:
- **Auto-increment ID**: ใช้ `static bookingCounter` สร้างหมายเลขการจองอัตโนมัติไม่ซ้ำ
- **Automatic Price Calculation**: คำนวณราคาอัตโนมัติในตอน Constructor
- **Date Validation**: ใช้ `LocalDate` จาก Java 8+ สำหรับจัดการวันที่อย่างปลอดภัย
- **Minimum 1 Day**: ระบบบังคับให้เช่าอย่างน้อย 1 วัน
- **Two-phase Booking**: 
  1. สร้าง Booking object (ยังไม่ชำระ)
  2. ชำระเงินแล้วจึงยืนยัน

---

### 4️⃣ คลาส `BookingSystem` - คลาสควบคุมระบบหลัก

**หน้าที่หลัก:** จัดการบ้านพักและการจองทั้งหมด เป็นศูนย์กลางของ Business Logic

#### 🔹 Static Attributes
| ชื่อ Attribute | ชนิดข้อมูล | ค่า | คำอธิบาย |
|---------------|-----------|-----|----------|
| `TOTAL_HOUSES` | `int` | `10` | จำนวนบ้านทั้งหมด (Constant) |

#### 🔹 Instance Attributes
| ชื่อ Attribute | ชนิดข้อมูล | Access Modifier | คำอธิบาย |
|---------------|-----------|-----------------|----------|
| `houses` | `List<House>` | `private` | รายการบ้านพักทั้งหมด (10 หลัง) |
| `bookings` | `List<Booking>` | `private` | รายการการจองทั้งหมด |

#### 🔹 Methods (เมธอด)

##### 🔸 Constructor & Initialization
| ชื่อ Method | คำอธิบาย |
|------------|----------|
| `BookingSystem()` | สร้างระบบและเริ่มต้นบ้าน 10 หลัง |
| `initializeHouses()` | สร้างบ้าน 10 หลังพร้อมกำหนดราคา |

##### 🔸 House Management
| ชื่อ Method | Return Type | คำอธิบาย |
|------------|-------------|----------|
| `displayAllHousesStatus()` | `void` | แสดงสถานะบ้านทั้งหมด |
| `findHouseByNumber(int)` | `House` | ค้นหาบ้านจากหมายเลข (1-10) |
| `getHouses()` | `List<House>` | ดึงรายการบ้านทั้งหมด |

##### 🔸 Booking Management
| ชื่อ Method | Return Type | คำอธิบาย |
|------------|-------------|----------|
| `isHouseAvailableForDates(House, LocalDate, LocalDate)` | `boolean` | ตรวจสอบว่าบ้านว่างในช่วงวันที่หรือไม่ (ตรวจทั้งสถานะและการทับซ้อน) |
| `createBooking(House, Customer, LocalDate, LocalDate)` | `Booking` | สร้างการจอง (ยังไม่ยืนยัน) |
| `confirmBooking(Booking)` | `void` | ยืนยันการจอง - เพิ่มเข้ารายการจอง |
| `getBookings()` | `List<Booking>` | ดึงรายการจองทั้งหมด |
| `displayAllBookings()` | `void` | แสดงรายการจองทั้งหมด |

##### 🔸 Payment Processing
| ชื่อ Method | Return Type | คำอธิบาย |
|------------|-------------|----------|
| `processPayment(Booking, double)` | `boolean` | ประมวลผลการชำระเงิน พร้อมยืนยันการจอง |

##### 🔸 Utility Methods
| ชื่อ Method | Return Type | คำอธิบาย |
|------------|-------------|----------|
| `parseDate(String)` | `LocalDate` | แปลงวันที่จาก String (dd/MM/yyyy) เป็น LocalDate |

#### 💡 จุดเด่นของการออกแบบ:
- **Central Controller**: ทำหน้าที่เป็นศูนย์กลางจัดการทุกอย่าง
- **Business Logic Separation**: แยก business logic ออกจาก GUI
- **Date Overlap Detection**: ตรวจจับการจองที่วันที่ทับซ้อนกัน
- **Two-phase Commit Pattern**:
  ```
  1. Create Booking → Pending (ยังไม่ในระบบ)
  2. Process Payment → Success → Confirm Booking (บันทึกเข้าระบบ)
  ```
- **Pre-initialized Houses**: สร้างบ้าน 10 หลังไว้ล่วงหน้าตอน Constructor

#### 📊 ราคาบ้านที่กำหนดไว้:
```
บ้านหมายเลข 1-2:   1,000 บาท/วัน
บ้านหมายเลข 3-4:   1,200 บาท/วัน
บ้านหมายเลข 5-6:   1,500 บาท/วัน
บ้านหมายเลข 7-8:   1,800 บาท/วัน
บ้านหมายเลข 9-10:  2,000 บาท/วัน
```

---

### 5️⃣ คลาส `SimpleBookingGUI` - คลาส Graphical User Interface

**หน้าที่หลัก:** สร้างหน้าต่างโปรแกรมด้วย Java Swing เพื่อให้ผู้ใช้โต้ตอบกับระบบ

#### 🔹 Attributes
| ชื่อ Attribute | ชนิดข้อมูล | Access Modifier | คำอธิบาย |
|---------------|-----------|-----------------|----------|
| `bookingSystem` | `BookingSystem` | `private` | ระบบจองบ้านพัก (Backend) |
| `textPane` | `JTextPane` | `private` | พื้นที่แสดงข้อความ (สามารถจัดกลางได้) |

#### 🔹 Methods (เมธอด)

##### 🔸 UI Setup
| ชื่อ Method | คำอธิบาย |
|------------|----------|
| `SimpleBookingGUI()` | Constructor - สร้าง GUI และ BookingSystem |
| `setupUI()` | ตั้งค่าหน้าต่าง ปุ่ม และพื้นที่แสดงผล |
| `setText(String)` | ตั้งค่าข้อความใน textPane และจัดกลาง |
| `displayWelcome()` | แสดงข้อความต้อนรับ |

##### 🔸 User Actions (Event Handlers)
| ชื่อ Method | คำอธิบาย |
|------------|----------|
| `viewHouses()` | แสดงสถานะบ้านทั้งหมด (เมนู 1) |
| `makeBooking()` | จองบ้านพัก (เมนู 2) |
| `processPayment(Booking)` | ประมวลผลการชำระเงิน (มี retry loop) |
| `viewBookings()` | แสดงรายการจองทั้งหมด (เมนู 3) |

##### 🔸 Main Method
| ชื่อ Method | คำอธิบาย |
|------------|----------|
| `main(String[])` | จุดเริ่มต้นโปรแกรม - สร้างและแสดง GUI |

#### 🎨 GUI Components ที่ใช้:
- `JFrame`: หน้าต่างหลัก
- `JTextPane`: แสดงข้อความ (รองรับการจัดกลาง)
- `JScrollPane`: เลื่อนดูข้อความ
- `JButton`: ปุ่มเมนู 4 ปุ่ม
- `JPanel`: กลุ่มปุ่ม (GridLayout 4×1)
- `JOptionPane`: Dialog รับข้อมูล/แสดงข้อความ
- `JTextField`: ช่องกรอกข้อมูล

#### 📐 Layout ที่ใช้:
```
┌──────────────────────────────────────┐
│     SimpleBookingGUI Window          │
│  (BorderLayout)                      │
├──────────────────────────┬───────────┤
│                          │  Button   │
│      JTextPane           │  Panel    │
│    (CENTER)              │  (EAST)   │
│  - แสดงข้อความ           │           │
│  - จัดกลางทุกบรรทัด       │  ปุ่ม 1   │
│  - ใน JScrollPane        │  ปุ่ม 2   │
│                          │  ปุ่ม 3   │
│                          │  ปุ่ม 4   │
└──────────────────────────┴───────────┘
```

#### 💡 จุดเด่นของการออกแบบ:
- **Event-Driven Programming**: ใช้ Lambda Expression สำหรับ Event Listeners
- **Input Validation**: ตรวจสอบข้อมูลครบถ้วนก่อนประมวลผล
- **Error Handling**: จับและแสดง Error ที่เกิดขึ้น
- **User-Friendly Dialogs**: ใช้ `JOptionPane` สำหรับการโต้ตอบ
- **Payment Retry Loop**: ให้ผู้ใช้กรอกจำนวนเงินซ้ำได้ถ้ากรอกผิด
- **Date Format**: รองรับรูปแบบวันที่ dd/MM/yyyy (เช่น 15/12/2025)
- **System Look and Feel**: ใช้ธีมของระบบปฏิบัติการ

---

## 🔄 ลำดับการทำงานของระบบ (Workflow)

### 📌 Sequence 1: ดูสถานะบ้าน
```
User → GUI → BookingSystem → House List
     ← GUI ← BookingSystem ← (แสดงสถานะทั้งหมด)
```

### 📌 Sequence 2: จองบ้าน (สมบูรณ์)
```
1. User เลือก "จองบ้านพัก"
   ↓
2. GUI แสดงสถานะบ้าน
   ↓
3. User เลือกหมายเลขบ้าน
   ↓
4. GUI → BookingSystem.findHouseByNumber()
   ↓
5. GUI แสดงฟอร์มกรอกข้อมูล (วันที่, ชื่อ, เบอร์, อีเมล)
   ↓
6. User กรอกข้อมูล + กด OK
   ↓
7. GUI validate ข้อมูล (ครบถ้วน + วันที่ถูกต้อง)
   ↓
8. GUI → BookingSystem.isHouseAvailableForDates()
   ├─ false → แสดง Error
   └─ true → ต่อไป
   ↓
9. GUI สร้าง Customer object
   ↓
10. GUI → BookingSystem.createBooking()
    ↓
11. Booking object ถูกสร้าง (ยังไม่บันทึกในระบบ)
    ↓
12. GUI → processPayment(booking)
    ↓
13. แสดง Dialog ให้กรอกจำนวนเงิน
    ↓
14. User กรอกจำนวนเงิน
    ↓
15. GUI → BookingSystem.processPayment(booking, amount)
    ├─ จำนวนเงินไม่ถูก → แสดง Error + วนกลับไป 13
    └─ จำนวนเงินถูก ↓
    ↓
16. BookingSystem.processPayment():
    - ตั้งค่า booking.setPaid(true)
    - ตั้งค่า house.setAvailable(false)
    - เรียก confirmBooking(booking) → เพิ่มเข้า bookings list
    ↓
17. GUI แสดงข้อความยืนยันการจองสำเร็จ
    ↓
18. GUI รีเฟรชสถานะบ้าน
```

### 📌 Sequence 3: ดูรายการจอง
```
User → GUI → BookingSystem.getBookings()
     ← GUI ← BookingSystem ← Booking List
     ← GUI (แสดงรายละเอียดทุกรายการ)
```

---

## 🎯 หลักการ OOP ที่ใช้

### 1. **Encapsulation (การห่อหุ้มข้อมูล)**
- ทุกคลาสใช้ `private` attributes
- เข้าถึงข้อมูลผ่าน public getters/setters เท่านั้น
- ซ่อน implementation details (เช่น `calculateTotalPrice()` เป็น private)

### 2. **Abstraction (การนามธรรม)**
- แยก business logic (`BookingSystem`) ออกจาก presentation (`SimpleBookingGUI`)
- User ไม่จำเป็นต้องรู้ว่าระบบทำงานอย่างไร (แค่กดปุ่มและกรอกข้อมูล)

### 3. **Information Hiding**
- ซ่อน `bookingCounter` เป็น static private
- ซ่อนการคำนวณราคาในคลาส `Booking`

### 4. **Composition (การประกอบ)**
- `Booking` ประกอบด้วย `House` และ `Customer` (Has-A Relationship)
- `BookingSystem` ประกอบด้วย `List<House>` และ `List<Booking>`
- `SimpleBookingGUI` ประกอบด้วย `BookingSystem`

```
BookingSystem
    ├── houses: List<House>
    └── bookings: List<Booking>
                      ├── house: House
                      └── customer: Customer
```

### 5. **Single Responsibility Principle (SRP)**
- `House`: รับผิดชอบข้อมูลบ้าน
- `Customer`: รับผิดชอบข้อมูลลูกค้า
- `Booking`: รับผิดชอบข้อมูลการจอง
- `BookingSystem`: รับผิดชอบ business logic
- `SimpleBookingGUI`: รับผิดชอบ user interface

---

## 📊 Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                        User Input                           │
└───────────────────────────┬─────────────────────────────────┘
                            ▼
                ┌───────────────────────┐
                │  SimpleBookingGUI     │
                │  (Presentation Layer) │
                └───────────┬───────────┘
                            │
                            ▼
                ┌───────────────────────┐
                │   BookingSystem       │
                │   (Business Logic)    │
                └───────────┬───────────┘
                            │
                ┌───────────┼───────────┐
                ▼           ▼           ▼
         ┌─────────┐ ┌──────────┐ ┌─────────┐
         │  House  │ │ Customer │ │ Booking │
         │  (Data) │ │  (Data)  │ │ (Data)  │
         └─────────┘ └──────────┘ └─────────┘
```

---

## 🛠️ เทคโนโลยีที่ใช้

| เทคโนโลยี | เวอร์ชัน | จุดประสงค์ |
|-----------|---------|-----------|
| Java | 8+ | ภาษาหลัก |
| Java Swing | Built-in | GUI Framework |
| Java Time API | Java 8+ | จัดการวันที่ (`LocalDate`) |
| ArrayList | Java Collections | เก็บ lists |
| DateTimeFormatter | Java 8+ | จัดรูปแบบวันที่ |

---

## 📁 โครงสร้างไฟล์

```
projectcs318/
├── Booking.java              # คลาสข้อมูลการจอง
├── Booking.class             # Compiled bytecode
├── BookingSystem.java        # คลาสควบคุมระบบหลัก
├── BookingSystem.class       # Compiled bytecode
├── Customer.java             # คลาสข้อมูลลูกค้า
├── Customer.class            # Compiled bytecode
├── House.java                # คลาสข้อมูลบ้านพัก
├── House.class               # Compiled bytecode
├── SimpleBookingGUI.java     # คลาส GUI
├── SimpleBookingGUI.class    # Compiled bytecode
└── README.md                 # เอกสารนี้
```

---

## 🚀 วิธีการรันโปรแกรม

### วิธีที่ 1: รันผ่าน Command Line
```bash
# Compile (ถ้ายังไม่ได้ compile)
javac *.java

# Run
java SimpleBookingGUI
```

### วิธีที่ 2: รันผ่าน IDE
- เปิดไฟล์ `SimpleBookingGUI.java` ใน IDE
- กด Run หรือ F5

---

## ✨ ฟีเจอร์หลัก

### ✅ ดูสถานะบ้าน
- แสดงบ้านทั้ง 10 หลัง
- แสดงสถานะว่าง/ไม่ว่าง
- แสดงราคาต่อวัน

### ✅ จองบ้าน
- เลือกบ้านจากหมายเลข 1-10
- กรอกวันที่เข้าพัก-คืนบ้าน (dd/MM/yyyy)
- กรอกข้อมูลลูกค้า (ชื่อ, เบอร์, อีเมล)
- ระบบตรวจสอบว่าบ้านว่างในช่วงเวลาที่เลือก
- คำนวณราคาอัตโนมัติ
- ระบบชำระเงิน (ต้องจ่ายตรงจำนวน)
- ยืนยันการจองหลังชำระเงินสำเร็จ

### ✅ ดูรายการจอง
- แสดงการจองทั้งหมดที่สำเร็จแล้ว
- รายละเอียด: หมายเลขการจอง, บ้าน, ลูกค้า, วันที่, ราคา, สถานะ

### ✅ ออกจากโปรแกรม
- ปิดโปรแกรมทันที

---

## 🔒 การตรวจสอบความถูกต้อง (Validation)

### 1. การตรวจสอบหมายเลขบ้าน
- ต้องเป็นตัวเลข 1-10 เท่านั้น

### 2. การตรวจสอบวันที่
- รูปแบบต้องเป็น dd/MM/yyyy (เช่น 15/12/2025)
- วันที่คืนบ้านต้องมาหลังวันที่เข้าพัก

### 3. การตรวจสอบข้อมูลลูกค้า
- ชื่อ, เบอร์โทร, อีเมล ต้องไม่ว่าง

### 4. การตรวจสอบความว่างของบ้าน
- ตรวจสถานะบ้าน (`isAvailable`)
- ตรวจการจองที่ทับซ้อน (Date Overlap Detection)

### 5. การตรวจสอบการชำระเงิน
- จำนวนเงินต้องเป็นตัวเลข
- จำนวนเงินต้องตรงกับราคารวมพอดี

---

## 🎓 ตัวอย่าง Use Case

### Scenario: จองบ้านหมายเลข 5
```
1. User: กดปุ่ม "จองบ้านพัก"
2. System: แสดงสถานะบ้านทั้งหมด
3. User: พิมพ์ "5" → กด OK
4. System: แสดงฟอร์มกรอกข้อมูล
5. User: กรอกข้อมูล
   - วันที่เข้าพัก: 15/12/2025
   - วันที่คืนบ้าน: 18/12/2025
   - ชื่อ: สมชาย ใจดี
   - เบอร์: 081-234-5678
   - อีเมล: somchai@example.com
6. System: ตรวจสอบบ้านว่าง → ผ่าน
7. System: คำนวณราคา
   - จำนวนวัน: 3 วัน
   - ราคาต่อวัน: 1,500 บาท
   - ราคารวม: 4,500 บาท
8. System: แสดงหน้าชำระเงิน
9. User: พิมพ์ "4500" → กด OK
10. System: ตรวจสอบจำนวนเงิน → ถูกต้อง
11. System: 
    - ตั้งสถานะชำระเงิน → true
    - อัพเดทสถานะบ้าน → ไม่ว่าง
    - บันทึกการจองเข้าระบบ
12. System: แสดงข้อความยืนยัน
    "จองสำเร็จ! หมายเลขการจอง: 1"
```

---

## 🐛 การจัดการข้อผิดพลาด

| ข้อผิดพลาด | วิธีแก้ไข |
|-----------|----------|
| หมายเลขบ้านไม่ถูกต้อง | แสดง Error Dialog "กรุณาเลือก 1-10 เท่านั้น" |
| รูปแบบวันที่ผิด | แสดง Error Dialog "กรุณาใช้รูปแบบ วว/ดด/ปปปป" |
| ข้อมูลไม่ครบ | แสดง Error Dialog "กรุณากรอกข้อมูลให้ครบถ้วน" |
| วันที่ไม่ถูกต้อง | แสดง Error Dialog "วันที่คืนบ้านต้องหลังจากวันที่เข้าพัก" |
| บ้านไม่ว่าง | แสดง Error Dialog "ขออภัย บ้านไม่ว่างในช่วงเวลาที่เลือก" |
| จำนวนเงินไม่ถูกต้อง | แสดง Error + ให้กรอกใหม่ได้ |
| กรอกไม่ใช่ตัวเลข | แสดง Error "กรุณาใส่ตัวเลข" |

---

## 📈 ข้อดีของการออกแบบนี้

### ✅ Maintainability (บำรุงรักษาง่าย)
- แยกส่วนชัดเจน แก้ไข 1 ส่วนไม่กระทบส่วนอื่น

### ✅ Scalability (ขยายได้)
- เพิ่มบ้านได้ง่าย (แก้ค่า `TOTAL_HOUSES`)
- เพิ่มฟีเจอร์ใหม่ได้ (เช่น ยกเลิกการจอง, คืนเงิน)

### ✅ Reusability (นำกลับมาใช้ได้)
- คลาส `House`, `Customer`, `Booking` ใช้ในโปรเจคอื่นได้

### ✅ Testability (ทดสอบได้ง่าย)
- แต่ละคลาสทดสอบแยกอิสระได้
- Business logic แยกจาก GUI

### ✅ User-Friendly
- GUI ใช้งานง่าย
- มี Error Handling ครบถ้วน
- มี Retry mechanism สำหรับชำระเงิน

---

## 🔮 แนวทางพัฒนาต่อ (Future Enhancements)

### 📝 ฟีเจอร์เพิ่มเติม
- [ ] ยกเลิกการจอง (Cancellation)
- [ ] แก้ไขการจอง (Edit Booking)
- [ ] คืนเงิน (Refund)
- [ ] รายงานสรุป (Report/Statistics)
- [ ] ค้นหาบ้านตามราคา (Filter by Price)
- [ ] บันทึกข้อมูลลงไฟล์ (File I/O หรือ Database)
- [ ] ระบบ Login สำหรับ Admin/User
- [ ] ส่วนลดสำหรับการจองนานๆ (Discount)

### 🛠️ การปรับปรุงเทคนิค
- [ ] ใช้ Database (MySQL/SQLite) แทน ArrayList
- [ ] เพิ่ม Unit Tests
- [ ] ใช้ Design Patterns เพิ่มเติม (Factory, Observer)
- [ ] รองรับหลายภาษา (Internationalization)
- [ ] Export รายงานเป็น PDF/Excel

---

## 👨‍💻 ผู้พัฒนา

**Project:** ระบบจองบ้านพัก  
**Course:** CS318  
**Language:** Java  
**GUI Framework:** Java Swing  

---

## 📄 License

This project is created for educational purposes.

---

## 📞 Contact

หากมีคำถามหรือข้อเสนอแนะ สามารถติดต่อได้ตามช่องทางที่กำหนด

---

**สิ้นสุดเอกสาร README.md**

*อัพเดทล่าสุด: 2 ธันวาคม 2025*
