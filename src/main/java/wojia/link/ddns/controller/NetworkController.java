package wojia.link.ddns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import wojia.link.ddns.model.DdnsResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@RestController
@RequestMapping("/net")
public class NetworkController {

    @GetMapping("/ip")
    public @ResponseBody DdnsResponse getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return DdnsResponse.of(ip);
    }

}
