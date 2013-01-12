package com.logan.weibo;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.logan.R;
import com.logan.weibo.adapter.ListViewAdapter;
import com.weibo.net.WeiboException;
/**
 * 微博主页
 * @author Logan
 *
 */
public class FriendTimeLine extends BaseTimeLine {

	private final String TAG = "FriendTimeLine";

	ListViewAdapter sa = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setSelectedFooterTab(0);
		titleTV.setText("微博主页");

		refreshBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Animation hyperspaceJump = AnimationUtils.loadAnimation(
						FriendTimeLine.this, R.anim.refresh_bt);
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

				try {
					sa = getWeiBoData(getFriendTimeline());
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (WeiboException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				lv.setAdapter(sa);
				sa.notifyDataSetInvalidated();
				sa.notifyDataSetChanged();
				final ProgressDialog myDialog = ProgressDialog.show(
						FriendTimeLine.this, "稍等", "数据加载中", true, true);
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
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				Log.v(TAG, "oItem " + id + " Clicked!");
				new Thread() {
					@Override
					public void run() {
						Intent intent = new Intent();
						intent.putExtra("position", position + "");
						intent.putExtra("jsonData", jsonData);
						intent.setClass(FriendTimeLine.this, StatusDetail.class);
						startActivity(intent);
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
		try {
			sa = getWeiBoData(getFriendTimeline());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (WeiboException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lv.setAdapter(sa);

		final ProgressDialog myDialog = ProgressDialog.show(FriendTimeLine.this, "稍等", "数据加载中", true, true);
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
