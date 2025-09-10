package cn.handsome.powershell;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * todo
 *
 * @author shay
 * @date 2025/3/22
 **/
@Slf4j
public class WinRMClient implements AutoCloseable {
    private static final String SOAP_TEMPLATE =
            "<s:Envelope xmlns:s='http://www.w3.org/2003/05/soap-envelope'>" +
                    "<s:Header><wsa:Action s:mustUnderstand='true'>http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Command</wsa:Action>" +
                    "<wsman:Locale xmlns:wsman='http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd' s:mustUnderstand='false' xml:lang='en-US'/>" +
                    "</s:Header><s:Body><rsp:CommandLine xmlns:rsp='http://schemas.microsoft.com/wbem/wsman/1/windows/shell'>" +
                    "<rsp:Command>%s</rsp:Command></rsp:CommandLine></s:Body></s:Envelope>";

    private final PowerShellServer server;
    private final CloseableHttpClient httpClient;

    public WinRMClient(PowerShellServer server, HttpClientConnectionManager connManager) {
        this.server = server;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(server.getConnectionTimeout())
                .setSocketTimeout(server.getSocketTimeout())
                .build();

        this.httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(config)
                .build();
    }

    public String executeCommand(String command) throws Exception {
        HttpPost post = new HttpPost(server.getEndpoint());
        post.setHeader(HttpHeaders.AUTHORIZATION, getBasicAuthHeader());
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/soap+xml;charset=UTF-8");

        String content =
                String.format(SOAP_TEMPLATE,
                        Base64.getEncoder().encodeToString(command.getBytes(StandardCharsets.UTF_16LE))
                );
        StringEntity entity = new StringEntity(content, StandardCharsets.UTF_8);
        post.setEntity(entity);
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            log.error(String.format("执行 command 异常：%s", command), e);
            throw e;
        }
    }

    private String getBasicAuthHeader() {
        String credentials = server.getUsername() + ":" + server.getPassword();
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void close() throws Exception {
        this.httpClient.close();
    }
}
