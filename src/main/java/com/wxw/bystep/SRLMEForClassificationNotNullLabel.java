package com.wxw.bystep;

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
import java.util.TreeMap;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.parse.AbstractParseStrategy;
import com.wxw.parse.SRLParseAddNULL_101HasPruning;
import com.wxw.srl.SRLTree;
import com.wxw.srl.SemanticRoleLabeling;
import com.wxw.stream.FileInputStreamFactory;
import com.wxw.stream.PlainTextByTreeStream;
import com.wxw.stream.SRLSample;
import com.wxw.stream.SRLSampleStream;
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
 * 论元分类模型的训练(不包含NULL标记)
 * @author 王馨苇
 *
 */
public class SRLMEForClassificationNotNullLabel implements SemanticRoleLabeling{

	public static final int DEFAULT_BEAM_SIZE = 15;
	private SRLContextGenerator contextGeneratorClas;
	private SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>> modelClas;
	
	private SRLContextGenerator contextGeneratorIden;
	private SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>> modelIden;

    private SequenceValidator<TreeNodeWrapper<HeadTreeNode>> sequenceValidator;
    private PhraseGenerateTree pgt = new PhraseGenerateTree();
	
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public SRLMEForClassificationNotNullLabel(SRLModelForIdentification modelIden,SRLModelForClassification modelClas, SRLContextGenerator contextGenIden, SRLContextGenerator contextGenClas) {
		init(modelIden, modelClas, contextGenIden, contextGenClas);
	}
    /**
     * 初始化工作
     * @param model 模型
     * @param contextGen 特征
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init(SRLModelForIdentification modelIden,SRLModelForClassification modelClas, SRLContextGenerator contextGenIden, SRLContextGenerator contextGenClas) {
		int beamSizeClas = SRLMEForClassificationContainsNullLabel.DEFAULT_BEAM_SIZE;

        String beamSizeStringClas = modelClas.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeStringClas != null) {
            beamSizeClas = Integer.parseInt(beamSizeStringClas);
        }
        contextGeneratorClas = contextGenClas;
        sequenceValidator = new DefaultSRLSequenceValidator();
        if (modelClas.getSRLClassificationSequenceModel() != null) {
            this.modelClas = (SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>>) modelClas.getSRLClassificationSequenceModel();
        } else {
        	this.modelClas = new BeamSearch(beamSizeClas,
        			modelClas.getSRLClassificationModel(), 0);
        }
        
        int beamSizeIden = SRLMEForIdentification.DEFAULT_BEAM_SIZE;

        String beamSizeStringIden = modelIden.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeStringIden != null) {
            beamSizeIden = Integer.parseInt(beamSizeStringIden);
        }
        contextGeneratorIden = contextGenIden;
        sequenceValidator = new DefaultSRLSequenceValidator();
        if (modelIden.getSRLIdentificationModel() != null) {
            this.modelIden = (SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>>) modelIden.getSRLIdentificationSequenceModel();
        } else {
        	this.modelIden = new BeamSearch(beamSizeIden,
        			modelIden.getSRLIdentificationModel(), 0);
        }
	}
	
	/**
	 * 训练模型
	 * @param file 训练文件
	 * @param params 训练参数
	 * @param contextGen 特征
	 * @param encoding 编码
	 * @return 模型和模型信息的包裹结果
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static SRLModelForClassification train(File file, TrainingParameters params, SRLContextGenerator contextGen,
			String encoding){
		SRLModelForClassification model = null;
		try {
			ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream);
			model = SRLMEForClassificationNotNullLabel.train("zh", sampleStream, params, contextGen);
			return model;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}

	/**
	 * 训练模型
	 * @param languageCode 编码
	 * @param sampleStream 文件流
	 * @param contextGen 特征
	 * @return 模型和模型信息的包裹结果
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static SRLModelForClassification train(String languageCode, ObjectStream<SRLSample<HeadTreeNode>> sampleStream, TrainingParameters params,
			SRLContextGenerator contextGen) throws IOException {
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
		int beamSize = SRLMEForClassificationNotNullLabel.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }
        MaxentModel SRLModel = null;
        Map<String, String> manifestInfoEntries = new HashMap<String, String>();
        TrainerType trainerType = TrainerFactory.getTrainerType(params.getSettings());
        SequenceClassificationModel<?> seqSRLModel = null;
        if (TrainerType.EVENT_MODEL_TRAINER.equals(trainerType)) {
            ObjectStream<Event> es = new SRLEventStreamForClassificationNotNullLabel(sampleStream, contextGen);
            EventTrainer trainer = TrainerFactory.getEventTrainer(params.getSettings(),
                    manifestInfoEntries);
//            SRLModel = GIS.trainModel(es, 100, 1, /2.0);
            SRLModel = trainer.train(es);                       
        }

        if (SRLModel != null) {
            return new SRLModelForClassification(languageCode, SRLModel, beamSize, manifestInfoEntries);
        } else {
            return new SRLModelForClassification(languageCode, seqSRLModel, manifestInfoEntries);
        }
	}

	/**
	 * 训练模型，并将模型写出
	 * @param file 训练的文本
	 * @param modeltxtFile 文本类型的模型文件
	 * @param params 训练的参数配置
	 * @param contextGen 上下文 产生器
	 * @param encoding 编码方式
	 * @param tagger 识别阶段训练出来的模型和特征类
	 * @return
	 */
	public static SRLModelForClassification train(File file, File modelFile, TrainingParameters params,
			SRLContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		SRLModelForClassification model = null;
		try {
			ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream);
			model = SRLMEForClassificationNotNullLabel.train("zh", sampleStream, params, contextGen);
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
	 * 得到最好的结果序列
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public Sequence topSequences(TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree) {
        return modelClas.bestSequences(1, argumentree, predicatetree, contextGeneratorClas, sequenceValidator)[0];
    }
	
	/**
	 * 得到最好的结果字符串
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public String[] tag(TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree){
		Sequence sequence = modelClas.bestSequence(argumentree, predicatetree, contextGeneratorClas, sequenceValidator);
		List<String> outcome = sequence.getOutcomes();
		return outcome.toArray(new String[outcome.size()]);
	}
	
	/**
	 * 得到最好的K个结果序列
	 * @param k
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public Sequence[] topKSequences(int k,TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree) {
        return modelClas.bestSequences(k, argumentree, predicatetree, contextGeneratorClas, sequenceValidator);
    }
	
	/**
	 * 得到最好的K个识别结果
	 * @param k
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public Sequence[] topKSequencesForIden(int k,TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree) {
        return modelIden.bestSequences(k, argumentree, predicatetree, contextGeneratorIden, sequenceValidator);
    }
	
	/**
	 * 得到最好的识别结果
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public Sequence topSequencesForIden(TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree) {
        return modelIden.bestSequences(1, argumentree, predicatetree, contextGeneratorIden, sequenceValidator)[0];
    }
	
	/**
	 * 得到最好的识别结果
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public String[] tagForIden(TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree){
		Sequence sequence = modelIden.bestSequence(argumentree, predicatetree, contextGeneratorIden, sequenceValidator);
		List<String> outcome = sequence.getOutcomes();
		return outcome.toArray(new String[outcome.size()]);
	}
	

	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public SRLTree srltree(TreeNode tree, String predicateinfo) {
		return kSrltree(1,tree,predicateinfo)[0];
	}
	
	/**
	 * 得到一棵树的语义角色标注
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public SRLTree srltree(String treeStr, String predicateinfo) {
		TreeNode node = pgt.generateTree("("+treeStr+")");
		return srltree(node,predicateinfo);
	}
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param k
	 * @param tree 句法分析得到的树
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public SRLTree[] kSrltree(int k,TreeNode tree, String predicateinfo) {
		AbstractParseStrategy<HeadTreeNode> ttst = new SRLParseAddNULL_101HasPruning();
		SRLSample<HeadTreeNode> sample = null;
		PreTreatTool.preTreat(tree);
        sample = ttst.parse(tree, predicateinfo);
        List<SRLTree> srllist = new ArrayList<>();
        Sequence[] sequence = topKSequencesForIden(k,sample.getArgumentTree(),sample.getPredicateTree());
        TreeMap<Sequence,TreeNodeWrapper<HeadTreeNode>[]> result = new TreeMap<>(); 
        for (int i = 0; i < sequence.length; i++) {
        	String[] newlabelinfo = sequence[i].getOutcomes().toArray(new String[sequence[i].getOutcomes().size()]);
    		List<Integer> index = SRLSample.filterNotNULLLabelIndex(newlabelinfo);
    		TreeNodeWrapper<HeadTreeNode>[] argumenttree = SRLSample.getArgumentTreeFromIndex(sample.getArgumentTree(), index);
    		Sequence[] res = topKSequences(k,argumenttree, sample.getPredicateTree());
    		for (int j = 0; j < res.length; j++) {
				result.put(res[i], argumenttree);
			}
        }      
        for (Sequence s : result.keySet()) {
        	SRLTreeNode srltreenode = TreeToSRLTreeTool.treeToSRLTree(tree, result.get(s), s.getOutcomes().toArray(new String[s.getOutcomes().size()]));
    		SRLTree srltree = new SRLTree();
    		srltree.setSRLTree(srltreenode);
    		srllist.add(srltree);
		}
		return srllist.toArray(new SRLTree[srllist.size()]);
	}
	
	/**
	 * 得到一棵树最好的K个角色标注
	 * @param tree 句法分析得到的树的括号表示
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public SRLTree[] kSrltree(int k,String treeStr, String predicateinfo) {
		TreeNode node = pgt.generateTree("("+treeStr+")");
		return kSrltree(k,node,predicateinfo);
	}
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树
	 * @return
	 */
	@Override
	public String srlstr(TreeNode tree, String predicateinfo) {
		return kSrlstr(1,tree,predicateinfo)[0];
	}
	
	/**
	 * 得到一棵树的语义角色标注的中括号表达式形式
	 * @param tree 句法分析得到的树的括号表达式形式
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public String srlstr(String treeStr, String predicateinfo) {
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
	public String[] kSrlstr(int k,TreeNode tree, String predicateinfo) {
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
	 * @param tree 句法分析得到的树的括号表示
	 * @param predicateinfo 谓词信息
	 * @return
	 */
	@Override
	public String[] kSrlstr(int k,String treeStr, String predicateinfo) {
		TreeNode node = pgt.generateTree("("+treeStr+")");
		return kSrlstr(k,node,predicateinfo);
	}
}
