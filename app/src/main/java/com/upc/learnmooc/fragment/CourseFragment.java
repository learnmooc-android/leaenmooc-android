package com.upc.learnmooc.fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;
import com.upc.learnmooc.domain.MainCourse;
import com.upc.learnmooc.global.GlobalConstants;
import com.upc.learnmooc.utils.ToastUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 课程主页
 * Created by Explorer on 2016/2/9.
 */
public class CourseFragment extends BaseFragment {

	@ViewInject(R.id.vp_top_course)
	private ViewPager mViewPager;
	@ViewInject(R.id.top_course_indicator)
	private CirclePageIndicator mIndictor;
	@ViewInject(R.id.lv_list_course)
	private ListView mListView;

	private MainCourse mainCourseInfo;//Course首页的数据对象
	private ArrayList<MainCourse.TopCourse> topCourseList;//头部轮播的数据对象
	private ArrayList<MainCourse.ListCourse> listCourseList;//课程列表的数据对象

	private String cacheDir;
	private ArrayList<String> cacheFileName;
	//放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;

	//轮播图图片数量
	private final static int IMAGE_COUNT = 1;
	//自动轮播的时间间隔
	private final static int TIME_INTERVAL = 3;
	//自动轮播启用开关
	private final static boolean isAutoPlay = true;
	//当前轮播页
	private int currentItem = 0;
	//定时任务
	private ScheduledExecutorService scheduledExecutorService;

	//Handler
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//自动轮播到最后一页的时候 无动画切换到第一页 实现无限轮播
			if (currentItem == 0) {
				mViewPager.setCurrentItem(currentItem, false);
			} else {
				mViewPager.setCurrentItem(currentItem);
			}
		}

	};

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.course_fragment, null);
		View heardView = View.inflate(mActivity,R.layout.heard_course_listview,null);

		//注入view和事件
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, heardView);

		//将头部的轮播等部分 作为listview的头布局
		mListView.addHeaderView(heardView);
		return view;
	}

	@Override
	public void initData() {
		cacheDir = Environment.getDataDirectory().getPath() + "/" + mActivity.getPackageName() + "/imgCache";// /data/packname ;
		cacheFileName = new ArrayList<>();
		imageViewsList = new ArrayList<>();
		getDataFromServer();
	}


	/**
	 * 开始轮播图切换
	 */
	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 2, 3, TimeUnit.SECONDS);
	}

	/**
	 * 停止轮播图切换
	 */
	private void stopPlay() {
		scheduledExecutorService.shutdown();
	}

	/**
	 * 执行轮播图切换任务
	 */
	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			synchronized (mViewPager) {
				currentItem = (currentItem + 1) % topCourseList.size();
				handler.obtainMessage().sendToTarget();
			}
		}

	}

	/**
	 * ViewPager的监听器
	 * 当ViewPager中页面的状态发生改变时调用
	 */
	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
				case 1:// 手势滑动，空闲中
					isAutoPlay = false;
					break;
				case 2:// 界面切换中
					isAutoPlay = true;
					break;
				case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
					if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
						mViewPager.setCurrentItem(0, false);
					}
					// 当前为第一张，此时从左向右滑，则切换到最后一张
					else if (mViewPager.getCurrentItem() == 0 && !isAutoPlay) {
						mViewPager.setCurrentItem(mViewPager.getAdapter().getCount() - 1, false);
					}
					currentItem = mViewPager.getCurrentItem();
					break;
				default:
					break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int pos) {

		}

	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		//
		httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
		httpUtils.configTimeout(1000 * 5);
		httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.GET_MAIN_COURSE_URL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				parseData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				ToastUtils.showToastShort(mActivity, "请求数据失败 请检查网络");
				mViewPager.setAdapter(new TopCourseAdapter());//获取数据失败的时候设置适配器(进行读取缓存或默认加载处理)
				mIndictor.setViewPager(mViewPager);//给指示器绑定viewpager
				mIndictor.setSnap(true);//支持快照
			}
		});
	}

	/**
	 * 解析返回json数据
	 */
	private void parseData(String result) {
		Gson gson = new Gson();
		mainCourseInfo = gson.fromJson(result, MainCourse.class);
		topCourseList = mainCourseInfo.topCourse;
		listCourseList = mainCourseInfo.listCourse;
		if (topCourseList != null) {
			TopCourseAdapter topCourseAdapter = new TopCourseAdapter();
			mViewPager.setAdapter(topCourseAdapter);//成功获取数据后数据适配器
			mIndictor.setViewPager(mViewPager);//给指示器绑定viewpager
			mIndictor.setSnap(true);//支持快照
			if (isAutoPlay) {
				startPlay();
			}
			mIndictor.setOnPageChangeListener(new MyPageChangeListener());
		}


		if (listCourseList != null) {
			ListCourseAdapter listCourseAdapter = new ListCourseAdapter();
			mListView.setAdapter(listCourseAdapter);
		}
	}

	/**
	 * 头部课程轮播适配器
	 */
	class TopCourseAdapter extends PagerAdapter {

		private BitmapUtils bitmapUtils;
		private BitmapDisplayConfig config;


		public TopCourseAdapter() {
			config = new BitmapDisplayConfig();
			config.setLoadingDrawable(getResources().getDrawable(R.drawable.top_course_default));
			bitmapUtils = new BitmapUtils(mActivity);
			System.out.println("cache is " + cacheDir);
			//设置默认记载过程中的默认显示图
			bitmapUtils.configDefaultLoadingImage(R.drawable.top_course_default);
		}

		@Override
		public int getCount() {
			if (topCourseList == null) {
				if (cacheFileName.isEmpty()) {
					return 1;
				}
				return cacheFileName.size();
			}
			return topCourseList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			if (topCourseList != null) {
				//成功获取数据后 保存缓存文件名
//				bitmapUtils.configDiskCacheFileNameGenerator(new FileNameGenerator() {
//					@Override
//					public String generate(String s) {
//						System.out.println("position is " + position);
//
//						return topCourseList.get(position).courseId + "";//
//					}
//				});
				bitmapUtils.display(imageView, topCourseList.get(position).topCourseImgUrl, config);
//				imageViewsList.add(imageView);
			} else {
				//获取网络数据失败时 显示默认图片
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.top_course_default));
			}

			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * 课程列表listview适配器
	 */
	class ListCourseAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public ListCourseAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);//设置默认显示的图片
		}

		@Override
		public int getCount() {
			return listCourseList.size();
		}

		@Override
		public MainCourse.ListCourse getItem(int position) {
			return listCourseList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_course_list, null);
				holder = new ViewHolder();
				holder.ivCourseImg = (ImageView) convertView.findViewById(R.id.iv_course_img);
				holder.tvCourseName = (TextView) convertView.findViewById(R.id.tv_course_description);
				holder.tvLearnerNum = (TextView) convertView.findViewById(R.id.tv_learner_number);
				holder.tvCoursedate = (TextView) convertView.findViewById(R.id.tv_update_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			MainCourse.ListCourse item = getItem(position);
			holder.tvCourseName.setText(item.getCourseName());
			holder.tvLearnerNum.setText(item.getNum()+"");
			holder.tvCoursedate.setText(item.getPubdate());
			bitmapUtils.display(holder.ivCourseImg, item.getThumbnailUrl());

			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivCourseImg;
		public TextView tvCourseName;
		public TextView tvLearnerNum;
		public TextView tvCoursedate;

	}

}
