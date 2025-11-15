package wojia.link.ddns.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import wojia.link.ddns.constant.State;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DdnsStatus {

    State state;
    String mainChannel;
    String fallbackChannel;
    Boolean mainChannelUpdateSuccess;
    Boolean fallbackChannelUpdateSuccess;
    LocalDateTime latestUpdateTime;

    public static class Holder {

        private static final AtomicReference<DdnsStatus> INSTANCE = new AtomicReference<>(new DdnsStatus());

        public static DdnsStatus get() {
            return INSTANCE.get();
        }

        public static void updateState(State state) {
            INSTANCE.get().setState(state);
        }

        public static void updateChannel(String main, String fallback) {
            DdnsStatus status = INSTANCE.get();
            status.setMainChannel(main);
            status.setFallbackChannel(fallback);
        }

        public static void refresh(boolean main, boolean success) {
            DdnsStatus status = INSTANCE.get();
            status.setLatestUpdateTime(LocalDateTime.now());
            if (main) {
                status.setMainChannelUpdateSuccess(true);
                status.setFallbackChannelUpdateSuccess(null);
            } else {
                status.setMainChannelUpdateSuccess(false);
                status.setFallbackChannelUpdateSuccess(success);
            }
        }

    }

}
