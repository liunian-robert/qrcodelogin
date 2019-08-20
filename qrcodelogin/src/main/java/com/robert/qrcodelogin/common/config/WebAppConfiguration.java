package com.robert.qrcodelogin.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.robert.qrcodelogin.common.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * web相关配置类
 * @author xiaotao.song
 *
 */
@Configuration
public class WebAppConfiguration implements WebMvcConfigurer {

	@Autowired
	private CorsFilter corsFilter;
	
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
		converters.add(mappingJackson2HttpMessageConverter());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	}

	@Bean
	public FilterRegistrationBean optionFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		//注入过滤器
		registration.setFilter(corsFilter);
		//拦截规则
		registration.addUrlPatterns("/*");
		//过滤器名称
		registration.setName("optionFilter");
		//过滤器顺序
		registration.setOrder(1);
		return registration;
	}

	@Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    	MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    	//设置日期格式
    	ObjectMapper objectMapper = new ObjectMapper();
    	SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	objectMapper.setDateFormat(smt);
    	objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    	//objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    	//在反序列化时，忽略目标对象没有的属性
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    	mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
    	//设置中文编码格式
    	List<MediaType> list = new ArrayList<MediaType>();
    	list.add(MediaType.APPLICATION_JSON_UTF8);
    	mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
    	
    	
    	return mappingJackson2HttpMessageConverter;
	}
	
}
