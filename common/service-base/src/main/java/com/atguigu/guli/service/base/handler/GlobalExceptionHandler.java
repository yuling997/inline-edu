package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 统一异常处理：所有controller层抛出的异常都会再此捕获
     * 铺获Exception异常对象和Exception的子类异常对象
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error (Exception e){
        //打印异常跟踪栈，在控制台显示异常跟踪信息
//        e.printStackTrace();
        //遵循日志记录器的配置输出日志
//        log.error(e.getMessage());
        log.error(ExceptionUtils.getMessage(e));
        //返回异常结果的R对象
        return R.error();
    }

    /**
     * sql错误处理
     * @param e
     * @return
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException e){
//        e.printStackTrace();
//        log.error(e.getMessage());
        log.error(ResultCodeEnum.BAD_SQL_GRAMMAR.toString());
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    /**
     * json异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e){
//        e.printStackTrace();
//        log.error(e.getMessage());
        log.error(ResultCodeEnum.JSON_PARSE_ERROR.toString());
        log.error(ExceptionUtils.getMessage(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
