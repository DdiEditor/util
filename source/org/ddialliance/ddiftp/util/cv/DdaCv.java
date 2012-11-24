package org.ddialliance.ddiftp.util.cv;

public enum DdaCv {
	STUDY_STATE("studystate", "1.0.0"),

	ACCESS_RESTRICTION("accessrestrictions", "1.0.0"),

	ACCESS_CONDITION("accessconditions", "1.0.0"),

	KIND_OF_DATA("kindofdata", "1.0.0"),

	DATA_COLLECTION_METHODOLOGY("datacollectionmethodology", "1.0.0"),
	
	DATA_COLLECTION_MODE("datacollectionmode", "1.0.0"),
	
	SAMPLING_PROCEDURE("samplingprocedure", "1.0.0");

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
