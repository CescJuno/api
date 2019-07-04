package com.skt.api.bible.service;

import java.util.List;

import com.skt.api.bible.vo.Bible;

public interface BibleService {
	public List<Bible> getBibleWoori(String bid) throws Exception;
	public List<Bible> getBibleRev(String bid) throws Exception;
	public List<Bible> getBibleNiv(String bid) throws Exception;
}
