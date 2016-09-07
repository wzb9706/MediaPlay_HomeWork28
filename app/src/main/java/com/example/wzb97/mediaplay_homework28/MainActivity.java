package com.example.wzb97.mediaplay_homework28;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.SeekBar;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button btnStart,btnStop;
    private ToggleButton btnLoop;
    private TextView timeText,time;
    private SeekBar seekBar;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private boolean isUse=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer=new MediaPlayer();
        btnStart=(Button)findViewById(R.id.button);
        btnStop=(Button)findViewById(R.id.button2);
        btnLoop=(ToggleButton)findViewById(R.id.toggleButton);
        timeText=(TextView)findViewById(R.id.textView);
        time=(TextView)findViewById(R.id.textView3);
        btnStop.setEnabled(false);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUse=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUse=false;
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
                switch(msg.arg1){
                    case 1:TimeTextChage();break;
                }
                super.handleMessage(msg);
            }
        };


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn=(Button)view;
                if(btn.getText().toString().equals("播放")){
                    btn.setText("暂停");
                    mediaPlayer.reset();
                    mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.a9981);
                    mediaPlayer.start();
                    btnStop.setEnabled(true);
                    int s,m;
                    s=mediaPlayer.getDuration();
                    seekBar.setMax(s);
                    timer=new Timer();
                    timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            if(!isUse)
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            Message message=new Message();
                            message.arg1=1;
                            handler.sendMessage(message);
                        }
                    };
                    timer.schedule(timerTask,0,10);
                    s/=1000;
                    m=s/60;
                    s%=60;
                    time.setText("/"+m+":"+s);
                }
                else if(btn.getText().toString().equals("暂停")){
                    btn.setText("继续播放");
                    mediaPlayer.pause();
                }
                else{
                    btn.setText("暂停");
                    mediaPlayer.start();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                time.setText("00:00");
                btnStop.setEnabled(false);
                btnStart.setText("播放");
                seekBar.setProgress(0);
                timer.cancel();
            }
        });
        btnLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.getText().toString().equals("循环播放")){
                    Boolean loop=mediaPlayer.isLooping();
                    mediaPlayer.setLooping(!loop);
                }
            }
        });
    }
    private void TimeTextChage(){
        int s,m;
        s=mediaPlayer.getCurrentPosition();
        s/=1000;
        m=s/60;
        s%=60;
        timeText.setText(m+":"+s);
    }
}
