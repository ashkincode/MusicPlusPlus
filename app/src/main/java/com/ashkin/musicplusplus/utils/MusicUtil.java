package com.ashkin.musicplusplus.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.ashkin.musicplusplus.app.Config;

import java.io.IOException;

/**
 * 媒体播放工具类
 * Media Util
 */
public class MusicUtil implements MediaPlayer.OnPreparedListener {
    public static final String TAG = "MusicUtil";
    private static MusicUtil instance;

    private Context mContext;
    private MediaPlayer mPlayer;
    private int msec;

    private MusicUtil(final Context context) {
        this.mContext = context;
        this.mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                context.sendBroadcast(new Intent(Config.ACTION_COMPLETE));
            }
        });
    }

    public static void initialization(Context context) {
        instance = new MusicUtil(context);
    }

    public static MusicUtil getInstance() {
        return instance;
    }

    /**
     * 恢复播放
     */
    public void start() {
        if (null != mPlayer && !mPlayer.isPlaying()) {
            LogUtil.i(TAG, "i : 恢复播放");
            mPlayer.start();
        }
    }

    /**
     * 播放音乐
     *
     * @param data 音乐的 data 数据
     */
    public void start(String data) {
        start(data, 0);
    }

    /**
     * 从 msec 处开始播放音乐
     *
     * @param data 音乐的 data 数据
     * @param msec 音乐的毫秒数
     */
    public void start(String data, int msec) {
        // TODO: 播放音乐
        try {
            this.msec = msec;
            LogUtil.i(TAG, "i : 播放音乐, data = " + data + " msec = " + msec);
            mPlayer.reset();
            mPlayer.setDataSource(data);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            LogUtil.e(TAG, "e : IOException = " + e.getMessage());
        }
    }

    /**
     * 暂停音乐
     */
    public void pause() {
        // TODO: 暂停音乐
        if (null != mPlayer && mPlayer.isPlaying()) {
            LogUtil.i(TAG, "i : 暂停音乐");
            mPlayer.pause();
        }
    }

    /**
     * 释放 MediaPlayer 资源
     */
    public void release() {
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.seekTo(msec);
        mp.start();
    }
}
