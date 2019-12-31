package com.shihuc.up.infra.mysql;

import com.alibaba.druid.util.DruidPasswordCallback;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * @author chengsh05
 * @Date: 2018/12/19 9:56
 *
 */

enum ACTION {
	ENC, DEC;
}

public class PasswordCallback extends DruidPasswordCallback {

	private static final long serialVersionUID = 3120366514460202332L;

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		String pwd = properties.getProperty("password");
		if (StringUtils.isNotBlank(pwd)) {
			try {
				// 这里的password是将jdbc.properties配置得到的密码进行解密之后的值所以这里的代码是将密码进行解密
				String password = SecurityUtil.getFromBase64(pwd);
				setPassword(password.toCharArray());
			} catch (Exception e) {
				setPassword(pwd.toCharArray());
			}
		}
	}

	// 请使用该方法加密后，把密文写入classpath:/config/jdbc.properties
	public static void main(String[] args) {
		System.out.println("加密操作，最终的输出，是放在jdbc.properties中的用户名和密码，防止明文传递");
		System.out.println(SecurityUtil.transToBase64("wmsodwh2018!"));
//		System.out.println(SecurityUtil.getFromBase64("V2hSb2JvdFRlYW0yMDE3IUBBbGwsIENNUyBJUyBBIFRLIE1BTkFHRVIgPCEp"));
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase(ACTION.ENC.name())) {
				System.out.println("Before ENC: " + args[1]);
				System.out.println("After ENC : " + SecurityUtil.transToBase64(args[1]));
			} else if (args[0].equalsIgnoreCase(ACTION.DEC.name())) {
				System.out.println("Before DEC: " + args[1]);
				System.out.println("After DEC : " + SecurityUtil.getFromBase64(args[1]));
			}
		} else {
			String promotion = "Usage: 输入一个待加密的字符串";
			System.out.println(promotion);
		}
	}

}
