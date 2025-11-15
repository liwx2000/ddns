package wojia.link.ddns.service;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import wojia.link.ddns.constant.Period;
import wojia.link.ddns.model.DdnsConfig;
import wojia.link.ddns.model.DdnsStatus;
import wojia.link.ddns.util.FileUtil;
import wojia.link.ddns.util.JsonUtil;
import wojia.link.ddns.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@UtilityClass
public class DdnsConfigLoader {

    private static final Logger logger = Log.getLogger();

    private static final String CONFIG_FILE_NAME = "config.json";

    private static final AtomicReference<DdnsConfig> CONFIG = new AtomicReference<>();

    private static final Timer LOADER = new Timer();

    private static final AtomicBoolean LOADING = new AtomicBoolean(false);

    public static DdnsConfig loadConfig() {
        if (LOADING.compareAndSet(false, true)) {
            DdnsConfig ddnsConfig = load();

            CONFIG.set(ddnsConfig);

            LOADER.scheduleAtFixedRate(
                    new TimerTask() {

                        @Override
                        public void run() {
                            CONFIG.set(load());
                        }

                    },
                    Period.REFRESH_CONFIG_PERIOD,
                    Period.REFRESH_CONFIG_PERIOD
            );

            return ddnsConfig;
        }

        DdnsConfig config = CONFIG.get();
        if (null == config) {
            config = load();
            CONFIG.set(config);
        }
        return config;
    }

    @SneakyThrows
    private static DdnsConfig load() {
        String jarPath = DdnsConfigLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        logger.info("[CONFIG LOADER] Jar package location: " + jarPath);

        logger.info("[CONFIG LOADER] Start to load config from " + jarPath + "/" + CONFIG_FILE_NAME);

        String data = FileUtil.readFile(jarPath, CONFIG_FILE_NAME);

        DdnsConfig config = JsonUtil.parse(data, DdnsConfig.class);

        logger.info("[CONFIG LOADER] Load config succeed. Config: " + JsonUtil.toJsonString(config));

        DdnsStatus.Holder.updateChannel(config.getMainChannel(), config.getFallbackChannel());

        return config;
    }

}
