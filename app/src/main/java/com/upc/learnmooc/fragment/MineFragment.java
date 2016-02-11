package com.upc.learnmooc.fragment;

import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.upc.learnmooc.R;

/**
 * 个人主页
 * Created by Explorer on 2016/2/10.
 */
public class MineFragment extends BaseFragment {


	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.mine_fragment,null);
		//注入view和事件
		ViewUtils.inject(this,view);
		return view;
	}
}
