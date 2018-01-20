package com.wxw.sample;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseNormalHasPruning;
import com.wxw.stream.SRLSample;
import com.wxw.tool.PreTreatTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.TreeNode;

/**
 * 样本类测试(NULL标记，有剪枝)
 * @author 王馨苇
 *
 */
public class SRLParseNormalHasPruningTest {

	private String roles;
	private String list;
	private List<String> label;
	private List<String> srlinfo;
	private PhraseGenerateTree pgt ;
	private TreeNode tree;
	private String roles1;
	private String list1;
	private List<String> label1;
	private List<String> srlinfo1;
	private TreeNode tree1;
	private AbstractParseStrategy<HeadTreeNode> parse;
	private AbstractParseStrategy<HeadTreeNode> parse1;
	private SRLSample<HeadTreeNode> sample;
	private SRLSample<HeadTreeNode> sample1;
	
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
		parse = new SRLParseNormalHasPruning();
		sample = parse.parse(tree, roles);

		srlinfo = new ArrayList<>();		
		srlinfo.add("14");
		srlinfo.add("14");
		srlinfo.add("14");
		srlinfo.add("15");
		srlinfo.add("16");
		srlinfo.add("16");
		srlinfo.add("17");
		srlinfo.add("17");
		srlinfo.add("18");
		srlinfo.add("19");
		srlinfo.add("19");
		srlinfo.add("20");
		srlinfo.add("20");
		srlinfo.add("20");
		srlinfo.add("21");
		srlinfo.add("22");
		srlinfo.add("23");
		srlinfo.add("24");
		srlinfo.add("24");
		srlinfo.add("25");
		srlinfo.add("25");
		
		srlinfo.add("11");
		srlinfo.add("8");
		srlinfo.add("9");
		srlinfo.add("6");
		srlinfo.add("7");
		srlinfo.add("4");
		srlinfo.add("2");
		srlinfo.add("0");
		
		label = new ArrayList<>();

		label.add("ARG1");
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
		label.add("ARG0");
		label.add("NULL");
		label.add("NULL");
		
		list = "(VB{shore[VB]} shore[12])";
		
		parse1 = new SRLParseNormalHasPruning();
		roles1 = "wsj/00/wsj_0071.mrg 37 9 gold go.13 pn--a 7:1-ARG1 9:1-rel";
		tree1 = pgt.generateTree("((S(S(NP-SBJ (PRP We))(VP (VBD got)(NP(PRP$ our)(CD two)(NNS six-packs))))(: --)(CC and)(S(NP-SBJ(PRP they))(VP (VBP 're) (VP (VBN gone) )))(. .)('' '')))");	
		PreTreatTool.preTreat(tree1);		
		sample1 = parse1.parse(tree1, roles1);
		srlinfo1 = new ArrayList<>();		
		srlinfo1.add("8");
		srlinfo1.add("7");
		
		label1 = new ArrayList<>();
		label1.add("NULL");
		label1.add("ARG1");

		list1 = "(VP{gone[VBN]}(VBN{gone[VBN]} gone[9]))";
	}
	
	@Test
	public void test(){
		assertEquals(Arrays.asList(sample.getLabelInfo()),label);
		for (int i = 0; i < srlinfo.size(); i++) {
			assertEquals(sample.getArgumentTree()[i].getLeftLeafIndex()+"",srlinfo.get(i));
		}
		assertEquals(sample.getPredicateTree()[0].getTree().toString(),list);

		assertEquals(Arrays.asList(sample1.getLabelInfo()),label1);
		for (int i = 0; i < srlinfo1.size(); i++) {
			assertEquals(sample1.getArgumentTree()[i].getLeftLeafIndex()+"",srlinfo1.get(i));
		}
		assertEquals(sample1.getPredicateTree()[0].getTree().toString(),list1);
	}
}
