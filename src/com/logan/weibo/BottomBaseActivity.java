package com.logan.weibo;

import com.logan.R;
import com.logan.authorize.AppList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BottomBaseActivity extends BaseActivity {

	private final String TAG = "BottomBaseActivity";

	public ImageView writeBtn = null;
	public ImageView refreshBtn = null;
	public TextView titleTV = null;
	public View friendTimeLine;
	public View userTimeLine;
	public View userNews;
	public View userInfo;
	public View more;
	public int mCurFooterTab = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayout());
		
		writeBtn=(ImageView) findViewById(R.id.weibo_writeBtn);
		writeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Tweet.class);
				startActivity(intent);
			}
		});
		titleTV=(TextView) findViewById(R.id.weibo_title_TV);
		refreshBtn=(ImageView) findViewById(R.id.weibo_refreshBtn);
		
		// LayoutInflater inflater = getLayoutInflater();
		// View menuBar = inflater.inflate(R.layout.weibo_menu, null);

		friendTimeLine = findViewById(R.id.weibo_menu_friendTimeLine);
		friendTimeLine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent();
				i.setClass(getApplicationContext(), FriendTimeLine.class);
				startActivity(i);
				finish();
			}
		});
		userTimeLine = findViewById(R.id.weibo_menu_userTimeLine);
		userTimeLine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent();
				i.setClass(getApplicationContext(), UserTimeLine.class);
				startActivity(i);
				finish();
			}
		});
		userNews = findViewById(R.id.weibo_menu_userNews);
		userNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Log.v(TAG, "userNews");
				Intent i = new Intent();
				i.setClass(getApplicationContext(), UserNews.class);
				startActivity(i);
				finish();
			}
		});
		userInfo = findViewById(R.id.weibo_menu_myInfo);
		userInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Log.v(TAG, "userInfo");
				Intent i = new Intent();
				i.setClass(getApplicationContext(), UserInfo.class);
				startActivity(i);
				finish();
			}
		});
		more = findViewById(R.id.weibo_menu_more);
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent();
				i.setClass(getApplicationContext(), More.class);
				startActivity(i);
				finish();
			}
		});

	}

	protected void setSelectedFooterTab(int i) {
		mCurFooterTab = i;
		if (i == 0)
			friendTimeLine
					.setBackgroundResource(R.drawable.weibo_menu_cp_bg_selected);
		if (i == 1)
			userTimeLine
					.setBackgroundResource(R.drawable.weibo_menu_cp_bg_selected);
		if (i == 2)
			userNews.setBackgroundResource(R.drawable.weibo_menu_cp_bg_selected);
		if (i == 3)
			userInfo.setBackgroundResource(R.drawable.weibo_menu_cp_bg_selected);
		if (i == 4)
			more.setBackgroundResource(R.drawable.weibo_menu_cp_bg_selected);
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@Override
	protected void onRestart() {
		
		super.onRestart();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
	}

	@Override
	protected void onStop() {
		
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(Menu.NONE, 1, 1, R.string.add).setIcon(R.drawable.add);
		menu.add(Menu.NONE, 2, 2, R.string.delete).setIcon(R.drawable.delete);
		menu.add(Menu.NONE, 3, 3, R.string.network).setIcon(R.drawable.network);
		menu.add(Menu.NONE, 4, 4, R.string.updates).setIcon(R.drawable.uodates);
		menu.add(Menu.NONE, 5, 5, R.string.about).setIcon(R.drawable.about);
		menu.add(Menu.NONE, 6, 6, R.string.exit).setIcon(R.drawable.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case 1:
			Intent intent = new Intent();
			intent.setClass(this, AppList.class);
			this.startActivity(intent);
			break;
		case 2:
			AlertDialog.Builder delete = new AlertDialog.Builder(this);
			delete.setTitle("删除账户");
			delete.setIcon(R.drawable.delete);
			delete.setMessage("提醒：" + "\n" + "\n"
					+ "删除账户会删除本地所存储的所有账户和密码，下次登录时需要重新输入。" + "\n" + "\n"
					+ "你确定要继续删除账户吗？");
			delete.setPositiveButton("删除",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialoginterface,
								int which) {
							// Toast.makeText(this, "你选择了删除", Toast.LENGTH_LONG)
							// .show();
						}
					});
			delete.setNegativeButton("不删除",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialoginterface,
								int which) {
							// Toast.makeText(this, "你选择了不删除",
							// Toast.LENGTH_LONG)
							// .show();
						}
					});
			delete.create().show();
			break;
		case 3:
			startActivityForResult(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
			break;
		case 4:
			AlertDialog.Builder updates = new AlertDialog.Builder(this);
			updates.setTitle("检查更新");
			updates.setIcon(R.drawable.uodates);
			updates.setMessage("提醒：" + "\n" + "\n" + "更新新版本将会替换以前的版本。" + "\n"
					+ "\n" + "你确定要继续更新吗？");
			updates.setPositiveButton("更新",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialoginterface,
								int which) {
							// Toast.makeText(this, "你选择了更新", Toast.LENGTH_LONG)
							// .show();
						}
					});
			updates.setNegativeButton("不更新",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialoginterface,
								int which) {
							// Toast.makeText(this, "你选择了不更新",
							// Toast.LENGTH_LONG)
							// .show();
						}
					});
			updates.create().show();
			break;
		case 5:
			AlertDialog.Builder about = new AlertDialog.Builder(this);
			about.setTitle("关于");
			about.setIcon(R.drawable.about);
			about.setMessage("作者：logan" + "\n" + "\n" + "QQ：676395711" + "\n"
					+ "\n" + "来自：骆驼部落");
			about.setNeutralButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialoginterface, int which) {

				}
			});
			about.create().show();
			break;
		case 6:
			AlertDialog.Builder exit = new AlertDialog.Builder(this);
			exit.setTitle("退出");
			exit.setIcon(R.drawable.exit);
			exit.setMessage("提醒：" + "\n" + "\n" + "你确定要退出微博通吗？");
			exit.setPositiveButton("退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialoginterface, int which) {
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(startMain);
					System.exit(0);
				}
			});
			exit.setNegativeButton("返回", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialoginterface, int which) {
					// Toast.makeText(this, "你选择了返回", Toast.LENGTH_LONG).show();
				}
			});
			exit.create().show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
