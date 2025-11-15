package wojia.link.ddns.util;

import lombok.experimental.UtilityClass;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/6/18 <br/>
 */
@UtilityClass
public class StringUtil {

    public static boolean isNullOrEmpty(String string) {
        return null == string || string.isEmpty();
    }

}