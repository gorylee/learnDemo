package example.common.annotation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/6/30
 */
public class EnumIntegerValidator implements ConstraintValidator<ValidateEnum, Integer> {
    private Class<? extends Enum> enumClass;
    private int[] enumValues;

    private String keyMethod;

    @Override
    public void initialize(ValidateEnum validateEnum) {
        enumClass = validateEnum.enumClass();
        enumValues = validateEnum.enumValues();
        keyMethod = validateEnum.keyMethod();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        try {
            if(enumValues.length ==0 ){ //enumValues 优先级高
                Enum[] enumArr = enumClass.getEnumConstants();
                if(enumArr.length>0){
                    for (Enum e : enumArr) {
                        Method method = e.getDeclaringClass().getMethod(keyMethod);
                        Object keyValue = method.invoke(e);
                        if(keyValue != null){
                            if(value.equals(keyValue)){
                                return true;
                            }
                        }
                    }
                    return false;
                }

            }else {
                for (int enumValue : enumValues) {
                    if(enumValue == value){
                        return true;
                    }
                }
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
