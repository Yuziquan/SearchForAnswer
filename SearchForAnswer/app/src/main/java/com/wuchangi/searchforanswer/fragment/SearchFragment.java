package com.wuchangi.searchforanswer.fragment;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.activity.TakingPhotoActivity;

/**
 * Created by WuchangI on 2018/8/4.
 */


/**
 * “搜题”功能页
 */
public class SearchFragment extends Fragment
{
    private ImageView cameraImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        cameraImageView  = (ImageView)rootView.findViewById(R.id.iv_camera);
        cameraImageView.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                //申请手机相机的运行时权限
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    SearchFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                }
                else
                {
                    //跳转到拍照界面
                    startActivity(new Intent(getActivity(), TakingPhotoActivity.class));
                }

            }
        });

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //跳转到拍照界面
                    startActivity(new Intent(getActivity(), TakingPhotoActivity.class));
                }
                else
                {
                    Toast.makeText(getActivity(), "授权失败，无法使用搜题功能~~", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }
}
