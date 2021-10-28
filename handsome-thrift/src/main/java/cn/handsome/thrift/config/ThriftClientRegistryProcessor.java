package cn.handsome.thrift.config;

import cn.handsome.core.Constants;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.core.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TServiceClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import java.util.Set;

/**
 * @author shoy
 * @date 2021/6/5
 */
@Slf4j
public class ThriftClientRegistryProcessor implements BeanDefinitionRegistryPostProcessor {
    private final BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] names = registry.getBeanDefinitionNames();
        log.info(JsonUtils.toJson(names));

        Set<Class<?>> clientSet = ReflectUtils.findClasses(Constants.BASE_PACKAGES, TServiceClient.class::isAssignableFrom);
        for (Class<?> client : clientSet) {
            log.info(client.getName());
            AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(client);
//            beanDefinition.setSource(clientFactory.createClient(client));
            beanDefinition.setScope("Request");
            String beanName = this.beanNameGenerator.generateBeanName(beanDefinition, registry);
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
