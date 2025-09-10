package cn.handsome.scim.controller;

import cn.handsome.scim.model.ServiceProviderConfig;
import cn.handsome.scim.service.ScimServiceProviderConfigService;
import cn.handsome.web.base.BaseController;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/scim/v2/ServiceProviderConfig")
@Api(value = "providerConfig", tags = "ProviderConfig 服务")
public class ScimServiceProviderConfigController extends BaseController {
    private final ScimServiceProviderConfigService configService;

    @GetMapping
    public ResponseEntity<ServiceProviderConfig> getServiceProviderConfig() {
        return ResponseEntity.ok(configService.getServiceProviderConfig());
    }
}
