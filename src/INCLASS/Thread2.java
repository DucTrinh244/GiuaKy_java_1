package INCLASS;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;

public class Thread2 extends Thread {
    private String dateOfBirth;
    private String hashBirth;

    public Thread2(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public void run() {
        // Tính toán tuổi
        LocalDate dob = LocalDate.parse(dateOfBirth);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dob, currentDate);
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

//        System.out.println("Received dateOfBirth: " + dateOfBirth);
//        System.out.println("Age: " + years + " years, " + months + " months, " + days + " days");

        // Mã hóa tuổi thành chữ số
        String ageString = years + "" + months + "" + days;
        hashBirth = null;
        try {
            hashBirth = HashBirth(ageString);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Hash age: " + hashBirth);
    }

    // Mã hóa tuổi thành SHA - 256
    public String HashBirth(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(input.getBytes());

        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashedBytes) {
            stringBuilder.append(String.format("%02x", b));
        }

        return stringBuilder.toString();
    }

    public String getHashAge() {
        return hashBirth;
    }
}

//    public static void main(String[] args) {
//        // Tạo một đối tượng Thread2 với ngày sinh cụ thể
//        String dateOfBirth = "2005-04-24"; // Thay đổi ngày sinh tùy ý
//        Thread2 thread2 = new Thread2(dateOfBirth);
//
//        // Gọi phương thức start() để bắt đầu chạy thread
//        thread2.start();
//    }

