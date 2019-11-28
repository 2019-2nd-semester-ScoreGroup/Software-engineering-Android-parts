package com.scoregroup.androidpos.Client;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.scoregroup.androidpos.R;

/**클라이언트 로딩창
 * 1. (.show)로 실행
 * 2. (.dismiss)로 종료*/
public class ClientLoading extends Dialog{
    public ClientLoading(Context con){
        super(con);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.client_loading);
    }
}
