package com.skt.api.user.service;

import java.util.List;

import com.skt.api.common.vo.CommonRes.Common;
import com.skt.api.user.vo.User;

public interface UserService {
	
	public User getUser(int userNo) throws Exception;
	public Common setUser(int userNo, User user) throws Exception;
	public Common deleteUser(int userNo) throws Exception;
	public Common createUser(User user) throws Exception;
	public List<User> getAllUsers() throws Exception;
	
}
