package cn.handsome.powershell.session;

import cn.handsome.powershell.config.WinRMConfig;
import cn.handsome.powershell.model.PowerShellResult;
import lombok.Getter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author luoyong
 * @date 2025/9/11
 */
public class WinRMSession implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(WinRMSession.class);

    // XML模板常量
    private static final String CREATE_SESSION_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" "
            + "xmlns:wsman=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\">"
            + "  <env:Header>"
            + "    <a:Action env:mustUnderstand=\"true\">http://schemas.xmlsoap.org/ws/2004/09/transfer/Create</a:Action>"
            + "    <a:MessageID>uuid:%s</a:MessageID>"
            + "    <a:To env:mustUnderstand=\"true\">%s</a:To>"
            + "    <a:ReplyTo>"
            + "      <a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address>"
            + "    </a:ReplyTo>"
            + "  </env:Header>"
            + "  <env:Body>"
            + "    <wsman:OptionSet>"
            + "      <wsman:Option Name=\"WINRS_NOPROFILE\">TRUE</wsman:Option>"
            + "      <wsman:Option Name=\"WINRS_CODEPAGE\">65001</wsman:Option>"
            + "    </wsman:OptionSet>"
            + "  </env:Body>"
            + "</env:Envelope>";

    private static final String CREATE_SHELL_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" "
            + "xmlns:wsman=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\">"
            + "  <env:Header>"
            + "    <a:Action env:mustUnderstand=\"true\">http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Create</a:Action>"
            + "    <a:MessageID>uuid:%s</a:MessageID>"
            + "    <a:To env:mustUnderstand=\"true\">%s</a:To>"
            + "    <a:ReplyTo>"
            + "      <a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address>"
            + "    </a:ReplyTo>"
            + "  </env:Header>"
            + "  <env:Body>"
            + "    <wsman:Shell>"
            + "      <wsman:InputStreams>stdin</wsman:InputStreams>"
            + "      <wsman:OutputStreams>stdout stderr</wsman:OutputStreams>"
            + "      <wsman:CommandLine>"
            + "        <wsman:Command>powershell</wsman:Command>"
            + "        <wsman:Arguments>-NoProfile -NonInteractive</wsman:Arguments>"
            + "      </wsman:CommandLine>"
            + "    </wsman:Shell>"
            + "  </env:Body>"
            + "</env:Envelope>";

    private static final String EXECUTE_COMMAND_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" "
            + "xmlns:wsman=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\">"
            + "  <env:Header>"
            + "    <a:Action env:mustUnderstand=\"true\">http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Run</a:Action>"
            + "    <a:MessageID>uuid:%s</a:MessageID>"
            + "    <a:To env:mustUnderstand=\"true\">%s</a:To>"
            + "    <a:ReplyTo>"
            + "      <a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address>"
            + "    </a:ReplyTo>"
            + "    <wsman:ResourceURI env:mustUnderstand=\"true\">http://schemas.microsoft.com/wbem/wsman/1/windows/shell/cmd</wsman:ResourceURI>"
            + "    <wsman:SelectorSet>"
            + "      <wsman:Selector Name=\"ShellId\">%s</wsman:Selector>"
            + "    </wsman:SelectorSet>"
            + "  </env:Header>"
            + "  <env:Body>"
            + "    <wsman:Run>"
            + "      <wsman:Command>%s</wsman:Command>"
            + "      <wsman:CommandId>%s</wsman:CommandId>"
            + "    </wsman:Run>"
            + "  </env:Body>"
            + "</env:Envelope>";

    private static final String GET_COMMAND_OUTPUT_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" "
            + "xmlns:wsman=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\">"
            + "  <env:Header>"
            + "    <a:Action env:mustUnderstand=\"true\">http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Receive</a:Action>"
            + "    <a:MessageID>uuid:%s</a:MessageID>"
            + "    <a:To env:mustUnderstand=\"true\">%s</a:To>"
            + "    <a:ReplyTo>"
            + "      <a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address>"
            + "    </a:ReplyTo>"
            + "    <wsman:ResourceURI env:mustUnderstand=\"true\">http://schemas.microsoft.com/wbem/wsman/1/windows/shell/cmd</wsman:ResourceURI>"
            + "    <wsman:SelectorSet>"
            + "      <wsman:Selector Name=\"ShellId\">%s</wsman:Selector>"
            + "    </wsman:SelectorSet>"
            + "  </env:Header>"
            + "  <env:Body>"
            + "    <wsman:Receive>"
            + "      <wsman:DesiredStream>stdout</wsman:DesiredStream>"
            + "      <wsman:DesiredStream>stderr</wsman:DesiredStream>"
            + "      <wsman:CommandId>%s</wsman:CommandId>"
            + "    </wsman:Receive>"
            + "  </env:Body>"
            + "</env:Envelope>";

    private static final String CLEANUP_COMMAND_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" "
            + "xmlns:wsman=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\">"
            + "  <env:Header>"
            + "    <a:Action env:mustUnderstand=\"true\">http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Signal</a:Action>"
            + "    <a:MessageID>uuid:%s</a:MessageID>"
            + "    <a:To env:mustUnderstand=\"true\">%s</a:To>"
            + "    <a:ReplyTo>"
            + "      <a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address>"
            + "    </a:ReplyTo>"
            + "    <wsman:ResourceURI env:mustUnderstand=\"true\">http://schemas.microsoft.com/wbem/wsman/1/windows/shell/cmd</wsman:ResourceURI>"
            + "    <wsman:SelectorSet>"
            + "      <wsman:Selector Name=\"ShellId\">%s</wsman:Selector>"
            + "    </wsman:SelectorSet>"
            + "  </env:Header>"
            + "  <env:Body>"
            + "    <wsman:Signal CommandId=\"%s\">"
            + "      <wsman:Code>http://schemas.microsoft.com/wbem/wsman/1/windows/shell/signal/terminate</wsman:Code>"
            + "    </wsman:Signal>"
            + "  </env:Body>"
            + "</env:Envelope>";

    private static final String CLOSE_SHELL_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" "
            + "xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" "
            + "xmlns:wsman=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\">"
            + "  <env:Header>"
            + "    <a:Action env:mustUnderstand=\"true\">http://schemas.xmlsoap.org/ws/2004/09/transfer/Delete</a:Action>"
            + "    <a:MessageID>uuid:%s</a:MessageID>"
            + "    <a:To env:mustUnderstand=\"true\">%s</a:To>"
            + "    <a:ReplyTo>"
            + "      <a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address>"
            + "    </a:ReplyTo>"
            + "    <wsman:ResourceURI env:mustUnderstand=\"true\">http://schemas.microsoft.com/wbem/wsman/1/windows/shell/cmd</wsman:ResourceURI>"
            + "    <wsman:SelectorSet>"
            + "      <wsman:Selector Name=\"ShellId\">%s</wsman:Selector>"
            + "    </wsman:SelectorSet>"
            + "  </env:Header>"
            + "  <env:Body/>"
            + "</env:Envelope>";

    private final WinRMConfig config;
    private final CloseableHttpClient httpClient;
    private final String endpoint;
    private String sessionId;
    private String shellId;
    private Instant lastActivityTime;
    /**
     *  检查会话是否已连接
     */
    @Getter
    private boolean connected;

    public WinRMSession(WinRMConfig config) throws Exception {
        this.config = config;
        this.endpoint = config.getEndpoint();
        this.httpClient = createHttpClient();

        initializeSession();
        initializeShell();
        loadExchangeModule();
        loadCustomFunctions(config.getCustomFunctions());

        this.connected = true;
        this.lastActivityTime = Instant.now();
    }

    /**
     * 创建HTTP客户端
     */
    private CloseableHttpClient createHttpClient() {
        // 配置超时参数
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getConnectionTimeoutSeconds() * 1000)
                .setSocketTimeout(config.getConnectionTimeoutSeconds() * 1000)
                .setConnectionRequestTimeout(config.getConnectionTimeoutSeconds() * 1000)
                .build();

        // 配置认证信息
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        String username = config.getDomain() != null ?
                config.getDomain() + "\\" + config.getUsername() :
                config.getUsername();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, config.getPassword())
        );

        // 配置SSL
        try {
            SSLContext sslContext;
            if (config.getSslContext() != null) {
                sslContext = config.getSslContext();
            } else {
                // 创建默认SSL上下文
                sslContext = SSLContexts.createDefault();

                // 如果配置了信任所有证书
                if (config.isTrustAllCertificates()) {
                    sslContext = SSLContexts.custom()
                            .loadTrustMaterial(null, (chain, authType) -> true)
                            .build();
                }
            }

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    // 支持的TLS版本
                    new String[]{"TLSv1.2", "TLSv1.3"},
                    null,
                    config.isVerifyHostname() ?
                            SSLConnectionSocketFactory.getDefaultHostnameVerifier() :
                            NoopHostnameVerifier.INSTANCE
            );

            // 创建HTTP客户端
            return HttpClients.custom()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setSSLSocketFactory(sslSocketFactory)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        } catch (Exception e) {
            logger.error("Failed to create SSL context, using default HTTP client", e);
            // 降级到非SSL配置作为备用方案
            return HttpClients.custom()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        }
    }

    /**
     * 初始化WinRM会话
     */
    private void initializeSession() throws Exception {
        this.sessionId = UUID.randomUUID().toString();

        String requestBody = createCreateSessionRequest();
        String response = executeHttpPost(requestBody);

        // 验证会话创建成功
        if (!response.contains("http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous")) {
            throw new Exception("Failed to create WinRM session");
        }
    }

    /**
     * 初始化PowerShell shell
     */
    private void initializeShell() throws Exception {
        String requestBody = createCreateShellRequest();
        String response = executeHttpPost(requestBody);

        // 解析shell ID
        Document doc = parseXmlResponse(response);
        XPath xpath = XPathFactory.newInstance().newXPath();
        this.shellId = xpath.evaluate(
                "//*[local-name()='ShellId']/text()",
                doc,
                XPathConstants.STRING
        ).toString();

        if (shellId == null || shellId.isEmpty()) {
            throw new Exception("Failed to create PowerShell shell");
        }
    }

    /**
     * 加载Exchange管理模块
     */
    private void loadExchangeModule() throws Exception {
        String exchangeModule = "Add-PSSnapin Microsoft.Exchange.Management.PowerShell.SnapIn;";
        executeCommandInternal(exchangeModule);
    }

    /**
     * 加载自定义函数
     */
    private void loadCustomFunctions(List<String> functions) throws Exception {
        if (functions == null || functions.isEmpty()) {
            return;
        }

        StringBuilder functionScript = new StringBuilder();
        for (String function : functions) {
            functionScript.append(function).append("\n");
        }

        executeCommandInternal(functionScript.toString());
    }

    /**
     * 执行PowerShell命令
     */
    public PowerShellResult executeCommand(String command) throws Exception {
        if (!connected) {
            throw new IllegalStateException("Session is not connected");
        }

        lastActivityTime = Instant.now();
        long startTime = System.currentTimeMillis();

        String output = executeCommandInternal(command);
        long executionTime = System.currentTimeMillis() - startTime;

        boolean hasError = output.contains("Error") ||
                output.contains("Exception") ||
                output.contains("无法找到");

        return new PowerShellResult(command, output, hasError, executionTime);
    }

    /**
     * 执行PowerShell脚本
     */
    public PowerShellResult executeScript(String scriptContent) throws Exception {
        if (!connected) {
            throw new IllegalStateException("Session is not connected");
        }

        lastActivityTime = Instant.now();
        long startTime = System.currentTimeMillis();

        String output = executeCommandInternal(scriptContent);
        long executionTime = System.currentTimeMillis() - startTime;

        boolean hasError = output.contains("Error") ||
                output.contains("Exception") ||
                output.contains("无法找到");

        return new PowerShellResult(scriptContent, output, hasError, executionTime);
    }

    /**
     * 内部命令执行实现
     */
    private String executeCommandInternal(String command) throws Exception {
        // 发送命令
        String commandId = UUID.randomUUID().toString();
        String requestBody = createExecuteCommandRequest(command, commandId);
        executeHttpPost(requestBody);

        // 等待命令完成并获取结果
        return waitForCommandCompletion(commandId);
    }

    /**
     * 等待命令完成并获取结果
     */
    private String waitForCommandCompletion(String commandId) throws Exception {
        StringBuilder output = new StringBuilder();
        boolean commandCompleted = false;
        long startTime = System.currentTimeMillis();
        // 默认命令超时时间为300秒（5分钟），可从配置中获取
        long maxWaitTime = config.getCommandTimeoutSeconds() > 0 ?
                config.getCommandTimeoutSeconds() * 1000L : 300000;

        while (!commandCompleted) {
            // 检查是否超时
            if (System.currentTimeMillis() - startTime > maxWaitTime) {
                // 尝试清理命令
                try {
                    String cleanupRequest = createCleanupCommandRequest(commandId);
                    executeHttpPost(cleanupRequest);
                } catch (Exception e) {
                    // 清理失败不影响主流程
                    logger.warn("Failed to cleanup timed out command", e);
                }
                throw new TimeoutException("Command execution timed out after " + (maxWaitTime / 1000) + " seconds");
            }

            // 检查命令状态
            String requestBody = createGetCommandOutputRequest(commandId);
            String response = executeHttpPost(requestBody);

            Document doc = parseXmlResponse(response);
            XPath xpath = XPathFactory.newInstance().newXPath();

            // 检查命令是否完成
            String state = xpath.evaluate(
                    "//*[local-name()='State']/text()",
                    doc,
                    XPathConstants.STRING
            ).toString();

            // 提取输出
            String outputBase64 = xpath.evaluate(
                    "//*[local-name()='Stream' and @Name='stdout']/text()",
                    doc,
                    XPathConstants.STRING
            ).toString();

            if (outputBase64 != null && !outputBase64.isEmpty()) {
                byte[] outputBytes = Base64.getDecoder().decode(outputBase64);
                output.append(new String(outputBytes, StandardCharsets.UTF_8));
            }

            // 检查错误输出
            String errorBase64 = xpath.evaluate(
                    "//*[local-name()='Stream' and @Name='stderr']/text()",
                    doc,
                    XPathConstants.STRING
            ).toString();

            if (errorBase64 != null && !errorBase64.isEmpty()) {
                byte[] errorBytes = Base64.getDecoder().decode(errorBase64);
                output.append("\nErrors:\n").append(new String(errorBytes, StandardCharsets.UTF_8));
            }

            // 命令完成状态判断
            if ("Completed".equals(state) || "Failed".equals(state)) {
                commandCompleted = true;

                // 清理命令
                requestBody = createCleanupCommandRequest(commandId);
                executeHttpPost(requestBody);
            } else {
                // 等待一段时间后重试
                Thread.sleep(500);
            }
        }

        return output.toString();
    }

    /**
     * 执行HTTP POST请求
     */
    private String executeHttpPost(String requestBody) throws Exception {
        HttpPost post = new HttpPost(config.getEndpoint());
        String auth = config.getDomain() + "\\" + config.getUsername() + ":" + config.getPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        post.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth);
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/soap+xml;charset=UTF-8");
        post.setHeader("WinRM-Identity", sessionId);
        post.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
        HttpResponse response = httpClient.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new RuntimeException("WinRM request failed: " + statusCode + " - " +
                    response.getStatusLine().getReasonPhrase());
        }

        HttpEntity entity = response.getEntity();
        return entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "";
    }

    /**
     * 解析XML响应
     */
    private Document parseXmlResponse(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    /**
     * 创建会话请求XML
     */
    private String createCreateSessionRequest() {
        return String.format(CREATE_SESSION_TEMPLATE, sessionId, endpoint);
    }

    /**
     * 创建Shell请求XML
     */
    private String createCreateShellRequest() {
        return String.format(CREATE_SHELL_TEMPLATE, UUID.randomUUID(), endpoint);
    }

    /**
     * 创建执行命令请求XML
     */
    private String createExecuteCommandRequest(String command, String commandId) {
        String encodedCommand = Base64.getEncoder().encodeToString(command.getBytes(StandardCharsets.UTF_8));

        return String.format(EXECUTE_COMMAND_TEMPLATE,
                UUID.randomUUID(), endpoint, shellId, encodedCommand, commandId);
    }

    /**
     * 创建获取命令输出请求XML
     */
    private String createGetCommandOutputRequest(String commandId) {
        return String.format(GET_COMMAND_OUTPUT_TEMPLATE,
                UUID.randomUUID(), endpoint, shellId, commandId);
    }

    /**
     * 创建清理命令请求XML
     */
    private String createCleanupCommandRequest(String commandId) {
        return String.format(CLEANUP_COMMAND_TEMPLATE,
                UUID.randomUUID(), endpoint, shellId, commandId);
    }

    /**
     * 检查会话是否过期
     */
    public boolean isExpired() {
        if (!connected) {
            return true;
        }

        Instant expirationTime = lastActivityTime.plusSeconds(config.getSessionTimeoutSeconds());
        return Instant.now().isAfter(expirationTime);
    }

    /**
     * 关闭会话
     */
    @Override
    public void close() {
        if (!connected) {
            return;
        }

        try {
            // 关闭shell
            if (shellId != null && !shellId.isEmpty()) {
                try {
                    String requestBody = String.format(CLOSE_SHELL_TEMPLATE,
                            UUID.randomUUID(), endpoint, shellId);
                    executeHttpPost(requestBody);
                } catch (Exception e) {
                    logger.warn("Failed to close shell: {}", e.getMessage());
                }
            }
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.warn("Failed to close HTTP client: {}", e.getMessage());
            }
            connected = false;
            shellId = null;
            sessionId = null;
        }
    }
}
