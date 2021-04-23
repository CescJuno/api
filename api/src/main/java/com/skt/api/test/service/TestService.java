package com.skt.api.test.service;

import com.skt.api.common.vo.CommonRes;
import com.skt.api.test.vo.Test;
import com.skt.api.user.vo.User;

import java.util.List;

public interface TestService {

    public Test getTest(int no) throws Exception;
    public CommonRes.Common setTest(Test test) throws Exception;
    public CommonRes.Common deleteTest(int no) throws Exception;
    public CommonRes.Common createTest(Test test) throws Exception;
    public List<Test> getAllTests() throws Exception;

}
