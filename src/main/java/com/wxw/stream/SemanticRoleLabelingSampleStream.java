package com.wxw.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxw.stream.SyntacticAnalysisSample;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.TreeNode;
import com.wxw.tree.TreeToSRLTree;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;

/**
 * 解析样本流
 * @author 王馨苇
 *
 */
public class SemanticRoleLabelingSampleStream extends FilterObjectStream<String[],SemanticRoleLabelingSample>{

	private Logger logger = Logger.getLogger(SemanticRoleLabelingSampleStream.class.getName());
	
	protected SemanticRoleLabelingSampleStream(ObjectStream<String[]> samples) {
		super(samples);
	}

	public SemanticRoleLabelingSample read() throws IOException {				
		String[] sentence = samples.read();
		SemanticRoleLabelingSample sample = null;
		PhraseGenerateTree pgt = new PhraseGenerateTree();
		TreeToSRLTree ttst = new TreeToSRLTree();
		if(sentence != null){
			if(sentence[0].compareTo("") != 0 && sentence[1].compareTo("") != 0){
				try{
					TreeNode tree = pgt.generateTree(sentence[0]);

				}catch(Exception e){
					if (logger.isLoggable(Level.WARNING)) {						
	                    logger.warning("Error during parsing, ignoring sentence: " + sentence);
	                }	
//					sample = new SyntacticAnalysisSample<HeadTreeNode>(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
				}
				return sample;
			}else {
				return null;
			}
		}else{
			return null;
		}
	}

}
