package cn.handsome.scim.service.impl;

import cn.handsome.scim.model.ServiceProviderConfig;
import cn.handsome.scim.service.ScimServiceProviderConfigService;
import org.springframework.stereotype.Service;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Service
public class ScimServiceProviderConfigServiceImpl implements ScimServiceProviderConfigService {
    @Override
    public ServiceProviderConfig getServiceProviderConfig() {
        return new ServiceProviderConfig();
    }
}
