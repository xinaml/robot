package com.xinaml.robot.common.aspect;

import com.alibaba.fastjson.JSON;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

@Order(Integer.MIN_VALUE + 1)
@Aspect
@Component
public class JSR303Aspect {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PatchMapping)"
    )
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        BindingResult result = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint
                .getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] argAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();//获取参数值
        if (method.getName().equals("errorHtml")) {//springmvc 错误请求
            return joinPoint.proceed(args);
        }
        boolean exists = false; //是否包含验证注解
        for (Annotation[] annotations : argAnnotations) {
            if (annotations.length > 0 && annotations[0] instanceof Validated) {
                exists = true;
                break;
            }
        }
        if (exists) {//检测是否带注解
            for (int i = 0, len = args.length; i < len; i++) {
                Object object = args[i];
                if (null != object) {
                    if (null == result && object instanceof BindingResult) {
                        result = (BindingResult) object;
                    }

                }
            }
            if (null != result && writeResult(result)) {
                return null;
            }
        }
        //处理 NotNull NotBlank 注解，必须放第一个
        int index = 0;
        for (Annotation[] ants : argAnnotations) {
            if (ants.length > 0 && (ants[0] instanceof NotBlank || ants[0] instanceof NotNull)) {
                Object obj = args[index++];
                if (null != obj && StringUtils.isNotBlank(obj.toString())) {
                } else {
                    String msg = ants[0].toString();
                    msg = StringUtils.substringAfter(msg, "message=");
                    msg = StringUtils.substringBefore(msg, ",");
                    writeResult(msg);
                    return null;
                }

            }
        }
        return joinPoint.proceed(args);
    }

    protected boolean writeResult(BindingResult result) {
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            if (null != fieldErrors && fieldErrors.size() > 0) {
                ActResult actResult = new ActResult();
                actResult.setCode(2);
                actResult.setMsg("参数检验不通过[" + fieldErrors.get(0).getDefaultMessage() + "]");
                actResult.setData(null);
                ResponseUtil.writeData(JSON.toJSONString(actResult));
                return true;
            }
        }
        return false;
    }

    protected void writeResult(String msg) {
        ActResult actResult = new ActResult();
        actResult.setCode(2);
        actResult.setMsg("参数校验不通过[" + msg + "]");
        actResult.setData("");
        ResponseUtil.writeData(JSON.toJSONString(actResult));
    }
}
