package io.katharsis.spring;

import io.katharsis.repository.RepositoryMethodParameterProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.*;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpringParameterProvider implements RepositoryMethodParameterProvider {
    private HttpServletRequest request;
    private ConfigurableBeanFactory beanFactory;
    private HandlerMethodArgumentResolverComposite argumentResolvers;

    public SpringParameterProvider(ConfigurableBeanFactory beanFactory, HttpServletRequest request) {
        this.request = request;
        this.beanFactory = beanFactory;

        List<HttpMessageConverter<?>> messageConverters = getHttpMessageConverters();
        argumentResolvers = new HandlerMethodArgumentResolverComposite()
            .addResolvers(getArgumentResolvers(messageConverters));

    }

    private List<HttpMessageConverter<?>> getHttpMessageConverters() {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);  // see SPR-7316

        List<HttpMessageConverter<?>> converters = new ArrayList<>(4);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(stringHttpMessageConverter);
        converters.add(new SourceHttpMessageConverter<>());
        converters.add(new AllEncompassingFormHttpMessageConverter());

        return converters;
    }

    private List<HandlerMethodArgumentResolver> getArgumentResolvers(List<HttpMessageConverter<?>> messageConverters) {
        List<HandlerMethodArgumentResolver> resolvers = new LinkedList<>();

        resolvers.add(new RequestParamMethodArgumentResolver(beanFactory, false));
        resolvers.add(new RequestParamMapMethodArgumentResolver());
        resolvers.add(new MatrixVariableMethodArgumentResolver());
        resolvers.add(new MatrixVariableMapMethodArgumentResolver());
        resolvers.add(new ServletModelAttributeMethodProcessor(false));
        resolvers.add(new RequestResponseBodyMethodProcessor(messageConverters));
        resolvers.add(new RequestPartMethodArgumentResolver(messageConverters));
        resolvers.add(new RequestHeaderMethodArgumentResolver(beanFactory));
        resolvers.add(new RequestHeaderMapMethodArgumentResolver());
        resolvers.add(new ServletCookieValueMethodArgumentResolver(beanFactory));
        resolvers.add(new ExpressionValueMethodArgumentResolver(beanFactory));

        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        resolvers.add(new HttpEntityMethodProcessor(messageConverters));
        resolvers.add(new RedirectAttributesMethodArgumentResolver());
        resolvers.add(new ModelMethodProcessor());
        resolvers.add(new MapMethodProcessor());
        resolvers.add(new ErrorsMethodArgumentResolver());
        resolvers.add(new SessionStatusMethodArgumentResolver());
        resolvers.add(new UriComponentsBuilderMethodArgumentResolver());

        resolvers.add(new RequestParamMethodArgumentResolver(beanFactory, true));
        resolvers.add(new ServletModelAttributeMethodProcessor(true));

        return resolvers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T provide(Method method, int parameterIndex) {
        MethodParameter methodParameter = new MethodParameter(method, parameterIndex);
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        NativeWebRequest webRequest = new ServletWebRequest(request);
        DefaultDataBinderFactory binderFactory = new DefaultDataBinderFactory(new ConfigurableWebBindingInitializer());

        try {
            return (T) argumentResolvers.resolveArgument(methodParameter, modelAndViewContainer, webRequest, binderFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
