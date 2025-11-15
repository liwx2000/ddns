package wojia.link.ddns.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/11/11 <br/>
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DdnsConfig {

    /**
     * 通过接口查询配置信息时的秘钥
     * 只有秘钥与配置文件中的秘钥一致时，才会返回配置信息
     * 避免因为接口开放至外网后，被恶意调用获取阿里云账号的AK/SK
     */
    String token;

    /**
     * 需要更新绑定关系的域名
     */
    String domain;

    /**
     * 主机记录（RR），如 @、www、或三级子域如 a.b。
     * 为空或未设置时，默认使用 @ 记录。
     */
    String rr;

    /**
     * 域名所在的region
     */
    String region;

    /**
     * 账号AK
     */
    String accessKeyId;

    /**
     * 账号SK
     */
    String accessKeySecret;

    /**
     * 获取公网IP的主通道
     */
    String mainChannel;

    /**
     * 获取公网IP的备用通道
     */
    String fallbackChannel;

}
