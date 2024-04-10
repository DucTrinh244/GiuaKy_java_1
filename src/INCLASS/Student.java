package INCLASS;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Student {
    private int id;
    private String name;
    private String address;
    private String dateOfBirth;

    public Student(int id, String name, String address, String dateOfBirth) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
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

    public static void main(String[] args) {
        // Tạo danh sách sinh viên
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Hoàng Đức Trình", "Quảng Trị", "2005-04-24"));
        students.add(new Student(2, "Phan Thị Kim Oanh", "Quảng trị", "2005-02-27"));
        students.add(new Student(3, "Nguyễn Thị Tố Trinh", "Quảng Nam", "2005-04-13"));

        // Tạo tệp XML và ghi thông tin sinh viên vào đó
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Tạo một tài liệu mới
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("students");
            doc.appendChild(rootElement);

            // Thêm thông tin sinh viên vào tài liệu
            for (Student student : students) {
                Element studentElement = doc.createElement("student");
                rootElement.appendChild(studentElement);

                // Thêm các thuộc tính
                studentElement.setAttribute("id", String.valueOf(student.getId()));

                // Thêm các phần tử con
                Element nameElement = doc.createElement("name");
                nameElement.appendChild(doc.createTextNode(student.getName()));
                studentElement.appendChild(nameElement);

                Element addressElement = doc.createElement("address");
                addressElement.appendChild(doc.createTextNode(student.getAddress()));
                studentElement.appendChild(addressElement);

                Element dobElement = doc.createElement("dateOfBirth");
                dobElement.appendChild(doc.createTextNode(student.getDateOfBirth()));
                studentElement.appendChild(dobElement);
            }

            // Ghi tài liệu vào tệp XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(new File("student.xml")));

            // Format đầu ra XML
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Ghi tài liệu XML vào tệp
            transformer.transform(source, result);

            System.out.println("Tạo tệp student.xml thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
