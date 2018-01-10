package com.wxw.tool;

/**
 * 语义信息工具类
 * @author 王馨苇
 *
 */
public class RoleTool {

	int begin;
	int up;
	String role;
	
	public RoleTool(int begin,int up,String role){
		this.begin = begin;
		this.up = up;
		this.role = role;
	}
	
	public int getBegin(){
		return this.begin;
	}
	
	public int getUp(){
		return this.up;
	}
	
	public String getRole(){
		return this.role;
	}
}
