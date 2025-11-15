package wojia.link.ddns.service;

import org.springframework.stereotype.Service;
import wojia.link.ddns.constant.Period;
import wojia.link.ddns.constant.State;
import wojia.link.ddns.model.DdnsStatus;
import wojia.link.ddns.util.Log;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/6/18 <br/>
 */
@Service
public class DdnsService {

    private static final Logger logger = Log.getLogger();

    private final Timer timer = new Timer();

    private TimerTask task;

    @PostConstruct
    public void autoStart() {
        start();
    }

    public String start() {
        if (null != task) {
            return "DDNS config is updating. Don't need start again.";
        }

        logger.info("Command >>> start");

        task = new TimerTask() {

            @Override
            public void run() {
                try {
                    DDNS.sync();
                } catch (Throwable e) {
                    logger.log(Level.SEVERE, "Start updating failed.", e);
                }
            }

        };

        timer.scheduleAtFixedRate(task, 0, Period.SYNC_IP_PERIOD);

        DdnsStatus.Holder.updateState(State.RUNNING);

        return "Syncing...";
    }

    public String stop() {
        if (null == task) {
            return "DDNS is not updating. Don't need stop.";
        }

        logger.info("Command >>> stop");

        task.cancel();
        task = null;

        DdnsStatus.Holder.updateState(State.STOPPED);

        return "Stopped.";
    }

    public DdnsStatus status() {
        logger.info("Command >>> status");
        return DdnsStatus.Holder.get();
    }

}
