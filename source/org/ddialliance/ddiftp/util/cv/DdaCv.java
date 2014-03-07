package org.ddialliance.ddiftp.util.cv;

public enum DdaCv {
	//
	// study description
	//
	KIND_OF_DATA("kindofdata", "1.0.0"),
	
	ANALYSIS_UNIT("analysisunit", "1.0.0"),
	
	//
	// data collection methodology
	//
	DATA_COLLECTION_METHODOLOGY("datacollectionmethodology", "1.0.0"),

	DATA_COLLETION_TIME_METHOD("timemethod", "1.0.0"),

	DATA_COLLECTION_SAMPLING_PROCEDURE("samplingprocedure", "1.0.0"),

	//
	// data collection event
	//
	DATA_COLLECTION_EVENT_MODE("datacollectionmode", "1.0.0"),

	DATA_COLLECTION_EVENT_SITUATION("collectionsituation", "1.0.0"),

	//
	// archive
	//
	STUDY_STATE("studystate", "2.0.0"),

	ACCESS_RESTRICTION("accessrestrictions", "1.0.0"),

	ACCESS_CONDITION("accessconditions", "1.0.0");

	private String name;
	private String version;

	private DdaCv(String name, String version) {
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}
}
