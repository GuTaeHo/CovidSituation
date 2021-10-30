package com.cosiguk.covidsituation.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Version;
import com.cosiguk.covidsituation.network.resultInterface.VersionListener;
import com.cosiguk.covidsituation.util.ActivityUtil;
import com.cosiguk.covidsituation.util.BasicUtil;
import com.cosiguk.covidsituation.util.NetworkUtil;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;


public class SplashActivity extends BaseActivity {
    public static HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                networkCheck();
            }
        }, 1000);
    }

    private void networkCheck() {
        if (NetworkUtil.isConnected(SplashActivity.this)) {
            setProvinceMap();
            ActivityUtil.startNewActivity(SplashActivity.this, MainActivity.class);
            // versionCheck();
        } else {
            new NoticeDialog(SplashActivity.this)
                    .setMsg(getString(R.string.internet_not_connect))
                    .setNegativeMsg(getResources().getString(R.string.dialog_close))
                    .setPositiveMsg(getResources().getString(R.string.dialog_setting))
                    .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                        @Override
                        public void positive() {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void negative() {
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void versionCheck() {
        MyApplication.showProgressDialog(SplashActivity.this);
        MyApplication.getNetworkPresenterInstance()
                .version(new VersionListener() {
                    @Override
                    public void success(Version version) {
                        MyApplication.dismissProgressDialog();
                        try {
                            String serverVersion = version.getAndroid();
                            // 패키지 매니저의 getPackageInfo() 메소드로 디바이스에 설치된 앱 버전 획득
                            String deviceVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

                            switch (BasicUtil.isVersionValid(deviceVersion, serverVersion)) {
                                case BasicUtil.VERSION_LATEST:
                                    permissionCheck();
                                    break;

                                case BasicUtil.VERSION_LOW_MAJOR:
                                case BasicUtil.VERSION_LOW_MINOR:
                                case BasicUtil.VERSION_LOW_PATCH:
                                    String message = "앱이 업데이트 되었습니다. \n업데이트 후 이용해주세요.\n확인을 선택하면 스토어로 이동합니다.";
                                    message += "\n\n";
                                    message += "현재버전 : " + deviceVersion + "\n";
                                    message += "최신버전 : " + serverVersion;

                                    new NoticeDialog(SplashActivity.this)
                                            .setBackPressButton(false)
                                            .setShowNegativeButton(false)
                                            .setMsg(message)
                                            .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                                                @Override
                                                public void positive() {
                                                    ActivityUtil.startUrlActivity(SplashActivity.this, getString(R.string.play_store));
                                                    finish();
                                                }

                                                @Override
                                                public void negative() {
                                                }
                                            }).show();
                                    break;

                                case BasicUtil.VERSION_ERROR:
                                    new NoticeDialog(SplashActivity.this)
                                            .setBackPressButton(false)
                                            .setShowNegativeButton(false)
                                            .setMsg("버전 확인에 실패했습니다.")
                                            .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                                                @Override
                                                public void positive() {
                                                    finish();
                                                }

                                                @Override
                                                public void negative() {
                                                }
                                            }).show();
                                    break;
                            }
                        } catch (Exception e) {
                            new NoticeDialog(SplashActivity.this)
                                    .setBackPressButton(false)
                                    .setShowNegativeButton(false)
                                    .setMsg(e.getMessage())
                                    .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                                        @Override
                                        public void positive() {
                                            finish();
                                        }

                                        @Override
                                        public void negative() {
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void fail(String message) {
                        MyApplication.dismissProgressDialog();
                        new NoticeDialog(SplashActivity.this)
                                .setBackPressButton(false)
                                .setShowNegativeButton(false)
                                .setMsg(getString(R.string.version_error))
                                .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                                    @Override
                                    public void positive() {
                                        finish();
                                    }

                                    @Override
                                    public void negative() {
                                    }
                                }).show();
                    }
                });
    }


    // 권한 체크
    private void permissionCheck() {
        String[] permission = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, // 위치 권한
                // 버전 Q 부터는 포어그라운드에서 실행 중 일때만 위치 정보 접근 가능
                Manifest.permission.WRITE_EXTERNAL_STORAGE, // 저장공간 권한
                Manifest.permission.READ_EXTERNAL_STORAGE};

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    // 모든 권한이 허용된 경우 호출
                    public void onPermissionGranted() {
                        startMainActivity();
                    }

                    @Override
                    // 일부 또는 모든 권한이 허용되지 않은 경우 호출
                    // 인자인 deniedPermissions 로 거부 된 권한 목록을 전달 받음
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        showToast(getString(R.string.permission_not_agree));
                        finish();
                    }
                })
                // 사용자가 권한을 거부했을때 출력되는 메시지 설정
                .setDeniedMessage(getString(R.string.ted_permission_denied_message))
                // 설정 화면으로 가는 버튼의 텍스트 설정
                .setGotoSettingButtonText(getString(R.string.ted_permission_go_to_setting_button_text))
                .setPermissions(permission)
                // 권한 체크 시작
                .check();
    }

    private void startMainActivity() {

    }

    private void setProvinceMap() {
        map = new HashMap<>();

        map.put("경기도", "경기");
        map.put("강원도", "강원");
        map.put("경상북도", "경북");
        map.put("경상남도", "경남");
        map.put("전라북도", "전북");
        map.put("전라남도", "전남");
        map.put("충청북도", "충북");
        map.put("충청남도", "충남");
        map.put("서울특별시", "서울");
        map.put("부산광역시", "부산");
        map.put("대구광역시", "대구");
        map.put("인천광역시", "인천");
        map.put("대전광역시", "대전");
        map.put("울산광역시", "울산");
        map.put("세종특별자치시", "세종");
        map.put("제주특별자치도", "제주");
    }

    public static String getProvince(String city) {
        return map.get(city);
    }

    @Override
    public void onBackPressed() {
    }
}