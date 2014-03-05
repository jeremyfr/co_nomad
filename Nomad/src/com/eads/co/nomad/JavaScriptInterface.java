package com.eads.co.nomad;

public class JavaScriptInterface {

	private AMMAnnexes activityAMM;
	private JobCard activityJobCard;
	private Classe classe;

	public JavaScriptInterface(AMMAnnexes activity) {
		this.activityAMM = activity;
		this.classe = Classe.AMM;
	}

	public JavaScriptInterface(JobCard activity) {
		this.activityJobCard = activity;
		this.classe = Classe.JOBCARD;
	}

	@android.webkit.JavascriptInterface
	public void receiveValueFromJs(String str) {
		switch (classe) {
		case AMM:
			activityAMM.setInfobulle((int) Double.parseDouble(str));
			break;
		case JOBCARD:
			activityJobCard.setInfobulle((int) Double.parseDouble(str));
			break;
		}
	}
}
