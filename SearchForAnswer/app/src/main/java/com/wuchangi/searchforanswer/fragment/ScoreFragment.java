package com.wuchangi.searchforanswer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuchangi.searchforanswer.R;

public class ScoreFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private int user_score;
    private int user_friend_score;

    private TextView user_score_text;
    private TextView user_friend_score_text;
    private TextView user_number;
    private TextView user_friend_number;

    public ScoreFragment() {
    }

    public static ScoreFragment newInstance(String param1, String param2) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_score, container, false);

        //初始化数据
        initalData(rootView);

        return rootView;
    }

    private void initalData(View rootView) {
        user_score=SmallGameFragment.getUser_score();
        user_friend_score=SmallGameFragment.getUser_friend_score();

        user_score_text=(TextView)rootView.findViewById(R.id.user_score);
        user_friend_score_text=(TextView)rootView.findViewById(R.id.user_friend_score);
        user_number=(TextView)rootView.findViewById(R.id.user_number);
        user_friend_number=(TextView)rootView.findViewById(R.id.user_friend_number);

        user_score_text.setText(user_score+"");
        user_friend_score_text.setText(user_friend_score+"");

        if(user_score>user_friend_score)
        {
            user_number.setText(1+"");
            user_friend_number.setText(2+"");
        }
        else if(user_score<user_friend_score)
        {
            user_number.setText(2+"");
            user_friend_number.setText(1+"");
        }
        else {
            user_number.setText(1+"");
            user_friend_number.setText(1+"");
        }
    }
}
