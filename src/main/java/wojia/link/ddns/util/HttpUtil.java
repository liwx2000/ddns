package wojia.link.ddns.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@UtilityClass
public class HttpUtil {

    @SneakyThrows
    public String get(String url) {
        Response response = Request.get(url).execute();
        return response.returnContent().asString();
    }

}
