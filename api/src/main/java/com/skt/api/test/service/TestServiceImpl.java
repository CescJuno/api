package com.skt.api.test.service;

import com.skt.api.common.resource.CodeConstants;
import com.skt.api.common.util.SHA512Crypt;
import com.skt.api.common.vo.CommonRes;
import com.skt.api.test.vo.Test;
import com.skt.api.user.vo.User;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("testService")
public class TestServiceImpl implements TestService{

    protected static Logger log = Logger.getLogger(TestServiceImpl.class.getName());
    private final String namespace ="test";

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Resource(name="messageSource")
    private MessageSource messageSource;

    @Override
    public Test getTest(int no) throws Exception {
        Map<String, Object> paramMap = new HashMap<String,Object>();
        paramMap.put("no",no);
        Test testInfo = sqlSession.selectOne(namespace+".selectTest",paramMap);

        return testInfo;
    }

    @Override
    public CommonRes.Common setTest(Test test) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("no",test.getNo());
        paramMap.put("title",test.getTitle());
        int statement = sqlSession.update(namespace+".updateTest",paramMap);
        log.debug(statement);
        CommonRes.Common common = new CommonRes.Common();
        if(statement == 1){
            common.setResultCode(CodeConstants.RESULT_CODE.SUCCESS);
            common.setResultMsg(CodeConstants.RESULT_MSG.SUCCESS);
        }
        else{
            common.setResultCode(CodeConstants.RESULT_CODE.ERROR_VALID_PARAM);
            common.setResultMsg(CodeConstants.RESULT_MSG.ERROR_VALID_PARAM);
        }
        return common;
    }

    @Override
    public CommonRes.Common deleteTest(int no) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("no", no);
        int statement = sqlSession.delete(namespace+".deleteTest", paramMap);
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
    public CommonRes.Common createTest(Test test) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("no", test.getNo());
        paramMap.put("title", test.getTitle());

        Test testInfo = sqlSession.selectOne(namespace+".selectTest", paramMap);
        CommonRes.Common common = new CommonRes.Common();
        if(testInfo == null) {
            int statement = sqlSession.insert(namespace+".insertTest", paramMap);
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
    public List<Test> getAllTests() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<Test> testList = sqlSession.selectList(namespace+".selectTestList", paramMap);

        return testList;
    }

}
