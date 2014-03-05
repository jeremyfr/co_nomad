package com.eads.co.nomad;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SwitchTaskManager extends WebViewClient {
	private Context context;
	private AMMAnnexes activityAMM;
	private JobCard activityJobCard;
	private Classe classe;

	public SwitchTaskManager(Context context, JobCard activity) {
		this.context = context;
		this.activityJobCard = activity;
		this.classe = Classe.JOBCARD;
	}

	public SwitchTaskManager(Context context, AMMAnnexes activity) {
		this.context = context;
		this.activityAMM = activity;
		this.classe = Classe.AMM;

	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Toast.makeText(context, url, Toast.LENGTH_SHORT).show();

		/* Ouverture d'une annexe */
		if (url.contains("?id=")) {
			String[] split = url.split("id=");
			switch (classe) {
			case AMM:
				activityAMM.onAnnexeClic(view, split[1]);
				break;
			case JOBCARD:
				activityJobCard.onAnnexeClic(view, split[1]);
				break;
			}

			/* Changement de tache */
		} else {
			Intent i = new Intent(context, AMMAnnexes.class);
			i.putExtra("task", url);
			context.startActivity(i);
		}
		return true;
	}
}
