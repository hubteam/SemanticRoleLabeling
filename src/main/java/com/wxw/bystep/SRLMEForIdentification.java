package com.wxw.bystep;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.parse.AbstractParseStrategy;
import com.wxw.stream.FileInputStreamFactory;
import com.wxw.stream.PlainTextByTreeStream;
import com.wxw.stream.SRLSample;
import com.wxw.stream.SRLSampleStream;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;
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
 * 论元识别模型的训练
 * @author 王馨苇
 *
 */
public class SRLMEForIdentification {

	public static final int DEFAULT_BEAM_SIZE = 15;
	private SRLContextGenerator contextGenerator;
	private int size;
	private SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>> model;

    private SequenceValidator<TreeNodeWrapper<HeadTreeNode>> sequenceValidator;
	
    private AbstractParseStrategy<HeadTreeNode> parse;
	
    public SRLMEForIdentification(AbstractParseStrategy<HeadTreeNode> parse) {
    	this.parse = parse;
	}
    
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public SRLMEForIdentification(SRLModelForIdentification model, SRLContextGenerator contextGen,AbstractParseStrategy<HeadTreeNode> parse) {
		init(model , contextGen);
		this.parse = parse;
	}
    /**
     * 初始化工作
     * @param model 模型
     * @param contextGen 特征
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init(SRLModelForIdentification model, SRLContextGenerator contextGen) {
		int beamSize = SRLMEForIdentification.DEFAULT_BEAM_SIZE;

        String beamSizeString = model.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }
        contextGenerator = contextGen;
        size = beamSize;
        sequenceValidator = new DefaultSRLSequenceValidator();
        if (model.getSRLIdentificationSequenceModel() != null) {
            this.model = (SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>>) model.getSRLIdentificationSequenceModel();
        } else {
        	this.model = new BeamSearch(beamSize,
                    model.getSRLIdentificationModel(), 0);
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
	public SRLModelForIdentification train(File file, TrainingParameters params, SRLContextGenerator contextGen,
			String encoding){
		SRLModelForIdentification model = null;
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
	public SRLModelForIdentification train(File file, File modelFile, TrainingParameters params,
			SRLContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		SRLModelForIdentification model = null;
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
	public SRLModelForIdentification train(String languageCode, ObjectStream<SRLSample<HeadTreeNode>> sampleStream, TrainingParameters params,
			SRLContextGenerator contextGen) throws IOException {
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
		int beamSize = SRLMEForIdentification.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }
        MaxentModel SRLModel = null;
        Map<String, String> manifestInfoEntries = new HashMap<String, String>();
        TrainerType trainerType = TrainerFactory.getTrainerType(params.getSettings());
        SequenceClassificationModel<?> seqPosModel = null;
        if (TrainerType.EVENT_MODEL_TRAINER.equals(trainerType)) {
            ObjectStream<Event> es = new SRLEventStreamForIdentification(sampleStream, contextGen);
            EventTrainer trainer = TrainerFactory.getEventTrainer(params.getSettings(),
                    manifestInfoEntries);
//            SRLModel = GIS.trainModel(es, 100, 1, /2.0);
            SRLModel = trainer.train(es);                       
        }

        if (SRLModel != null) {
            return new SRLModelForIdentification(languageCode, SRLModel, beamSize, manifestInfoEntries);
        } else {
            return new SRLModelForIdentification(languageCode, seqPosModel, manifestInfoEntries);
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
	 * 得到最好的结果字符串
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public String[] tag(TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree){
		Sequence sequence = model.bestSequence(argumentree, predicatetree, contextGenerator, sequenceValidator);
		List<String> outcome = sequence.getOutcomes();
		return outcome.toArray(new String[outcome.size()]);
	}
	
	/**
	 * 得到最好的结果序列
	 * @param headtree 子树序列
	 * @param semanticinfo 语义信息
	 * @return
	 */
	public Sequence[] topKSequences(TreeNodeWrapper<HeadTreeNode>[] argumentree, Object[] predicatetree) {
        return model.bestSequences(size, argumentree, predicatetree, contextGenerator, sequenceValidator);
    }
}
