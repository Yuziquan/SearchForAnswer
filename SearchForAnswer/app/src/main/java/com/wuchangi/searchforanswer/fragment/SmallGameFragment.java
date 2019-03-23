package com.wuchangi.searchforanswer.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.utils.TypefaceUtils;

/**
 * Created by WuchangI on 2018/8/4.
 */

/**
 * “小游戏”功能页
 */
public class SmallGameFragment extends Fragment implements View.OnClickListener {
    private Button game_start;
    private Button game_score;

    private TextView mTvGameTitle;

    private static int user_score;                     //记录用户成绩
    private static int user_friend_score;              //记录用户朋友成绩

    private FragmentManager manager;            //获得碎片管理者

    private UserFragment user_fragment = new UserFragment();

    private UserFriendFragment user_friend_fragment = new UserFriendFragment();

    private ScoreFragment score_fragment = new ScoreFragment();

    {
        user_score = 0;
        user_friend_score = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_small_game, container, false);

        game_start = rootView.findViewById(R.id.game_start);
        game_score = rootView.findViewById(R.id.game_score);

        game_start.setTypeface(TypefaceUtils.getInstance().getTypeface1());
        game_score.setTypeface(TypefaceUtils.getInstance().getTypeface1());

        mTvGameTitle = rootView.findViewById(R.id.tv_game_title);
        mTvGameTitle.setTypeface(TypefaceUtils.getInstance().getTypeface1());

        Log.d("GamePlay", "cheng31");
        Button game_start = (Button) rootView.findViewById(R.id.game_start);
        game_start.setOnClickListener(this);

        Button game_score = (Button) rootView.findViewById(R.id.game_score);
        game_score.setOnClickListener(this);

        manager = getActivity().getSupportFragmentManager();
        user_score = 0;
        user_friend_score = 0;

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_start:
                Log.d("GamePlay", "cheng11");
                manager.beginTransaction().replace(R.id.user_layout, user_fragment).commit();
                manager.beginTransaction().replace(R.id.user_friend_layout, user_friend_fragment).commit();
                game_start.setVisibility(View.GONE);
                game_score.setVisibility(View.GONE);
                break;
            case R.id.game_score:
                Log.d("GamePlay", "cheng12");
                manager.beginTransaction().replace(R.id.main_layout, score_fragment).commit();
                break;
        }

    }

    //设置用户的成绩
    public static void setUser_score(int score) {
        user_score = score;
    }

    //设置用户朋友的成绩
    public static void setUser_friend_score(int score) {
        user_friend_score = score;
    }

    //获得用户的成绩
    public static int getUser_score() {
        return user_score;
    }

    //获得用户朋友的成绩
    public static int getUser_friend_score() {
        return user_friend_score;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
