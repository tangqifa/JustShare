package com.logan.weibo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.logan.R;
import com.logan.util.Status;
import com.logan.weibo.adapter.ListViewAdapter;
import com.weibo.net.WeiboException;

public class BaseTimeLine extends BaseActivity {

	private final String TAG = "BaseTimeLine";
	// ----------头部工具栏-----------------------
	public ImageView writeBtn = null;
	public ImageView refreshBtn = null;
	public TextView titleTV = null;

	// -------中部ListView组件和适配器------------
	public ListViewAdapter listAdapter;
	public String jsonData;
	public ListView lv = null;
	public List<JSONObject> infos = null;// Tencent专用
	// ----------底部导航栏------------------------
	public View friendTimeLine;
	public View userTimeLine;
	public View userNews;
	public View userInfo;
	public View more;

	public int mCurFooterTab = -1;

	@Override
	public int getLayout() {

		return R.layout.weibo_timeline;
	}

	public List<JSONObject> getQWeiBoData(String jsonStrData) {
		jsonData = jsonStrData;
		Log.v(TAG, "jsonData:  " + jsonData);
		JSONObject obj = null;
		infos = new ArrayList<JSONObject>();
		try {
			obj = new JSONObject(jsonData);
		} catch (JSONException e2) {

			e2.printStackTrace();
		}

		JSONObject dataObj = null;
		try {
			if (!obj.isNull("data"))
				dataObj = obj.getJSONObject("data");
			else
				return null;
		} catch (JSONException e1) {

			e1.printStackTrace();
			return null;
		}
		JSONArray data = null;
		try {
			data = dataObj.getJSONArray("info");
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		if (data != null && data.length() > 0) {
			// TOTAL_PAGE++;
			infos.clear();
			int lenth = data.length();
			for (int i = 0; i < lenth; i++) {
				infos.add(data.optJSONObject(i));
			}
		}
		return infos;

	}

	/**
	 * Sina API
	 * 
	 * @param jsonStrData
	 * @return SearchAdapter
	 */
	public ListViewAdapter getWeiBoData(String jsonStrData) {
		jsonData = jsonStrData;

		List<Status> status = null;
		JSONObject jsonStatus = null;
		try {
			jsonStatus = new JSONObject(jsonData);
		} catch (JSONException e1) {

			e1.printStackTrace();
		}
		JSONArray statuses = null;
		if (!jsonStatus.isNull("statuses")) {
			try {
				statuses = jsonStatus.getJSONArray("statuses");

				if (!jsonStatus.isNull("reposts")) {
					statuses = jsonStatus.getJSONArray("reposts");
				}
				int size = statuses.length();
				status = new ArrayList<Status>(size);
				for (int i = 0; i < size; i++) {
					status.add(new Status(statuses.getJSONObject(i)));
				}
			} catch (JSONException e) {

				e.printStackTrace();
			} catch (WeiboException e) {

				e.printStackTrace();
			}
		}
		Log.v(TAG, "List<? extends Map<String, ?>>staus----------" + status);
		listAdapter = new ListViewAdapter(this,
				(List<? extends Map<String, ?>>) status, 0, null, null);

		return listAdapter;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayout());
		// -------头部工具栏----------------------------------
		writeBtn = (ImageView) findViewById(R.id.weibo_writeBtn);
		writeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(BaseTimeLine.this, Tweet.class);
				startActivity(intent);
			}
		});
		titleTV = (TextView) findViewById(R.id.weibo_title_TV);
		refreshBtn = (ImageView) findViewById(R.id.weibo_refreshBtn);

		// --------------中部ListView和适配器---------------------
		lv = (ListView) findViewById(R.id.listView);
		

		// -----------------底部导航栏---------------------------
		friendTimeLine = findViewById(R.id.weibo_menu_friendTimeLine);
		friendTimeLine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent();
				if (isSina)
					i.setClass(getApplicationContext(), FriendTimeLine.class);
				if (isTencent)
					i.setClass(getApplicationContext(), QFriendTimeLine.class);
				startActivity(i);
				finish();
			}
		});
		userTimeLine = findViewById(R.id.weibo_menu_userTimeLine);
		userTimeLine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent();
				if (isSina)
					i.setClass(getApplicationContext(), UserTimeLine.class);
				if (isTencent)
					i.setClass(getApplicationContext(), QUserTimeLine.class);
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
				if (isSina)
					i.setClass(getApplicationContext(), UserNews.class);
				if (isTencent)
					i.setClass(getApplicationContext(), QUserNews.class);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, 1, 1, "发布微博").setIcon(R.drawable.add_account);
		menu.add(Menu.NONE, 2, 2, "我的动态").setIcon(R.drawable.network);
		menu.add(Menu.NONE, 3, 3, "退出").setIcon(R.drawable.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		// mWeibo = null;
		// oAuth = null;

		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = new Intent();
		switch (item.getItemId()) {
		case 1:
			i.setClass(this, Tweet.class);
			this.startActivity(i);
			break;
		case 2:
			i = new Intent();
			if (isSina)
				i.setClass(this, UserNews.class);
			if (isTencent)
				i.setClass(this, QUserNews.class);
			this.startActivity(i);
			break;

		case 3:
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

	@Override
	protected void onPause() {

		super.onPause();
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
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();
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

}
