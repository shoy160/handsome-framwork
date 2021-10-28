package cn.handsome.sdk.payment;

import cn.handsome.core.Constants;
import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.core.http.HttpHelper;
import cn.handsome.core.http.HttpRequest;
import cn.handsome.core.http.HttpResponse;
import cn.handsome.core.http.enums.HttpMethod;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.core.utils.MapUtils;
import cn.handsome.sdk.payment.config.PaymentProperties;
import cn.handsome.sdk.payment.entity.*;
import cn.handsome.sdk.payment.entity.*;
import cn.handsome.sdk.payment.enums.PaymentMode;
import cn.handsome.sdk.payment.enums.PaymentType;
import cn.handsome.sdk.payment.utils.SignUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 支付客户端
 *
 * @author shay
 * @date 2020/8/7
 */
@Component
@AllArgsConstructor
public class PaymentClient {

    private final PaymentProperties property;

    private String getUrl(String path) {
        try {
            if (StrUtil.isEmpty(property.getGateway())) {
                return path;
            }
            return URLUtil.completeUrl(property.getGateway(), path);
        } catch (Exception ex) {
            return path;
        }
    }

    private TreeMap<String, Object> getPayMap(BasePayDTO dto) {
        TreeMap<String, Object> payMap = new TreeMap<>();
        payMap.put("OrderNo", dto.getOrderNo());
        payMap.put("Amount", dto.getAmount());
        payMap.put("Title", dto.getTitle());
        payMap.put("Body", dto.getBody());
        payMap.put("Extend", dto.getExtend());
        if (null != dto.getTimeout() && dto.getTimeout() > 0) {
            payMap.put("Timeout", dto.getTimeout());
        }
        payMap.put("ProjectCode", property.getProjectCode());
        payMap.put("RedirectUrl", dto.getRedirectUrl());
        return payMap;
    }

    private ResultDTO<String> readResult(HttpResponse resp) {
        return readResult(resp, String.class);
    }

    private <T> ResultDTO<T> readResult(HttpResponse resp, Class<T> clazz) {
        if (resp.isErrorCode()) {
            return ResultDTO.failT(500, "支付异常");
        }
        String body = resp.readBody();
        ResultDTO<T> resultDTO = JsonUtils.json(body, t -> t.constructParametricType(ResultDTO.class, clazz));
        if (resultDTO == null) {
            return ResultDTO.failT(500, "支付返回参数异常");
        }
        if (resultDTO.getCode() == 0) {
            return ResultDTO.success(resultDTO.getData());
        }
        return ResultDTO.failT(resultDTO.getCode(), resultDTO.getMessage());
    }

    /**
     * 创建收银台
     *
     * @param inputDTO 收银台参数
     * @return result
     */
    public ResultDTO<String> createCashier(CashierInputDTO inputDTO) {
        TreeMap<String, Object> payMap = getPayMap(inputDTO);
        payMap.put("Scan", inputDTO.getScan());
        // sign
        String sign = SignUtils.signMap(payMap, property.getPrivateKey());
        payMap.put("sign", sign);
        String url = getUrl("trade");
        HttpResponse resp = HttpHelper.post(url, payMap);
        return readResult(resp);
    }

    /**
     * 创建支付
     *
     * @param mode     支付方式
     * @param type     支付类型
     * @param inputDTO 支付参数
     * @return result
     */
    public ResultDTO<String> createPayment(PaymentMode mode, PaymentType type, PayInputDTO inputDTO) {
        TreeMap<String, Object> payMap = getPayMap(inputDTO);
        if (mode == PaymentMode.WeChat) {
            switch (type) {
                case Applet:
                case Public:
                    String openId = inputDTO.getOpenId();
                    payMap.put("OpenId", CommonUtils.isEmpty(openId) ? "" : openId);
                    break;
                case H5:
                    payMap.put("SceneInfo", "");
                    break;
                default:
                    break;
            }
        }
        String sign = SignUtils.signMap(payMap, property.getPrivateKey());
        payMap.put("sign", sign);
        String api = String.format("%s/%s", mode.toString().toLowerCase(), type.toString().toLowerCase());
        String url = getUrl(api);
        HttpResponse resp = HttpHelper.get(url, payMap);
        return readResult(resp);
    }

    /**
     * 交易查询
     *
     * @param orderNo 订单编号
     * @return TradeDTO
     */
    public ResultDTO<TradeDTO> query(String orderNo) {
        Map<String, Object> queryMap = new HashMap<>(2);
        queryMap.put("OrderNo", orderNo);
        queryMap.put("ProjectCode", property.getProjectCode());
        String sign = SignUtils.signMap(queryMap, property.getPrivateKey());
        queryMap.put("sign", sign);
        String url = getUrl("trade/query");
        HttpResponse resp = HttpHelper.get(url, queryMap);
        return readResult(resp, TradeDTO.class);
    }

    /**
     * 退款
     *
     * @param inputDTO dto
     * @return result
     */
    public ResultDTO<?> refund(RefundInputDTO inputDTO) {
        String api = "trade/refund";
        String url = getUrl(api);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("OrderNo", inputDTO.getOrderNo());
        queryMap.put("Amount", inputDTO.getAmount());
        queryMap.put("RefundNo", inputDTO.getRefundNo());
        queryMap.put("Reason", inputDTO.getReason());
        queryMap.put("ProjectCode", property.getProjectCode());
        String sign = SignUtils.signMap(queryMap, property.getPrivateKey());
        queryMap.put("sign", sign);
        HttpRequest request = new HttpRequest(url, HttpMethod.PUT);
        request.setParams(queryMap);
        HttpResponse resp = HttpHelper.request(request);
        if (resp.isErrorCode()) {
            return ResultDTO.fail(500, "支付异常");
        }
        String body = resp.readBody();
        ResultDTO<?> resultDTO = JsonUtils.json(body, ResultDTO.class);
        if (resultDTO != null && resultDTO.getCode() == 0) {
            resultDTO.setCode(Constants.CODE_SUCCESS);
            resultDTO.setSuccess(true);
        }
        return resultDTO;
    }

    /**
     * 回调验证
     *
     * @param dto dto
     * @return result
     */
    public boolean verify(NotifyDTO dto) {
        Map<String, Object> map = MapUtils.map(dto);
        return SignUtils.verifyMap(map, property.getPrivateKey(), dto.getSign());
    }

    /**
     * 微信OAuth认证
     *
     * @param code code
     * @param type type
     * @return oauth
     */
    public OAuthDTO oauth(String code, PaymentType type) {
        Map<String, Object> queryMap = new HashMap<>(2);
        queryMap.put("code", code);
        queryMap.put("type", type.getValue());
        queryMap.put("ProjectCode", property.getProjectCode());
        String sign = SignUtils.signMap(queryMap, property.getPrivateKey());
        queryMap.put("sign", sign);
        String url = getUrl("wechat/oauth");
        HttpResponse resp = HttpHelper.get(url, queryMap);
        if (resp.isErrorCode()) {
            return null;
        }
        return resp.readBodyT(OAuthDTO.class);
    }

    /**
     * 微信OAuth认证
     *
     * @param code code
     * @return oauth
     */
    public OAuthDTO oauth(String code) {
        return oauth(code, PaymentType.Public);
    }
}
