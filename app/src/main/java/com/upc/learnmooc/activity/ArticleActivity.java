package com.upc.learnmooc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.upc.learnmooc.R;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

/**
 * 文章内容
 * Created by Explorer on 2016/3/7.
 */
public class ArticleActivity extends BaseActivity {

	private WebView mWebView;
	private ProgressBar mProgressBar;
	private ImageView ivCollection;
	private ImageView ivShare;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_acticity);

		initViews();
		setCollection(false);
		ivCollection.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCollection(true);
			}
		});
	}

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void initViews() {
		mWebView = (WebView) findViewById(R.id.wb_article);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
		ivCollection = (ImageView) findViewById(R.id.iv_collection);
		ivShare = (ImageView) findViewById(R.id.iv_article_share);

		url = getIntent().getStringExtra("url");

		WebSettings settings = mWebView.getSettings();


		settings.setJavaScriptEnabled(true);//支持js
		settings.setUseWideViewPort(true);//支持双击缩放

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				mProgressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			/**
			 * 所有跳转链接的回调
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				return true;
			}
		});

		mWebView.loadUrl(url);
	}


	/**
	 * 返回前一个页面
	 * 不是主页面  而是社区页的文章板块
	 */
	public void ToArticleList(View view) {
		Intent intent = new Intent();
		intent.setClass(ArticleActivity.this, MainActivity.class);
		setResult(0,intent);//返回 result == 0 MainActivity处理 显示
//		startActivity(intent);
		finish();
	}



	/**
	 * 收藏
	 */
	public void setCollection(boolean isClick) {
		//文章的url  key:url  value:url
		String article = UserInfoCacheUtils.getCache(url, ArticleActivity.this);
		System.out.println("文章地址 " + article);

		if(isClick){
			//如果没有收藏这个文章  设置收藏 缓存 改变图标
			if(article == null){
				UserInfoCacheUtils.setCache(url,url,ArticleActivity.this);
				ivCollection.setBackgroundResource(R.drawable.has_collection);

				//数据库保存用户收藏的文章

			}else {
				//如果已经收藏  取消收藏 清除缓存 改变图标
				UserInfoCacheUtils.setCache(url,null,ArticleActivity.this);
				ivCollection.setBackgroundResource(R.drawable.collection);

				//数据库删除用户收藏的文章的记录
			}
		}else {
			if(article == null){
				ivCollection.setBackgroundResource(R.drawable.collection);
			}else {
				ivCollection.setBackgroundResource(R.drawable.has_collection);
			}
		}

	}

	/**
	 * 分享
	 */
	public void ArticleShare(View view) {

	}
}
