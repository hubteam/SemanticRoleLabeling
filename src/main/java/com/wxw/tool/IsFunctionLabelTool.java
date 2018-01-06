package com.wxw.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断当前标记是否是功能标记
 * @author 王馨苇
 *
 */
public class IsFunctionLabelTool {

	/**
	 * 判断是否是功能标记
	 * @param str 标记
	 * @return
	 */
	public static boolean isFunction(String str){
		List<String> list = new ArrayList<>();
		list.add("EXT");
		list.add("DIR");
		list.add("LOC");
		list.add("TMP");
		list.add("REC");
		list.add("PRD");
		list.add("NEG");
		list.add("MOD");
		list.add("ADV");
		list.add("MNR");
		list.add("CAU");
		list.add("PNC");
		list.add("DIS");
		return list.contains(str);
	}
}
