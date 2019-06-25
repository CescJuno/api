package com.skt.api.common.util;

import java.security.MessageDigest;

public class SHA512Crypt {

	public static String getEncrypt(String str) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			byte[] pb = messageDigest.digest(str.getBytes());
			StringBuffer sb = new StringBuffer(pb.length << 1);
			
			for(int i=0, iend=pb.length; i<iend; i++) {
				int val = (pb[i] + 256) & 0xff;
				sb.append(Integer.toHexString(val>>4)).append(Integer.toHexString(val & 0xff));
			}
			return sb.toString();
		}catch(Exception e) {
			return null;
		}
	}
}
