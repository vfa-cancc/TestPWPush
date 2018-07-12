package com.cancc.testpwp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBInstallation;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.List;

public class InstallationActivity extends AppCompatActivity {

    TextView _objectId;
    TextView _appversion;
    TextView _channels;
    TextView _badge;
    TextView _devicetoken;
    TextView _sdkversion;
    TextView _timezone;
    TextView _createdate;
    TextView _updatedate;
    TextView _txtPrefectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation);
        //表示する端末情報のデータを反映
        _objectId = (TextView) findViewById(R.id.txtObject);
        _appversion = (TextView) findViewById(R.id.txtAppversion);
        _channels = (TextView) findViewById(R.id.txtChannel);
        _badge = (TextView) findViewById(R.id.txtBadge);
        _devicetoken = (TextView) findViewById(R.id.txtDevicetoken);
        _sdkversion = (TextView) findViewById(R.id.txtSdkversion);
        _timezone = (TextView) findViewById(R.id.txtTimezone);
        _createdate = (TextView) findViewById(R.id.txtCreatedate);
        _updatedate = (TextView) findViewById(R.id.txtUpdatedate);
        _txtPrefectures = (TextView) findViewById(R.id.txtPrefecture);

        getInstallation();
    }

    private void getInstallation() {
//        //installationクラスを検索するクエリの作成
        NCMBQuery<NCMBInstallation> query = NCMBInstallation.getQuery();
        //同じRegistration IDをdeviceTokenフィールドに持つ端末情報を検索する
        query.whereEqualTo("deviceToken", NCMBInstallation.getCurrentInstallation().getDeviceToken());
        //データストアの検索を実行
        query.findInBackground(new FindCallback<NCMBInstallation>() {
            @Override
            public void done(List<NCMBInstallation> results, NCMBException e) {
                //検索された端末情報のobjectIdを設定
//                NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();
                NCMBInstallation installation = results.get(0);
                if (installation != null) {
                    try {
                        _objectId.setText(installation.getObjectId());
                        _appversion.setText(installation.getAppVersion());
                        if (installation.getChannels() != null) {
                            _channels.setText(installation.getChannels().toString());
                        }
                        _badge.setText(String.valueOf(installation.getBadge()));
                        _devicetoken.setText(installation.getDeviceToken());
                        _sdkversion.setText(installation.getSDKVersion());
                        _timezone.setText(installation.getTimeZone());
                        _createdate.setText(installation.getCreateDate().toString());
                        _updatedate.setText(installation.getUpdateDate().toString());
                        if (installation.getString("Prefectures") != null) {
                            _txtPrefectures.setText(installation.getString("Prefectures"));
                        }
                    } catch (NCMBException ex) {

                    }
                }
            }
        });

    }
}
