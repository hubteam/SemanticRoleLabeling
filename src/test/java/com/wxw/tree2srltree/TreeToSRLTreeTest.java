package com.wxw.tree2srltree;

import org.junit.After;
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
	private TreeNode tree;
	private SemanticRoleTree roletree;
	@Before
	public void setUp(){
		pgt = new PhraseGenerateTree();
		ttst = new TreeToSRLTree();		
		tree = pgt.generateTree("((S(NP(PRP I))(VP(VP(VBD saw)(NP(DT the)(NN man)))(PP(IN with)(NP(DT the)(NN telescope))))))");
		roletree =ttst.treeAddWordIndex(tree);
	}
	
	@Test
	public void test(){
		
	}
	
	@After
	public void tearDown(){
		
	}
}
