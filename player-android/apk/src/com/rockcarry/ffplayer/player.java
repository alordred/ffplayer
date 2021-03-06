package com.rockcarry.ffplayer;

public final class player
{
    public static final int SCALING_MODE_SCALE_TO_WINDOW = 1;
    public static final int SCALING_MODE_SCALE_CROP      = 2;
    public static final int SCALING_MODE_NO_SCALE_CROP   = 3;

    public static final int EVENT_PLAY_PROGRESS          = (('R' << 24) | ('U' << 16) | ('N' << 8) | (' ' << 0));
    public static final int EVENT_PLAY_COMPLETED         = (('E' << 24) | ('N' << 16) | ('D' << 8) | (' ' << 0));

    public static final int PARAM_MEDIA_DURATION         = 0x1000 + 0;
    public static final int PARAM_MEDIA_POSITION         = 0x1000 + 1;
    public static final int PARAM_VIDEO_WIDTH            = 0x1000 + 2;
    public static final int PARAM_VIDEO_HEIGHT           = 0x1000 + 3;
//  public static final int PARAM_VIDEO_MODE             = 0x1000 + 4;
    public static final int PARAM_AUDIO_VOLUME           = 0x1000 + 5;
    public static final int PARAM_PLAY_SPEED             = 0x1000 + 6;
    public static final int PARAM_DECODE_THREAD_COUNT    = 0x1000 + 7;
//  public static final int PARAM_VISUAL_EFFECT          = 0x1000 + 8;
    public static final int PARAM_AVSYNC_TIME_DIFF       = 0x1000 + 9;
//  public static final int PARAM_PLAYER_CALLBACK        = 0x1000 +10;
    public static final int PARAM_AUDIO_STREAM_TOTAL     = 0x1000 +11;
    public static final int PARAM_VIDEO_STREAM_TOTAL     = 0x1000 +12;
    public static final int PARAM_SUBTITLE_STREAM_TOTAL  = 0x1000 +13;
    public static final int PARAM_AUDIO_STREAM_CUR       = 0x1000 +14;
    public static final int PARAM_VIDEO_STREAM_CUR       = 0x1000 +15;
    public static final int PARAM_SUBTITLE_STREAM_CUR    = 0x1000 +16;

    private long m_hPlayer = 0;
    public boolean open(String url) {
        m_hPlayer = nativeOpen(url, null, 0, 0);
        nativeInitJniObject(m_hPlayer);
        return (m_hPlayer != 0);
    }

    public void close()                      { nativeClose(m_hPlayer);     }
    public void play ()                      { nativePlay (m_hPlayer);     }
    public void pause()                      { nativePause(m_hPlayer);     }
    public void seek (long ms)               { nativeSeek (m_hPlayer, ms); }
    public void setParam(int id, long value) { nativeSetParam(m_hPlayer, id, value); }
    public long getParam(int id)             { return nativeGetParam(m_hPlayer, id); }
    public void setDisplaySurface(Object surface) { nativeSetDisplaySurface(m_hPlayer, surface); }
    public void setDisplayTexture(Object texture) { nativeSetDisplayTexture(m_hPlayer, texture); }
    public void setDispScaleMode (int    mode   ) { nativeSetDispScaleMode(m_hPlayer, mode    ); }

    public void setPlayerEventCallback(playerEventCallback cb) { mPlayerEventCB = cb; }
    public interface playerEventCallback {
        public void onPlayerEvent(int event, long param);
    }

    //++ for player event callback
    private playerEventCallback mPlayerEventCB = null;

    private void internalPlayerEventCallback(int event, long param) {
        if (mPlayerEventCB != null) {
            mPlayerEventCB.onPlayerEvent(event, param);
        }
        nativeEnableCallback(m_hPlayer, mPlayerEventCB != null ? 1 : 0);
    }

    private native void nativeInitJniObject(long hplayer);
    //-- for player event callback

    private static native long nativeOpen (String url, Object surface, int w, int h);
    private static native void nativeClose(long hplayer);
    private static native void nativePlay (long hplayer);
    private static native void nativePause(long hplayer);
    private static native void nativeSeek (long hplayer, long ms);
    private static native void nativeSetParam(long hplayer, int id, long value);
    private static native long nativeGetParam(long hplayer, int id);
    private static native void nativeSetDisplaySurface(long hplayer, Object surf);
    private static native void nativeSetDisplayTexture(long hplayer, Object text);
    private static native void nativeSetDispScaleMode(long hplayer, int mode  );
    private static native void nativeEnableCallback  (long hplayer, int enable);

    static {
        System.loadLibrary("ffplayer_jni");
    }
};

