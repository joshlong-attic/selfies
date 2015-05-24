package demo;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utils.ToString;
import utils.ToStringBeanPostProcessor;

public class ToStringBeanPostProcessorTest {

    private ToStringBeanPostProcessor toStringBeanPostProcessor;

    private Log log = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        this.toStringBeanPostProcessor = new ToStringBeanPostProcessor();
        this.applicationContext = new AnnotationConfigApplicationContext(SimpleConfig.class);
    }

    @Test
    public void testToStringBeanPostProcessor() throws Throwable {

        Cat cat = new Cat("Felix", 4);
        Cat result = Cat.class.cast(this.toStringBeanPostProcessor
                .postProcessAfterInitialization(cat, "cat"));
        Assert.assertNotNull(result);
        Assert.assertTrue(cat.getClass().equals(Cat.class));
        Assert.assertTrue(Cat.class.isAssignableFrom(result.getClass()));
        Assert.assertEquals(cat, this.toStringBeanPostProcessor.postProcessBeforeInitialization(cat, "cat"));
        Assert.assertEquals(result.getName(), cat.getName());
        Assert.assertEquals(result.getAge(), cat.getAge());
    }

    @Test
    public void testCatHasToString() {
        Cat cat = this.applicationContext.getBean(Cat.class);
        String string = cat.toString();
        Assert.assertNotNull(string);
        Assert.assertTrue(string.contains(cat.getName()) &&
                string.contains(Integer.toString(cat.getAge())));
        log.info(String.format("cat.toString() = %s", string));
    }

    @Configuration
    public static class SimpleConfig {

        @Bean
        Cat cat() {
            return new Cat("Fido", 2);
        }

        @Bean
        ToStringBeanPostProcessor toStringBeanPostProcessor() {
            return new ToStringBeanPostProcessor();
        }
    }

    @ToString
    public static class Cat {

        private String name;

        private int age;

        public Cat(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

    }


}
