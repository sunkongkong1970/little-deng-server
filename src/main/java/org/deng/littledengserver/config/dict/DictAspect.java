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
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
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

                    // 获取字典值
                    String dictValue = dictService.getDict(dict.type(), (String) fieldValue);

                    // 设置对应的名称字段
                    String nameFieldName = field.getName() + dict.suffix();
                    setFieldValue(obj, nameFieldName, dictValue);
                } catch (Exception e) {
                    log.error("trans dict error,message:{}", e.getMessage());
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
