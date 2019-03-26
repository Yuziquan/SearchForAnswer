package com.wuchangi.searchforanswer.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.base.AnalysisEvent;
import com.wuchangi.searchforanswer.base.AnswerEvent;
import com.wuchangi.searchforanswer.bean.AnalysisResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class AnalysisActivity extends AppCompatActivity {

    private TextView mTvQuestion;
    private TextView mTvOptionA;
    private TextView mTvOptionB;
    private TextView mTvOptionC;
    private TextView mTvOptionD;
    private TextView mTvYourAnswer;
    private TextView mTvRightAnswer;
    private TextView mTvAnalysis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("题目解析");
        }

        initView();
    }

    private void initView() {
        mTvQuestion = (TextView) findViewById(R.id.tv_question);
        mTvOptionA = (TextView) findViewById(R.id.tv_option_a);
        mTvOptionB = (TextView) findViewById(R.id.tv_option_b);
        mTvOptionC = (TextView) findViewById(R.id.tv_option_c);
        mTvOptionD = (TextView) findViewById(R.id.tv_option_d);
        mTvYourAnswer = (TextView) findViewById(R.id.tv_your_answer);
        mTvRightAnswer = (TextView) findViewById(R.id.tv_right_answer);
        mTvAnalysis = (TextView) findViewById(R.id.tv_analysis);

        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveAnswerEvent(AnswerEvent answerEvent) {
        String yourAnswer = answerEvent.getAnswer();
        // yourAnswer = "C\r\n",去掉后面的\r\n
        yourAnswer = yourAnswer.substring(0, yourAnswer.length() - 2);
        if (yourAnswer.length() != 1) {
            yourAnswer = "识别失败";
        }
        mTvYourAnswer.setText(yourAnswer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveAnalysisEvent(AnalysisEvent analysisEvent) {
        String analysis = analysisEvent.getAnalysis();

        if (analysis.length() >= 1 && analysis.substring(0, 1).equals("{")) {
            Gson gson = new Gson();
            AnalysisResult analysisResult = gson.fromJson(analysis, AnalysisResult.class);
            mTvQuestion.setText(analysisResult.getRealquestion());
            mTvOptionA.setText(analysisResult.getOptionA());
            mTvOptionB.setText(analysisResult.getOptionB());
            mTvOptionC.setText(analysisResult.getOptionC());
            mTvOptionD.setText(analysisResult.getOptionD());
            mTvRightAnswer.setText(analysisResult.getAnswer());
            mTvAnalysis.setText(analysisResult.getAnalysis());
        } else {
            mTvQuestion.setText("识别题目失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
