package com.wxw.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.TreeNode;

/**
 * 样本类
 * @author 王馨苇
 *
 * @param <T>
 */
public class SRLSample <T extends TreeNode>{
	private T tree;//句法分析得到的树
	private List<TreeNodeWrapper<T>> argumenttree = new ArrayList<>();//论元为根的树
	private List<TreeNodeWrapper<T>> predicatetree = new ArrayList<>();//谓词为根的树
	private List<String> labelinfo = new ArrayList<String>();//角色标注论元标记信息
	private String[][] addtionalContext;
	private boolean isPruning;
	
	public SRLSample(T tree, List<TreeNodeWrapper<T>> argumenttree,  List<TreeNodeWrapper<T>> predicatetree, List<String> labelinfo) {
		this(tree, argumenttree,predicatetree,labelinfo,null);
	}
	
	public SRLSample(T tree, List<TreeNodeWrapper<T>> argumenttree,  List<TreeNodeWrapper<T>> predicatetree, List<String> labelinfo,String[][] addtionalContext) {
		this.tree = tree;
		this.argumenttree = Collections.unmodifiableList(argumenttree);
		this.labelinfo = Collections.unmodifiableList(labelinfo);
        this.predicatetree = Collections.unmodifiableList(predicatetree);
        String[][] ac;
        if (addtionalContext != null) {
            ac = new String[addtionalContext.length][];
            for (int i = 0; i < addtionalContext.length; i++) {
                ac[i] = new String[addtionalContext[i].length];
                System.arraycopy(addtionalContext[i], 0, ac[i], 0,
                		addtionalContext[i].length);
            }
        } else {
            ac = null;
        }
        this.addtionalContext = ac;
	}
	
	/**
	 * 获取句法分析得到的树
	 * @return
	 */
	public T getTree(){
		return this.tree;
	}
	
	/**
	 * 获取以论元为树根的树
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TreeNodeWrapper<T>[] getArgumentTree(){
		return this.argumenttree.toArray(new TreeNodeWrapper[this.argumenttree.size()]);
	}
	
	/**
	 * 获取以谓词为树根的树
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TreeNodeWrapper<T>[] getPredicateTree(){
		return this.predicatetree.toArray(new TreeNodeWrapper[this.predicatetree.size()]);
	}
	
	/**
	 * 获取论元的标记的信息
	 * @return
	 */
	public String[] getLabelInfo(){
		return this.labelinfo.toArray(new String[this.labelinfo.size()]);
	}
	
	/**
	 * 将类别转换为识别阶段要用的类别
	 * 说明：
	 * （1）对非类别解析成NULL类别的，识别阶段为二分类
	 * （2）对非类别解析成NULL_1 NULL0 NULL1的，识别阶段就是多分类
	 * @return
	 */
	public String[] getIdentificationLabelInfo(){
		String[] recognizeLabelInfo = new String[labelinfo.size()];
		for (int i = 0; i < recognizeLabelInfo.length; i++) {
			if(labelinfo.get(i).contains("NULL")){
				recognizeLabelInfo[i] = labelinfo.get(i);
			}else{
				recognizeLabelInfo[i] = "YES";
			}
		}
		return recognizeLabelInfo;
	}
	
	public String[][] getAdditionalContext(){
		return this.addtionalContext;
	}
	
	/**
	 * 设置是否进行了剪枝
	 * @param isPruning
	 */
	public void setPruning(boolean isPruning){
		this.isPruning = isPruning;
	}
	
	/**
	 * 获取是否剪枝的设置信息
	 * @return
	 */
	public boolean getIsPruning(){
		return this.isPruning;
	}
	
	/**
	 * 过滤出标记中为论元的标记
	 * @param label 标记序列
	 * @return
	 */
	public static List<Integer> filterNotNULLLabelIndex(String[] label){
		
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < label.length; i++) {
			if(label[i].contains("NULL")){
				
			}else{
				list.add(i);
			}
		}
		return list;
	}
	
	/**
	 * 根据给定的索引数组，获取索引对应的标记
	 * @param index 索引数组
	 * @return
	 */
	public static String[] getLabelFromIndex(String[] label,List<Integer> index){
		String[] newlabel = new String[index.size()];
		for (int i = 0; i < index.size(); i++) {
			if(label[index.get(i)].contains("NULL")){
				newlabel[i] = "NULL";
			}else{
				newlabel[i] = label[index.get(i)];
			}
		}
		return newlabel;
	}
	
	/**
	 * 过滤出不是论元，却被识别成论元的成分的索引,这些成分在分类步骤中标签作为NULL
	 * @param identificationLabelRef 参考的识别阶段的结果
	 * @param identificationLabelPre 预测出来的识别阶段的结果
	 * @return
	 */
	public static List<Integer> filter(String[] identificationLabelRef,String[] identificationLabelPre){
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < identificationLabelPre.length && i < identificationLabelRef.length; i++) {
			if(identificationLabelPre[i].equals("YES") && (identificationLabelRef[i].contains("NULL"))){
				list.add(i);
			}
		}
		return list;
	}
	
	/**
	 * 根据得到的是论元的下标，获取对应的子树
	 * @param argumenttree 论元子树序列
	 * @param index 索引数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T extends TreeNode> TreeNodeWrapper<T>[] getArgumentTreeFromIndex(TreeNodeWrapper<T>[] argumenttree,List<Integer> index){
		List<TreeNodeWrapper<T>> list = new ArrayList<>();
		for (int i = 0; i < index.size(); i++) {
			list.add(argumenttree[index.get(i)]);
		}
		return list.toArray(new TreeNodeWrapper[list.size()]);
	}
}

