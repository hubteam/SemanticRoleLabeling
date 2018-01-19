package com.wxw.bystep;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import opennlp.tools.ml.BeamSearch;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.ml.model.SequenceClassificationModel;
import opennlp.tools.util.model.BaseModel;

/**
 * 论元分类模型
 * @author 王馨苇
 *
 */
public class SRLModelForClassification extends BaseModel{
	private static final String COMPONENT_NAME = "SRLMEForClassification";
	private static final String SRLCLASSIFICATION_MODEL_ENTRY_NAME = "SRLClassification.model";
	
	/**
	 * 构造
	 * @param modelFile 模型文件
	 * @throws IOException IO异常
	 */
	public SRLModelForClassification(File modelFile) throws IOException {
		super(COMPONENT_NAME, modelFile);
	}

	/**
	 * 构造
	 * @param languageCode 编码
	 * @param model 最大熵模型
	 * @param beamSize 大小
	 * @param manifestInfoEntries 配置的信息
	 */
	public SRLModelForClassification(String languageCode, MaxentModel model, int beamSize,
			Map<String, String> manifestInfoEntries) {
		super(COMPONENT_NAME, languageCode, manifestInfoEntries, null);
		if (model == null) {
            throw new IllegalArgumentException("The maxentSRLModel param must not be null!");
        }

        Properties manifest = (Properties) artifactMap.get(MANIFEST_ENTRY);
        manifest.setProperty(BeamSearch.BEAM_SIZE_PARAMETER, Integer.toString(beamSize));

        //放入新训练出来的模型
        artifactMap.put(SRLCLASSIFICATION_MODEL_ENTRY_NAME, model);
        checkArtifactMap();
	}
	

	public SRLModelForClassification(String languageCode, SequenceClassificationModel<?> seqModel,
			Map<String, String> manifestInfoEntries) {
		super(COMPONENT_NAME, languageCode, manifestInfoEntries, null);
		if (seqModel == null) {
            throw new IllegalArgumentException("The maxent SRLModel param must not be null!");
        }

        artifactMap.put(SRLCLASSIFICATION_MODEL_ENTRY_NAME, seqModel);
		
	}

	/**
	 * 获取模型
	 * @return 最大熵模型
	 */
	public MaxentModel getSRLClassificationModel() {
		if (artifactMap.get(SRLCLASSIFICATION_MODEL_ENTRY_NAME) instanceof MaxentModel) {
            return (MaxentModel) artifactMap.get(SRLCLASSIFICATION_MODEL_ENTRY_NAME);
        } else {
            return null;
        }
	}
	
	@SuppressWarnings("rawtypes")
	public SequenceClassificationModel<?> getSRLClassificationSequenceModel() {

        Properties manifest = (Properties) artifactMap.get(MANIFEST_ENTRY);
        if (artifactMap.get(SRLCLASSIFICATION_MODEL_ENTRY_NAME) instanceof MaxentModel) {
            String beamSizeString = manifest.getProperty(BeamSearch.BEAM_SIZE_PARAMETER);
            int beamSize = SRLMEForClassificationContainsNullLabel.DEFAULT_BEAM_SIZE;
            if (beamSizeString != null) {
                beamSize = Integer.parseInt(beamSizeString);
            }
            return new BeamSearch(beamSize, (MaxentModel) artifactMap.get(SRLCLASSIFICATION_MODEL_ENTRY_NAME));
        } else if (artifactMap.get(SRLCLASSIFICATION_MODEL_ENTRY_NAME) instanceof SequenceClassificationModel) {
            return (SequenceClassificationModel) artifactMap.get(SRLCLASSIFICATION_MODEL_ENTRY_NAME);
        } else {
            return null;
        }
    }
}
