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
 * 论元识别模型
 * @author 王馨苇
 *
 */
public class SRLModelForIdentification extends BaseModel{
	private static final String COMPONENT_NAME = "SRLMEForIdentification";
	private static final String SRLIDENTIFICATION_MODEL_ENTRY_NAME = "SRLIdentification.model";
	
	/**
	 * 构造
	 * @param modelFile 模型文件
	 * @throws IOException IO异常
	 */
	public SRLModelForIdentification(File modelFile) throws IOException {
		super(COMPONENT_NAME, modelFile);
	}

	/**
	 * 构造
	 * @param languageCode 编码
	 * @param model 最大熵模型
	 * @param beamSize 大小
	 * @param manifestInfoEntries 配置的信息
	 */
	public SRLModelForIdentification(String languageCode, MaxentModel model, int beamSize,
			Map<String, String> manifestInfoEntries) {
		super(COMPONENT_NAME, languageCode, manifestInfoEntries, null);
		if (model == null) {
            throw new IllegalArgumentException("The maxentSRLModel param must not be null!");
        }

        Properties manifest = (Properties) artifactMap.get(MANIFEST_ENTRY);
        manifest.setProperty(BeamSearch.BEAM_SIZE_PARAMETER, Integer.toString(beamSize));

        //放入新训练出来的模型
        artifactMap.put(SRLIDENTIFICATION_MODEL_ENTRY_NAME, model);
        checkArtifactMap();
	}
	

	public SRLModelForIdentification(String languageCode, SequenceClassificationModel<?> seqModel,
			Map<String, String> manifestInfoEntries) {
		super(COMPONENT_NAME, languageCode, manifestInfoEntries, null);
		if (seqModel == null) {
            throw new IllegalArgumentException("The maxent SRLModel param must not be null!");
        }

        artifactMap.put(SRLIDENTIFICATION_MODEL_ENTRY_NAME, seqModel);
		
	}

	/**
	 * 获取模型
	 * @return 最大熵模型
	 */
	public MaxentModel getSRLIdentificationModel() {
		if (artifactMap.get(SRLIDENTIFICATION_MODEL_ENTRY_NAME) instanceof MaxentModel) {
            return (MaxentModel) artifactMap.get(SRLIDENTIFICATION_MODEL_ENTRY_NAME);
        } else {
            return null;
        }
	}
	
	@SuppressWarnings("rawtypes")
	public SequenceClassificationModel<?> getSRLIdentificationSequenceModel() {

        Properties manifest = (Properties) artifactMap.get(MANIFEST_ENTRY);
        if (artifactMap.get(SRLIDENTIFICATION_MODEL_ENTRY_NAME) instanceof MaxentModel) {
            String beamSizeString = manifest.getProperty(BeamSearch.BEAM_SIZE_PARAMETER);
            int beamSize = SRLMEForIdentification.DEFAULT_BEAM_SIZE;
            if (beamSizeString != null) {
                beamSize = Integer.parseInt(beamSizeString);
            }
            return new BeamSearch(beamSize, (MaxentModel) artifactMap.get(SRLIDENTIFICATION_MODEL_ENTRY_NAME));
        } else if (artifactMap.get(SRLIDENTIFICATION_MODEL_ENTRY_NAME) instanceof SequenceClassificationModel) {
            return (SequenceClassificationModel) artifactMap.get(SRLIDENTIFICATION_MODEL_ENTRY_NAME);
        } else {
            return null;
        }
    }
}
