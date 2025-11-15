package wojia.link.ddns.constant;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@UtilityClass
public class Period {

    public static final long SYNC_IP_PERIOD = TimeUnit.MINUTES.toMillis(15L);

    public static final long REFRESH_CONFIG_PERIOD = TimeUnit.MINUTES.toMillis(1L);

}
