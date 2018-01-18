package com.wxw.onestep;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.wxw.feature.SRLContextGenerator;
import com.wxw.stream.FileInputStreamFactory;
import com.wxw.stream.PlainTextByTreeStream;
import com.wxw.stream.SRLSample;
import com.wxw.stream.SRLSampleEventStream;
import com.wxw.stream.SRLSampleStream;
import com.wxw.tool.TreeNodeWrapper;
import com.wxw.tree.HeadTreeNode;

import opennlp.tools.ml.BeamSearch;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.TrainerFactory.TrainerType;
import opennlp.tools.ml.maxent.GIS;
import opennlp.tools.ml.maxent.GISModel;
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
public class SRLME {
	public static final int DEFAULT_BEAM_SIZE = 15;
	private SRLContextGenerator contextGenerator;
	private int size;
	private Sequence bestSequence;
	private SequenceClassificationModel<TreeNodeWrapper<HeadTreeNode>> model;
	private SRLModel modelPackage;

    private SequenceValidator<TreeNodeWrapper<HeadTreeNode>> sequenceValidator;
	
	/**
	 * 构造函数，初始化工作
	 * @param model 模型
	 * @param contextGen 特征
	 */
	public SRLME(SRLModel model, SRLContextGenerator contextGen) {
		init(model , contextGen);
	}
    /**
     * 初始化工作
     * @param model 模型
     * @param contextGen 特征
     */
	private void init(SRLModel model, SRLContextGenerator contextGen) {
		int beamSize = SRLME.DEFAULT_BEAM_SIZE;

        String beamSizeString = model.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }

        modelPackage = model;

        contextGenerator = contextGen;
        size = beamSize;
        sequenceValidator = new DefaultSRLSequenceValidator();
        if (model.getSRLTreeSequenceModel() != null) {
            this.model = model.getSRLTreeSequenceModel();
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
	public static SRLModel train(File file, TrainingParameters params, SRLContextGenerator contextGen,
			String encoding){
		SRLModel model = null;
		try {
			ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream);
			model = SRLME.train("zh", sampleStream, params, contextGen);
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
	 * @param encoding 编码
	 * @return 模型和模型信息的包裹结果
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static SRLModel train(String languageCode, ObjectStream<SRLSample<HeadTreeNode>> sampleStream, TrainingParameters params,
			SRLContextGenerator contextGen) throws IOException {
		String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
		int beamSize = SRLME.DEFAULT_BEAM_SIZE;
        if (beamSizeString != null) {
            beamSize = Integer.parseInt(beamSizeString);
        }
        MaxentModel SRLModel = null;
        Map<String, String> manifestInfoEntries = new HashMap<String, String>();
        TrainerType trainerType = TrainerFactory.getTrainerType(params.getSettings());
        SequenceClassificationModel<String> seqPosModel = null;
        if (TrainerType.EVENT_MODEL_TRAINER.equals(trainerType)) {
            ObjectStream<Event> es = new SRLSampleEventStream(sampleStream, contextGen);
            EventTrainer trainer = TrainerFactory.getEventTrainer(params.getSettings(),
                    manifestInfoEntries);
//            SRLModel = GIS.trainModel(es, 100, 1, /2.0);
            SRLModel = trainer.train(es);                       
        }

        if (SRLModel != null) {
            return new SRLModel(languageCode, SRLModel, beamSize, manifestInfoEntries);
        } else {
            return new SRLModel(languageCode, seqPosModel, manifestInfoEntries);
        }
	}

	/**
	 * 训练模型，并将模型写出
	 * @param file 训练的文本
	 * @param modelbinaryFile 二进制的模型文件
	 * @param modeltxtFile 文本类型的模型文件
	 * @param params 训练的参数配置
	 * @param contextGen 上下文 产生器
	 * @param encoding 编码方式
	 * @return
	 */
	public static SRLModel train(File file, File modelFile, TrainingParameters params,
			SRLContextGenerator contextGen, String encoding) {
		OutputStream modelOut = null;
		SRLModel model = null;
		try {
			ObjectStream<String[]> lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(file), encoding);
			ObjectStream<SRLSample<HeadTreeNode>> sampleStream = new SRLSampleStream(lineStream);
			model = SRLME.train("zh", sampleStream, params, contextGen);
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
        return model.bestSequences(1, argumentree, predicatetree, contextGenerator, sequenceValidator)[0];
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


