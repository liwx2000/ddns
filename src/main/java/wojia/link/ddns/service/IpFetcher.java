package wojia.link.ddns.service;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import wojia.link.ddns.model.DdnsConfig;
import wojia.link.ddns.model.DdnsResponse;
import wojia.link.ddns.model.DdnsStatus;
import wojia.link.ddns.util.HttpUtil;
import wojia.link.ddns.util.JsonUtil;
import wojia.link.ddns.util.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

import static wojia.link.ddns.util.RetryUtil.retryIfThrowException;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@UtilityClass
public class IpFetcher {

    private static final Logger logger = Log.getLogger();

    public static String fetch(DdnsConfig config) {
        String main = config.getMainChannel();
        String fallback = config.getFallbackChannel();

        logger.info("[Fetch IP] " + main + " | " + fallback);

        try {
            String ip = getIp(main);
            DdnsStatus.Holder.refresh(true, true);

            logger.info("[Fetch IP][main] IP: " + ip + " from " + main);

            return ip;
        } catch (Exception e) {
            try {
                String ip = getIp(fallback);
                DdnsStatus.Holder.refresh(false, true);

                logger.info("[Fetch IP][fallback] IP: " + ip + " from " + fallback);

                return ip;
            } catch (Exception ex) {
                DdnsStatus.Holder.refresh(false, false);

                logger.log(Level.SEVERE, "Cannot get current ip.", ex);

                throw ex;
            }
        }
    }

    @SneakyThrows
    public static String getIp(String url) {
        return retryIfThrowException(
                () -> {
                    String result = HttpUtil.get(url);

                    DdnsResponse body = JsonUtil.parse(result, DdnsResponse.class);
                    return body.getIp();
                },
                3,
                3000L,
                e -> logger.log(Level.SEVERE, "Cannot get current ip from " + url, e)
        );
    }

}
