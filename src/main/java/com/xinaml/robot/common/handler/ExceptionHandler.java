package com.xinaml.robot.common.handler;

import com.xinaml.robot.common.custom.exception.ActException;
import com.xinaml.robot.common.custom.result.ActResult;
import com.xinaml.robot.common.utils.ResponseUtil;
import com.xinaml.robot.common.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常统一处理
 *
 * @author lgq
 * @date 2018/4/15
 */
@Component
public class ExceptionHandler extends AbstractHandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);
    private static final String JSON_CONTEXT = "text/html;charset=utf-8";
    private static final int SUCCESS_STATUS = 200;
    private static final int EXCEPTION_STATUS = 500;
    private static final int EXCEPTION_CODE = -1;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        e.printStackTrace();
        ActResult rs = new ActResult();
        httpServletResponse.setContentType(JSON_CONTEXT);
        if (e instanceof ActException) {
            String code = StringUtils.substringBefore(e.getMessage(), "@");
            if (StringUtils.isNumeric(code)) {
                rs.setCode(Integer.parseInt(code));
                rs.setMsg(StringUtils.substringAfter(e.getMessage(), "@"));
            } else {
                rs.setCode(1);
            }
            httpServletResponse.setStatus(SUCCESS_STATUS);

        } else {
            httpServletResponse.setStatus(EXCEPTION_STATUS);
            rs.setCode(EXCEPTION_CODE);
        }
        if (!StringUtil.isChinese(e.getMessage())) {
            rs.setMsg("服务器错误！");
        } else {
            if (StringUtils.isBlank(rs.getMsg())) {
                rs.setMsg(e.getMessage());
                LOGGER.error(o + rs.getMsg());
            }
        }
        LOGGER.error(rs.getMsg());
        ResponseUtil.writeData(rs);
        return new ModelAndView();
    }


}
