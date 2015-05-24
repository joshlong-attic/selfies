package utils;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


/**
 * @author <A href="mailto:josh@joshlong">Josh Long</A>
 */
public class ToStringBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (o.getClass().getAnnotationsByType(ToString.class).length > 0) {
            try {
                return this.buildToStringCapableProxy(o.getClass(), o);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return o;
    }

    private <T> T buildToStringCapableProxy(Class<? extends T> tClass, T t) throws ClassNotFoundException {

        MethodInterceptor toString = methodInvocation ->
                (methodInvocation.getMethod().getName().equals("toString")) ?
                        ToStringBuilder.reflectionToString(t, ToStringStyle.MULTI_LINE_STYLE) :
                        methodInvocation.getMethod().invoke(t, methodInvocation.getArguments());

        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(t);
        proxyFactoryBean.setProxyTargetClass(true);
        proxyFactoryBean.addAdvice(toString);
        proxyFactoryBean.setTargetClass(tClass);
        return tClass.cast(proxyFactoryBean.getObject());
    }
}
