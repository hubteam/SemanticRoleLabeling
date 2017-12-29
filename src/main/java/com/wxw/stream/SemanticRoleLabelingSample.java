package com.wxw.stream;

import java.util.ArrayList;
import java.util.List;

import com.wxw.tree.SemanticRoleTree;

/**
 * 语义角色标注的样本类格式
 * @author 王馨苇
 *
 * @param <T> 语义角色标注树及其子类
 */
public class SemanticRoleLabelingSample<T extends SemanticRoleTree> {

	private List<T> roleTree;//带语义角色的子树序列
	private List<String> roleInfo = new ArrayList<String>();//角色标注信息
}
