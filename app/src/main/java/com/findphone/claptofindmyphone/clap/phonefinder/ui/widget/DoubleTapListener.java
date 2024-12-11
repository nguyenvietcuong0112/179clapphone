package com.findphone.claptofindmyphone.clap.phonefinder.ui.widget;

import android.content.Context;
import android.view.View;

public class DoubleTapListener implements View.OnClickListener{

    private boolean isRunning= false;
    private int resetInTime =500;
    private int counter=0;

    private DoubleTapCallback listener;

    public DoubleTapListener(Context context)
    {
        listener = (DoubleTapCallback)context;
    }

    @Override
    public void onClick(View v) {

        if(isRunning)
        {
            if(counter==1)
                listener.onDoubleClick(v);
        }

        counter++;

        if(!isRunning)
        {
            isRunning=true;
            new Thread(() -> {
                try {
                    Thread.sleep(resetInTime);
                    isRunning = false;
                    counter=0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

}
