package com.cosiguk.covidsituation.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Notice;
import com.cosiguk.covidsituation.network.resultInterface.NoticeListener;
import com.cosiguk.covidsituation.util.ActivityUtil;
import com.cosiguk.covidsituation.util.NetworkUtil;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplashActivity extends BaseActivity {
    private static HashMap<String, String> cityMap;
    private static List<String> cityList;
    private int noticeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        networkCheck();
    }

    private void networkCheck() {
        if (NetworkUtil.isConnected(SplashActivity.this)) {
            versionCheck();
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
        showProgressDialog(SplashActivity.this, getResources().getString(R.string.progress_version));
        permissionCheck();
        /*
        MyApplication.getNetworkPresenterInstance()
                .version(new VersionListener() {
                    @Override
                    public void success(Version version) {
                        try {
                            String serverVersion = version.getAndroid();
                            // ????????? ???????????? getPackageInfo() ???????????? ??????????????? ????????? ??? ?????? ??????
                            String deviceVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

                            switch (BasicUtil.isVersionValid(deviceVersion, serverVersion)) {
                                case BasicUtil.VERSION_LATEST:
                                    // permissionCheck();
                                    break;

                                case BasicUtil.VERSION_LOW_MAJOR:
                                case BasicUtil.VERSION_LOW_MINOR:
                                case BasicUtil.VERSION_LOW_PATCH:
                                    String message = "?????? ???????????? ???????????????. \n???????????? ??? ??????????????????.\n????????? ???????????? ???????????? ???????????????.";
                                    message += "\n\n";
                                    message += "???????????? : " + deviceVersion + "\n";
                                    message += "???????????? : " + serverVersion;

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
                                                public void negative() {}
                                            }).show();
                                    break;

                                case BasicUtil.VERSION_ERROR:
                                    new NoticeDialog(SplashActivity.this)
                                            .setBackPressButton(false)
                                            .setShowNegativeButton(false)
                                            .setMsg(getResources().getString(R.string.version_error))
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
                        dismissProgressDialog();
                        new NoticeDialog(SplashActivity.this)
                                .setBackPressButton(false)
                                .setShowNegativeButton(false)
                                .setMsg(getString(R.string.version_fail))
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
         */
    }

    // ?????? ??????
    private void permissionCheck() {
        String[] permission = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION};

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    // ?????? ????????? ????????? ?????? ??????
                    public void onPermissionGranted() {
                        checkNotice();
                    }

                    @Override
                    // deniedPermissions ??? ?????? ??? ?????? ????????? ?????? ??????
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        showToast(getString(R.string.permission_not_agree));
                        finish();
                    }
                })
                .setDeniedMessage(getString(R.string.ted_permission_denied_message))
                // ?????? ???????????? ?????? ????????? ????????? ??????
                .setGotoSettingButtonText(getString(R.string.ted_permission_go_to_setting_button_text))
                .setPermissions(permission)
                .check();
    }

    // ??? ?????? ??????
    private void checkNotice() {
        networkPresenter.notice(new NoticeListener() {
            @Override
            public void success(ArrayList<Notice> notices) {
                noticeCount = 0;

                // ?????? ??? ????????? ??????
                if (preferencesManager.isFirstLaunch()) {
                    preferencesManager.setNotices(notices);
                    preferencesManager.setFirstLaunch(false);
                } else {
                    // ????????? ?????? ?????? ??????
                    int serverNoticeCount = notices.size();
                    // ????????? ?????? ?????? ??????
                    int savedNoticeCount = preferencesManager.getNotices().size();
                    // ????????? ????????? ?????? ??????
                    if (serverNoticeCount > savedNoticeCount) {
                        noticeCount = serverNoticeCount - savedNoticeCount;
                        // ??? ????????? ??????
                        ArrayList<Notice> newNotices = new ArrayList<>();

                        for (int i = 0; i < noticeCount; i++) {
                            preferencesManager.initNotices();
                            preferencesManager.addNotice(notices.get(i));
                        }
                    }
                }
                startMainActivity();
            }

            @Override
            public void fail(String message) {
                noticeCount = 0;
                new NoticeDialog(SplashActivity.this)
                        .setMsg(message)
                        .show();
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        dismissProgressDialog();
        setProvinceMap();
        setProvinceList();
        ActivityUtil.startNewActivityExtra(this, MainActivity.class, noticeCount);
    }

    private void setProvinceMap() {
        cityMap = new HashMap<>();

        cityMap.put("?????????", "??????");
        cityMap.put("?????????", "??????");
        cityMap.put("????????????", "??????");
        cityMap.put("????????????", "??????");
        cityMap.put("????????????", "??????");
        cityMap.put("????????????", "??????");
        cityMap.put("????????????", "??????");
        cityMap.put("????????????", "??????");
        cityMap.put("???????????????", "??????");
        cityMap.put("???????????????", "??????");
        cityMap.put("???????????????", "??????");
        cityMap.put("???????????????", "??????");
        cityMap.put("???????????????", "??????");
        cityMap.put("???????????????", "??????");
        cityMap.put("?????????????????????", "??????");
        cityMap.put("?????????????????????", "??????");
    }

    public static String getProvince(String city) {
        return cityMap.get(city);
    }

    private void setProvinceList() {
        cityList = new ArrayList<>();

        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
        cityList.add("??????");
    }

    public static List<String> getCityList() {
        return cityList;
    }

    @Override
    public void onBackPressed() { }
}