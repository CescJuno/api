package com.skt.api.bible.service;

import java.util.List;

import com.skt.api.bible.vo.Bible;
import com.skt.api.bible.vo.BibleBook;

public interface BibleService {
	public List<BibleBook> getBibleBook(String typ) throws Exception;
	public List<Object> getBibleWoori(String bid) throws Exception;
	public List<Object> getBibleRev(String bid) throws Exception;
	public List<Object> getBibleNiv(String bid) throws Exception;
}
