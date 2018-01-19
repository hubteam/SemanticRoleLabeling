package com.wxw.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseNormalHasPruning;
import com.wxw.stream.SRLSample;
import com.wxw.tool.PreTreatTool;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.TreeNode;

/**
 * 将样本解析成分类阶段要用的样本的样式
 * 有剪枝
 * 分类阶段只有论元标签
 * @author 王馨苇
 *
 */
public class SRLHavePruningForClassificationNotNULLTest {
	private String roles;
	private String list;
	private List<String> label;
	private TreeNode tree;
	private PhraseGenerateTree pgt ;	
	private AbstractParseStrategy<HeadTreeNode> parse;
	private SRLSample<HeadTreeNode> sample;
	private List<Integer> index1;
	private TreeNodeWrapper<HeadTreeNode>[] wrapper1;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp(){
		pgt = new PhraseGenerateTree();
		parse = new SRLParseNormalHasPruning();
		
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
		sample = parse.parse(tree, roles);
		index1 = new ArrayList<>();
		index1.add(0);
		index1.add(26);
		wrapper1 = new TreeNodeWrapper[2];
		wrapper1[0] = sample.getArgumentTree()[0];
		wrapper1[1] = sample.getArgumentTree()[26];
		
		label = new ArrayList<>();
		label.add("ARG1");
		label.add("ARG0");

		list = "(VB{shore[VB]} shore[12])";
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void test(){
		assertEquals(index1,SRLSample.filterNotNULLLabelIndex(sample.getIdentificationLabelInfo()));	
		assertEquals(Arrays.asList(sample.getLabelFromIndex(sample.getLabelInfo(), SRLSample.filterNotNULLLabelIndex(sample.getIdentificationLabelInfo()))),label);
		assertTrue(sample.getArgumentTreeFromIndex(sample.getArgumentTree(), SRLSample.filterNotNULLLabelIndex(sample.getIdentificationLabelInfo())).length==wrapper1.length);
		assertEquals(sample.getArgumentTreeFromIndex(sample.getArgumentTree(), SRLSample.filterNotNULLLabelIndex(sample.getIdentificationLabelInfo()))[0],wrapper1[0]);
		assertEquals(sample.getArgumentTreeFromIndex(sample.getArgumentTree(), SRLSample.filterNotNULLLabelIndex(sample.getIdentificationLabelInfo()))[1],wrapper1[1]);
		assertEquals(sample.getPredicateTree()[0].getTree().toString(),list);
	}
}
