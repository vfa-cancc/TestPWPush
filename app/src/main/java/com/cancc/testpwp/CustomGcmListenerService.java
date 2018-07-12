package com.cancc.testpwp;

import android.os.Bundle;
import android.util.Log;

import com.nifty.cloud.mb.core.NCMBDialogPushConfiguration;
import com.nifty.cloud.mb.core.NCMBGcmListenerService;
import com.nifty.cloud.mb.core.NCMBPush;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomGcmListenerService extends NCMBGcmListenerService {

    private static final String TAG = "GcmService";
    private static final int REQUEST_RESULT = 0;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //ペイロードデータの取得
        Log.d(TAG, data.toString());


        if(data.containsKey("com.nifty.Dialog")) {

            NCMBDialogPushConfiguration dialogPushConfiguration = new NCMBDialogPushConfiguration();
            dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_DIALOG);
            if (data.containsKey("com.nifty.Data")) {
                try {
                    JSONObject json = new JSONObject(data.getString("com.nifty.Data"));
                    if (json != null) {
                        int dialogType = (int) json.get("type");
                        switch (dialogType) {
                            case NCMBDialogPushConfiguration.DIALOG_DISPLAY_NONE:
                                dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_NONE);
                                break;
                            case NCMBDialogPushConfiguration.DIALOG_DISPLAY_DIALOG:
                                dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_DIALOG);
                                break;
                            case NCMBDialogPushConfiguration.DIALOG_DISPLAY_BACKGROUND:
                                dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_BACKGROUND);
                                break;
                            case NCMBDialogPushConfiguration.DIALOG_DISPLAY_ORIGINAL:
                                dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_ORIGINAL);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (JSONException e) {
                    //エラー処理
                }
            }

            NCMBPush.dialogPushHandler(this, data, dialogPushConfiguration);
        } else {
            super.onMessageReceived(from, data);
        }
    }
}
