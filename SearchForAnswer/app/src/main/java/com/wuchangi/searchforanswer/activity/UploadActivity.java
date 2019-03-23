package com.wuchangi.searchforanswer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.base.AnalysisEvent;
import com.wuchangi.searchforanswer.base.AnswerEvent;
import com.wuchangi.searchforanswer.base.BitmapEvent;
import com.wuchangi.searchforanswer.bean.AnalysisResult;
import com.wuchangi.searchforanswer.network.SendMessageManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class UploadActivity extends AppCompatActivity {

    private ImageView mIvQuestion;
    private ImageView mIvAnswer;
    private Button mBtnUploadAnswer;
    private Button mBtnUploadAll;
    public static boolean sIsAnswerTakingPhoto = false;
    private Bitmap mQuestionPicture = null;
    private Bitmap mAnswerPicture = null;
    private ProgressDialog mProgressDialog;
    private AnswerEvent mAnswerEvent = null;
    private AnalysisEvent mAnalysisEvent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("上传题目");
        }

        initWidget();
    }

    private void initWidget() {
        mIvQuestion = (ImageView) findViewById(R.id.iv_question);
        mIvAnswer = (ImageView) findViewById(R.id.iv_answer);
        mBtnUploadAnswer = (Button) findViewById(R.id.btn_upload_answer);
        mBtnUploadAll = (Button) findViewById(R.id.btn_upload_all);

        EventBus.getDefault().register(this);

        mBtnUploadAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadActivity.this, TakingPhotoActivity.class));
                sIsAnswerTakingPhoto = true;
            }
        });


        mBtnUploadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadAll();
            }
        });

    }

    private void upLoadAll() {
        if (mAnswerPicture == null) {
            Toast.makeText(this, "别忘了上传您的手写体答案~~", Toast.LENGTH_SHORT).show();
            return;
        }

        SendMessageManager.getInstance().getHandwritingResult(answerBitmap2File(mAnswerPicture));
        SendMessageManager.getInstance().getAnalysisResult(questionBitmap2File(mQuestionPicture));

        // 显示等待条
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("图片正在上传中...");
        mProgressDialog.show();
//        mProgressDialog.dismiss();
//        Intent intent = new Intent(UploadActivity.this, AnswerActivity.class);
//        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveAnalysisEvent(AnalysisEvent analysisEvent) {
        mProgressDialog.dismiss();
        Intent intent = new Intent(UploadActivity.this, AnswerActivity.class);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceivePicture(BitmapEvent bitmapEvent) {
        Bitmap bitmap = bitmapEvent.getBitmap();
        if (!sIsAnswerTakingPhoto) {
            mQuestionPicture = bitmap;
            mIvQuestion.setImageBitmap(bitmap);
        } else {
            mAnswerPicture = bitmap;
            mIvAnswer.setImageBitmap(bitmap);
            sIsAnswerTakingPhoto = false;
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public File questionBitmap2File(Bitmap bitmap) {
        File file = new File(getExternalCacheDir(), "question.jpg");//将要保存图片的路径
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


    public File answerBitmap2File(Bitmap bitmap) {
        File file = new File(getExternalCacheDir(), "answer.jpg");//将要保存图片的路径
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
