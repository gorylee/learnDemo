package example.common.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 枚举参数校验注解
 */

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumIntegerValidator.class})
@Documented
public @interface ValidateEnum {
    String message() default "invalid parameter";

    String keyMethod() default "getKey";// 默认属性名称为key，可自定义

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 允许的枚举
     * @return
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 允许的枚举值, 约定为int类型, 判断优先级高于enumClass
     * @return
     */
    int[] enumValues() default {};

}
