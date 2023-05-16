package com.hzp.web.annotation;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hzp.web.util.SQLFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *  排序、分组字段验证
 *
 *  @author yxy
 *  @date 2020/4/3 10:32
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {Sort.SortValidator.class})
public @interface Sort {
    String message() default "不支持的排序字段，或者格式有误[字段名-desc]";

    Class<?>[] FieldOfClass() default {};

    String[] fields() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Slf4j
    class SortValidator implements ConstraintValidator<Sort, String> {

        private static Map<String, Set<String>> supportBeanFields = Maps.newConcurrentMap();
        private static List<String> supportSortTypes = Lists.newArrayList("asc", "ASC", "desc", "DESC");

        private Sort sortAnn;

        @Override
        public void initialize(Sort constraintAnnotation) {
            this.sortAnn = constraintAnnotation;

            if (ArrayUtils.isNotEmpty(sortAnn.FieldOfClass())) {
                for (Class<?> clz : sortAnn.FieldOfClass()) {
                    Set<String> clzFields = supportBeanFields.get(clz.getName());
                    if (Objects.nonNull(clzFields)) {
                        continue;
                    }
                    Field[] fields = clz.getDeclaredFields();
                    if (ArrayUtils.isEmpty(fields)) {
                        supportBeanFields.putIfAbsent(clz.getName(), Sets.newHashSet());
                        continue;
                    }
                    clzFields = Sets.newHashSetWithExpectedSize(fields.length * 2);
                    for (Field field : fields) {
                        field.setAccessible(true);
                        clzFields.add(field.getName());
                        clzFields.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()));
                    }
                    supportBeanFields.putIfAbsent(clz.getName(), clzFields);

                }

            }
        }

        /**
         * 校验排序字段
         * 如果为空,则返回true,如果校验非空请使用@NotEmpty组合注解
         *
         * @param value     值
         * @param context   校验器上下文
         * @return          是否校验成功
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isEmpty(value)) {
                return true;
            }

            if (!SQLFilter.checkSqlInject(value)) {
                return false;
            }

            String[] split = value.split("-");
            if (split.length != 2) {
                log.error("排序字段格式格式有误，比如[字段名-asc]");
                return false;
            }

            String sortField = split[0];
            String sortType = split[1];

            if (ArrayUtils.isEmpty(sortAnn.fields()) && ArrayUtils.isEmpty(sortAnn.FieldOfClass())) {
                log.error("排序字段必须限定枚举值，请对@sort的fields或者fieldOfClass赋值)");
                return false;
            }

            boolean isSupport = false;
            if (ArrayUtils.isNotEmpty(sortAnn.fields())) {
                if (ArrayUtils.contains(sortAnn.fields(), sortField)) {
                    isSupport = true;
                }
            }

            if (ArrayUtils.isNotEmpty(sortAnn.FieldOfClass())) {
                for (Class<?> clz : sortAnn.FieldOfClass()) {
                    Set<String> supportFields = supportBeanFields.get(clz.getName());
                    if (supportFields.contains(sortField)) {
                        isSupport = true;
                        break;
                    }
                }
            }

            if (!supportSortTypes.contains(sortType)) {
                log.error("不支持的排序类型， 仅支持[asc、desc]");
                return false;
            }
            return isSupport;
        }
    }
}
