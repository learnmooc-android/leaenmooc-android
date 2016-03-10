package com.upc.learnmooc.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;
import com.upc.learnmooc.R;

/**
 * 课程视频页
 * Created by Explorer on 2016/3/10.
 */
public class VideoActivity extends BaseActivity implements VDVideoExtListeners.OnVDVideoPlaylistListener {

	private VDVideoView mVDVideoView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_activity);

		initViews();
	}

	@Override
	public void initViews() {
		mVDVideoView = (VDVideoView) findViewById(R.id.vd_video_view);
		mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView.getParent());

		VDVideoListInfo infoList = new VDVideoListInfo();
		VDVideoInfo info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频0";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131396797&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频1";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频2";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频3";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		mVDVideoView.setPlaylistListener(this);

		// 简单方式处理的视频列表
		VDVideoPlayListView listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);
		if (listView != null) {
			listView.onVideoList(infoList);
		}
		mVDVideoView.open(VideoActivity.this, infoList);
		mVDVideoView.play(0);
	}


	@Override
	protected void onResume() {
		super.onResume();
		mVDVideoView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mVDVideoView.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		mVDVideoView.onStop();
	}

	@Override
	protected void onDestroy() {
		mVDVideoView.release(false);
		super.onDestroy();
	}

	@Override
	public void onPlaylistClick(VDVideoInfo info, int p) {
		// TODO Auto-generated method stub
		mVDVideoView.play(p);
	}
}
