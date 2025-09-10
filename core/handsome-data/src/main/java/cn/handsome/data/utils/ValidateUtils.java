package cn.handsome.data.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.handsome.core.enums.ResultCode;
import cn.handsome.core.exception.BusinessException;
import cn.handsome.core.utils.CommonUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 校验工具类
 *
 * @author shay
 * @date 2020/11/11
 */
public final class ValidateUtils {

    /**
     * 实体校验
     *
     * @param obj        obj
     * @param clazzArray class list
     */
    public static void validate(Object obj, Class<?>... clazzArray) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> errors = validator.validate(obj, clazzArray);
        if (errors.size() == 0) {
            return;
        }
        List<String> errorList = new ArrayList<>();
        for (ConstraintViolation<Object> error : errors) {
            String fieldName = error.getPropertyPath().toString();
            Field field = ReflectUtil.getField(obj.getClass(), fieldName);
            if (field != null) {
                fieldName = CommonUtils.getDesc(field, obj.getClass());
            }
            String message = error.getMessage();
            errorList.add(fieldName.concat(message));
        }
        throw new BusinessException(ResultCode.PARAM_VALID_ERROR.getCode(), String.join(";", errorList));
    }
}
