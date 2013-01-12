package com.logan.weibo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.logan.R;
import com.logan.util.QAsyncImageLoader;
import com.logan.util.QAsyncImageLoader.ImageCallback;
import com.logan.util.TimeUtil;
import com.logan.util.WebImageBuilder;

public class QStatusDetail extends Activity {
	private final String TAG = "QStatusDetail";

	private QAsyncImageLoader imageLoader = new QAsyncImageLoader();
	ImageView back = null;

	ImageView head;// 头像
	TextView nick;// 用户名
	TextView origText;// 微博文本
	ImageView image;// 微博图片
	TextView from;// 平台来源
	ImageView isVip;
	TextView mcount;
	TextView count;
	TextView timeStamp;
	// --------转发微博-----------------
	TextView source_text;// 转发微博文字
	ImageView source_image;// 转发微博图片
	View source_ll;// 转发微博父容器

	String jsonData;
	JSONObject obj;

	String httpMethod = "GET";
	String position = null;

	String num = null;

	private JSONObject getOneStatus() {
		JSONArray statuses = null;// 多条微博
		JSONObject mStatus = null;// 单条微博
		try {
			statuses = getStatuses();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mStatus = statuses.getJSONObject(Integer.parseInt(position));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mStatus;
	}

	private JSONArray getStatuses() throws JSONException {

		JSONObject jsonStatus = null;// 所有返回信息
		JSONArray data = null;// 多条微博信息，也就是各个info节点信息
		try {
			jsonStatus = new JSONObject(jsonData);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (!jsonStatus.isNull("data")) {
			try {

				jsonStatus = jsonStatus.getJSONObject("data");

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}

			try {
				data = jsonStatus.getJSONArray("info");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return data;
	}

	private void initViews() {
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		head = (ImageView) findViewById(R.id.status_profile_image);
		nick = (TextView) findViewById(R.id.status_screen_name);
		origText = (TextView) findViewById(R.id.status_text);
		image = (ImageView) findViewById(R.id.status_microBlogImage);
		from = (TextView) findViewById(R.id.status_from);
		timeStamp = (TextView) findViewById(R.id.status_created_at);
		isVip = (ImageView) findViewById(R.id.status_vipImage);
		mcount = (TextView) findViewById(R.id.status_commentsCount);
		count = (TextView) findViewById(R.id.status_repostsCount);
		// ------------转发微博------------------------
		source_image = (ImageView) findViewById(R.id.status_retweeted_status_microBlogImage);
		source_text = (TextView) findViewById(R.id.status_retweeted_status_text);
		source_ll = findViewById(R.id.status_retweeted_status_ll);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_statusdetail);
		Intent intent = getIntent();
		position = intent.getStringExtra("position");
		jsonData = intent.getStringExtra("jsonData");
		initViews();
		JSONObject mStatus = getOneStatus();
		try {
			setData2Views(mStatus);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setData2Views(JSONObject mStatus) throws JSONException {
		// 头像
		String profile_image_url = "";
		// --转发微博---
		Boolean isVisible = true;// 是否显示转发微博的背景图
		JSONObject source = null;
		// String source_nick = "";
		String source_text_data = "";// 微博文字信息，包含URL、昵称、是否认证的信息
		String source_image_data = "";

		if (!mStatus.isNull("head")) {
			profile_image_url = mStatus.getString("head") + "/100";
		}
		Log.v(TAG, "profile_image_url:  " + profile_image_url);
		String name2 = mStatus.getString("nick");
		Log.v(TAG, "name:  " + name2);
		String text2 = mStatus.getString("origtext");
		Log.v(TAG, "text:  " + text2);
		String microBlogImage = "";
		JSONArray imageArray = mStatus.optJSONArray("image");// 如果此微博有图片内容，就显示出来
		if (imageArray != null && imageArray.length() > 0) {
			microBlogImage = imageArray.optString(0) + "/460";
			Log.v(TAG, "microBlogImage:  " + microBlogImage);
		}
		String source2 = mStatus.getString("from");
		Log.v(TAG, "source:  " + source2);
		int verified2 = mStatus.getInt("isvip");
		Log.v(TAG, "verified:  " + verified2);
		String statuses_count2 = mStatus.getString("mcount");
		Log.v(TAG, "statuses_count:  " + statuses_count2);
		String followers_count2 = mStatus.getString("count");
		Log.v(TAG, "followers_count:  " + followers_count2);

		String created_at_temp2 = TimeUtil.converTime(Long.parseLong(mStatus
				.getString("timestamp")));

		Log.v(TAG, "created_at:  " + created_at_temp2);

		// --------------获取转发微博的信息----包括图片、文字信息，不包含视频、音频信息------------------------
		if (!mStatus.isNull("source")) {
			source = mStatus.getJSONObject("source");
			Log.v(TAG, "sourcel:  " + source);

			if (!source.isNull("image")) {
				JSONArray images = source.getJSONArray("image");
				if (images != null && images.length() > 0) {
					source_image_data = images.optString(0) + "/460";
					Log.v(TAG, "source_image_url:  " + source_image);
				}
			}
			if (!source.isNull("nick")) {
				String source_nick = source.getString("nick");
				Log.v(TAG, "source_nick:  " + source_nick);

				if (!source.isNull("origtext")) {
					source_text_data = source_nick + ":  "
							+ source.getString("origtext");
					Log.v(TAG, "source_origtext:  " + source_text);
				}
			}
		} else
			isVisible = false;

		Drawable cachedImage = null;
		if (!profile_image_url.equals("") && profile_image_url != null) {
			// setViewImage(head, profile_image_url2 + "/100");
			cachedImage = imageLoader.loadDrawable(profile_image_url, head,
					new ImageCallback() {
						@Override
						public void imageLoaded(Drawable imageDrawable,
								ImageView imageView, String imageUrl) {
							imageView.setImageDrawable(imageDrawable);
						}
					});
		}
		if (cachedImage == null) {
			head.setImageResource(R.drawable.icon);
		} else {
			head.setImageDrawable(cachedImage);
		}
		// else
		// profile_image.setVisibility(View.GONE);

		nick.setText(name2);
		origText.setText(text2);

		if (!microBlogImage.equals("") && microBlogImage != null)
			setViewImage(image, microBlogImage);
		else
			image.setVisibility(View.GONE);

		from.setText(source2.toString());
		timeStamp.setText(created_at_temp2);

		mcount.setText(followers_count2 + "");
		count.setText(statuses_count2 + "");

		if (verified2 == 1)
			isVip.setVisibility(View.VISIBLE);
		else
			isVip.setVisibility(View.GONE);

		// ------------------------转发微博----------------------------------

		if (!source_image_data.equals("") && source_image_data != null) {
			source_image.setVisibility(View.VISIBLE);
			setViewImage(source_image, source_image_data);
		} else
			source_image.setVisibility(View.GONE);
		if (!source_text_data.equals("")) {
			source_text.setVisibility(View.VISIBLE);
			source_text.setText(source_text_data);
		} else
			source_text.setVisibility(View.GONE);

		if (!isVisible)
			source_ll.setVisibility(View.GONE);
		else
			source_ll.setVisibility(View.VISIBLE);

	}

	public void setViewImage(final ImageView v, String url) {
		Bitmap bitmap = WebImageBuilder.returnBitMap(url);
		v.setImageBitmap(bitmap);
		imageLoader.loadDrawable(url, v, new ImageCallback() {
			@Override
			public void imageLoaded(Drawable imageDrawable,
					ImageView imageView, String imageUrl) {
				imageView.setImageDrawable(imageDrawable);
			}
		});
	}

}
