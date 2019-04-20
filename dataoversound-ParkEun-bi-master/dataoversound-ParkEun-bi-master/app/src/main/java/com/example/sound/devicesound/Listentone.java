package com.example.sound.devicesound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.*;

import java.util.ArrayList;

import static com.example.sound.devicesound.ToneThread.duration;

public class Listentone {

    int HANDSHAKE_START_HZ = 4096; //어플로 소리넣을 때 들어가는 주파수
    int HANDSHAKE_END_HZ = 5120 + 1024; //끝나는 주파수

    int START_HZ = 1024;
    int STEP_HZ = 256;
    int BITS = 4;

    int FEC_BYTES = 4;

    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    private int mSampleRate = 44100;
    private int mChannelCount = AudioFormat.CHANNEL_IN_MONO;//1채널 오디오 사용함.
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT; //오디오의 한 데이터를 16비트로 표시함.
    private float interval = 0.1f;

    private int mBufferSize = AudioRecord.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat);

    public AudioRecord mAudioRecord = null;
    int audioEncodig;
    boolean startFlag;
    FastFourierTransformer transform;


    public Listentone(){

        transform = new FastFourierTransformer(DftNormalization.STANDARD);
        startFlag = false;
        mAudioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
        mAudioRecord.startRecording();

        
    }

    private double findFrequency(double[] toTransform){
        int len = toTransform.length;
        double[] real = new double[len];
        double[] img = new double[len];
        double realNum;
        double imgNum;
        double[] mag = new double[len];

        Complex[] complx = transform.transform(toTransform, TransformType.FORWARD);
        Double[] freq = this.fftfreq(complx.length, duration: 1);

        for(int i = 0; i< complx.length; i++){
            realNum = complx[i].getReal();
            imgNum = complx[i].getImaginary();
            mag[i] = Math.sqrt((realNum * realNum) + (imgNum * imgNum));
        }
    }

    private Double[] fftfreq(int length, float duration) {

        return new Double[0];
    }


    public void PreRequest() { //decode.py의 listen_linux함수 기능을 해줌.
        int blocksize = findPowerSize((int)(long)Math.round(interval/2*mSampleRate));
        short[] buffer = new short[blocksize];
        int bufferReadResult = mAudioRecord.read(buffer,0,blocksize);
        //위의 세줄이 recorder로부터 음성을 읽는 코드라고 함. buffer를 이용해 푸리에 변환하면 됨.
    }

    private int findPowerSize(int round) { //bufferSize는 2의 제곱수 형태로 들어가야함.

    }
}
