package com.wuchangi.searchforanswer.fragment;;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wuchangi.searchforanswer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UserFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private FragmentManager manager;     //碎片管理者

    private TextView leastTime;         //记录剩下的题数
    private Button question;            //记录每道题的题目
    private Button answer1;             //记录每道题第一个选项
    private Button answer2;             //记录每道题第二个选项
    private Button answer3;             //记录每道题第三个选项
    private Button answer4;             //记录每道题第四个选项

    private int totalCount;             //记录总共的答题数
    private int rightCount;             //记录答对的题目数

    private String[] questions = new String[22];     //记录问题的数组
    private String answers[][] = new String[22][4]; //记录选项的二维数组
    private int[] rightAnsewrs = new int[22];         //记录每道题正确的选项在哪个位置

    private ScoreFragment score_fragment = new ScoreFragment();

    public UserFragment() {
    }


    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        Log.d("UserFragment", "cheng1");
        //获得碎片管理者
        manager = this.getFragmentManager();

        //开始初始化数据
        initalData(rootView);

        return rootView;
    }

    private void initalData(View rootView) {

        readFile();

        totalCount = 0;
        rightCount = 0;

        //设置剩余的题数
        leastTime = (TextView) rootView.findViewById(R.id.leastTime);
        leastTime.setText((Integer.toString(10 - totalCount)));

        //设置问题
        question = (Button) rootView.findViewById(R.id.question);
        question.setText(questions[0]);

        //设置选项1
        answer1 = (Button) rootView.findViewById(R.id.answer1);
        answer1.setText(answers[0][0]);
        answer1.setOnClickListener(this);

        //设置选项2
        answer2 = (Button) rootView.findViewById(R.id.answer2);
        answer2.setText(answers[0][1]);
        answer2.setOnClickListener(this);

        //设置选项3
        answer3 = (Button) rootView.findViewById(R.id.answer3);
        answer3.setText(answers[0][2]);
        answer3.setOnClickListener(this);

        //设置选项4
        answer4 = (Button) rootView.findViewById(R.id.answer4);
        answer4.setText(answers[0][3]);
        answer4.setOnClickListener(this);

        Log.d("UserFragment", "cheng2");
    }

    private void readFile() {
        try {
            //从res中的raw文件夹的data.txt文件读取数据
            InputStream is = getResources().openRawResource(R.raw.data);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader f = new BufferedReader(isr);

            String s;
            int i = 0;
            for (s = f.readLine(); s != null; s = f.readLine()) {
                questions[i] = s;
                s = f.readLine();
                answers[i] = s.split(",");
                s = f.readLine();
                rightAnsewrs[i] = Integer.parseInt(s);
                i++;
            }
            f.close();
        } catch (IOException e) {
            System.err.println("发生异常");
            e.printStackTrace();
        }
    }




    @Override
    public void onClick(View view) {

        int button = 0;
        switch (view.getId()) {
            case R.id.answer1:
                button = 1;
                break;
            case R.id.answer2:
                button = 2;
                break;
            case R.id.answer3:
                button = 3;
                break;
            case R.id.answer4:
                button = 4;
                break;
        }
        Log.d("UserFragment", "cheng3");
        if (rightAnsewrs[totalCount] == button) {
            Toast.makeText(getActivity(), "答对了", Toast.LENGTH_SHORT).show();
            rightCount++;
        } else
            Toast.makeText(getActivity(), "答错了", Toast.LENGTH_SHORT).show();

        //更新总答题数
        totalCount++;

        //更新剩余答题数
        leastTime.setText((Integer.toString(10 - totalCount)));

        //如果题目已经答完
        if (totalCount == 10) {
            SmallGameFragment.setUser_score(rightCount);
            manager.beginTransaction().replace(R.id.main_layout, score_fragment).commit();
        }

        Log.d("UserFragment", "cheng4");

        //设置下一道题的文本
        question.setText(questions[totalCount]);
        answer1.setText(answers[totalCount][0]);
        answer2.setText(answers[totalCount][1]);
        answer3.setText(answers[totalCount][2]);
        answer4.setText(answers[totalCount][3]);
    }

}
