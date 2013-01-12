package com.logan.weibo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.logan.R;
import com.logan.weibo.adapter.QListViewAdapter;

public class QUserTimeLine extends BaseTimeLine {

	private final String TAG = "QUserTimeLine";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setSelectedFooterTab(1);
		titleTV.setText("我的微博");

		refreshBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Animation hyperspaceJump = AnimationUtils.loadAnimation(
						QUserTimeLine.this, R.anim.refresh_bt);
				LinearInterpolator lir = new LinearInterpolator();
				hyperspaceJump.setInterpolator(lir);
				refreshBtn.startAnimation(hyperspaceJump);
				Runnable r = new Runnable() {
					@Override
					public void run() {
						while (!Thread.currentThread().isInterrupted()) {
							try {
								Thread.sleep(1000);
								refreshBtn.clearAnimation();
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}
					}
				};
				Thread td = new Thread(r);
				td.start();
				QListViewAdapter sa = new QListViewAdapter(QUserTimeLine.this,
						getQWeiBoData(getQUserTimeLine()));
				lv.setAdapter(sa);
				sa.notifyDataSetInvalidated();
				sa.notifyDataSetChanged();
				final ProgressDialog myDialog = ProgressDialog.show(
						QUserTimeLine.this, "稍等", "数据加载中", true, true);
				new Thread() {
					@Override
					public void run() {
						try {
							sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						myDialog.dismiss();
					}
				}.start();
			}
		});

		statushow();

	}

	private void statushow() {
		Handler handler = new Handler();
		Runnable mTasks = new Runnable() {

			@Override
			public void run() {

			}
		};
		Thread t = new Thread(mTasks);
		t.start();

		lv.setDivider(null);
		QListViewAdapter sa = new QListViewAdapter(QUserTimeLine.this,
				getQWeiBoData(getQUserTimeLine()));
		lv.setAdapter(sa);

		final ProgressDialog myDialog = ProgressDialog.show(QUserTimeLine.this,
				"稍等", "数据加载中", true, true);
		handler.post(mTasks);

		new Thread() {
			@Override
			public void run() {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				myDialog.dismiss();
			}
		}.start();
	}

}
