package com.scoregroup.androidpos.Client;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ClientLoading extends AsyncTask<Void, Void, Void> {
    ProgressDialog asyncDialog;

    public ClientLoading(Context con){
        asyncDialog = new ProgressDialog(con);
    }

    @Override
    protected void onPreExecute() {
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("로딩중입니다..");

        // show dialog
        asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        while(ClientManger.get_wait())
            System.out.println("data wait...");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        asyncDialog.dismiss();
        super.onPostExecute(result);
    }
}
