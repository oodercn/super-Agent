package net.ooder.nexus.skillcenter.exception;

import net.ooder.nexus.skillcenter.model.ResultModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 全局异常处理器 - 符合 ooderNexus 规范
 * 捕获所有未处理的异常并转换为标准格式的错误响应
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResultModel<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        return ResultModel.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResultModel<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return ResultModel.badRequest(e.getMessage());
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResultModel<?> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        return ResultModel.error(500, "系统内部错误：空指针异常");
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public ResultModel<?> handleSQLException(SQLException e, HttpServletRequest request) {
        return ResultModel.error(500, "数据库操作异常：" + e.getMessage());
    }

    /**
     * 处理IO异常
     */
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResultModel<?> handleIOException(IOException e, HttpServletRequest request) {
        return ResultModel.error(500, "IO操作异常：" + e.getMessage());
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResultModel<?> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        return ResultModel.notFound("请求的接口不存在：" + e.getRequestURL());
    }

    /**
     * 处理其他所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultModel<?> handleException(Exception e, HttpServletRequest request) {
        // 记录异常日志
        System.err.println("全局异常捕获：" + e.getMessage());
        e.printStackTrace();
        
        return ResultModel.error(500, "系统内部错误：" + e.getMessage());
    }
}