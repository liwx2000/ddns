package wojia.link.ddns.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.profile.DefaultProfile;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import wojia.link.ddns.model.DdnsConfig;
import wojia.link.ddns.util.Log;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import static wojia.link.ddns.util.RetryUtil.retryIfThrowException;
import static wojia.link.ddns.util.StringUtil.isNullOrEmpty;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/6/17 <br/>
 */
@UtilityClass
public class DDNS {

    private static final Logger logger = Log.getLogger();

    private static final AtomicBoolean dryRun = new AtomicBoolean(false);

    static {
        String enable = System.getProperty("dryRun");
        dryRun.set(null != enable);
    }

    @SneakyThrows
    public static void sync() {
        DdnsConfig config = DdnsConfigLoader.loadConfig();

        // 设置鉴权参数，初始化客户端
        DefaultProfile profile = DefaultProfile.getProfile(config.getRegion(), config.getAccessKeyId(), config.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        // 获取域名记录
        DescribeDomainRecordsResponse.Record record = retryIfThrowException(
                () -> getDomainRecord(client, config),
                5,
                3000L,
                e -> logger.log(Level.SEVERE, "Cannot domain record.", e)
        );

        // 更新域名记录
        retryIfThrowException(
                () -> updateDomainRecord(client, record, config),
                5,
                3000L,
                e -> logger.log(Level.SEVERE, "Update domain record failed.", e)
        );
    }

    @SneakyThrows
    private static void updateDomainRecord(IAcsClient client, DescribeDomainRecordsResponse.Record record, DdnsConfig config) {
        // 记录值
        String recordsValue = record.getValue();
        // 当前主机公网IP
        String currentHostIP = IpFetcher.fetch(config);

        if (!Objects.equals(recordsValue, currentHostIP)) {
            logger.info("[UPDATE] Current IP: " + currentHostIP + ", record: " + recordsValue);

            if (dryRun.get()) {
                logger.info("[SKIP] Dry run.");
                return;
            }

            // 修改解析记录
            UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
            // 主机记录
            updateDomainRecordRequest.setRR("@");
            // 记录ID
            updateDomainRecordRequest.setRecordId(record.getRecordId());
            // 将主机记录值改为当前主机IP
            updateDomainRecordRequest.setValue(currentHostIP);
            // 解析记录类型
            updateDomainRecordRequest.setType("A");

            // 修改解析记录
            UpdateDomainRecordResponse updateDomainRecordResponse = client.getAcsResponse(updateDomainRecordRequest);
            if (null == updateDomainRecordResponse || isNullOrEmpty(updateDomainRecordResponse.getRecordId())) {
                throw new RuntimeException("Update domain record failed.");
            }

            return;
        }

        logger.info("[SKIP] Current IP: " + currentHostIP + ", record: " + recordsValue);
    }

    @SneakyThrows
    private static DescribeDomainRecordsResponse.Record getDomainRecord(IAcsClient client, DdnsConfig config) {
        // 查询指定二级域名的最新解析记录
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
        // 主域名
        describeDomainRecordsRequest.setDomainName(config.getDomain());
        // 主机记录
        describeDomainRecordsRequest.setRRKeyWord("@");
        // 解析记录类型
        describeDomainRecordsRequest.setType("A");

        // 获取主域名的所有解析记录列表
        DescribeDomainRecordsResponse describeDomainRecordsResponse = client.getAcsResponse(describeDomainRecordsRequest);
        if (null == describeDomainRecordsResponse || describeDomainRecordsResponse.getTotalCount() <= 0) {
            throw new RuntimeException("Cannot find any domain record.");
        }

        List<DescribeDomainRecordsResponse.Record> domainRecords = describeDomainRecordsResponse.getDomainRecords();

        // 最新的一条解析记录
        if (null != domainRecords && !domainRecords.isEmpty()) {
            return domainRecords.get(0);
        }

        throw new RuntimeException("Domain record not exists.");
    }

}