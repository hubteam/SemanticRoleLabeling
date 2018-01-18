package com.wxw.posttreat;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseNormal;
import com.wxw.stream.SRLSample;
import com.wxw.tool.PostTreatTool;
import com.wxw.tool.PreTreatTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.TreeNode;

import opennlp.tools.util.Sequence;

/**
 * 对没有剪枝得到的结果后处理的测试
 * 后处理是为了防止嵌套情况的出现，当一颗树的根被标记成论元，根的子节点也有被标记成论元的，就是嵌套结构
 * 解决：保留一棵树中概率最大的论元
 * @author 王馨苇
 *
 */
public class PostTreatTest {

	private String roles;
	private Sequence sequence;
	private List<String> label;
	private List<String> result;
	private String[] pre;
	private List<Double> prop;
	private PhraseGenerateTree pgt ;
	private TreeNode tree;
	private AbstractParseStrategy<HeadTreeNode> parse;
	private SRLSample<HeadTreeNode> sample;
	
	@Before
	public void setUp(){
		pgt = new PhraseGenerateTree();		
		roles = "wsj/00/wsj0012.mrg 9 12 gold shore.01 i---a 4:1*10:0-ARG0 12:0,13:1-rel 14:2-ARG1";
		tree = pgt.generateTree(""
				+ "((S(S(NP-SBJ(NNP Mr.)(NNP Spoon))(VP(VBD said)(SBAR (-NONE- 0)(S(NP-SBJ(DT the)(NN plan))"
				+ "(VP(VBZ is)(RB not)(NP-PRD(DT an)(NN attempt)(S(NP-SBJ(-NONE- *))(VP(TO to)(VP(VB shore)"
				+ "(PRT(RP up))(NP(NP(DT a)(NN decline))(PP-LOC(IN in)(NP(NN ad)(NNS pages)))(PP-TMP(IN in)"
				+ "(NP(NP(DT the)(JJ first)(CD nine)(NNS months))(PP(IN of)(NP(CD 1989)))))))))))))))"
				+ "(: ;)(S(NP-SBJ(NP(NNP Newsweek)(POS 's))(NN ad)(NNS pages))(VP(VBD totaled)(NP"
				+ "(NP(CD 1,620))(, ,)(NP(NP(DT a)(NN drop))(PP(IN of)(NP (CD 3.2)(NN %)))"
				+ "(PP-DIR(IN from)(NP(JJ last)(NN year)))))(, ,)(PP(VBG according)(PP(TO to)"
				+ "(NP(NNP Publishers)(NNP Information)(NNP Bureau))))))(. .)))");	

		PreTreatTool.preTreat(tree);
		parse = new SRLParseNormal();
		sample = parse.parse(tree, roles);
		
		label = new ArrayList<>();
		label.add("NULL1");
		
		label.add("NULL2");
		label.add("NULL3");
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("A");
		label.add("B");
		label.add("C");
		
		label.add("NULL");
		
		label.add("F");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("D");
		label.add("NULL");
		
		label.add("NULL");
		
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		
		label.add("NULL");
		label.add("M");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("NULL");
		label.add("E");
		
		prop = new ArrayList<>();
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(5.0);
		prop.add(4.0);
		prop.add(7.0);
		prop.add(0.0);
		prop.add(3.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(1.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(6.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(8.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(0.0);
		prop.add(5.0);
		
		result = new ArrayList<>();
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("C");
		
		result.add("NULL");
		
		result.add("F");
		
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("M");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("NULL");
		result.add("E");
		
		sequence = new Sequence();
		
		for (int i = 0; i < label.size() && i < prop.size(); i++) {
			sequence.add(label.get(i), prop.get(i));
		}
		for (int i = 0; i < sample.getArgumentTree().length; i++) {
			System.out.println(PostTreatTool.getSonTreeCount(sample.getArgumentTree()[i].getTree()));
		}
		if(sample.getIsPruning() == true){
			pre = PostTreatTool.postTreat(sample.getArgumentTree(), sequence,PostTreatTool.getSonTreeCount(sample.getPredicateTree()[0].getTree()));
		}else{
			pre = PostTreatTool.postTreat(sample.getArgumentTree(), sequence,sample.getArgumentTree().length);
		}
	}
	
	@Test
	public void test(){
		assertEquals(Arrays.asList(pre),result);
	}
}
