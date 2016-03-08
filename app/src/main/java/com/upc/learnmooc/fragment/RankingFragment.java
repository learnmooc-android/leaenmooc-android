package com.upc.learnmooc.fragment;

import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.upc.learnmooc.R;

/**
 * 社区页中的文章板块
 * Created by Explorer on 2016/3/3.
 */
public class RankingFragment extends BaseFragment {



	@Override
	public View initViews() {

		View view = View.inflate(mActivity, R.layout.community_ranking_frgment,null);

		ViewUtils.inject(view);
		return view;
	}
}
