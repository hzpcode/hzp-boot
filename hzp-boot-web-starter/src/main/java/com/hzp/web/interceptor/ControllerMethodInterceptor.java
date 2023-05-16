package com.hzp.web.interceptor;

import com.hzp.web.annotation.PreventFrequentRequest;
import com.hzp.web.exception.AuthException;
import com.hzp.web.exception.constant.ReturnCode;
import com.hzp.web.holder.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Controller层AOP拦截器
 *
 * @author yxy
 * @date 2019/12/05 14:08
 **/

@Order(1)
@Slf4j
@Aspect
public class ControllerMethodInterceptor {

    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
//    private CompositeLogProcessor compositeLogProcessor;

    @Around("(PointCut.controllerPointcut() || PointCut.msControllerPointcut()) && PointCut.mappingPointcut()")
    public Object interceptor(ProceedingJoinPoint pjp) {
        // todo
//        long beginTime = System.currentTimeMillis();
//        MethodSignature signature = (MethodSignature) pjp.getSignature();
//        Method method = signature.getMethod();
//        String methodName = method.getName();
//        Object[] args = pjp.getArgs();
//        log.info("请求开始，方法：{}", methodName);
//        if (!methodName.contains("FileUpload")) {
//            log.debug("请求参数：{}", JsonUtils.toJsonString(args));
//        }
//        Object result;
//        JsonResult<?> errorJsonResult = null;
//        Set<Object> allParams = new LinkedHashSet<>(4);
//        Map<String, Object> paramMap = new HashMap<>(8);
//        paramMap.put(AuthConstant.AUTHORIZATION, request.getHeader(AuthConstant.AUTHORIZATION));
//        paramMap.put(TraceConstant.TRACE_HEADER_NAME, MDC.get(TraceConstant.TRACE_ID_NAME));
//        paramMap.put(AuthConstant.PLATFORM, request.getHeader(AuthConstant.PLATFORM));
//        if (!methodName.contains("FileUpload")) {
//            for (Object arg : args) {
//                if (!(arg instanceof String)) {
//                    paramMap.put("args", arg);
//                    break;
//                }
//            }
//        }
//        allParams.add(paramMap);
//        UserOpLog<JsonResultLogOps> userOpLog = new UserOpLog();
//        userOpLog.setMethod(methodName);
//        userOpLog.setCtime(beginTime / 1000);
//        userOpLog.setUrl(request.getRequestURL().toString());
//        String ip = WebUtils.getIpAddress(request);
//        userOpLog.setIp(ip);
//
//        Account account = AccountContext.getAccount();
//        int userId = Objects.isNull(account) ? -1 : account.getId();
//        Throwable ex = null;
//
//        try {
//
//            checkTooFrequentRequest(method, userId);
//
//            result = pjp.proceed();
//        } catch (BizException e) {
//            ex = e;
//            result = JsonResult.build(e.getCode(), e.getMsg());
//        } catch (ConstraintViolationException e) {
//            throw e;
//        } catch (Throwable e) {
//            ex = e;
//            log.error("exception: " + e.getMessage(), e);
//            result = JsonResult.build(ReturnCode.ERROR);
//            errorJsonResult = JsonResult.build(ReturnCode.ERROR, "错误日志", ExceptionUtil.stacktraceToString(e));
//        }
//
//        log.info("接口返回结果:{}", JsonUtils.toJsonString(result));
//        long costTime = System.currentTimeMillis() - beginTime;
//        log.info("请求耗时:{}ms", costTime);
//
//        if (result instanceof JsonResult) {
//            userOpLog.setResult(JsonResultHelper.convert2LogOps((JsonResult<?>) result));
//        } else {
//            userOpLog.setResult(JsonResultHelper.convert2LogOps(JsonResult.success("注意：返回值不是JsonResult类型", result)));
//        }
//
//        userOpLog.setuId(userId);
//        userOpLog.setParams(allParams);
//        userOpLog.setMs(costTime);
//        compositeLogProcessor.process(userOpLog);
//        if (errorJsonResult != null) {
//            UserOpLog<JsonResultLogOps> userOpLogError = BeanUtil.toBean(userOpLog, UserOpLog.class);
//            userOpLogError.setResult(JsonResultHelper.convert2LogOps(errorJsonResult));
//            userOpLogError.setMethod(userOpLog.getMethod() + "-Error");
//            compositeLogProcessor.process(userOpLogError);
//        }
//
//        // 返回类型
//        Class<?> methodReturnType = method.getReturnType();
//        // 返回类型和接口不一致
//        if (Objects.nonNull(result) && !methodReturnType.equals(result.getClass()) && Objects.nonNull(ex) && result instanceof JsonResult) {
//            JsonResult result1 = (JsonResult) result;
//            throw new BizException(result1.getCode(), result1.getMessage());
//        }
//        return result;
        return null;
    }

    /**
     * 判断是否太频繁的请求
     */
    public void checkTooFrequentRequest(Method method, int userId) {
        //如果方法没有@PreventFrequentRequest标注，或者用户id为空，则不检查是否太频繁的请求
        if (!method.isAnnotationPresent(PreventFrequentRequest.class) || userId == -1) {
            return;
        }
        PreventFrequentRequest annotation = method.getAnnotation(PreventFrequentRequest.class);
        String uniqueId = request.getRequestURI() + "-" + userId;
        tooFrequentRequest(uniqueId, annotation.value());
    }

    /**
     * 判断是否太频繁的请求
     *
     * @param uniqueId 唯一标识，建议：url+userId
     * @param interval 允许的时间间隔（秒）
     * @return true/false
     */
    private void tooFrequentRequest(String uniqueId, int interval) {
        boolean result = false;
        String cacheKey = "apiReq:" + ApplicationContextHolder.getApplicationContext().getApplicationName() + "-" + uniqueId;
        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps(cacheKey);
        if ("X".equals(ops.get())) {
            result = true;
        } else {
            ops.set("X", interval, TimeUnit.SECONDS);
        }
        if (result) {
            throw new AuthException(ReturnCode.TOO_FREQUENT_REQUEST);
        }
    }
}