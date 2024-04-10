package INCLASS;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);// dùng thread pool
        List<Thread2> thread2List = new ArrayList<>();
        List<Thread3> thread3List = new ArrayList<>();
        XMLWriter xmlWriter = new XMLWriter();
        // Thread 1: Đọc file student.xml
        executor.execute(() -> {
            try {
                readStudentFile(thread2List, thread3List);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        executor.shutdown();
        // Đợi cho tất cả các luồng hoàn thành trước khi ghi vào file kq.xml
        while (!executor.isTerminated()) {
            // Đợi cho executor kết thúc
        }
        // Ghi vào file kq.xml sử dụng dữ liệu từ biến trạng thái chia sẻ
        // writeToFile(thread2List, thread3List);
        try {
            xmlWriter.writeResultToFile(thread2List,thread3List);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readStudentFile(List<Thread2> thread2List, List<Thread3> thread3List) throws ParserConfigurationException, SAXException, IOException {
        File file = new File("student.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        NodeList studentList = document.getElementsByTagName("student");
        for (int i = 0; i < studentList.getLength(); i++) {
            Element student = (Element) studentList.item(i);
            String id = student.getAttribute("id");
            String name = student.getElementsByTagName("name").item(0).getTextContent();
            String address = student.getElementsByTagName("address").item(0).getTextContent();
            String dateOfBirth = student.getElementsByTagName("dateOfBirth").item(0).getTextContent();

            // Gửi dateOfBirth đến Thread 2 và Thread 3
            Thread2 thread2 = new Thread2(dateOfBirth);
            thread2.start();
            thread2List.add(thread2);

            Thread3 thread3 = new Thread3(dateOfBirth);
            thread3.start();
            thread3List.add(thread3);
        }
    }

}
