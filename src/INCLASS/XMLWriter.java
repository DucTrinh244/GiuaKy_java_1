package INCLASS;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLWriter {
    private static final String FILE_PATH = "kq.xml";

    public void writeResultToFile(List<Thread2> thread2List, List<Thread3> thread3List) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(FILE_PATH);

            // Ghi tiêu đề của file XML
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<results>\n");

            // Lặp qua danh sách Thread2 và ghi thông tin vào file XML
            for (Thread2 thread2 : thread2List) {
                String age = thread2.getHashAge(); // Giả sử Thread2 có một phương thức để lấy tuổi
                writer.write("<age>" + age + "</age>\n");
            }

            // Lặp qua danh sách Thread3 và ghi thông tin vào file XML
            for (Thread3 thread3 : thread3List) {
                int sum = thread3.Sumage(); // Giả sử Thread3 có một phương thức để lấy tổng
                boolean isPrime = thread3.isDigitThread3(); // Giả sử Thread3 có một phương thức để kiểm tra số nguyên tố
                writer.write("<sum>" + sum + "</sum>\n");
                writer.write("<isPrime>" + isPrime + "</isPrime>\n");
            }

            // Kết thúc file XML
            writer.write("</results>");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
