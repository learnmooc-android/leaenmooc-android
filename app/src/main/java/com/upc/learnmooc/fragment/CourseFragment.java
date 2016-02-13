package com.upc.learnmooc.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.upc.learnmooc.R;

/**
 * 课程主页
 * Created by Explorer on 2016/2/9.
 */
public class CourseFragment extends BaseFragment {

	@ViewInject(R.id.vp_top_course)
	private ViewPager mViewPager;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.course_fragment, null);

		//注入view和事件
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		getDataFromServer();
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {

	}

	/**
	 * 头部课程轮播适配器
	 */
	class TopCourseAdapter extends PagerAdapter{

		private final BitmapUtils bitmapUtils;

		public TopCourseAdapter(){
			bitmapUtils = new BitmapUtils(mActivity);
			//设置默认记载过程中的默认显示图
			bitmapUtils.configDefaultLoadingImage(R.drawable.top_course_default);
		}


		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);

//			bitmapUtils.display(imageView,url);
			return super.instantiateItem(container, position);
		}
	}

}
