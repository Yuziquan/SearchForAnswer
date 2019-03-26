package com.wuchangi.searchforanswer.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.base.AnswerEvent;
import com.wuchangi.searchforanswer.base.AnswerSheetEvent;
import com.wuchangi.searchforanswer.base.BaseActivity;
import com.wuchangi.searchforanswer.bean.AnalysisResult;
import com.wuchangi.searchforanswer.bean.AnswerSheetResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 测试结果界面
 */
public class SheetResultActivity extends AppCompatActivity {
    private TextView mTvRecognitionAnswer;
    private TextView mTvCorrectAnswer;
    private TextView mTvCorrectNo;
    private TextView mTvCorrectNum;
    private TextView mTvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_result);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("题目解析");
        }

        //界面初始化
        initView();
    }


    /**
     * 界面初始化
     */
    public void initView() {
        mTvRecognitionAnswer = findViewById(R.id.tv_recognition_answer);
        mTvCorrectAnswer = findViewById(R.id.tv_correct_answer);
        mTvCorrectNo = findViewById(R.id.tv_correct_no);
        mTvCorrectNum = findViewById(R.id.tv_correct_num);
        mTvScore = findViewById(R.id.tv_score);
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
    public void onReceiveAnswerSheetEvent(AnswerSheetEvent answerSheetEvent) {
        String result = answerSheetEvent.getAnswerSheetResult();

        if (result.length() >= 1 && result.substring(0, 1).equals("{")) {
            Gson gson = new Gson();
            AnswerSheetResult answerSheetResult = gson.fromJson(result, AnswerSheetResult.class);

            String recognitionAnswer = answerSheetResult.getRecognitionAnswer();
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < recognitionAnswer.length(); i++) {
                sb1.append((i + 1) + ". " + recognitionAnswer.charAt(i) + "              ");
            }
            mTvRecognitionAnswer.setText(sb1.toString());

            String correctAnswer = answerSheetResult.getRealAnswer();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < correctAnswer.length(); i++) {
                sb2.append((i + 1) + ". " + correctAnswer.charAt(i) + "              ");
            }
            mTvCorrectAnswer.setText(sb2.toString());

            StringBuilder sb3 = new StringBuilder();
            for(int i = 0; i < recognitionAnswer.length(); i++){
                if(recognitionAnswer.charAt(i) == correctAnswer.charAt(i)){
                    sb3.append((i+1) + "  ");
                }
            }
            mTvCorrectNo.setText(sb3.toString());

            mTvCorrectNum.setText(answerSheetResult.getCount() + "");
            mTvScore.setText(answerSheetResult.getCount() + "");
        } else {
            mTvRecognitionAnswer.setText("识别答题卡失败");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
