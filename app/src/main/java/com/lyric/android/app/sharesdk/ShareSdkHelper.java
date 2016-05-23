package com.lyric.android.app.sharesdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.lyric.android.app.R;
import com.lyric.android.library.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareSdkHelper {
    private static ShareSdkHelper mInstance;
    private static String[] mPlatforms;

    private Context mContext;
    private final static String LOGO_PATH = "logo.png";

    private ShareSdkHelper(Context context) {
        mContext = context.getApplicationContext();
        mPlatforms = context.getResources().getStringArray(R.array.share_platform);
        ShareSDK.initSDK(context);
        ShareSDK.setConnTimeout(20000);
        ShareSDK.setReadTimeout(20000);
    }

    public static synchronized ShareSdkHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ShareSdkHelper(context);
        }
        return mInstance;
    }

    /**
     * 创建分享参数,不带私信分享
     *
     * @param title     分享标题
     * @param content   分享内容
     * @param imageUrl  分享图片地址
     * @param targetUrl 分享目标网站地址
     * @return 分享参数
     */
    public ShareContentParams buildShareParams(String title, String content, String imageUrl, String targetUrl) {
        return this.bulidShareParams(title, content, imageUrl, targetUrl, "", "", "", "", "");
    }

    public ShareContentParams bulidShareParams(String title, String content, String imageUrl, String targetUrl, String msgType, String msgId, String targetId, String userType, String sixinTitle) {
        ShareSdkHelper.ShareContentParams params = new ShareSdkHelper.ShareContentParams();
        params.setTitle(title);
        params.setContent(content);
        params.setImageUrl(imageUrl);
        params.setTargetUrl(targetUrl);
        params.setType(msgType);
        params.setTargetId(msgId);
        params.setSourceId(targetId);
        params.setUserType(userType);
        params.setSubtitle(sixinTitle);
        return params;
    }

    public void share(final PlatformType platformType, final ShareSdkHelper.ShareContentParams params) {
        if (params == null) {
            return;
        }
        if (PlatformType.WECHAT.equals(platformType) || PlatformType.WECHAT_MOMENTS.equals(platformType)) {
            Wechat wechat = new Wechat(mContext);
            if (!wechat.isClientValid()) {
                return;
            }
        }
        final OnekeyShare oks = new OnekeyShare();
        oks.setPlatform(platformType.getPlatform());//这里必须设置分享平台
        oks.setTitle(params.getTitle());
        oks.setTitleUrl(params.getTargetUrl());
        if (PlatformType.SINA_WEIBO.equals(platformType)) {
            oks.setText(params.getTitle() + " " + params.getTargetUrl());
        } else {
            oks.setText(params.getContent());
        }
        String imageUrl = params.getImageUrl();
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = getLogoPath();
            oks.setImagePath(imageUrl);
        } else {
            oks.setImageUrl(imageUrl);
        }
        oks.setUrl(params.getTargetUrl());
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        oks.disableSSOWhenAuthorize();
        // 去自定义不同平台的字段内容
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
            }
        });
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                // 分享失败的统计
                ShareSDK.logDemoEvent(4, platform);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                // 分享失败的统计
                ShareSDK.logDemoEvent(5, platform);
            }
        });

        oks.show(mContext);
    }

    private String getLogoPath() {
        String path = CommonUtils.getDiskCacheDir(mContext, LOGO_PATH);
        File file = new File(path);
        if (!file.exists()) {//不存在就保存到SD卡
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
            FileOutputStream os;
            try {
                os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public static class ShareContentParams implements Parcelable {
        //分享Title
        private String title;
        //内容
        private String content;
        //图片地址
        private String imageUrl;
        //跳转链接
        private String targetUrl;
        /**
         * 分享类型（问题，圈子，求助等）
         */
        private String type;
        /**
         * 分享目标id(圈子id,用户id等)
         */
        private String targetId;
        /**
         * 分享来源ID：求助发布的机构id，只有求助分享到私信用到，其他分享不用
         */
        private String sourceId;
        /**
         * 分享的用户类型，只有机构主页和个人主页分享到私信用到
         */
        private String userType;
        /**
         * 分享到私信的标题和医生圈文献的来源，其他分享不用
         */
        private String subtitle;

        public ShareContentParams() {
        }

        protected ShareContentParams(Parcel in) {
            title = in.readString();
            content = in.readString();
            imageUrl = in.readString();
            targetUrl = in.readString();
            type = in.readString();
            targetId = in.readString();
            sourceId = in.readString();
            userType = in.readString();
            subtitle = in.readString();
        }

        public static final Creator<ShareContentParams> CREATOR = new Creator<ShareContentParams>() {
            @Override
            public ShareContentParams createFromParcel(Parcel in) {
                return new ShareContentParams(in);
            }

            @Override
            public ShareContentParams[] newArray(int size) {
                return new ShareContentParams[size];
            }
        };

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTargetUrl() {
            return targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.content);
            dest.writeString(this.imageUrl);
            dest.writeString(this.targetUrl);
            dest.writeString(this.type);
            dest.writeString(this.targetId);
            dest.writeString(this.sourceId);
            dest.writeString(this.userType);
            dest.writeString(this.subtitle);
        }
    }

    /**
     * 分享平台
     */
    public enum PlatformType {
        WECHAT(Wechat.NAME, mPlatforms[2], R.mipmap.sharesdk_ic_wechat),
        WECHAT_MOMENTS(WechatMoments.NAME, mPlatforms[3], R.mipmap.sharesdk_ic_wechat_moments),
        QQ(cn.sharesdk.tencent.qq.QQ.NAME, mPlatforms[4], R.mipmap.sharesdk_ic_qq),
        SINA_WEIBO(SinaWeibo.NAME, mPlatforms[5], R.mipmap.sharesdk_ic_sina);

        private String platform;
        private String name;
        private int platformLogo;

        PlatformType(String platform, String name, int platformLogo) {
            this.platform = platform;
            this.name = name;
            this.platformLogo = platformLogo;
        }

        public String getPlatform() {
            return platform;
        }

        public String getName() {
            return name;
        }

        public int getPlatformLogo() {
            return platformLogo;
        }
    }

}
