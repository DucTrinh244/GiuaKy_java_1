package INCLASS;

public class ShareData {

    private String hashedAge;
    private int sum;
    private boolean isPrime;

    // Các phương thức getter và setter cho các biến trạng thái

    public synchronized void setHashedAge(String hashedAge) {
        this.hashedAge = hashedAge;
    }

    public synchronized void setSum(int sum) {
        this.sum = sum;
    }

    public synchronized void setIsPrime(boolean isPrime) {
        this.isPrime = isPrime;
    }

    // Các phương thức getter

    public synchronized String getHashedAge() {
        return hashedAge;
    }

    public synchronized int getSum() {
        return sum;
    }

    public synchronized boolean isPrime() {
        return isPrime;
    }
}
