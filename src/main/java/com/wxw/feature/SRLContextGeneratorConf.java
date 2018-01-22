package com.wxw.feature;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;

/**
 * 生成上下文特征
 * @author 王馨苇
 *
 */
public class SRLContextGeneratorConf implements SRLContextGenerator{
	
	private List<String> content = new ArrayList<>();
	private List<String> contentpos = new ArrayList<>();
	
	private boolean predicateSet;
	private boolean predicateposSet;
	private boolean predicatesuffixSet;
	private boolean pathlengthSet;
	private boolean partialpathSet;
	private boolean pathSet; 
	private boolean phrasetypeSet; 
	private boolean positionSet;  
	private boolean voiceSet;
	private boolean headwordSet;
	private boolean headwordposSet;
	private boolean governingcategoriesSet;
	private boolean subcategorizationSet; 
	private boolean firstargumentSet; 
	private boolean firstargumentposSet;   
	private boolean lastargumentSet;  
	private boolean lastargumentposSet;  
	private boolean positionAndvoiceSet;
	private boolean predicateAndpathSet;
	private boolean pathAndpositionAndvoiceSet;
	private boolean pathAndpositionAndvoiceAndpredicateSet;
	private boolean headwordAndpredicateAndpathSet;
	private boolean headwordAndPhrasetypeSet;
	private boolean predicateAndHeadwordSet;   
	private boolean predicateAndPhrasetypeSet;
	private boolean pathAndSubcategorizationSet;
	private boolean predicatephraseSet;
	private boolean voiceAndGoverningcategoriesSet;
	private boolean contentSet;
	private boolean contentposSet;
	private boolean left_1wordSet;
	private boolean left_2wordSet;
	private boolean left_1posSet;
	private boolean left_2posSet;
	private boolean right1wordSet;
	private boolean right2wordSet;
	private boolean right1posSet;
	private boolean right2posSet;
				
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
			
		predicateSet = (config.getProperty("feature.predicate", "true").equals("true"));
		predicateposSet = (config.getProperty("feature.predicatepos", "true").equals("true"));
		predicatesuffixSet = (config.getProperty("feature.predicatesuffix", "true").equals("true"));
		pathlengthSet = (config.getProperty("feature.pathlength", "true").equals("true"));
		partialpathSet = (config.getProperty("feature.partialpath", "true").equals("true"));
		pathSet = (config.getProperty("feature.path", "true").equals("true"));
		phrasetypeSet = (config.getProperty("feature.phrasetype", "true").equals("true"));
		positionSet = (config.getProperty("feature.position", "true").equals("true"));
		voiceSet = (config.getProperty("feature.voice", "true").equals("true"));
		headwordSet = (config.getProperty("feature.headword", "true").equals("true"));
		headwordposSet = (config.getProperty("feature.headwordpos", "true").equals("true"));
		governingcategoriesSet = (config.getProperty("feature.governingcategories", "true").equals("true"));
		subcategorizationSet = (config.getProperty("feature.subcategorization", "true").equals("true"));
		firstargumentSet = (config.getProperty("feature.firstargument", "true").equals("true"));
		firstargumentposSet = (config.getProperty("feature.firstargumentpos", "true").equals("true"));
		lastargumentSet = (config.getProperty("feature.lastargument", "true").equals("true"));
		lastargumentposSet = (config.getProperty("feature.lastargumentpos", "true").equals("true"));
		positionAndvoiceSet = (config.getProperty("feature.positionAndvoice", "true").equals("true"));
		predicateAndpathSet = (config.getProperty("feature.predicateAndpath", "true").equals("true"));
		pathAndpositionAndvoiceSet = (config.getProperty("feature.pathAndpositionAndvoice", "true").equals("true"));
		pathAndpositionAndvoiceAndpredicateSet = (config.getProperty("feature.pathAndpositionAndvoiceAndpredicate", "true").equals("true"));
		headwordAndpredicateAndpathSet = (config.getProperty("feature.headwordAndpredicateAndpath", "true").equals("true"));
		headwordAndPhrasetypeSet = (config.getProperty("feature.headwordAndPhrasetype", "true").equals("true"));
		predicateAndHeadwordSet = (config.getProperty("feature.predicateAndHeadword", "true").equals("true"));		
		predicateAndPhrasetypeSet = (config.getProperty("feature.predicateAndPhrasetype", "true").equals("true"));
		predicatephraseSet = (config.getProperty("feature.predicatephrase", "true").equals("true"));
		voiceAndGoverningcategoriesSet = (config.getProperty("feature.predicatephrase", "true").equals("true"));
		pathAndSubcategorizationSet = (config.getProperty("feature.pathAndSubcategorization", "true").equals("true"));
		contentSet = (config.getProperty("feature.content", "true").equals("true"));
		contentposSet = (config.getProperty("feature.contentpos", "true").equals("true"));	
		
		left_1wordSet = (config.getProperty("feature.left_1word", "true").equals("true"));
		left_2wordSet = (config.getProperty("feature.left_2word", "true").equals("true"));		
		left_1posSet = (config.getProperty("feature.left_1pos", "true").equals("true"));
		left_2posSet = (config.getProperty("feature.left_2pos", "true").equals("true"));
		right1wordSet = (config.getProperty("feature.right1word", "true").equals("true"));
		right2wordSet = (config.getProperty("feature.right2word", "true").equals("true"));
		right1posSet = (config.getProperty("feature.right1pos", "true").equals("true"));
		right2posSet = (config.getProperty("feature.right2pos", "true").equals("true"));	
	}
	
	/**
	 * 用于训练句法树模型的特征
	 */
	@Override
	public String toString() {
		return "SRLContextGeneratorConf{" + "predicateSet=" + predicateSet + ", predicateposSet=" + predicateposSet + 
                ", pathSet=" + pathSet + ", phrasetypeSet=" + phrasetypeSet + 
                ", positionSet=" + positionSet + ", voiceSet=" + voiceSet +  
                ", headwordSet=" + headwordSet + ", headwordposSet=" + headwordposSet + 
                ", governingcategoriesSet=" + governingcategoriesSet +
                ", subcategorizationSet=" + subcategorizationSet + ", firstargumentSet=" + firstargumentSet + 
                ", firstargumentposSet=" + firstargumentposSet + 
                ", lastargumentSet=" + lastargumentSet + ", lastargumentposSet=" + lastargumentposSet + 
                ", positionAndvoiceSet=" + positionAndvoiceSet + ", predicateAndpathSet=" + predicateAndpathSet + 
                ", pathAndpositionAndvoiceSet=" + pathAndpositionAndvoiceSet + 
                ", pathAndpositionAndvoiceAndpredicateSet=" + pathAndpositionAndvoiceAndpredicateSet + 
                ", headwordAndpredicateAndpathSet=" + headwordAndpredicateAndpathSet + 
                ", headwordAndPhraseSet=" + headwordAndPhrasetypeSet + 
                ", predicateAndHeadwordSet=" + predicateAndHeadwordSet +  
                ", predicateAndPhrasetypeSet=" + predicateAndPhrasetypeSet +
                ", predicatephraseSet=" + predicatephraseSet +
                ", voiceAndGoverningcategoriesSet=" + voiceAndGoverningcategoriesSet +
                ", pathAndSubcategorizationSet=" + pathAndSubcategorizationSet +
                ", contentSet=" + contentSet +
                ", contentposSet=" + contentposSet +
                ", left_1wordSet=" + left_1wordSet + 
                ", left_2wordSet=" + left_2wordSet +  
                ", left_1posSet=" + left_1posSet +
                ", left_2posSet=" + left_2posSet +
                ", right1wordSet=" + right1wordSet +
                ", right2wordSet=" + right2wordSet +
                ", right1posSet=" + right1posSet +
                ", right2posSet=" + right2posSet +
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
		int predicateposition = predicatetree[0].getLeftLeafIndex();
		int argumentposition = argumenttree[i].getLeftLeafIndex();
		HeadTreeNode headtree = predicatetree[0].getTree();
		while(headtree.getChildren().size() != 0){
			headtree = headtree.getChildren().get(0);
		}
		String voice;
		if(headtree.getParent().getNodeName().equals("VBN")){
			voice = "p";
		}else{
			voice = "a";
		}
		String position;
		if(argumentposition < predicateposition){
			position = "before";
		}else{
			position = "after";
		}
		String predicate = headtree.getNodeName();
		String path = getPath(predicatetree[0].getTree(),argumenttree[i].getTree());
		String governingcategories = "";
		if(argumenttree[i].getTree().getNodeName().equals("NP")){
			if(argumenttree[i].getTree().getParent().getNodeName().equals("S")){
				governingcategories = "S";
			}else if(argumenttree[i].getTree().getParent().getNodeName().equals("VP")){
				governingcategories = "VP";
			}
		}
		String subcategorization = getSubcategorization(predicatetree[0].getTree());
		if(predicateSet){
			features.add("predicate="+predicate);
		}
		if(predicateposSet){
			features.add("predicatepos="+headtree.getParent().getNodeName());
		}
		if(predicatesuffixSet){
			if(predicate.length() >= 3){
				features.add("predicatesuffix="+predicate.substring(predicate.length()-3, predicate.length()));
			}else{
				features.add("predicatesuffix="+predicate);
			}
		}
		if(pathSet){
			features.add("path="+path);
		}
		if(pathlengthSet){
			features.add("pathlength="+getPathLength(path));
		}
		if(partialpathSet){
			features.add("partialpath="+getPartialPath(path));
		}
		if(phrasetypeSet){
			features.add("phrasetype="+argumenttree[i].getTree().getNodeName());
		}
		if(positionSet){
			features.add("position="+position);			
		}
		if(voiceSet){
			features.add("voice="+voice);
		}
		if(headwordSet){
			features.add("headword="+argumenttree[i].getTree().getHeadWords());
		}
		if(headwordposSet){
			features.add("headwordpos="+argumenttree[i].getTree().getHeadWordsPos());
		}
		if(governingcategoriesSet){
			if(!governingcategories.equals("")){
				features.add("governingcategories="+governingcategories);
			}
		}
		if(subcategorizationSet){
			features.add("subcategorization="+subcategorization);
		}
		if(firstargumentSet){
			features.add("firstargument="+getFirstArgument(argumenttree[i].getTree()).split("_")[0]);
		}
		if(firstargumentposSet){
			features.add("firstargumentpos="+getFirstArgument(argumenttree[i].getTree()).split("_")[1]);
		}
		if(lastargumentSet){
			features.add("lastargument="+getLastArgument(argumenttree[i].getTree()).split("_")[0]);
		}
		if(lastargumentposSet){
			features.add("lastargumentpos="+getLastArgument(argumenttree[i].getTree()).split("_")[1]);
		}
		if(positionAndvoiceSet){
			features.add("positionAndvoice="+position+"|"+voice);
		}
		if(predicateAndpathSet){
			features.add("predicateAndpath="+predicate+"|"+path);
		}
		if(pathAndpositionAndvoiceSet){
			features.add("pathAndpositionAndvoice="+path+"|"+position+"|"+voice);
		}
		if(pathAndpositionAndvoiceAndpredicateSet){
			features.add("pathAndpositionAndvoiceAndpredicate="+path+"|"+position+"|"+voice+"|"+predicate);
		}
		if(headwordAndpredicateAndpathSet){
			features.add("headwordAndpredicateAndpath="+argumenttree[i].getTree().getHeadWords()+"|"+predicate+"|"+path);
		}
		if(headwordAndPhrasetypeSet){
			features.add("headwordAndPhrasetype="+argumenttree[i].getTree().getHeadWords()+"|"+argumenttree[i].getTree().getNodeName());
		}
		if(predicateAndHeadwordSet){
			features.add("predicateAndHeadword="+predicate+"|"+argumenttree[i].getTree().getHeadWords());
		}
		if(predicateAndPhrasetypeSet){
			features.add("predicateAndPhrasetype="+predicate+"|"+argumenttree[i].getTree().getNodeName());
		}	
		if(voiceAndGoverningcategoriesSet){
			features.add("voiceAndGoverningcategories="+predicatetree[0].getTree().getParent().getNodeName());
		}
		if(voiceAndGoverningcategoriesSet){
			if(!governingcategories.equals("")){
				features.add("voiceAndGoverningcategories="+voice+"|"+governingcategories);
			}
		}
		if(pathAndSubcategorizationSet){
			features.add("pathAndSubcategorization="+path+"|"+subcategorization);
		}
		content.clear();
		contentpos.clear();
		getContentAndContentPos(argumenttree[i].getTree());
		if(contentSet){
			features.add("content="+content.toString());
		}
		if(contentposSet){
			features.add("contentpos="+contentpos.toString());
		}
		
		if(left_1wordSet){
			if(content.size() > 0){
				features.add("left_1word"+content.get(0));
			}
		}
		if(left_1posSet){
			if(contentpos.size() > 0){
				features.add("left_1pos"+contentpos.get(0));
			}
		}
		if(left_2wordSet){
			if(content.size() > 1){
				features.add("left_2word"+content.get(1));
			}
		}
		if(left_2posSet){
			if(contentpos.size() > 1){
				features.add("left_2pos"+contentpos.get(1));
			}
		}
		if(right1wordSet){
			if(content.size() > 0){
				features.add("right1word"+content.get(content.size()-1));
			}
		}
		if(right1posSet){
			if(contentpos.size() > 0){
				features.add("right1pos"+contentpos.get(contentpos.size()-1));
			}
		}
		if(right2wordSet){
			if(content.size() > 1){
				features.add("right2word"+content.get(content.size()-2));
			}
		}
		if(right2posSet){
			if(contentpos.size() > 1){
				features.add("right2pos"+contentpos.get(contentpos.size()-2));
			}
		}
		String[] contexts = features.toArray(new String[features.size()]);
        return contexts;
	}
	
	/**
	 * 获得词语内容和词性标记
	 * @param headtree 
	 */
	private void getContentAndContentPos(HeadTreeNode headtree){
		if(headtree.getChildren().size() == 1 && headtree.getChildren().get(0).getChildren().size() == 0){
			content.add(headtree.getChildren().get(0).getNodeName());
			contentpos.add(headtree.getNodeName());
		}else{
			for (HeadTreeNode node : headtree.getChildren()) {
				getContentAndContentPos(node);
			}
		}
	}
	
	private String getSubcategorization(HeadTreeNode headTree){
		int index = headTree.getIndex();
		String str = "";
		if(headTree.getParent() != null){
			headTree = headTree.getParent();
			str += headTree.getNodeName()+"→";
			for (int i = index; i < headTree.getChildren().size(); i++) {
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
		return headTree.getNodeName()+"_"+headTree.getParent().getNodeName();
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
		return headTree.getNodeName()+"_"+headTree.getParent().getNodeName();
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
