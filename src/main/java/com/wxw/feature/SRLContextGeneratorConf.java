package com.wxw.feature;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.SRLHeadTreeNode;

/**
 * 生成上下文特征
 * @author 王馨苇
 *
 */
public class SRLContextGeneratorConf implements SRLContextGenerator{
	
	private boolean predicateSet;
	private boolean pathSet; 
	private boolean phrasetypeSet; 
	private boolean positionSet;  
	private boolean voiceSet;
	private boolean headwordSet;
	private boolean headwordposSet;
	private boolean subcategorizationSet; 
	private boolean firstargumentSet;   
	private boolean lastargumentSet;  
	private boolean predicateAndHeadwordSet;   
	private boolean predicateAndPhrasetypeSet;
		
		
	/**
	 * 无参构造
	 * @throws IOException 		 
	 */	
	public SRLContextGeneratorConf() throws IOException{
		Properties featureConf = new Properties();	
		InputStream featureStream = SRLContextGeneratorConf.class.getClassLoader().getResourceAsStream("com/wxw/run/feature.properties");	
		featureConf.load(featureStream);
		init(featureConf);
		
	}
			
	/**
	 * 有参构造
	 * @param properties 配置文件
	 */	
	public SRLContextGeneratorConf(Properties properties){	
		init(properties);
	}

		/**
		 * 根据配置文件中的信息初始化变量
		 * @param properties
		 */
		
	private void init(Properties config) {
			
		predicateSet = (config.getProperty("tree.predicate", "true").equals("true"));
		pathSet = (config.getProperty("tree.path", "true").equals("true"));
		phrasetypeSet = (config.getProperty("tree.phrasetype", "true").equals("true"));
		positionSet = (config.getProperty("tree.position", "true").equals("true"));
		voiceSet = (config.getProperty("tree.voice", "true").equals("true"));
		headwordSet = (config.getProperty("tree.headword", "true").equals("true"));
		headwordposSet = (config.getProperty("tree.headwordpos", "true").equals("true"));
		subcategorizationSet = (config.getProperty("tree.subcategorization", "true").equals("true"));
		firstargumentSet = (config.getProperty("tree.firstargument", "true").equals("true"));
		lastargumentSet = (config.getProperty("tree.lastargument", "true").equals("true"));
		predicateAndHeadwordSet = (config.getProperty("tree.predicateAndHeadword", "true").equals("true"));		
		predicateAndPhrasetypeSet = (config.getProperty("tree.predicateAndPhrasetype", "true").equals("true"));		
	}
	
	/**
	 * 用于训练句法树模型的特征
	 */
	@Override
	public String toString() {
		return "SRLContextGeneratorConf{" + "predicateSet=" + predicateSet + 
                ", pathSet=" + pathSet + ", phrasetypeSet=" + phrasetypeSet + 
                ", positionSet=" + positionSet + ", voiceSet=" + voiceSet +  
                ", headwordSet=" + headwordSet + ", headwordposSet=" + headwordposSet + 
                ", subcategorizationSet=" + subcategorizationSet + ", firstargumentSet=" + firstargumentSet + 
                ", lastargumentSet=" + lastargumentSet + ", predicateAndHeadwordSet=" + predicateAndHeadwordSet +  
                ", predicateAndPhrasetypeSet=" + predicateAndPhrasetypeSet +
                '}';
	}	
	
	/**
	 * 为训练语料生成上下文特征
	 * @param i 当前位置
	 * @param roleTree 以谓词和论元为根的角色树数组
	 * @param semanticinfo 语义角色信息
	 * @param labelinfo 标记信息
	 * @return
	 */
	@Override
	public String[] getContextForTrain(int i, SRLHeadTreeNode[] roleTree, String[] semanticinfo, Object[] labelinfo) {
		return getContextForTrain(i,roleTree,semanticinfo,(String[])labelinfo);
	}

	/**
	 * 为测试语料生成上下文特征
	 * @param i 当前位置
	 * @param roleTree 以谓词和论元为根的树数组
	 * @param semanticinfo 语义角色信息
	 * @param labelinfo 标记信息
	 * @return
	 */
	@Override
	public String[] getContext(int i, HeadTreeNode[] roleTree, String[] semanticinfo, Object[] labelinfo) {
		return getContext(i,roleTree,semanticinfo,(String[])labelinfo);
	}
	
	/**
	 * 为训练语料生成上下文特征
	 * @param i 当前位置
	 * @param roleTree 以谓词和论元为根的角色树数组
	 * @param semanticinfo 语义角色信息
	 * @param labelinfo 标记信息
	 * @return
	 */
	public String[] getContextForTrain(int i, SRLHeadTreeNode[] roleTree, String[] semanticinfo, String[] labelinfo) {
		
		return null;
	}
	
	/**
	 * 为测试语料生成上下文特征
	 * @param i 当前位置
	 * @param roleTree 以谓词和论元为根的树数组
	 * @param semanticinfo 语义角色信息
	 * @param labelinfo 标记信息
	 * @return
	 */
	public String[] getContext(int i, HeadTreeNode[] roleTree, String[] semanticinfo, String[] labelinfo) {
		List<String> features = new ArrayList<String>();
		int predicateposition = Integer.parseInt(semanticinfo[0]);
		int argumentposition = Integer.parseInt(semanticinfo[i+2]);
		String voice = semanticinfo[2].toCharArray()[4]+"";
		if(predicateSet){
			features.add("predicate="+semanticinfo[1]);
		}
		if(pathSet){
			features.add("path="+getPath(roleTree[0],roleTree[i]));
		}
		if(phrasetypeSet){
			features.add("phrasetype="+roleTree[i].getNodeName());
		}
		if(positionSet){
			if(argumentposition < predicateposition){
				features.add("position="+"before");
			}else{
				features.add("position="+"after");
			}			
		}
		if(voiceSet){
			features.add("voice="+voice);
		}
		if(headwordSet){
			features.add("headword="+roleTree[i].getHeadWords());
		}
		if(headwordposSet){
			features.add("headwordpos="+roleTree[i].getHeadWordsPos());
		}
		if(subcategorizationSet){
			features.add("subcategorization="+getSubcategorization(roleTree[0]));
		}
		if(firstargumentSet){
			features.add("firstargument="+getFirstArgument(roleTree[i]));
		}
		if(lastargumentSet){
			features.add("lastargument="+getLastArgument(roleTree[i]));
		}
		if(predicateAndHeadwordSet){
			features.add("predicateAndHeadword="+semanticinfo[1]+"|"+roleTree[i].getHeadWords());
		}
		if(predicateAndPhrasetypeSet){
			features.add("predicateAndPhrasetype="+semanticinfo[1]+"|"+roleTree[i].getNodeName());
		}
		String[] contexts = features.toArray(new String[features.size()]);
        return contexts;
	}
	
	private String getSubcategorization(HeadTreeNode headTree){
		int index = headTree.getIndex();
		String str = "";
		if(headTree.getParent() != null){
			headTree = headTree.getParent();
			str += headTree.getNodeName()+"→";
			for (int i = index+1; i < headTree.getChildren().size(); i++) {
				if(i == headTree.getChildren().size()-1){
					str += headTree.getChildren().get(i).getNodeName();
				}else{
					str += headTree.getChildren().get(i).getNodeName()+" ";
				}				
			}
		}
		return str;
	}
	
	/**
	 * 获取论元的第一个词
	 * @param headTree 以当前论元为根节点的树
	 * @return
	 */
	private String getFirstArgument(HeadTreeNode headTree){
		while(headTree.getChildren().size() != 0){
			headTree = headTree.getChildren().get(0);
		}
		return headTree.getNodeName();
	}
	
	/**
	 * 获取论元的最后一个词
	 * @param headTree 以当前论元为根节点的树
	 * @return
	 */
	private String getLastArgument(HeadTreeNode headTree){
		while(headTree.getChildren().size() != 0){
			headTree = headTree.getChildren().get(headTree.getChildren().size()-1);
		}
		return headTree.getNodeName();
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
		while(argumenttree.getParent() != null){
			boolean flag = false;
			HeadTreeNode tree = predicatetree;
			while(predicatetree.getParent() != null){
				if(argumenttree.toString().equals(predicatetree.toString())){
					flag = true;
					break;
				}else{
					predicatetree = predicatetree.getParent();
				}
			}
			if(flag == true){
				break;
			}
			argumenttree = argumenttree.getParent();
			predicatetree = tree;
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
}
