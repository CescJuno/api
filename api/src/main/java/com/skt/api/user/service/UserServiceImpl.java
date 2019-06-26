package com.skt.api.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.skt.api.common.resource.CodeConstants;
import com.skt.api.common.util.SHA512Crypt;
import com.skt.api.common.vo.CommonRes;
import com.skt.api.common.vo.CommonRes.Common;
import com.skt.api.user.vo.User;


@Service("userService")
public class UserServiceImpl implements UserService {

	protected static Logger log = Logger.getLogger(UserServiceImpl.class.getName()); 
	private final String namespace = "user";
	
    @Autowired
    private SqlSessionTemplate sqlSession;
    
	@Resource(name="messageSource")
	private MessageSource messageSource;
    
	@Override
	public List<User> getAllUsers() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<User> userList = sqlSession.selectList(namespace+".selectUserList", paramMap);
		
		return userList;
	}
	
	@Override
	public User getUser(int userNo) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userNo", userNo);
		User userInfo = sqlSession.selectOne(namespace+".selectUser", paramMap);
		
		return userInfo;
	}
	

	@Override
	public Common setUser(int userNo, User user) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userNo", userNo);
		paramMap.put("userId", user.getUserId());
		paramMap.put("ctn", user.getCtn());
		int statement = sqlSession.update(namespace+".updateUser", paramMap);
		log.debug(statement);
		CommonRes.Common common = new CommonRes.Common();
		if(statement == 1) {
			common.setResultCode(CodeConstants.RESULT_CODE.SUCCESS);
			common.setResultMsg(CodeConstants.RESULT_MSG.SUCCESS);
		}else {
			common.setResultCode(CodeConstants.RESULT_CODE.ERROR_VALID_PARAM);
			common.setResultMsg(CodeConstants.RESULT_MSG.ERROR_VALID_PARAM);
		}
		return common;
	}
	

	@Override
	public Common createUser(User user) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", user.getUserId());
		paramMap.put("userPwd", SHA512Crypt.getEncrypt(user.getUserPwd()));
		paramMap.put("ctn", user.getCtn());
		
		User userInfo = sqlSession.selectOne(namespace+".selectUserByEmail", paramMap);
		CommonRes.Common common = new CommonRes.Common();
		if(userInfo == null) {
			int statement = sqlSession.insert(namespace+".insertUser", paramMap);
			log.debug(statement);
			if(statement == 1) {
				common.setResultCode(CodeConstants.RESULT_CODE.SUCCESS);
				common.setResultMsg(CodeConstants.RESULT_MSG.SUCCESS);
			}else {
				common.setResultCode(CodeConstants.RESULT_CODE.ERROR_VALID_PARAM);
				common.setResultMsg(CodeConstants.RESULT_MSG.ERROR_VALID_PARAM);
			}
			return common;
		}

		common.setResultCode(CodeConstants.RESULT_CODE.ERROR_DUPLICATION_EMAL);
		common.setResultMsg(CodeConstants.RESULT_MSG.ERROR_DUPLICATION_EMAL);
		
		
		return common;
	}
	
	@Override
	public Common deleteUser(int userNo) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userNo", userNo);
		int statement = sqlSession.delete(namespace+".deleteUser", paramMap);
		log.debug(statement);
		CommonRes.Common common = new CommonRes.Common();
		if(statement == 1) {
			common.setResultCode(CodeConstants.RESULT_CODE.SUCCESS);
			common.setResultMsg(CodeConstants.RESULT_MSG.SUCCESS);
		}else {
			common.setResultCode(CodeConstants.RESULT_CODE.ERROR_VALID_PARAM);
			common.setResultMsg(CodeConstants.RESULT_MSG.ERROR_VALID_PARAM);
		}
		return common;
	}
}
