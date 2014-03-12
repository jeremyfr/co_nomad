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
		/* Ouverture d'une annexe */
		if (url.contains("?id=")) {
			String[] split = url.split("id=");
			String t = split[1];
			System.out.println("split1 :" + t);
			t = t.replaceAll("\\.\\.", " ");
			System.out.println("replaceAll :" + t);
			switch (classe) {
			case AMM:
				activityAMM.onAnnexeClic(view, t);
				break;
			case JOBCARD:
				activityJobCard.onAnnexeClic(view, t);
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
