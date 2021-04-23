package com.skt.api.member.service;

import com.skt.api.bible.service.BibleServiceImpl;
import org.apache.log4j.Logger;

import java.util.List;

public class MemberServiceImpl implements MemberService{

    protected static Logger log = Logger.getLogger(MemberServiceImpl.class.getName());
    private final String namespace = "member";

    @Override
    public List<Object> getMemberName(String id) throws Exception {
        return null;
    }

    @Override
    public List<Object> getMemberAddr(String id) throws Exception {
        return null;
    }

    @Override
    public List<Object> getMemberPwd(String id) throws Exception {
        return null;
    }
}
