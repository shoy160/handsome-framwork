package cn.handsome.thrift.server.impl;

import cn.handsome.core.Constants;
import cn.handsome.core.utils.ReflectUtils;
import cn.handsome.thrift.annotation.ThriftService;
import cn.handsome.thrift.domain.ThriftDescriptor;
import cn.handsome.thrift.server.DescriptorFinder;
import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shoy
 * @date 2021/6/4
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultDescriptorFinder implements DescriptorFinder {
    private final ApplicationContext context;
    private static final Pattern CLASS_REG = Pattern.compile("\\$([A-Za-z]+)$", Pattern.DOTALL);
    private static final String FACE_NAME = "Iface";
    private static final String PROCESSOR_NAME = "Processor";
    private static final String CLIENT_NAME = "Client";


    private static Class<?> getClass(Class<?> interfaceClass, String name) throws ClassNotFoundException {
        Matcher matcher = CLASS_REG.matcher(interfaceClass.getName());
        if (matcher.find()) {
            String faceName = matcher.group(1);
            name = faceName.replace(FACE_NAME, Constants.STR_EMPTY).concat(name);
            String className = interfaceClass.getName().replace(faceName, name);
            log.info("get class name:{}", className);
            return Class.forName(className);
        }
        throw new ClassNotFoundException();
    }

    @Override
    public ThriftDescriptor[] find() {
        List<ThriftDescriptor> processorList = new ArrayList<>();
        Set<Class<?>> classSet = ReflectUtils.findClasses(Constants.BASE_PACKAGES, clz -> clz.getAnnotation(ThriftService.class) != null);
        for (Class<?> clazz : classSet) {
            ThriftService service = clazz.getAnnotation(ThriftService.class);
            if (service == null) {
                continue;
            }
            try {
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> interfaceClazz : interfaces) {
                    Class<?> processorClass = getClass(interfaceClazz, PROCESSOR_NAME);
                    Class<?> clientClass = getClass(interfaceClazz, CLIENT_NAME);
                    Constructor<?> constructor = processorClass.getConstructor(interfaceClazz);
                    Object instance = context.getBean(clazz);
                    TProcessor processor = (TProcessor) constructor.newInstance(instance);
                    ThriftDescriptor descriptor = new ThriftDescriptor();
                    descriptor.setServiceName(clientClass.getName());
                    descriptor.setProcessor(processor);
                    descriptor.setClientClass(clientClass);
                    processorList.add(descriptor);
                }
            } catch (Exception ex) {
                log.warn("service find error:{}", clazz, ex);
            }
        }
        return ArrayUtil.toArray(processorList, ThriftDescriptor.class);
    }
}
