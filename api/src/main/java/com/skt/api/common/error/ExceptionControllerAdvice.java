package com.skt.api.common.error;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.skt.api.common.logger.LoggerInterceptor;
import com.skt.api.common.resource.CodeConstants;
import com.skt.api.common.vo.ErrorResponse;
import com.skt.api.user.controller.UserController;



@ControllerAdvice
@EnableWebMvc
public class ExceptionControllerAdvice {
	protected static Logger log = Logger.getLogger(ExceptionControllerAdvice.class.getName()); 
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	/**
	 * ETC Exception All Error
	 * @param ex
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllException(final Exception ex, final HttpServletRequest request) throws Exception {
		log.error("Request raised " + ex.getClass().getSimpleName());
		
		ErrorResponse.Common common = new ErrorResponse.Common();
		
		common.setResultCode(CodeConstants.RESULT_CODE.ERROR_HTTP_METHOD);
		common.setResultMsg(messageSource.getMessage(CodeConstants.RESULT_CODE.ERROR_HTTP_METHOD, null, Locale.getDefault()));
		/*
		TloModel tloModel = TloAttributeManager.getAttribute();
		if(tloModel != null) {
			log.error("error");
			tloModel.setResultCode(common.getResultCode());
			tloModel.setLogTime(TimeUtil.timeStamp(14));
			tloModel.setRspTime(TimeUtil.timeStamp(17));
			TloFileWriter.write(tloModel.toString());
		}else {
			return tloErrorEntity(common);
		}
		*/
		return new ResponseEntity<Object>(new ErrorResponse(common), HttpStatus.OK);
	}
	
	/**
	 * Parameter Validation Error
	 * @param ex
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<Object> handleBindException(final BindException ex) throws Exception {
		
		ErrorResponse.Common common = new ErrorResponse.Common();
		
		FieldError fe = ex.getFieldError();
		
		common.setResultCode(CodeConstants.RESULT_CODE.ERROR_VALID_PARAM);
		common.setResultMsg(messageSource.getMessage(CodeConstants.RESULT_CODE.ERROR_VALID_PARAM, null, Locale.getDefault()) + "("+ fe.getField()+")");
		
		
		
		return new ResponseEntity<Object>(new ErrorResponse(common), HttpStatus.OK);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws Exception {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<Object> handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException ex) throws Exception {
		ErrorResponse.Common common = new ErrorResponse.Common();
		
		common.setResultCode(CodeConstants.RESULT_CODE.ERROR_HTTP_MULTIPART);
		common.setResultMsg(messageSource.getMessage(CodeConstants.RESULT_CODE.ERROR_HTTP_MULTIPART, null, Locale.getDefault()));
		
		
		
		return new ResponseEntity<Object>(new ErrorResponse(common), HttpStatus.OK);
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex) throws Exception {
		ErrorResponse.Common common = new ErrorResponse.Common();
		
		common.setResultCode(CodeConstants.RESULT_CODE.ERROR_NOT_FOUND_URL);
		common.setResultMsg(messageSource.getMessage(CodeConstants.RESULT_CODE.ERROR_NOT_FOUND_URL, null, Locale.getDefault()));
		
		
		return new ResponseEntity<Object>(new ErrorResponse(common), HttpStatus.OK);
	}
}
