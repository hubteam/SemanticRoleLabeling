package com.wxw.tree2srltree;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseNormalHasPruning;
import com.wxw.stream.SRLSample;
import com.wxw.tool.PreTreatTool;
import com.wxw.tool.TreeToSRLTreeTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.SRLTreeNode;
import com.wxw.tree.TreeNode;

/**
 * 将一颗语义角色树输出为中括号形式的测试
 * @author 王馨苇
 *
 */
public class SRLTreeToSRLBracketTest {

	private String roles;
	private PhraseGenerateTree pgt ;
	private TreeNode tree;
	private SRLSample<HeadTreeNode> sample;
	private AbstractParseStrategy<HeadTreeNode> parse;
	private SRLTreeNode srltree;
	private String result;
	private String roles1;
	private TreeNode tree1;
	private SRLSample<HeadTreeNode> sample1;
	private AbstractParseStrategy<HeadTreeNode> parse1;
	private SRLTreeNode srltree1;
	private String result1;
	
	@Before
	public void setUp(){
		pgt = new PhraseGenerateTree();
		
		roles = "wsj/00/wsj0012.mrg 9 12 gold shore.01 i---a 4:1*10:0-ARG0 12:0,13:1-rel 14:2-ARG1";
		tree = pgt.generateTree(""
				+ "((S(S(NP-SBJ(NNP Mr.)(NNP Spoon))(VP(VBD said)(SBAR (-NONE- 0)(S(NP-SBJ(DT the)(NN plan))"
				+ "(VP(VBZ is)(RB not)(NP-PRD(DT an)(NN attempt)(S(NP-SBJ(-NONE- *))(VP(TO to)(VP(VB shore)"
				+ "(PRT(RP up))(NP(NP(DT a)(NN decline))(PP-LOC(IN in)(NP(NN ad)(NNS pages)))(PP-TMP(IN in)"
				+ "(NP(NP(DT the)(JJ first)(CD nine)(NNS months))(PP(IN in)(NP(CD 1989)))))))))))))))"
				+ "(: ;)(S(NP-SBJ(NP(NNP Newsweek)(POS 's))(NN ad)(NNS pages))(VP(VBD totaled)(NP"
				+ "(NP(CD 1,620))(, ,)(NP(NP(DT a)(NN drop))(PP(IN of)(NP (CD 3.2)(NN %)))"
				+ "(PP-DIR(IN from)(NP(JJ last)(NN year)))))(, ,)(PP(VBG according)(PP(TO to)"
				+ "(NP(NNP Publishers)(NNP Information)(NNP Bureau))))))(. .)))");	

		PreTreatTool.preTreat(tree);
		parse = new SRLParseNormalHasPruning();
		sample = parse.parse(tree, roles);
		srltree = TreeToSRLTreeTool.treeToSRLTree(tree, sample.getArgumentTree(), sample.getLabelInfo());
		result = "Mr. Spoon said [ the plan ]ARG0 is not an attempt to shore up [ a decline in ad pages in the first nine months in 1989 ]ARG1"
				+ " ; Newsweek 's ad pages totaled 1,620 , a drop of 3.2 % from last year , according to Publishers Information Bureau . ";
		System.out.println(SRLTreeNode.printSRLBracket(srltree));
		parse1 = new SRLParseNormalHasPruning();
		roles1 = "wsj/00/wsj_0071.mrg 37 9 gold go.13 pn--a 7:1-ARG1 9:1-rel";
		tree1 = pgt.generateTree("((S(S(NP-SBJ (PRP We))(VP (VBD got)(NP(PRP$ our)(CD two)(NNS six-packs))))(: --)(CC and)(S(NP-SBJ(PRP they))(VP (VBP 're) (VP (VBN gone) )))(. .)('' '')))");	
		PreTreatTool.preTreat(tree1);		
		sample1 = parse1.parse(tree1, roles1);
		srltree1 = TreeToSRLTreeTool.treeToSRLTree(tree1, sample1.getArgumentTree(), sample1.getLabelInfo());
		result1 = "We got our two six-packs -- and [ they ]ARG1 're gone . '' ";
	}
	
	@Test
	public void test(){
		assertEquals(result,SRLTreeNode.printSRLBracket(srltree));
		assertEquals(result1,SRLTreeNode.printSRLBracket(srltree1));
	}
}
