package com.cancc.testpwp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBPush;
import com.nifty.cloud.mb.core.NCMBQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView _pushId;
    TextView _richurl;
    Button _btnGetInstallation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _btnGetInstallation = (Button) findViewById(R.id.btnGetInstallation);
        _btnGetInstallation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InstallationActivity.class);
                startActivity(intent);
            }
        });

        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this.getApplicationContext(), "27d6ef658a713529090f5d98963517fee7ae861d3864d66c7f7ab211ccf907c4",
                "ed76d3b2bee59bd4b203013034c522ae246e588067fdc830973055e4bb520e03");

        NCMBPush.trackAppOpened(getIntent());
        final NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();

        try {
            JSONObject tmpBlank = new JSONObject( "{'No key':'No value'}");
            ListView lv = (ListView) findViewById(R.id.lsJson);
            if (lv != null) {
                lv.setAdapter(new ListAdapter(this, tmpBlank));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // subscribe channel
        try {
            NCMBInstallation.subscribe("Ch1", ChannelActivity.class, android.R.drawable.ic_dialog_alert);
            JSONArray channels = new JSONArray();
            channels.put("Ch1");
            installation.setChannels(channels);
            installation.save();
        } catch (Exception e) {
            //エラー処理
        }

        //GCMからRegistrationIdを取得しinstallationに設定する
        installation.getRegistrationIdInBackground("695821559910", new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _btnGetInstallation.setVisibility(View.VISIBLE);
                        }
                    });
                    installation.saveInBackground(new DoneCallback() {
                        @Override
                        public void done(NCMBException e) {
                            if (e == null) {
                                //保存成功
                            } else if (NCMBException.DUPLICATE_VALUE.equals(e.getCode())) {
                                //保存失敗 : registrationID重複
                                updateInstallation(installation);
                            } else {
                                //保存失敗 : その他
                            }
                        }
                    });
                } else {
                    //ID取得失敗
                }
            }
        });



    }

    public static void updateInstallation(final NCMBInstallation installation) {
        //installationクラスを検索するクエリの作成
        NCMBQuery<NCMBInstallation> query = NCMBInstallation.getQuery();
        //同じRegistration IDをdeviceTokenフィールドに持つ端末情報を検索する
        query.whereEqualTo("deviceToken", installation.getDeviceToken());
        //データストアの検索を実行
        query.findInBackground(new FindCallback<NCMBInstallation>() {
            @Override
            public void done(List<NCMBInstallation> results, NCMBException e) {
                //検索された端末情報のobjectIdを設定
                try {
                    installation.setObjectId(results.get(0).getObjectId());
                } catch (NCMBException e1) {
                    e1.printStackTrace();
                }
                //端末情報を更新する
                installation.saveInBackground();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //**************** ペイロード、リッチプッシュを処理する ***************
        Intent intent = getIntent();

        //プッシュ通知IDを表示
        _pushId = (TextView) findViewById(R.id.txtPushid);
        String pushid = intent.getStringExtra("com.nifty.PushId");
        _pushId.setText(pushid);

        //RichURLを表示
        _richurl = (TextView) findViewById(R.id.txtRichurl);
        String richurl = intent.getStringExtra("com.nifty.RichUrl");
        _richurl.setText(richurl);

        //プッシュ通知のペイロードを表示
        if (intent.getStringExtra("com.nifty.Data") != null) {
            try {
                JSONObject json = new JSONObject(intent.getStringExtra("com.nifty.Data"));
                if (json != null) {
                    ListView lv = (ListView) findViewById(R.id.lsJson);
                    lv.setAdapter(new ListAdapter(this, json));
                }
            } catch (JSONException e) {
                //エラー処理
            }
        }
        NCMBPush.richPushHandler(this, getIntent());
        intent.removeExtra("com.nifty.RichUrl");
    }
}
