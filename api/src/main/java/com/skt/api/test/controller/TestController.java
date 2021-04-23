package com.skt.api.test.controller;

import com.skt.api.common.vo.CommonRes;
import com.skt.api.test.service.TestService;
import com.skt.api.test.vo.Test;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    protected static Logger log = Logger.getLogger(TestController.class.getName());

    @Resource(name="testService")
    private TestService testService;

    @Resource(name="messageSource")
    private MessageSource messageSource;

    @Resource(name="beanValidator")
    private Validator validator;

    //복수형/ID해야 객체 1개 가져옴
    @RequestMapping(value="/tests/{id}", method= RequestMethod.GET, produces="application/json; charset=utf-8")
    public @ResponseBody
    Test getTestInfo(@PathVariable @Min(1) int id) throws Exception{
        log.debug("getTestInfo");

        return testService.getTest(id);
    }

    //복수형 - 전체를 가져옴
    @RequestMapping(value="/tests", method=RequestMethod.GET, produces="application/json; charset=utf-8")
    public @ResponseBody
    List<Test> getAllTests() throws Exception{
        log.debug("getAllUsers");

        return testService.getAllTests();
    }


    @RequestMapping(value="/tests/{id}", method=RequestMethod.PUT, produces="application/json; charset=utf-8")
    public @ResponseBody
    CommonRes.Common setTestInfo(@PathVariable @Min(1) int id, @Valid @RequestBody Test test, BindingResult bindingResult) throws Exception{
        log.debug("setUserInfo");
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return testService.setTest(test);
    }


    @Transactional(rollbackFor=Exception.class)
    @RequestMapping(value="/tests/{id}", method=RequestMethod.DELETE, produces="application/json; charset=utf-8")
    public @ResponseBody
    CommonRes.Common deleteTestInfo(@PathVariable @Min(1) int id, BindingResult bindingResult) throws Exception{
        log.debug("deleteUserInfo");
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return testService.deleteTest(id);
    }

    @Transactional(rollbackFor=Exception.class)
    @RequestMapping(value="/tests", method=RequestMethod.POST, produces="application/json; charset=utf-8")
    public @ResponseBody
    CommonRes.Common createTestInfo(@Valid @RequestBody Test test, BindingResult bindingResult) throws Exception{
        log.debug("createUserInfo");
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return testService.createTest(test);
    }
}
