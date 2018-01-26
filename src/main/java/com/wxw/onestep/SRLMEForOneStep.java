package com.wxw.onestep;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.parse.AbstractParseStrategy;
import com.wxw.srl.SRLTree;
import com.wxw.srl.SemanticRoleLabeling;
import com.wxw.stream.FileInputStreamFactory;
import com.wxw.stream.PlainTextByTreeStream;
import com.wxw.stream.SRLSample;
import com.wxw.stream.SRLSampleStream;
import com.wxw.tool.PostTreatTool;
import com.wxw.tool.PreTreatTool;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tool.TreeToSRLTreeTool;
import com.wxw.tree.HeadTreeNode;
import com.wxw.tree.PhraseGenerateTree;
import com.wxw.tree.SRLTreeNode;
import com.wxw.tree.TreeNode;
import com.wxw.validate.DefaultSRLSequenceValidator;

import opennlp.tools.ml.BeamSearch;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.TrainerFactory.TrainerType;
import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.ml.model.SequenceClassificationModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.SequenceValidator;
import opennlp.tools.util.TrainingParameters;

/**
 * 训练模型以及得到最好的K个结果
 * @author 王馨苇
 *
 */
public class SRLMEForOneStep implements SemanticRoleLabeling{
	public static final int DEFAULT_BEAM_SIZE = 15;
	private SRLContextGenerator contextGenerator;
	private SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>> model;

    private SequenceValidator<TreeNodeWrapper<HeadTreeNode>> sequenceValidator;
    
    private PhraseGenerateTree pgt = new PhraseGenerateTree();
    private AbstractParseStrategy<HeadTreeNode> parse;
	
    public SRLMEForOneStep(AbstractParseStrategy<HeadTreeNode> parse) {
    	this.parse = parse;
	}
    
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public SRLMEForOneStep(SRLModelForOneStep model, SRLContextGenerator contextGen, AbstractParseStrategy<HeadTreeNode> parse) {
		init(model , contextGen);
		this.parse = parse;
	}
    /**
     * 初始化工作
     * @param model 模型
     * @param contextGen 特征
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init(SRLModelForOneStep model, SRLContextGenerator contextGen) {
		int beamSize = SRLMEForOneStep.DEFAULT_BEAM_SIZE;

        String beamSizeString = model.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }
        contextGenerator = contextGen;
        sequenceValidator = new DefaultSRLSequenceValidator();
        if (model.getSRLTreeSequenceModel() != null) {
            this.model = (SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>>) model.getSRLTreeSequenceModel();
        } else {
        	this.model = new BeamSearch(beamSize,
                    model.getSRLTreeModel(), 0);
        }
		
	}

	/**
	 * 训练模型
	 * @param file 训练文件
	 * @param params 训练
	 * @param contextGen 特征
	 * @param encoding 编码
	 * @return 模型和模型信息的包裹结果
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public SRLModelForOneStep train(File file, TrainingParameters params, SRLContextGenerator contextGen,
			String encoding){
		SRLModelForOneStep model = null;
		try {
			ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream,parse);
			model = train("zh", sampleStream, params, contextGen);
			return model;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * 训练模型，并将模型写出
	 * @param file 训练的文本
	 * @param modelFile 模型文件
	 * @param params 训练的参数配置
	 * @param contextGen 上下文 产生器
	 * @param encoding 编码方式
	 * @return
	 */
	public SRLModelForOneStep train(File file, File modelFile, TrainingParameters params,
			SRLContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		SRLModelForOneStep model = null;
		try {
			ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream,parse);
			model = train("zh", sampleStream, params, contextGen);
            //模型的写出，文本文件
            modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));           
            model.serialize(modelOut);
            return model;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {			
            if (modelOut != null) {
                try {
                	modelOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }	
		return null;
	}
	
	/**
	 * 训练模型
	 * @param languageCode 编码
	 * @param sampleStream 文件流
	 * @param contextGen 特征
	 * @param encoding 编码
	 * @return 模型和模型信息的包裹结果
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public SRLModelForOneStep train(String languageCode, ObjectStream<SRLSample<HeadTreeNode>> sampleStream, TrainingParameters params,
			SRLContextGenerator contextGen) throws IOException {
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
		int beamSize = SRLMEForOneStep.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }
        MaxentModel SRLModel = null;
        Map<String, String> manifestInfoEntries = new HashMap<String, String>();
        TrainerType trainerType = TrainerFactory.getTrainerType(params.getSettings());
        SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>> seqSRLModel = null;
        if (TrainerType.EVENT_MODEL_TRAINER.equals(trainerType)) {
            ObjectStream<Event> es = new SRLEventStreamForOneStep(sampleStream, contextGen);
            EventTrainer trainer = TrainerFactory.getEventTrainer(params.getSettings(),
                    manifestInfoEntries);
//            SRLModel = GIS.trainModel(es, 100, 1, /2.0);
            SRLModel = trainer.train(es);                       
        }

        if (SRLModel != null) {
            return new SRLModelForOneStep(languageCode, SRLModel, beamSize, manifestInfoEntries);
        } else {
            return new SRLModelForOneStep(languageCode, seqSRLModel, manifestInfoEntries);
        }
	}
	
	/**
	 * 得到最好的结果序列
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public Sequence topSequences(TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree) {
        return model.bestSequences(1, argumentree, predicatetree, contextGenerator, sequenceValidator)[0];
    }
	
	/**
	 * 得到最好的结果序列
	 * @param k
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public Sequence[] topKSequences(int k,TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree) {
        return model.bestSequences(k, argumentree, predicatetree, contextGenerator, sequenceValidator);
    }
	
	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public SRLTree srltree(TreeNode tree, int[] predicateinfo) {
		return kSrltree(1,tree,predicateinfo)[0];
	}
	
	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public SRLTree srltree(String treeStr, int[] predicateinfo) {
		TreeNode node = pgt.generateTree("("+treeStr+")");
		return srltree(node,predicateinfo);
	}
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public SRLTree[] kSrltree(int k,TreeNode tree, int[] predicateinfo) {
		SRLSample<HeadTreeNode> sample = null;
		PreTreatTool.preTreat(tree);
		String str = SRLSample.indexToTrainSample(predicateinfo);
        sample = parse.parse(tree, str);
        List<SRLTree> srllist = new ArrayList<>();
        Sequence[] sequence = topKSequences(k,sample.getArgumentTree(),sample.getPredicateTree());
        for (int i = 0; i < sequence.length; i++) {
        	String[] newlabelinfo = sequence[i].getOutcomes().toArray(new String[sequence[i].getOutcomes().size()]);
    		if(sample.getIsPruning() == true){
    			newlabelinfo = PostTreatTool.postTreat(sample.getArgumentTree(),sequence[i],PostTreatTool.getSonTreeCount(sample.getArgumentTree()[0].getTree().getParent()));
        	}else{
        		newlabelinfo = PostTreatTool.postTreat(sample.getArgumentTree(),sequence[i],sample.getArgumentTree().length);
        	}
    		SRLTreeNode srltreenode = TreeToSRLTreeTool.treeToSRLTree(tree, sample.getArgumentTree(), newlabelinfo);
    		SRLTree srltree = new SRLTree();
    		srltree.setSRLTree(srltreenode);
    		srllist.add(srltree);
        }
		return srllist.toArray(new SRLTree[srllist.size()]);
	}
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param k
	 * @param tree 句法分析得到的树的括号表示
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public SRLTree[] kSrltree(int k,String treeStr, int[] predicateinfo) {
		TreeNode node = pgt.generateTree("("+treeStr+")");
		return kSrltree(k,node,predicateinfo);
	}
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public String srlstr(TreeNode tree, int[] predicateinfo) {
		return kSrlstr(1,tree,predicateinfo)[0];
	}
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public String srlstr(String treeStr, int[] predicateinfo) {
		TreeNode node = pgt.generateTree("("+treeStr+")");
		return srlstr(node,predicateinfo);
	}
	
	/**
	 * 得到一棵树最好的K个角色标注的中括号表达式形式
	 * @param k
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public String[] kSrlstr(int k,TreeNode tree, int[] predicateinfo) {
		SRLTree[] srltree = kSrltree(k,tree,predicateinfo);
		String[] output = new String[srltree.length];
 		for (int i = 0; i < srltree.length; i++) {
			String str = SRLTreeNode.printSRLBracket(srltree[i].getSRLTreeRoot());
			output[i] = str;
		}
		return output;
	}
	
	/**
	 * 得到一棵树最好的K个角色标注的中括号表达式形式
	 * @param k
	 * @param tree 句法分析得到的树的括号表示
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public String[] kSrlstr(int k,String treeStr, int[] predicateinfo) {
		TreeNode node = pgt.generateTree("("+treeStr+")");
		return kSrlstr(k,node,predicateinfo);
	}
	
	
	@Override
	public SRLTree srltree(TreeNode tree, String[] predicateinfo) {
		return kSrltree(1,tree,predicateinfo)[0];
	}
	@Override
	public SRLTree srltree(String treeStr, String[] predicateinfo) {
		TreeNode tree = pgt.generateTree("("+treeStr+")");
		return srltree(tree,predicateinfo);
	}
	@Override
	public SRLTree[] kSrltree(int k, TreeNode tree, String[] predicateinfo) {
		int[] index = SRLSample.getPredicateIndex(tree, predicateinfo);
		return kSrltree(k,tree,index);
	}
	@Override
	public SRLTree[] kSrltree(int k, String treeStr, String[] predicateinfo) {
		TreeNode tree = pgt.generateTree("("+treeStr+")");
		return kSrltree(k,tree,predicateinfo);
	}
	@Override
	public String srlstr(TreeNode tree, String[] predicateinfo) {
		return kSrlstr(1,tree,predicateinfo)[0];
	}
	@Override
	public String srlstr(String treeStr, String[] predicateinfo) {
		TreeNode tree = pgt.generateTree("("+treeStr+")");
		return srlstr(tree,predicateinfo);
	}
	@Override
	public String[] kSrlstr(int k, TreeNode tree, String[] predicateinfo) {
		int[] index = SRLSample.getPredicateIndex(tree, predicateinfo);
		return kSrlstr(k,tree,index);
	}
	@Override
	public String[] kSrlstr(int k, String treeStr, String[] predicateinfo) {
		TreeNode tree = pgt.generateTree("("+treeStr+")");
		return kSrlstr(k,tree,predicateinfo);
	}
}


