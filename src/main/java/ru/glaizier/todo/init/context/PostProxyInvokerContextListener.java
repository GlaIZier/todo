package ru.glaizier.todo.init.context;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.glaizier.todo.init.annotation.PostProxy;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * We use this context listener to be notified when Spring's context is initialized. So, we are sure that all proxies
 * have been set (eg proxies to support @Transactional). Because we can't use @PostConstruct to initialize db because
 * it is called before proxies are set, before @Transactional takes place. We could inject here persistence and call
 * init method from here, but this is more general solution because @PostProxy can be used everywhere in project.
 */
@Component
public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ConfigurableListableBeanFactory factory;

    @Autowired
    public PostProxyInvokerContextListener(ConfigurableListableBeanFactory factory) {
        this.factory = factory;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("3rd phase of initialization after all proxies have been set...");

        ApplicationContext context = event.getApplicationContext();
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = factory.getBeanDefinition(beanDefinitionName);
            String originalBeanClassName = beanDefinition.getBeanClassName();
            // Sometimes bean class name is null when beanDefinitionName isn't null. Maybe it's connected with spring config classes:
            // https://stackoverflow.com/questions/33697722/spring-bean-definition-class-name-is-null-when-using-javaconfig
            if (originalBeanClassName == null)
                continue;
            Class<?> originalClass = Class.forName(originalBeanClassName);
            for (Method method : originalClass.getMethods()) {
                if (method.isAnnotationPresent(PostProxy.class)) {
                    Object bean = context.getBean(beanDefinitionName);
                    Method currentMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                    currentMethod.invoke(bean);
                }
            }
        }
    }
}
