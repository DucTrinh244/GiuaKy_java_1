package Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {
    private static final String FILE_PATH = "kq.xml";
    private static final String AES_ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "encryptionKey12345"; // Key for encryption, you should use a secure key generation method.

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Student> students = new ArrayList<>();
        XMLWriter xmlWriter = new XMLWriter();

        executor.execute(() -> {
            try {
                readStudentFile(students);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        while (!executor.isTerminated()) {}

        try {
            xmlWriter.writeResultToFile(students);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read and decode the output
        try {
            List<Student> decodedStudents = xmlWriter.readResultFromFile();
            for (Student student : decodedStudents) {
                System.out.println(student);
            }
        } catch (IOException | NoSuchAlgorithmException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        System.out.println("Sau khi mã hóa dữ liệu ");
        try {
            List<Student> decodedStudentss = xmlWriter.readResultFromFile1();
            for (Student student : decodedStudentss) {
                System.out.println(student);
            }
        } catch (IOException | NoSuchAlgorithmException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static void readStudentFile(List<Student> students)
            throws ParserConfigurationException, SAXException, IOException {
        File file = new File("student.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        NodeList studentList = document.getElementsByTagName("student");
        for (int i = 0; i < studentList.getLength(); i++) {
            Element studentElement = (Element) studentList.item(i);
            String id = studentElement.getAttribute("id");
            String name = studentElement.getElementsByTagName("name").item(0).getTextContent();
            String address = studentElement.getElementsByTagName("address").item(0).getTextContent();
            String dateOfBirth = studentElement.getElementsByTagName("dateOfBirth").item(0).getTextContent();

            Student student = new Student(id, name, address, dateOfBirth);
            students.add(student);
        }
    }

    static class Student {
        private String id;
        private String name;
        private String address;
        private String dateOfBirth;
        private String hashedAge;
        private int sumOfDigits;
        private boolean isPrime;

        public Student(String id, String name, String address, String dateOfBirth) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.dateOfBirth = dateOfBirth;
            calculateAge();
            calculateSumOfDigits();
            checkPrime();
        }

        private void calculateAge() {
            LocalDate dob = LocalDate.parse(dateOfBirth);
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(dob, currentDate);
            int years = period.getYears();
            int months = period.getMonths();
            int days = period.getDays();
            String ageString = years + "" + months + "" + days;

            this.hashedAge = HashBirth(ageString);
        }

        private void calculateSumOfDigits() {
            int sum = 0;
            for (int i = 0; i < dateOfBirth.length(); i++) {
                char c = dateOfBirth.charAt(i);
                if (Character.isDigit(c)) {
                    sum += Character.getNumericValue(c);
                }
            }
            this.sumOfDigits = sum;
        }

        public void setHashedAge(String hashedAge) {
            this.hashedAge = hashedAge;
        }

        public void setSumOfDigits(int sumOfDigits) {
            this.sumOfDigits = sumOfDigits;
        }

        public void setPrime(boolean isPrime) {
            this.isPrime = isPrime;
        }
        private void checkPrime() {
            if (sumOfDigits <= 1) {
                isPrime = false;
                return;
            }
            for (int i = 2; i <= Math.sqrt(sumOfDigits); i++) {
                if (sumOfDigits % i == 0) {
                    isPrime = false;
                    return;
                }
            }
            isPrime = true;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public String getHashedAge() {
            return hashedAge;
        }

        public int getSumOfDigits() {
            return sumOfDigits;
        }

        public boolean isPrime() {
            return isPrime;
        }

        @Override
        public String toString() {
            return "Student(id=" + id + ", name=" + name + ", address=" + address + ", dateOfBirth=" + dateOfBirth
                    + ", hashedAge=" + hashedAge + ", sumOfDigits=" + sumOfDigits + ", isPrime=" + isPrime + ")";
        }
    }

    static class XMLWriter {
        public void writeResultToFile(List<Student> students) throws IOException {
            FileWriter writer = null;
            try {
                writer = new FileWriter(FILE_PATH);

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<results>\n");

                for (Student student : students) {
                    writer.write("<student>\n");
                    writer.write("<id>" + student.getId() + "</id>\n");
                    writer.write("<name>" + student.getName() + "</name>\n");
                    writer.write("<address>" + student.getAddress() + "</address>\n");
                    writer.write("<dateOfBirth>" + student.getDateOfBirth() + "</dateOfBirth>\n");
                    writer.write("<hashedAge>" + student.getHashedAge() + "</hashedAge>\n");
                    writer.write("<sumOfDigits>" + student.getSumOfDigits() + "</sumOfDigits>\n");
                    writer.write("<isPrime>" + student.isPrime() + "</isPrime>\n");
                    writer.write("</student>\n");
                }

                writer.write("</results>");
            } finally {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            }
        }

        public List<Student> readResultFromFile() throws IOException, NoSuchAlgorithmException, ParserConfigurationException, SAXException {
            List<Student> students = new ArrayList<>();
            File file = new File(FILE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList studentList = document.getElementsByTagName("student");
            for (int i = 0; i < studentList.getLength(); i++) {
                Element studentElement = (Element) studentList.item(i);
                String id = studentElement.getElementsByTagName("id").item(0).getTextContent();
                String name = studentElement.getElementsByTagName("name").item(0).getTextContent();
                String address = studentElement.getElementsByTagName("address").item(0).getTextContent();
                String dateOfBirth = studentElement.getElementsByTagName("dateOfBirth").item(0).getTextContent();
                String hashedAge = studentElement.getElementsByTagName("hashedAge").item(0).getTextContent();
                int sumOfDigits = Integer.parseInt(studentElement.getElementsByTagName("sumOfDigits").item(0).getTextContent());
                boolean isPrime = Boolean.parseBoolean(studentElement.getElementsByTagName("isPrime").item(0).getTextContent());

                Student student = new Student(id, name, address, dateOfBirth);
                student.setHashedAge(hashedAge);
                student.setSumOfDigits(sumOfDigits);
                student.setPrime(isPrime);

                students.add(student);
            }

            return students;
        }
        public List<Student> readResultFromFile1() throws IOException, NoSuchAlgorithmException, ParserConfigurationException, SAXException {
            List<Student> students = new ArrayList<>();
            File file = new File(FILE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);

                NodeList studentList = document.getElementsByTagName("student");
                for (int i = 0; i < studentList.getLength(); i++) {
                    Element studentElement = (Element) studentList.item(i);
                    String id = studentElement.getElementsByTagName("id").item(0).getTextContent();
                    String name = studentElement.getElementsByTagName("name").item(0).getTextContent();
                    String address = studentElement.getElementsByTagName("address").item(0).getTextContent();
                    String dateOfBirth = studentElement.getElementsByTagName("dateOfBirth").item(0).getTextContent();
                    String encryptedHashedAge = studentElement.getElementsByTagName("hashedAge").item(0).getTextContent();
                    String hashedAge = decryptData(encryptedHashedAge);
                    int sumOfDigits = Integer.parseInt(studentElement.getElementsByTagName("sumOfDigits").item(0).getTextContent());
                    boolean isPrime = Boolean.parseBoolean(studentElement.getElementsByTagName("isPrime").item(0).getTextContent());

                    Student student = new Student(id, name, address, dateOfBirth);
                    student.setHashedAge(hashedAge);
                    student.setSumOfDigits(sumOfDigits);
                    student.setPrime(isPrime);

                    students.add(student);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return students;
        }
    }


//    private static String HashBirth(String input) throws NoSuchAlgorithmException {
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] hashedBytes = digest.digest(input.getBytes());
//
//        StringBuilder stringBuilder = new StringBuilder();
//        for (byte b : hashedBytes) {
//            stringBuilder.append(String.format("%02x", b));
//        }
//
//        return stringBuilder.toString();
//    }
    // mã hóa dữ liệu
    public static String HashBirth(String data) {
        try {
            // Chỉ lấy 16 ký tự đầu của khóa nếu khóa dài hơn 16 ký tự
            byte[] keyBytes = ENCRYPTION_KEY.substring(0, 16).getBytes();
            SecretKeySpec key = new SecretKeySpec(keyBytes, AES_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // giải mã hóa dữ liệu
    public static String decryptData(String encryptedData) {
        try {
            byte[] keyBytes = ENCRYPTION_KEY.substring(0, 16).getBytes();
            SecretKeySpec key = new SecretKeySpec(keyBytes, AES_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
