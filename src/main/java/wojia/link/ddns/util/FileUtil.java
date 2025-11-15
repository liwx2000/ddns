package wojia.link.ddns.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@UtilityClass
public class FileUtil {

    @SneakyThrows
    public static String readFile(String path, String fileName) {
        try (FileReader fis = new FileReader(new File(path, fileName)); BufferedReader br = new BufferedReader(fis)) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.toString();
        }
    }

}
