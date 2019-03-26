package com.wuchangi.searchforanswer.network;

import com.wuchangi.searchforanswer.base.AnalysisEvent;
import com.wuchangi.searchforanswer.base.AnswerEvent;
import com.wuchangi.searchforanswer.base.AnswerSheetEvent;
import com.wuchangi.searchforanswer.bean.AnalysisResult;
import com.wuchangi.searchforanswer.bean.AnswerSheetResult;
import com.wuchangi.searchforanswer.constant.Constant;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by WuchangI on 2019/3/9.
 */

public class ReceiveMessageManager {
    private static ReceiveMessageManager sReceiveMessageManager;

    private ReceiveMessageManager() {
    }

    public static ReceiveMessageManager getInstance() {
        return sReceiveMessageManager == null ? sReceiveMessageManager = new ReceiveMessageManager() :
                sReceiveMessageManager;
    }

    public void dispatchAnalysisMessage(String response, String appendUrl) {
        switch (appendUrl) {
            case Constant.GET_HANDWRITING_RESULT:
                AnswerEvent answerEvent = new AnswerEvent();
                answerEvent.setAnswer(response);
                EventBus.getDefault().postSticky(answerEvent);
                break;

            case Constant.GET_ANALYSIS_RESULT:
                AnalysisEvent analysisEvent = new AnalysisEvent();
                analysisEvent.setAnalysis(response);
                EventBus.getDefault().postSticky(analysisEvent);
                break;

            case Constant.GET_ANSWER_SHEET_RESULT:
                AnswerSheetEvent answerSheetEvent = new AnswerSheetEvent();
                answerSheetEvent.setAnswerSheetResult(response);
                EventBus.getDefault().postSticky(answerSheetEvent);
                break;

            default:
                break;
        }

    }

}
