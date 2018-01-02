package com.wxw.tree2srltree;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.SemanticRoleTree;
import com.wxw.tree.TreeNode;
import com.wxw.tree.TreeToSRLTree;

/**
 * 将树转成语义角色标注树的单元测试
 * @author 王馨苇
 *
 */
public class TreeToSRLTreeTest {

	private PhraseGenerateTree pgt ;
	private TreeToSRLTree ttst ;
	private TreeNode tree1;
	private SemanticRoleTree roletree1;
	private String roles1;
	private String resu1t1;
	private TreeNode tree2;
	private SemanticRoleTree roletree2_1;
	private SemanticRoleTree roletree2_2;
	private String roles2_1;
	private String roles2_2;
	private String result2_1;
	private String result2_2;
	private TreeNode tree3;
	private SemanticRoleTree roletree3;
	private String roles3;
	private String resu1t3;
	private TreeNode tree4;
	private SemanticRoleTree roletree4;
	private String roles4;
	private String resu1t4;
	
	@Before
	public void setUp(){
		pgt = new PhraseGenerateTree();
		ttst = new TreeToSRLTree();		
		roles1 = "wsj/00/wsj_0001.mrg 0 8 gold join.01 vf--a 0:2-ARG0 7:0-ARGM-MOD 8:0-rel 9:1-ARG1 11:1-ARGM-PRD 15:1-ARGM-TMP";
		tree1 = pgt.generateTree("((S(NP-SBJ(NP(NNP Pierre)(NNP Vinken))(, ,)(ADJP(NP(CD 61)(NNS years))(JJ old))(, ,))(VP(MD will)(VP(VB join)(NP(DT the)(NN board))(PP-CLR(IN as)(NP (DT a)(JJ nonexecutive)(NN director)))(NP-TMP(NNP Nov.)(CD 29))))(. .)))");
		roletree1 = ttst.treeToSRLTree(tree1, roles1);
		resu1t1 = "(S(NP-SBJ{ARG0}(NP(NNP Pierre_[0])(NNP Vinken_[1]))(, ,_[2])(ADJP(NP(CD 61_[3])(NNS years_[4]))(JJ old_[5]))(, ,_[6]))(VP(MD{ARGM-MOD} will_[7])(VP(VB{rel} join_[8])(NP{ARG1}(DT the_[9])(NN board_[10]))(PP-CLR{ARGM-PRD}(IN as_[11])(NP(DT a_[12])(JJ nonexecutive_[13])(NN director_[14])))(NP-TMP{ARGM-TMP}(NNP Nov._[15])(CD 29_[16]))))(. ._[17]))";
		roles2_1 = "wsj/00/wsj_0003.mrg 22 5 gold remain.01 p---a 5:0-rel 6:0-ARG1";
		roles2_2 = "wsj/00/wsj_0003.mrg 22 12 gold outlaw.01 pf--p 0:1-ARGM-TMP 10:0-ARGM-MOD 12:0-rel 3:3*13:0-ARG1";
		tree2 = pgt.generateTree("((S(PP-TMP(IN By)(NP(CD 1997)))(, ,)(NP-SBJ-6(NP(ADJP(RB almost)(DT all))(VBG remaining)(NNS uses))(PP(IN of)(NP(JJ cancer-causing)(NN asbestos))))(VP(MD will)(VP(VB be)(VP(VBN outlawed)(NP(-NONE- *-6)))))(. .)))");
		roletree2_1 = ttst.treeToSRLTree(tree2, roles2_1);
		result2_1 = "(S(PP-TMP(IN By_[0])(NP(CD 1997_[1])))(, ,_[2])(NP-SBJ-6(NP(ADJP(RB almost_[3])(DT all_[4]))(VBG{rel} remaining_[5])(NNS{ARG1} uses_[6]))(PP(IN of_[7])(NP(JJ cancer-causing_[8])(NN asbestos_[9]))))(VP(MD will_[10])(VP(VB be_[11])(VP(VBN outlawed_[12])(NP(-NONE- *-6_[13])))))(. ._[14]))";
		roletree2_2 = ttst.treeToSRLTree(tree2, roles2_2);
		result2_2 = "(S(PP-TMP{ARGM-TMP}(IN By_[0])(NP(CD 1997_[1])))(, ,_[2])(NP-SBJ-6{ARG1}(NP(ADJP(RB almost_[3])(DT all_[4]))(VBG remaining_[5])(NNS uses_[6]))(PP(IN of_[7])(NP(JJ cancer-causing_[8])(NN asbestos_[9]))))(VP(MD{ARGM-MOD} will_[10])(VP(VB be_[11])(VP(VBN{rel} outlawed_[12])(NP(-NONE-{ARG1} *-6_[13])))))(. ._[14]))";
		roles3 = "wsj/00/wsj_0004.mrg 0 5 gold continue.01 vp--a 0:2,6:2-ARG1 5:0-rel 10:1-ARGM-MNR";
		tree3 = pgt.generateTree("((S(NP-SBJ-1(NP (NNS Yields))(PP (IN on)(NP (JJ money-market)(JJ mutual)(NNS funds))))(VP (VBD continued)(S (NP-SBJ (-NONE- *-1) )(VP (TO to) (VP (VB slide) )))(, ,)"+
		"(PP-LOC (IN amid) (NP (NNS signs) (SBAR (IN that)"+
		"(S(NP-SBJ (NN portfolio) (NNS managers))(VP(VBP expect)(NP(NP(JJ further)(NNS declines))(PP-LOC (IN in) (NP (NN interest)(NNS rates) )))))))))(. .) ))");
		roletree3 = ttst.treeToSRLTree(tree3, roles3);
		resu1t3 = "(S(NP-SBJ-1{ARG1}(NP(NNS Yields_[0]))(PP(IN on_[1])(NP(JJ money-market_[2])(JJ mutual_[3])(NNS funds_[4]))))(VP(VBD{rel} continued_[5])"+
				"(S{ARG1}(NP-SBJ(-NONE- *-1_[6]))(VP(TO to_[7])(VP(VB slide_[8]))))(, ,_[9])(PP-LOC{ARGM-MNR}(IN amid_[10])(NP(NNS signs_[11])(SBAR(IN that_[12])"+
				"(S(NP-SBJ(NN portfolio_[13])(NNS managers_[14]))(VP(VBP expect_[15])"
				+ "(NP(NP(JJ further_[16])(NNS declines_[17]))(PP-LOC(IN in_[18])(NP(NN interest_[19])(NNS rates_[20]))))))))))(. ._[21]))";
		roles4 = "wsj/00/wsj_0016.mrg 5 7 gold benefit.01 vp--a 7:0-rel 8:1-ARG0-from 0:1,1:1*5:1*6:0-ARG1";
		tree4 = pgt.generateTree("((S(NP-SBJ(NP(NNS Sales))(PP(IN of)(NP(JJ medium-sized)(NNS cars)))(, ,)(SBAR(WHNP-21(WDT which))"+
		"(S(NP-SBJ(-NONE- *T*-21))(VP(VBD benefited)(PP-CLR(IN from)(NP(NP(NN price)(NNS reductions))"+
		"(VP(VBG arising)(PP-DIR(IN from)(NP(NP(NN introduction))(PP(IN of)(NP(DT the)(NN consumption)"+
		"(NN tax)))))))))))(, ,))(VP(ADVP(RBR more)(IN than))(VBD doubled) (PP-DIR(TO to)(NP(CD 30,841)"+
		"(NNS units)))(PP-DIR(IN from)(NP(CD 13,056))(PP-TMP(IN in)(NP(NNP October)(CD 1988)))))(. .)))");
		roletree4 = ttst.treeToSRLTree(tree4, roles4);
		resu1t4 = "(S(NP-SBJ(NP{ARG1}(NNS Sales_[0]))(PP{ARG1}(IN of_[1])(NP(JJ medium-sized_[2])(NNS cars_[3])))(, ,_[4])(SBAR(WHNP-21{ARG1}(WDT which_[5]))"+
				"(S(NP-SBJ(-NONE-{ARG1} *T*-21_[6]))(VP(VBD{rel} benefited_[7])(PP-CLR{ARG0-from}(IN from_[8])(NP(NP(NN price_[9])(NNS reductions_[10]))"+
				"(VP(VBG arising_[11])(PP-DIR(IN from_[12])(NP(NP(NN introduction_[13]))(PP(IN of_[14])(NP(DT the_[15])(NN consumption_[16])"+
				"(NN tax_[17])))))))))))(, ,_[18]))(VP(ADVP(RBR more_[19])(IN than_[20]))(VBD doubled_[21])(PP-DIR(TO to_[22])(NP(CD 30,841_[23])"+
				"(NNS units_[24])))(PP-DIR(IN from_[25])(NP(CD 13,056_[26]))(PP-TMP(IN in_[27])(NP(NNP October_[28])(CD 1988_[29])))))(. ._[30]))";
	}
	
	@Test
	public void test(){
		assertEquals(roletree1.toString(),resu1t1);
		assertEquals(roletree2_1.toString(),result2_1);
		assertEquals(roletree2_2.toString(),result2_2);
		assertEquals(roletree3.toString(),resu1t3);
		assertEquals(roletree4.toString(),resu1t4);
	}
}
