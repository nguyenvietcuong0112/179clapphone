package com.findphone.claptofindmyphone.clap.phonefinder.ui.services.thread;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;

import androidx.core.app.ActivityCompat;

import com.findphone.claptofindmyphone.clap.phonefinder.model.DefaultPreferences;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.alert_vocal.VocalAlert;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.services.VocalService;

import be.hogent.tarsos.dsp.AudioEvent;
import be.hogent.tarsos.dsp.AudioFormat;
import be.hogent.tarsos.dsp.onsets.OnsetHandler;
import be.hogent.tarsos.dsp.onsets.PercussionOnsetDetector;

public class DetectClapClap implements OnsetHandler {

    static int SAMPLE_RATE = 8000;
    private byte[] buffer;
    private int clap;
    private DefaultPreferences defaultPreferences;
    private Context mContext;
    private boolean mIsRecording;
    private PercussionOnsetDetector mPercussionOnsetDetector;
    private int nb_claps = 3;
    private int rateSupported;
    private boolean rate_send;
    private AudioRecord recorder;
    private AudioFormat tarsosFormat;


    public DetectClapClap(Context context) {
        Context context2 = context;
        this.defaultPreferences = new DefaultPreferences(context2);
        SAMPLE_RATE = getValidSampleRates();
        this.mContext = context2;
        int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, 16, 2);
        this.buffer = new byte[minBufferSize];
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.recorder = new AudioRecord(1, SAMPLE_RATE, 16, 2, minBufferSize);
        this.mPercussionOnsetDetector = new PercussionOnsetDetector((float) SAMPLE_RATE, minBufferSize / 2, this, 24.0d, 5.0d);
        this.clap = 0;
        this.mIsRecording = true;
    }

    public int getValidSampleRates() {
        for (int i : new int[]{44100, 22050, 16000, 11025, 8000}) {
            if (AudioRecord.getMinBufferSize(i, 1, 2) > 0 && !this.rate_send) {
                this.rateSupported = i;
                this.rate_send = true;
            }
        }
        return this.rateSupported;
    }

    public void handleOnset(double d, double d2) {
        this.clap++;
        if (this.clap >= this.nb_claps) {
            this.defaultPreferences.save("detectClap", "1");
            this.mIsRecording = false;
            Intent intent = new Intent(this.mContext, VocalAlert.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
            this.mContext.startActivity(intent);
            this.mContext.stopService(new Intent(this.mContext, VocalService.class));
        }
    }

    public void listen() {
        this.recorder.startRecording();
        this.tarsosFormat = new AudioFormat((float) SAMPLE_RATE, 16, 1, true, false);
        new Thread(new Runnable() {
            public void run() {
                while (DetectClapClap.this.mIsRecording) {
                    AudioEvent audioEvent = new AudioEvent(DetectClapClap.this.tarsosFormat, (long) DetectClapClap.this.recorder.read(DetectClapClap.this.buffer, 0, DetectClapClap.this.buffer.length));
                    audioEvent.setFloatBufferWithByteBuffer(DetectClapClap.this.buffer);
                    DetectClapClap.this.mPercussionOnsetDetector.process(audioEvent);
                }
                DetectClapClap.this.recorder.stop();
            }
        }).start();
    }
}
