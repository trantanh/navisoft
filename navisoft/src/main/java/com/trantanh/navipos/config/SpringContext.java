package com.trantanh.navipos.config;

import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;

/**
 * Bridges the JavaFX lifecycle with the Spring application context.
 *
 * FXML controllers are created by Spring's bean factory, so constructor and
 * field injection can be introduced incrementally without changing FXML.
 */
@Component
public final class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public SpringContext() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        initialize(applicationContext);
    }

    public static void initialize(ApplicationContext applicationContext) {
        context = Objects.requireNonNull(applicationContext, "applicationContext");
    }

    public static <T> T getBean(Class<T> type) {
        return requireContext().getBean(type);
    }

    public static <T> T createBean(Class<T> type) {
        AutowireCapableBeanFactory beanFactory = requireContext().getAutowireCapableBeanFactory();
        return beanFactory.createBean(type);
    }

    public static FXMLLoader fxmlLoader(URL resource) {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(resource, "FXML resource"));
        loader.setControllerFactory(SpringContext::createBean);
        return loader;
    }

    private static ApplicationContext requireContext() {
        if (context == null) {
            throw new IllegalStateException("Spring context has not been initialized yet");
        }
        return context;
    }
}
