package com.wxw.feature;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;

/**
 * 为论元识别阶段生成特征
 * @author 王馨苇
 *
 */
public class SRLContextGeneratorConfForIdentification implements SRLContextGenerator{

	private boolean pathSet; 	
	private boolean headwordSet;
	private boolean headwordposSet;	
	private boolean predicateAndHeadwordSet;   
	private boolean predicateAndPhrasetypeSet;
	private boolean pathlengthSet;
	private boolean phrasetypeSet;
	private boolean partialpathSet;
	private boolean predicateSet;
	private boolean predicateposSet;
	private boolean predicateAndPathSet;
	private boolean headwordAndPhrasetypeSet;
	private boolean headwordAndpredicateAndpathSet;
			
	/**
	 * 无参构造
	 * @throws IOException 		 
	 */	
	public SRLContextGeneratorConfForIdentification() throws IOException{
		Properties featureConf = new Properties();	
		InputStream featureStream = SRLContextGeneratorConfForIdentification.class.getClassLoader().getResourceAsStream("com/wxw/run/feature.properties");	
		featureConf.load(featureStream);
		init(featureConf);
		
	}
			
	/**
	 * 有参构造
	 * @param properties 配置文件
	 */	
	public SRLContextGeneratorConfForIdentification(Properties properties){	
		init(properties);
	}

	/**
	 * 根据配置文件中的信息初始化变量
	 * @param properties
	 */
	private void init(Properties config) {
			
		pathlengthSet = (config.getProperty("identify.pathlength", "true").equals("true"));
		pathSet = (config.getProperty("identify.path", "true").equals("true"));
		headwordSet = (config.getProperty("identify.headword", "true").equals("true"));
		headwordposSet = (config.getProperty("identify.headwordpos", "true").equals("true"));
		predicateAndHeadwordSet = (config.getProperty("identify.predicateAndHeadword", "true").equals("true"));		
		predicateAndPhrasetypeSet = (config.getProperty("identify.predicateAndPhrasetype", "true").equals("true"));		
		
		phrasetypeSet = (config.getProperty("identify.phrasetype", "true").equals("true"));
		partialpathSet = (config.getProperty("identify.partialpath", "true").equals("true"));
		predicateSet = (config.getProperty("identify.predicate", "true").equals("true"));
		predicateposSet = (config.getProperty("identify.predicatepos", "true").equals("true"));
		predicateAndPathSet = (config.getProperty("identify.predicateAndPath", "true").equals("true"));		
		headwordAndPhrasetypeSet = (config.getProperty("identify.headwordAndPhrasetype", "true").equals("true"));	
		headwordAndpredicateAndpathSet = (config.getProperty("identify.headwordAndpredicateAndpath", "true").equals("true"));	
	}
	
	/**
	 * 用于训练句法树模型的特征
	 */
	@Override
	public String toString() {
		
		return "SRLContextGeneratorConfForIdentification{" +
                ", pathSet=" + pathSet + ", pathlengthSet=" + pathlengthSet + 
                ", headwordSet=" + headwordSet + ", headwordposSet=" + headwordposSet + 
                ", predicateAndHeadwordSet=" + predicateAndHeadwordSet +  
                ", predicateAndPhrasetypeSet=" + predicateAndPhrasetypeSet +
                ", phrasetypeSet=" + phrasetypeSet +  
                ", partialpathSet=" + partialpathSet +
                ", predicateSet=" + predicateSet +  
                ", predicateposSet=" + predicateposSet +
                ", predicateAndPathSet=" + predicateAndPathSet +  
                ", headwordAndPhrasetypeSet=" + headwordAndPhrasetypeSet +
                ", headwordAndpredicateAndpathSet=" + headwordAndpredicateAndpathSet +
                '}';
	}	
	
	/**
	 * 为测试语料生成上下文特征
	 * @param i 当前位置
	 * @param roleTree 以谓词和论元为根的树数组
	 * @param semanticinfo 语义角色信息
	 * @param labelinfo 标记信息
	 * @return
	 */
	public String[] getContext(int i, TreeNodeWrapper<HeadTreeNode>[] argumenttree , String[] labelinfo, TreeNodeWrapper<HeadTreeNode>[] predicatetree) {
		List<String> features = new ArrayList<String>();
		HeadTreeNode headtree = predicatetree[0].getTree();
		while(headtree.getChildren().size() != 0){
			headtree = headtree.getChildren().get(0);
		}
		String predicate = headtree.getNodeName();
		String path = getPath(predicatetree[0].getTree(),argumenttree[i].getTree());
		if(pathSet){
			features.add("path="+path);
		}
		if(pathlengthSet){
			features.add("pathlength="+getPathLength(path));
		}
		if(headwordSet){
			features.add("headword="+argumenttree[i].getTree().getHeadWords());
		}
		if(headwordposSet){
			features.add("headwordpos="+argumenttree[i].getTree().getHeadWordsPos());
		}
		if(predicateAndHeadwordSet){
			features.add("predicateAndHeadword="+predicate+"|"+argumenttree[i].getTree().getHeadWords());
		}
		if(predicateAndPhrasetypeSet){
			features.add("predicateAndPhrasetype="+predicate+"|"+argumenttree[i].getTree().getNodeName());
		}	

		if(phrasetypeSet){
			features.add("phrasetype="+argumenttree[i].getTree().getNodeName());
		}
		if(partialpathSet){
			features.add("partialpath="+getPartialPath(path));
		}
		if(predicateSet){
			features.add("predicate="+predicate);
		}
		if(predicateposSet){
			features.add("predicatepos="+headtree.getParent().getNodeName());
		}
		if(predicateAndPathSet){
			features.add("predicateAndpath="+predicate+"|"+path);
		}
		if(headwordAndPhrasetypeSet){
			features.add("headwordAndPhrasetype="+argumenttree[i].getTree().getHeadWords()+"|"+argumenttree[i].getTree().getNodeName());
		}
		if(headwordAndpredicateAndpathSet){
			features.add("headwordAndpredicateAndpath="+argumenttree[i].getTree().getHeadWords()+"|"+predicate+"|"+path);
		}
		String[] contexts = features.toArray(new String[features.size()]);
        return contexts;
	}
	
	/**
	 * 获得路径  
	 * @param predicatetree 以谓词为根节点的树 
	 * @param argumenttree 以论元为根节点的树
	 * @return
	 */
	private String getPath(HeadTreeNode predicatetree,HeadTreeNode argumenttree){
		String argumentpath = "";
		String predicatepath = "";
		String path = "";
		HeadTreeNode initargument = argumenttree;
		HeadTreeNode initpredicate = predicatetree;
		//找到共同的根节点
		while(!argumenttree.toString().equals(predicatetree.toString())){
			HeadTreeNode tree = predicatetree;
			while(!argumenttree.toString().equals(predicatetree.toString())){
				if(predicatetree.getParent() != null){
					predicatetree = predicatetree.getParent();
				}else{
					break;
				}
			}
			if(argumenttree.getParent() != null && !argumenttree.toString().equals(predicatetree.toString())){
				argumenttree = argumenttree.getParent();
				predicatetree = tree;
			}else{
				break;
			}
		}
		
		while(!argumenttree.toString().equals(initargument.toString())){
			argumentpath += initargument.getNodeName()+"↑";
			initargument = initargument.getParent();
		}
		
		while(!predicatetree.toString().equals(initpredicate.toString())){
			String temppath = "↓"+initpredicate.getNodeName();
			temppath += predicatepath;	
			predicatepath = temppath;
			initpredicate = initpredicate.getParent();
		}
		
		path += argumentpath+predicatetree.getNodeName()+predicatepath;
		return path;
	}

	/**
	 * 获取路径的长度
	 * @param path 路径
	 * @return
	 */
	public int getPathLength(String path){
		int count = 1;
		char[] c = path.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(c[i] == '↓' || c[i] == '↑'){
				count++;
			}
		}
		return count;
	}
	
	public String getPartialPath(String path){
		String partialpath = "";
		char[] c = path.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(c[i] == '↓'){
				break;
			}else{
				partialpath += c[i];
			}
		}
		return partialpath;
	}
	
	/**
	 * 为语料生成上下文特征
	 * @param i 当前位置
	 * @param argumenttree 以论元为根的树数组
	 * @param predicatetree 以谓词为根的树
	 * @param labelinfo 标记信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String[] getContext(int i, TreeNodeWrapper<HeadTreeNode>[] argumenttree, String[] labelinfo,
			Object[] predicatetree) {
		return getContext(i,argumenttree,labelinfo,(TreeNodeWrapper<HeadTreeNode>[])predicatetree);
	}
}
