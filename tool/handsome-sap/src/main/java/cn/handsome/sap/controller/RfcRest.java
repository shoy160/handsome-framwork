package cn.handsome.sap.controller;

import cn.handsome.sap.SapRfcException;
import cn.handsome.sap.dto.SapRfcConfig;
import cn.handsome.sap.dto.SapRfcCredentials;
import cn.handsome.sap.manage.SapRfcHelper;
import com.sap.conn.jco.JCoDestination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author luoyong
 * @date 2025/8/1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rfc")
@Api(value = "rfc", tags = "RFC 服务")
public class RfcRest {
    private final Map<String, SapRfcHelper> helpers;

    @PostMapping("{conn}")
    @ApiOperation(value = "SAP 注册", notes = "SAP 注册")
    public boolean register(
            @PathVariable String conn,
            @RequestBody SapRfcCredentials credentials
    ) {
        SapRfcHelper helper = new SapRfcHelper(conn, credentials);
        JCoDestination destination = helper.connect();
        if (Objects.nonNull(destination)) {
            helpers.put(conn, helper);
            return true;
        }
        return false;
    }

    @GetMapping("conn")
    @ApiOperation(value = "SAP 连接列表", notes = "SAP 连接列表")
    public List<String> conn() {
        return new ArrayList<>(helpers.keySet());
    }

    @PostMapping("{conn}/invoke")
    @ApiOperation(value = "RFC 调用", notes = "RFC 调用")
    public Object invoke(@PathVariable String conn, @RequestBody SapRfcConfig config, HttpServletRequest request) {
        SapRfcHelper helper = helpers.get(conn);
        if (Objects.isNull(helper)) {
            throw new SapRfcException(String.format("SAP 连接「%s」不存在", conn));
        }
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        helper.verify(token);
        return helper.invokeFunc(config.getFunction(), config.getParams(), Objects.equals(true, config.getErrorCheck()));
    }
}
