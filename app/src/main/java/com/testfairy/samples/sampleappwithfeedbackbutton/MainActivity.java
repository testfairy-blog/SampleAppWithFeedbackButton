package com.testfairy.samples.sampleappwithfeedbackbutton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.testfairy.TestFairy;
import com.testfairy.TestFairyFeedbackOverlay;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

	static private final String TESTFAIRY_APP_TOKEN = "c0a3512fb696bfc899ded02d339f83fe4ab6aa18";

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
		findViewById(R.id.record_a_video_with_overlay_button).setOnClickListener(reproduceBugWithOverlayClickListener);

		findViewById(R.id.start_second_activity).setOnClickListener(startSecondActivityClickListener);
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
			Toast.makeText(MainActivity.this, "Started recording video", Toast.LENGTH_LONG).show();
			TestFairy.begin(MainActivity.this, TESTFAIRY_APP_TOKEN);
		}
	};

	private View.OnClickListener reproduceBugWithOverlayClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// installOverlay() shows dialog to require user approval, and then calls begin().
			TestFairyFeedbackOverlay.installOverlay(MainActivity.this, TESTFAIRY_APP_TOKEN, TestFairyFeedbackOverlay.OverlayPurpose.VIDEO);
		}
	};
}
