package wojia.link.ddns.controller;

import org.springframework.web.bind.annotation.*;
import wojia.link.ddns.model.DdnsConfig;
import wojia.link.ddns.model.DdnsStatus;
import wojia.link.ddns.service.DdnsConfigLoader;
import wojia.link.ddns.service.DdnsService;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/6/18 <br/>
 */
@RestController
@RequestMapping("/ddns")
public class DdnsController {

    @Resource
    private DdnsService ddnsService;

    @GetMapping("/start")
    public String start() {
        return ddnsService.start();
    }

    @GetMapping("/stop")
    public String stop() {
        return ddnsService.stop();
    }

    @GetMapping("/status")
    public @ResponseBody DdnsStatus status() {
        return ddnsService.status();
    }

    @GetMapping("/config")
    public @ResponseBody DdnsConfig config(@RequestParam("token") String token) {
        DdnsConfig config = DdnsConfigLoader.loadConfig();

        if (!Objects.equals(config.getToken(), token)) {
            return null;
        }

        DdnsConfig result = new DdnsConfig();
        result.setDomain(config.getDomain());
        result.setRegion(config.getRegion());
        result.setAccessKeyId(config.getAccessKeyId());
        result.setAccessKeySecret(config.getAccessKeySecret());
        result.setMainChannel(config.getMainChannel());
        result.setFallbackChannel(config.getFallbackChannel());
        return result;
    }

}
