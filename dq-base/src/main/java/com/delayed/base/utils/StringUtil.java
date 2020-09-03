package com.delayed.base.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static Pattern pattern = Pattern.compile("[0-9]{1,}");
	private static Pattern numberPattern = Pattern.compile("([0-9].{5,5})");

	
	public static boolean isNull(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	/**
	 * 截取一键加群的href部分
	 * 
	 * @param str
	 * @return
	 * 
	 */
	public static String getLianjie(String str) {
		if (isNotNull(str)) {

			if (str.contains("href")) {
				int start = str.indexOf("href=");
				int end = str.indexOf("><img");
				String sString = str.substring(start, end);
				String[] newString = sString.split("\"");
				str = newString[1];
			}
		}
		return str;
	}

	/**
	 * 判断一个字符串是否都为数字
	 * @param strNum
	 * @return
	 */
	public static boolean isDigit(String strNum) {  
	    Matcher matcher = pattern.matcher((CharSequence) strNum);  
	    return matcher.matches();  
	}

	


	/**
	 * 判断参数是否有空值
	 * @return
	 */
	public static boolean isHaveNull(String ... args) {
		int len = args.length;
		for(int i=0;i<len;i++) {
			if(StringUtil.isNull(args[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 找到字符串第一个数字
	 * @param str
	 * @return 
	 */
	public static String findFastNumber(String str) {
		Matcher matcher = numberPattern.matcher(str);
		boolean find = matcher.find();
		if(find) {
			return matcher.group(1).substring(0, 1);
		}
		return "-1";
	}

	
	/**
	 * 判断 str2 是否被 str 包含
	 * @param str
	 * @param str2
	 * @return
	 */
	public static boolean isInclud(String str,String str2) {
		if(str ==null || str2 ==null){
			return false;
		}
		return str.indexOf(str2)!=-1?true:false;
	}


	/**
	 * 超过size 后面的小数点替代
	 *
	 * @param size
	 * @return
	 */
	public static String simplify(String name,int size){
		if(name == null || name.trim().length() == 0)
			return null;

		StringBuffer reslut = new StringBuffer(name);
		if((name.length() <= size))
			return reslut.toString();
		//截取
		reslut = new StringBuffer(reslut.toString().substring(0,size));
		//拼接小数点
		reslut.append("...");
		return reslut.toString();
	}




	/**
	 * 删除字符
	 * @return
	 */
	public static String deleteString(String str, char delChar){
		String delStr = "";
		final String strTable = "|^$*+?.(){}\\";
		String tmpRegex = "[";
		for (int i = 0; i < strTable.length(); i++) {
			if (strTable.charAt(i) == delChar) {
				tmpRegex += "//";
				break;
			}
		}
		tmpRegex += delChar + "]";
		delStr = str.replaceAll(tmpRegex, "");
		return delStr;
	}


}


