package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


import com.google.gson.annotations.SerializedName;

/**
 * author hbzhou
 * date 2019/7/31 11:42
 */
public class VideoLiveStateInfoBean {

    /**
     * id : 30e0cc6e01384fe9a66135fe179ce850
     * createAt : 2019-07-19 11:02:48
     * updateAt : 2019-07-19 11:35:27
     * name : 月度会议
     * recordReserve : 0
     * shared : false
     * ws : false
     * wsAudio : false
     * authed : true
     * actived : true
     * beginAt :
     * endAt :
     * sign : 7G0nu4HZR
     * storePath :
     * url : rtmp://www.ynzhxf.com:8186/hls/30e0cc6e01384fe9a66135fe179ce850?sign=7G0nu4HZR
     * sharedLink : /share.html?id=30e0cc6e01384fe9a66135fe179ce850&type=live
     * session : {"Id":"30e0cc6e01384fe9a66135fe179ce850","Name":"月度会议","Type":"live","Application":"hls","HLS":"/hls/30e0cc6e01384fe9a66135fe179ce850/30e0cc6e01384fe9a66135fe179ce850_live.m3u8","HTTP-FLV":"/flv/hls/30e0cc6e01384fe9a66135fe179ce850.flv","RTMP":"rtmp://www.ynzhxf.com:8186/hls/30e0cc6e01384fe9a66135fe179ce850","Time":"0h 1m 54s","NumOutputs":0,"InBytes":37874785,"OutBytes":4603937,"InBitrate":362306,"OutBitrate":0,"AudioBitrate":22011,"VideoBitrate":340295,"VideoHeight":480,"VideoWidth":640,"AudioChannel":2,"AudioCodec":"AAC","AudioSampleRate":44100,"AudioSampleSize":16,"StartTime":"2019-07-19 11:35:27","VideoCodec":"H264"}
     */

    private String id;
    private String createAt;
    private String updateAt;
    private String name;
    private int recordReserve;
    private boolean shared;
    private boolean ws;
    private boolean wsAudio;
    private boolean authed;
    private boolean actived;
    private String beginAt;
    private String endAt;
    private String sign;
    private String storePath;
    private String url;
    private String sharedLink;
    private SessionBean session;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecordReserve() {
        return recordReserve;
    }

    public void setRecordReserve(int recordReserve) {
        this.recordReserve = recordReserve;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isWs() {
        return ws;
    }

    public void setWs(boolean ws) {
        this.ws = ws;
    }

    public boolean isWsAudio() {
        return wsAudio;
    }

    public void setWsAudio(boolean wsAudio) {
        this.wsAudio = wsAudio;
    }

    public boolean isAuthed() {
        return authed;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public String getBeginAt() {
        return beginAt;
    }

    public void setBeginAt(String beginAt) {
        this.beginAt = beginAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSharedLink() {
        return sharedLink;
    }

    public void setSharedLink(String sharedLink) {
        this.sharedLink = sharedLink;
    }

    public SessionBean getSession() {
        return session;
    }

    public void setSession(SessionBean session) {
        this.session = session;
    }

    public static class SessionBean {
        /**
         * Id : 30e0cc6e01384fe9a66135fe179ce850
         * Name : 月度会议
         * Type : live
         * Application : hls
         * HLS : /hls/30e0cc6e01384fe9a66135fe179ce850/30e0cc6e01384fe9a66135fe179ce850_live.m3u8
         * HTTP-FLV : /flv/hls/30e0cc6e01384fe9a66135fe179ce850.flv
         * RTMP : rtmp://www.ynzhxf.com:8186/hls/30e0cc6e01384fe9a66135fe179ce850
         * Time : 0h 1m 54s
         * NumOutputs : 0
         * InBytes : 37874785
         * OutBytes : 4603937
         * InBitrate : 362306
         * OutBitrate : 0
         * AudioBitrate : 22011
         * VideoBitrate : 340295
         * VideoHeight : 480
         * VideoWidth : 640
         * AudioChannel : 2
         * AudioCodec : AAC
         * AudioSampleRate : 44100
         * AudioSampleSize : 16
         * StartTime : 2019-07-19 11:35:27
         * VideoCodec : H264
         */

        private String Id;
        private String Name;
        private String Type;
        private String Application;
        private String HLS;
        @SerializedName("HTTP-FLV")
        private String HTTPFLV;
        private String RTMP;
        private String Time;
        private int NumOutputs;
        private int InBytes;
        private int OutBytes;
        private int InBitrate;
        private int OutBitrate;
        private int AudioBitrate;
        private int VideoBitrate;
        private int VideoHeight;
        private int VideoWidth;
        private int AudioChannel;
        private String AudioCodec;
        private int AudioSampleRate;
        private int AudioSampleSize;
        private String StartTime;
        private String VideoCodec;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getApplication() {
            return Application;
        }

        public void setApplication(String Application) {
            this.Application = Application;
        }

        public String getHLS() {
            return HLS;
        }

        public void setHLS(String HLS) {
            this.HLS = HLS;
        }

        public String getHTTPFLV() {
            return HTTPFLV;
        }

        public void setHTTPFLV(String HTTPFLV) {
            this.HTTPFLV = HTTPFLV;
        }

        public String getRTMP() {
            return RTMP;
        }

        public void setRTMP(String RTMP) {
            this.RTMP = RTMP;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public int getNumOutputs() {
            return NumOutputs;
        }

        public void setNumOutputs(int NumOutputs) {
            this.NumOutputs = NumOutputs;
        }

        public int getInBytes() {
            return InBytes;
        }

        public void setInBytes(int InBytes) {
            this.InBytes = InBytes;
        }

        public int getOutBytes() {
            return OutBytes;
        }

        public void setOutBytes(int OutBytes) {
            this.OutBytes = OutBytes;
        }

        public int getInBitrate() {
            return InBitrate;
        }

        public void setInBitrate(int InBitrate) {
            this.InBitrate = InBitrate;
        }

        public int getOutBitrate() {
            return OutBitrate;
        }

        public void setOutBitrate(int OutBitrate) {
            this.OutBitrate = OutBitrate;
        }

        public int getAudioBitrate() {
            return AudioBitrate;
        }

        public void setAudioBitrate(int AudioBitrate) {
            this.AudioBitrate = AudioBitrate;
        }

        public int getVideoBitrate() {
            return VideoBitrate;
        }

        public void setVideoBitrate(int VideoBitrate) {
            this.VideoBitrate = VideoBitrate;
        }

        public int getVideoHeight() {
            return VideoHeight;
        }

        public void setVideoHeight(int VideoHeight) {
            this.VideoHeight = VideoHeight;
        }

        public int getVideoWidth() {
            return VideoWidth;
        }

        public void setVideoWidth(int VideoWidth) {
            this.VideoWidth = VideoWidth;
        }

        public int getAudioChannel() {
            return AudioChannel;
        }

        public void setAudioChannel(int AudioChannel) {
            this.AudioChannel = AudioChannel;
        }

        public String getAudioCodec() {
            return AudioCodec;
        }

        public void setAudioCodec(String AudioCodec) {
            this.AudioCodec = AudioCodec;
        }

        public int getAudioSampleRate() {
            return AudioSampleRate;
        }

        public void setAudioSampleRate(int AudioSampleRate) {
            this.AudioSampleRate = AudioSampleRate;
        }

        public int getAudioSampleSize() {
            return AudioSampleSize;
        }

        public void setAudioSampleSize(int AudioSampleSize) {
            this.AudioSampleSize = AudioSampleSize;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getVideoCodec() {
            return VideoCodec;
        }

        public void setVideoCodec(String VideoCodec) {
            this.VideoCodec = VideoCodec;
        }
    }
}
