package com.egencia.webapp.spur.config;

import com.egencia.library.commons.web.locale.EgenciaLocaleInterceptor;
import com.egencia.library.uitoolkit.ViewNameInModelInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import com.egencia.library.uitoolkit.interceptors.HeaderFooterModelInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring {@link Configuration} element for Spring MVC.
 * <p/>
 * <p>
 * This {@link Configuration} bean is responsible for setting up the Spring MVC configuration, such as to register
 * {@link HttpMessageConverter}s and Spring MVC interceptors.
 * </p>
 * <p>
 * Note that it also import the standard {@link JacksonConfiguration} that defines how Jackson 2 behaves, and the Swagger's
 * {@link DocumentationConfig} to automatically expose the REST API documentation endpoint.
 * </p>
 *
 * @author Remy CREPIN
 */
@Configuration
@EnableWebMvc
@ComponentScan(value = {"com.egencia"}, excludeFilters = @ComponentScan.Filter({Configuration.class}))
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ObjectMapper defaultObjectMapper;

    @Autowired
    private HeaderFooterModelInterceptor headerFooterModelInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new EgenciaLocaleInterceptor());
        registry.addInterceptor(new ViewNameInModelInterceptor());
        registry.addInterceptor(headerFooterModelInterceptor);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.addAll(buildMessageConverters());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ServletWebArgumentResolverAdapter(new DeviceWebArgumentResolver()));
        argumentResolvers.add(new SitePreferenceHandlerMethodArgumentResolver());
    }

    @Bean
    public List<HttpMessageConverter<?>> buildMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter jackson2Converter = new MappingJackson2HttpMessageConverter();
        jackson2Converter.setObjectMapper(defaultObjectMapper);
        messageConverters.add(jackson2Converter);
        return messageConverters;
    }

    /**
     * Resources mapping
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/assets/css/");
        registry.addResourceHandler("/data/**").addResourceLocations("classpath:/assets/data/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/assets/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/assets/images/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/assets/js/");
        registry.addResourceHandler("/views/**").addResourceLocations("classpath:/assets/views/");
    }
}