package com.skt.api.bible.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.Min;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.skt.api.bible.service.BibleService;
import com.skt.api.bible.vo.Bible;

@RestController
public class BibleController {
	protected static Logger log = Logger.getLogger(BibleController.class.getName()); 

	@Resource(name="bibleService")
	private BibleService bibleService;
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="beanValidator")
	private Validator validator;

	@RequestMapping(value="/bible/woori/{id}", method=RequestMethod.GET, produces="application/json; charset=utf-8")
	public @ResponseBody List<Bible> getBibleWooriInfo(@PathVariable @Min(1) String id) throws Exception{
		log.debug("getBibleWooriInfo");
		
		return bibleService.getBibleWoori(id);
	}
	
	@RequestMapping(value="/bible/rev/{id}", method=RequestMethod.GET, produces="application/json; charset=utf-8")
	public @ResponseBody List<Bible> getBibleRevInfo(@PathVariable @Min(1) String id) throws Exception{
		log.debug("getBibleRevInfo");
		
		return bibleService.getBibleRev(id);
	}
	
	@RequestMapping(value="/bible/niv/{id}", method=RequestMethod.GET, produces="application/json; charset=utf-8")
	public @ResponseBody List<Bible> getBibleNivInfo(@PathVariable @Min(1) String id) throws Exception{
		log.debug("getBibleNivInfo");
		
		return bibleService.getBibleNiv(id);
	}
}
