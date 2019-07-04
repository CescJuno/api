package com.skt.api.bible.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skt.api.bible.vo.Bible;
import com.skt.api.common.resource.CodeConstants;


@Service("bibleService")
public class BibleServiceImpl implements BibleService{

	protected static Logger log = Logger.getLogger(BibleServiceImpl.class.getName()); 
	private final String namespace = "bible";
	
    @Autowired
    private SqlSessionTemplate sqlSession;
    
    @Override
	public List<Bible> getBibleWoori(String bid) throws Exception {
    	if(bid.startsWith("01") || bid.startsWith("02")) {
    		if(bid.length() == 7 || bid.length() == 10) {
    			Map<String, Object> paramMap = new HashMap<String, Object>();
    			paramMap.put("bid", bid);
    			List<Bible> bibleInfo = sqlSession.selectList(namespace+".selectBibleWoori", paramMap);

    			return bibleInfo;
    		}else {
        		throw new Exception();
        	}
		}else {
			throw new Exception();
		}
    	
	}

    @Override
	public List<Bible> getBibleRev(String bid) throws Exception {
    	if(bid.startsWith("01") || bid.startsWith("02")) {
    		if(bid.length() == 7 || bid.length() == 10) {
    			Map<String, Object> paramMap = new HashMap<String, Object>();
    			paramMap.put("bid", bid);
    			List<Bible> bibleInfo = sqlSession.selectList(namespace+".selectBibleRev", paramMap);

    			return bibleInfo;
    		}else {
        		throw new Exception();
        	}
		}else {
			throw new Exception();
		}
	}

    @Override
	public List<Bible> getBibleNiv(String bid) throws Exception {
    	if(bid.startsWith("01") || bid.startsWith("02")) {
    		if(bid.length() == 7 || bid.length() == 10) {
    			Map<String, Object> paramMap = new HashMap<String, Object>();
    			paramMap.put("bid", bid);
    			List<Bible> bibleInfo = sqlSession.selectList(namespace+".selectBibleNiv", paramMap);

    			return bibleInfo;
    		}else {
        		throw new Exception();
        	}
		}else {
			throw new Exception();
		}
	}
    
}
