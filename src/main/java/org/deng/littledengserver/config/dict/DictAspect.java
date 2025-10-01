package org.deng.littledengserver.config.dict;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.deng.littledengserver.service.DictService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@Aspect
@Component
public class DictAspect {
    @Resource
    DictService dictService;

    @Pointcut("execution(public * org.deng.littledengserver.controller..*.*(..))")
    public void dictPointCut() {
    }

    /**
     * 环绕通知，处理返回结果
     */
    @Around("dictPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 执行原方法
        Object result = point.proceed();

        // 处理返回结果，添加字典名称
        handleResult(result);

        return result;
    }

    /**
     * 处理返回结果，递归处理对象、集合、数组等
     */
    private void handleResult(Object result) {
        if (result == null) {
            return;
        }

        // 处理BaseResult包装类，获取其中的data字段进行处理
        if (result.getClass().getName().contains("BaseResult")) {
            try {
                Field dataField = findField(result.getClass(), "data");
                if (dataField != null) {
                    dataField.setAccessible(true);
                    Object data = dataField.get(result);
                    handleResult(data);
                }
            } catch (Exception e) {
                log.error("handle BaseResult error: {}", e.getMessage());
            }
            return;
        }

        // 处理集合
        if (result instanceof Collection<?>) {
            ((Collection<?>) result).forEach(this::handleResult);
        }
        // 处理数组
        else if (result.getClass().isArray()) {
            Object[] array = (Object[]) result;
            for (Object obj : array) {
                handleResult(obj);
            }
        }
        // 处理对象
        else {
            handleObject(result);
        }
    }

    /**
     * 处理单个对象，为带有@Dict注解的字段添加对应名称
     */
    private void handleObject(Object obj) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
        // 获取所有字段（包括父类字段）
        List<Field> fields = new ArrayList<>();
        Class<?> tempClazz = clazz;
        while (tempClazz != null && tempClazz != Object.class) {
            fields.addAll(Arrays.asList(tempClazz.getDeclaredFields()));
            tempClazz = tempClazz.getSuperclass();
        }

        // 处理每个字段
        for (Field field : fields) {
            // 检查是否有@Dict注解
            if (field.isAnnotationPresent(Dict.class)) {
                Dict dict = field.getAnnotation(Dict.class);
                try {
                    // 获取字段值
                    field.setAccessible(true);
                    Object fieldValue = field.get(obj);

                    // 如果字段值为null，跳过
                    if (fieldValue == null) {
                        continue;
                    }

                    // 获取字典值
                    String dictValue = dictService.getDict(dict.type(), (String) fieldValue);

                    // 构造字典翻译字段名
                    String nameFieldName = field.getName() + dict.suffix();

                    // 优先使用 DictTranslatable 基类的动态字段
                    if (obj instanceof DictTranslatable) {
                        ((DictTranslatable) obj).addDictField(nameFieldName, dictValue);
                    } else {
                        // 兼容旧方式：尝试设置预定义的字段
                        setFieldValue(obj, nameFieldName, dictValue);
                    }
                } catch (Exception e) {
                    log.error("trans dict error, field: {}, message: {}", field.getName(), e.getMessage());
                }
            }
        }
    }

    /**
     * 为对象设置字段值
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field nameField = findField(obj.getClass(), fieldName);
            if (nameField != null) {
                nameField.setAccessible(true);
                nameField.set(obj, value);
            }
        } catch (Exception e) {
            log.error("field not exist,message:{}", e.getMessage());
            // 字段不存在时不做处理，或根据需要添加日志
        }
    }

    /**
     * 查找对象中的字段（包括父类）
     */
    private Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
