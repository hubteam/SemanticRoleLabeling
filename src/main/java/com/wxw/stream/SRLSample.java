package com.wxw.stream;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(addtionalContext);
		result = prime * result + ((argumenttree == null) ? 0 : argumenttree.hashCode());
		result = prime * result + ((labelinfo == null) ? 0 : labelinfo.hashCode());
		result = prime * result + ((predicatetree == null) ? 0 : predicatetree.hashCode());
		result = prime * result + ((tree == null) ? 0 : tree.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SRLSample other = (SRLSample) obj;
		if (!Arrays.deepEquals(addtionalContext, other.addtionalContext))
			return false;
		if (argumenttree == null) {
			if (other.argumenttree != null)
				return false;
		} else if (!argumenttree.equals(other.argumenttree))
			return false;
		if (labelinfo == null) {
			if (other.labelinfo != null)
				return false;
		} else if (!labelinfo.equals(other.labelinfo))
			return false;
		if (predicatetree == null) {
			if (other.predicatetree != null)
				return false;
		} else if (!predicatetree.equals(other.predicatetree))
			return false;
		if (tree == null) {
			if (other.tree != null)
				return false;
		} else if (!tree.equals(other.tree))
			return false;
		return true;
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
	
	/**
	 * 得到所有的非终结点序号
	 * @param tree
	 * @return
	 */
	public static List<String> getLeaf(TreeNode tree){
		List<String> list = new ArrayList<>();
		if(tree.getChildren().size() == 0){
			list.add(tree.getNodeName());
		}else{
			for (TreeNode node : tree.getChildren()) {
				list.addAll(getLeaf(node));
			}
		}
		return list;
	}
	
	/**
	 * 给定动词或动词短语，得到对应下标
	 * @param tree
	 * @param predicateinfo
	 * @return
	 */
	public static int[] getPredicateIndex(TreeNode tree,String[] predicateinfo){
		List<String> list = getLeaf(tree);
		int[] index = new int[predicateinfo.length];
		for (int i = 0; i < index.length; i++) {
			index[i] = list.indexOf(predicateinfo[i]);
		}
		return index;
	}
	
	/**
	 * 将给的谓词信息包装成训练时候解析的文本的样式：大于等于六列
	 * 【文本名称 在文本中的序号 谓词索引 标注标准 框架集 动词的一些描述 论元信息。。。】
	 * 在这里没有的信息用NULL占一个位置，这样解析时候就可以按照和训练文本一样的解析方式，解析出一样的样本类
	 * @param tree
	 * @param predicateinfo
	 * @return
	 */
	public static String predicateToTrainSample(TreeNode tree,String[] predicateinfo){
		int[] index = getPredicateIndex(tree,predicateinfo);
		int min = index[0];
		for (int i = 1; i < index.length; i++) {
			if(index[i] < min){
				min = index[i];
			}
		}
		String str = "";
		for (int i = 0; i < index.length; i++) {
			if(i == index.length - 1){
				str += index[i]+":"+0;
			}else{
				str += index[i]+":"+0+",";
			}
		}
		String res = "NULL"+" "+"NULL"+" "+min+" "+"NULL"+" "+"NULL"+" "+"NULL"+" "+str;
		return res;
	}
	
	/**
	 * 将给的谓词索引信息包装成训练时候解析的文本的样式：大于等于六列
	 * 【文本名称 在文本中的序号 谓词索引 标注标准 框架集 动词的一些描述 论元信息。。。】
	 * 在这里没有的信息用NULL占一个位置，这样解析时候就可以按照和训练文本一样的解析方式，解析出一样的样本类
	 * @param tree
	 * @param predicateinfo
	 * @return
	 */
	public static String indexToTrainSample(int[] index){
		int min = index[0];
		for (int i = 1; i < index.length; i++) {
			if(index[i] < min){
				min = index[i];
			}
		}
		String str = "";
		for (int i = 0; i < index.length; i++) {
			if(i == index.length - 1){
				str += index[i]+":"+0;
			}else{
				str += index[i]+":"+0+",";
			}
		}
		String res = "NULL"+" "+"NULL"+" "+min+" "+"NULL"+" "+"NULL"+" "+"NULL"+" "+str;
		return res;
	}
}

