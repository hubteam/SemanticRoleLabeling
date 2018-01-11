package com.wxw.tool;

import java.util.HashSet;

/**
 * 判断是否是数字的工具类
 * @author 王馨苇
 *
 */
public class IsDigitTool {

	private static HashSet<Character> hsalbdigit = new HashSet<>();
	
	static{
		//罗列了半角和全角的情况
		String albdigits = "０１２３４５６７８９0123456789";
		for (int i = 0; i < albdigits.length(); i++) {
			hsalbdigit.add(albdigits.charAt(i));
		}
	}
	
	//判断是否是数字【中文数字，阿拉伯数字（全角和半角）】
	public static boolean isDigit(char c){
		if(hsalbdigit.contains(c)){
			return true;
		}else{
			return false;
		}	
	}
}
