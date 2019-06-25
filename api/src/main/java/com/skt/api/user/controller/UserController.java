package com.skt.api.user.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.skt.api.common.logger.LoggerInterceptor;
import com.skt.api.common.vo.CommonRes.Common;
import com.skt.api.user.service.UserService;
import com.skt.api.user.vo.User;


@RestController
public class UserController {

	protected static Logger log = Logger.getLogger(UserController.class.getName()); 

	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="beanValidator")
	private Validator validator;

	@RequestMapping(value="/users/{id}", method=RequestMethod.GET, produces="application/json; charset=utf-8")
	public @ResponseBody User getUserInfo(@PathVariable @Min(1) int id) throws Exception{
		log.debug("getUserInfo");
		
		return userService.getUser(id);
	}
	
	
	@RequestMapping(value="/users", method=RequestMethod.GET, produces="application/json; charset=utf-8")
	public @ResponseBody List<User> getAllUsers() throws Exception{
		log.debug("getAllUsers");
		
		return userService.getAllUsers();
	}
	

	@Transactional(rollbackFor=Exception.class)
	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT, produces="application/json; charset=utf-8")
	public @ResponseBody Common setUserInfo(@PathVariable @Min(1) int id, @Valid @RequestBody User user, BindingResult bindingResult) throws Exception{
		log.debug("setUserInfo");
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		return userService.setUser(id, user);
	}
	

	@Transactional(rollbackFor=Exception.class)
	@RequestMapping(value="/users/{id}", method=RequestMethod.DELETE, produces="application/json; charset=utf-8")
	public @ResponseBody Common deleteUserInfo(@PathVariable @Min(1) int id) throws Exception{
		log.debug("deleteUserInfo");
		return userService.deleteUser(id);
	}

	@Transactional(rollbackFor=Exception.class)
	@RequestMapping(value="/users", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody Common createUserInfo(@Valid @RequestBody User user, BindingResult bindingResult) throws Exception{
		log.debug("createUserInfo");
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		return userService.createUser(user);
	}
}
