package com.yfkk.cardbag.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;

import com.yfkk.cardbag.MainApplication;
import com.yfkk.cardbag.log.LogUtils;

/**
 * Created by litao on 2018/2/9.
 */

public class MediaPlayerUtils {

    private static MediaRecorder recorder;
    private static boolean isCompleteRecorder;
    private static MediaPlayer mPlayer;
    private static Handler handler = new Handler();

    /**
     * 开始录音
     */
    public static void startRecord(final RecordListener recordListener, final int maxDuration) {
        // 录音之前，停止播放
        stopPlayMedia();
        stopRecorder();
        isCompleteRecorder = false;

        // 初始化录音
        final String recordingPath = FileUtils.getExternalCachePath(MainApplication.getInstance()) + "/record_ls.AAC";
        if (recorder == null) {
            recorder = new MediaRecorder();//初始化录音对象
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置录音的输入源(麦克)
            recorder.setAudioChannels(2);

//            recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);//设置音频格式(amr)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioSamplingRate(44100);
            recorder.setAudioEncodingBitRate(48000); // 48000: 4秒30kb , 12800:4秒55KB

            recorder.setOutputFile(recordingPath); //设置录音保存的文件
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            try {
                recorder.prepare();//准备录音
            } catch (Exception e) {
                e.printStackTrace();
                if (recordListener != null) {
                    recordListener.errorRecord(recordingPath);
                }
                return;
            }
        }

        if (recorder != null) {
            try {
                // 开始录音
                recorder.start();
                if (recordListener != null) {
                    recordListener.startRecord(recordingPath);
                }

                final long startRecordTime = System.currentTimeMillis();
                handler.removeCallbacksAndMessages(null);
                new Runnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        if (System.currentTimeMillis() - startRecordTime > maxDuration + 500 || (isCompleteRecorder && count >= 30)) { // 最多录制10秒，最少录制2秒
                            // 录音结束
                            if (recordListener != null) {
                                recordListener.completionRecorder(recordingPath, System.currentTimeMillis() - startRecordTime);
                            }
                            stopRecorder();
                        } else {
                            // 录音进度
                            count++;
                            if (recordListener != null) {
                                recordListener.progcressRecorder(System.currentTimeMillis() - startRecordTime, recordingPath);
                            }
                            handler.postDelayed(this, 50);// 每50毫秒刷新一次
                        }
                    }
                }.run();
            } catch (Exception e) {
                e.printStackTrace();
                if (recordListener != null) {
                    recordListener.errorRecord(recordingPath);
                }
            }
        }
    }

    /**
     * 完成录音
     */
    public static void completeRecorder() {
        isCompleteRecorder = true;
    }

    public static void stopRecorder() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                handler.removeCallbacksAndMessages(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    static MediaListener playMediaListener;
    static String playMediasource;

    public static void addPlayMediaListener(String source, MediaListener mediaListener) {
        if (source.equals(playMediasource)) {
            playMediaListener = mediaListener;
        }
    }

    public static void playMedia(final String source, final MediaListener mediaListener) {
        if (StringUtils.isEmpty(source)) {
            return;
        }
        // 停止播放和录音
        stopPlayMedia();
        stopRecorder();

        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        try {
            playMediaListener = mediaListener;
            playMediasource = source;

            mPlayer.reset();
            mPlayer.setDataSource(StringUtils.mosaicUrl(source));
            mPlayer.prepareAsync();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 播放完毕
                    if (playMediaListener != null) {
                        playMediaListener.progressPlayMedia(mPlayer.getCurrentPosition(), mPlayer.getDuration(), source);
                        playMediaListener.completionPlayMedia(source);
                        playMediaListener = null;
                    }
                }
            });
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                /* 覆盖错误处理事件 */
                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    try {
                        /* 发生错误时也解除资源与MediaPlayer的赋值 */
                        arg0.release();
                        if (playMediaListener != null) {
                            playMediaListener.errorPlayMedia(source);
                            playMediaListener = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 开始播放
                    mPlayer.start();
                    if (playMediaListener != null) {
                        playMediaListener.startPlayMedia(source);
                    }

                    // 播放进度
                    handler.removeCallbacksAndMessages(null);
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mPlayer != null && mPlayer.isPlaying()) {
                                    if (playMediaListener != null) {
                                        playMediaListener.progressPlayMedia(mPlayer.getCurrentPosition(), mPlayer.getDuration(), source);
                                    }
                                    handler.postDelayed(this, 500);// 每500毫秒刷新一次
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtils.e("播放进度监听，线程异常");
                                if (playMediaListener != null) {
                                    playMediaListener.errorPlayMedia(source);
                                    playMediaListener = null;
                                }
                            }
                        }
                    }.run();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("播放进度监听，线程异常");
            if (playMediaListener != null) {
                playMediaListener.errorPlayMedia(source);
                playMediaListener = null;
            }
        }
    }

    /**
     * 播放短促的，不循环的，可叠加的声音
     */
    static MediaPlayer mPlaySoundPool;

    public static void playSoundPool(final Context context, final int resid) {
        // 释放资源
        if (mPlaySoundPool != null) {
            try {
                mPlaySoundPool.release();
                mPlaySoundPool = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            mPlaySoundPool = MediaPlayer.create(context, resid);
            mPlaySoundPool.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                /* 覆盖错误处理事件 */
                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    try {
                        /* 发生错误时也解除资源与MediaPlayer的赋值 */
                        arg0.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            mPlaySoundPool.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
            mPlaySoundPool.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 开始播放
                    mPlaySoundPool.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放本地音乐
     */
    public static void playMedia(final Context context, final int resid,
                                 final boolean isLooping) {
        // 停止播放和录音
        stopPlayMedia();
        stopRecorder();
        try {
            if (mPlayer == null) {
                mPlayer = MediaPlayer.create(context, resid);
            }
            mPlayer.setLooping(isLooping);
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                /* 覆盖错误处理事件 */
                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    try {
                        /* 发生错误时也解除资源与MediaPlayer的赋值 */
                        arg0.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 开始播放
                    mPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seekMedia(String source, int seek) {
        if (source.equals(playMediasource) && mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.seekTo((int) (seek / 100f * mPlayer.getDuration()));
        }
    }

    public static boolean isPlaying() {
        if (mPlayer != null) {
            try {
                return mPlayer.isPlaying();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void stopPlayMedia() {
        if (mPlayer != null) {
            try {
                mPlayer.release();
                mPlayer = null;
                handler.removeCallbacksAndMessages(null);
                if (playMediaListener != null) {
                    playMediaListener.stopPlayMedia(null);
                    playMediaListener = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public interface RecordListener {

        void startRecord(String recordingPath); // 开始录音

        void completionRecorder(String recordingPath, long duration); // 完成录音

        void progcressRecorder(long progressTime, String recordingPath); // 录音进度(毫秒)

        void errorRecord(String recordingPath);// 录音发生错误
    }

    public interface MediaListener {

        void startPlayMedia(String mediaPath); // 开始播放

        void completionPlayMedia(String mediaPath); // 完成播放

        void progressPlayMedia(long progressTime, long duration, String mediaPath); // 播放进度(毫秒)

        void errorPlayMedia(String mediaPath);// 播放发生错误

        void stopPlayMedia(String mediaPath);// 播放停止（非正常播放結束）

    }

}
