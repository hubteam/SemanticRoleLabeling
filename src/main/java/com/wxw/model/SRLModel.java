package com.wxw.model;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.wxw.tree.HeadTreeNode;

import opennlp.tools.ml.BeamSearch;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.ml.model.SequenceClassificationModel;
import opennlp.tools.util.model.BaseModel;

/**
 * 模型类
 * @author 王馨苇
 *
 */
public class SRLModel extends BaseModel{

	private static final String COMPONENT_NAME = "SRLME";
	private static final String SRL_MODEL_ENTRY_NAME = "srl.model";
	
	/**
	 * 构造
	 * @param modelFile 模型文件
	 * @throws IOException IO异常
	 */
	public SRLModel(File modelFile) throws IOException {
		super(COMPONENT_NAME, modelFile);
	}

	/**
	 * 构造
	 * @param languageCode 编码
	 * @param model 最大熵模型
	 * @param beamSize 大小
	 * @param manifestInfoEntries 配置的信息
	 */
	public SRLModel(String languageCode, MaxentModel model, int beamSize,
			Map<String, String> manifestInfoEntries) {
		super(COMPONENT_NAME, languageCode, manifestInfoEntries, null);
		if (model == null) {
            throw new IllegalArgumentException("The maxentSRLModel param must not be null!");
        }

        Properties manifest = (Properties) artifactMap.get(MANIFEST_ENTRY);
        manifest.setProperty(BeamSearch.BEAM_SIZE_PARAMETER, Integer.toString(beamSize));

        //放入新训练出来的模型
        artifactMap.put(SRL_MODEL_ENTRY_NAME, model);
        checkArtifactMap();
	}
	

	public SRLModel(String languageCode, SequenceClassificationModel<String> seqModel,
			Map<String, String> manifestInfoEntries) {
		super(COMPONENT_NAME, languageCode, manifestInfoEntries, null);
		if (seqModel == null) {
            throw new IllegalArgumentException("The maxent SRLModel param must not be null!");
        }

        artifactMap.put(SRL_MODEL_ENTRY_NAME, seqModel);
		
	}

	/**
	 * 获取模型
	 * @return 最大熵模型
	 */
	public MaxentModel getSRLTreeModel() {
		if (artifactMap.get(SRL_MODEL_ENTRY_NAME) instanceof MaxentModel) {
            return (MaxentModel) artifactMap.get(SRL_MODEL_ENTRY_NAME);
        } else {
            return null;
        }
	}
	
	public SequenceClassificationModel<HeadTreeNode> getSRLTreeSequenceModel() {

        Properties manifest = (Properties) artifactMap.get(MANIFEST_ENTRY);

        if (artifactMap.get(SRL_MODEL_ENTRY_NAME) instanceof MaxentModel) {
            String beamSizeString = manifest.getProperty(BeamSearch.BEAM_SIZE_PARAMETER);

            int beamSize = SRLME.DEFAULT_BEAM_SIZE;
            if (beamSizeString != null) {
                beamSize = Integer.parseInt(beamSizeString);
            }

            return new BeamSearch(beamSize, (MaxentModel) artifactMap.get(SRL_MODEL_ENTRY_NAME));
        } else if (artifactMap.get(SRL_MODEL_ENTRY_NAME) instanceof SequenceClassificationModel) {
            return (SequenceClassificationModel<HeadTreeNode>) artifactMap.get(SRL_MODEL_ENTRY_NAME);
        } else {
            return null;
        }
    }
}

