package com.shihuc.up.infra.mysql;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * @author chengsh05
 *
 * @Created: 2016-10-17
 * 
 * @Description
 * 
 * 				为了解决数据库连接时，数据库密码明文存在安全隐患的问题，对数据库密码进行加密处理。 逻辑很简单：
 *              在jdbc.properties文件中配置的密码，用加密后的字符串显示。
 *              但是真正用于数据库连接的密码，其实是jdbc.properties文件中配置的密码经过解密后的真实密码。 用户名也是如此逻辑。
 */
public class SecurityUtil {

	private final static String saltInfo = "@All, JOBS IS A GOOD MANAGER <!)";

	// 加密
	public static String transToBase64(String str) {
		byte[] b = null;
		String s = null;
		String rawStr = str + saltInfo;
		try {
			b = rawStr.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int saltIndex = result.indexOf(saltInfo);
		String finalRes = result.substring(0, saltIndex);
		return finalRes;
	}

	public static String transToBase64ByCount(String str) {
		str = SecurityUtil.transToBase64(str);
		//str = getNewString(str);
		return str;
	}
/**
 * 这个才是对的解密用户名  上面的是骗人的
 * @param str
 * @return
 */
	public static String getFromBase64ByCount(String str) {
		//str = getNewString(str);
		str = SecurityUtil.getFromBase64(str);
		return str;
	}

	@Deprecated
	public static String getNewString(String str) {
		char[] array = str.toCharArray();
		for (int i = 0; i < array.length; i++) {
			array[i] = (char) (array[i] ^ 4); // 对每个数组元素进行异或运算
		}
		return new String(array);
	}
	
	
	public static String getNewString2(String str) {
		return str;
	}
	

	public static void main(String[] args) {
		String str2 = getFromBase64("WE1saWFubHVveWlUS0BBbGwsIEpPQlMgSVMgQSBHT09EIE1BTkFHRVIgPCEp");
		System.out.println(str2);
		String str = transToBase64ByCount("shihuc@2019");
		System.out.println(str);

		// String str =
		// getFromBase64("WVdSdGFXNHhRRUZzYkN3Z1EwMVRJRWxUSUVFZ1ZFc2dUVUZPUVVkRlVpQThJU2s9QEFsbCwgQ01T\r\nIElTIEEgVEsgTUFOQUdFUiA8ISk=");
		/*str = getFromBase64ByCount("]SVpeS1EUS|wHGF@PRIcWRIcUWFQW}FJUQ1FV4RWM@slOU99");
		System.out.println(str);*/
	}

}
