package INCLASS;

public class Thread3 extends Thread{
    private String dateOfBirth;// truyển birt
    private boolean isDigit;
    private int sum;
    public Thread3(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    @Override
    public void run() {
        // Tính tổng các chữ số trong ngày tháng năm sinh
         sum = calculateDigitSum(dateOfBirth);

   //     System.out.println("Received dateOfBirth: " + dateOfBirth);
        System.out.println("Sum of digits: " + sum);

        // Kiểm tra xem tổng có phải là số nguyên tố không
        boolean isPrime = isPrime(sum);

        System.out.println("Is the sum of digits prime? " + isPrime);
    }

    // Tính tổng các chữ số trong một chuỗi
    private int calculateDigitSum(String input) {
        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                sum += Character.getNumericValue(c);
            }
        }
//        System.out.println(sum);
        return sum;
    }
    // check isprime
    private boolean isPrime(int num) {
        if (num <= 1) {
            isDigit=false;
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                isDigit=false;
                return false;
            }
        }
        isDigit=true;
        return true;
    }
    public boolean isDigitThread3(){
        return isDigit;
    }
    public int Sumage(){
        return sum;
    }
    public static void main(String[] args) {
        String dateOfBirth = "2000-01-02";
        Thread3 thread3 = new Thread3(dateOfBirth);
        thread3.start();
    }
}
