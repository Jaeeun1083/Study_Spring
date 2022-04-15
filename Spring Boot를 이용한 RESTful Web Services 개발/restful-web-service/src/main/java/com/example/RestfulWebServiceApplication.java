package com.example;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class RestfulWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfulWebServiceApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.KOREA);
		return localeResolver;
	}

	/**
	 * spring boot 2.6.0부터 요청 경로를 ControllerHandler에 매칭시키기 위한 전략의 기본값이 ant_path_matcher 전략 -> path_pattern_parser 전략으로 변경되었기 때문에 Swagger 사용을 위해 필요한 Bean 등록
	 * Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException:
	 * Cannot invoke "org.springframework.web.servlet.mvc.condition.PatternsRequestCondition.getPatterns()" because "this.condition" is null
	 * */
	@Bean
	public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
		return new BeanPostProcessor() {
			@Override
			@SuppressWarnings({"NullableProblems", "unchecked"})
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
					try {
						Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
						if (null != field) {
							field.setAccessible(true);
							List<RequestMappingInfoHandlerMapping> mappings = (List<RequestMappingInfoHandlerMapping>) field.get(bean);
							mappings.removeIf(e -> null != e.getPatternParser());
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new IllegalStateException(e);
					}
				}
				return bean;
			}
		};
	}
}
