package team.benchem.webapi.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 方法参数校验器
 *
 * 将方法参数校验器注入到Spring Boot 运行环境
 *
 * 用于@RequestParam
 *
 * 用法：
 * 1. 在需要进行参数校验的Controller类上添加@Validated注解
 * 2. 在Controller方法中添加校验注解
 *
 * 例：
 * public String test(@Site(min=3,message="值长度不足3") @RequestParam String name){ return "";}
 *
 * 常用注解：
 *
 * @AssertFalse 校验false
 * @AssertTrue 校验true
 * @DecimalMax(value=,inclusive=) 小于等于value，inclusive=true,是小于等于
 * @DecimalMin(value=,inclusive=) 与上类似
 * @Max(value=) 小于等于value
 * @Min(value=) 大于等于value
 * @NotNull 检查Null
 * @Past 检查日期
 * @Pattern(regex=,flag=) 正则
 * @Size(min=, max=)  字符串，集合，map限制大小
 * @Valid 对po实体类进行校验
 * <p>
 * <p>
 * https://docs.spring.io/spring/docs/5.0.4.BUILD-SNAPSHOT/javadoc-api/org/springframework/validation/beanvalidation/MethodValidationPostProcessor.html
 */
@Configuration
public class MethodParamValidator {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
