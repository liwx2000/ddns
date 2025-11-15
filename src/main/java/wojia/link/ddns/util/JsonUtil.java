package wojia.link.ddns.util;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@UtilityClass
public class JsonUtil {

    private static final Gson gson = new Gson();

    public <T> T parse(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public <T> T parse(byte[] json, Class<T> clazz) {
        return gson.fromJson(new String(json), clazz);
    }

    public String toJsonString(Object obj) {
        return gson.toJson(obj);
    }

}
