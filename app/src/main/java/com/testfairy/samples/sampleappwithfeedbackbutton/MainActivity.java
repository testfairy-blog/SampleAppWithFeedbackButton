package com.testfairy.samples.sampleappwithfeedbackbutton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.testfairy.TestFairy;
import com.testfairy.TestFairyFeedbackOverlay;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

//	static private final String TESTFAIRY_APP_TOKEN = "APP-TOKEN";
	static private final String TESTFAIRY_APP_TOKEN = "e27cf8c46bb25d8986e21915d700e493b268df0b";

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TestFairy.setServerEndpoint("vzw.testfairy.com");
		TestFairy.setUserId("john@example.com");
		TestFairy.setAttribute("userType", "1");
		TestFairy.log("App", "User launched application on device");
		TestFairy.installCrashHandler(this, TESTFAIRY_APP_TOKEN);

		imageView = findViewById(R.id.image_view);
		Timer timer = new Timer();
		timer.schedule(rotateAndroidImageTask, 0, 1000/30);

		findViewById(R.id.report_a_bug_button).setOnClickListener(reportBugClickListener);
		findViewById(R.id.take_screenshot_button).setOnClickListener(takeScreenshotClickListener);
		findViewById(R.id.record_a_video_button).setOnClickListener(reproduceBugClickListener);

		findViewById(R.id.start_second_activity).setOnClickListener(startSecondActivityClickListener);
	}

	private void startRecording() {
		// To record immediately, just call begin().
		// TestFairy.begin(this, TESTFAIRY_APP_TOKEN);

		// installOverlay() shows dialog to require user approval, and then calls begin().
		TestFairyFeedbackOverlay.installOverlay(this, TESTFAIRY_APP_TOKEN, TestFairyFeedbackOverlay.OverlayPurpose.VIDEO);
	}

	private TimerTask rotateAndroidImageTask = new TimerTask() {

		private float y = 0;

		@Override
		public void run() {
			y += 0.5;

			imageView.setRotation(y);
		}
	};

	private View.OnClickListener startSecondActivityClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(MainActivity.this, SecondActivity.class);
			startActivity(intent);
		}
	};

	private View.OnClickListener reportBugClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			TestFairy.log("App", "User clicked on report a bug");
			TestFairy.showFeedbackForm(MainActivity.this, TESTFAIRY_APP_TOKEN, false);
		}
	};

	private View.OnClickListener takeScreenshotClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			TestFairy.log("App", "User clicked on take screenshot");
			TestFairyFeedbackOverlay.installOverlay(MainActivity.this, TESTFAIRY_APP_TOKEN, TestFairyFeedbackOverlay.OverlayPurpose.SCREENSHOT);
		}
	};

	private View.OnClickListener reproduceBugClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			startRecording();
		}
	};
}
