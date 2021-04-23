package com.skt.api.member.service;

import java.util.List;

public interface MemberService {

    public List<Object> getMemberName(String id) throws Exception;
    public List<Object> getMemberAddr(String id) throws Exception;
    public List<Object> getMemberPwd(String id) throws Exception;
}
