package com.skt.api.common.vo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.skt.api.common.error.ExceptionControllerAdvice;

public class BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3873809383866904399L;
	protected static Logger log = Logger.getLogger(BaseModel.class.getName()); 

	@Override
	public String toString() {
		Class cls = this.getClass();
		Method[] arrMethod = cls.getMethods();
		StringBuilder sb = new StringBuilder(this.getClass().toString());
		sb.append(" => ");
		
		try {
			for(Method m : arrMethod) {
				if(m.getName().startsWith("get") && !m.getName().equals("getClass")) {
					sb.append(m.getName());
					sb.append(": ");
					sb.append(m.invoke(this, null));
					sb.append(", ");
					
				}
			}
		}catch(IllegalArgumentException e) {
			log.error("exception : ", e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.error("exception : ", e);
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("exception : ", e);
		}
		
		return sb.toString().substring(0, sb.length()-2);
	}
}
