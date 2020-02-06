package com.example.meep.firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class abootus extends Activity {
int click =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abootus);
        TextView n=(TextView) findViewById(R.id.intro);
        n.setClickable(true);
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(click<=5)
                {
                    click++;
                    switch (click)
                    {
                        case 1:  Toast.makeText(getBaseContext(),"Please don't click here",Toast.LENGTH_SHORT).show(); break;
                        case 3:  Toast.makeText(getBaseContext(),"OMG STAAAP Clicking!!",Toast.LENGTH_SHORT).show(); break;
                        case 4:  Toast.makeText(getBaseContext(),"I dare you to click one more time ",Toast.LENGTH_SHORT).show(); break;
                        case 5:  Toast.makeText(getBaseContext(),"Told ya!!!!",Toast.LENGTH_LONG).show();
                            Intent k=new Intent(getBaseContext(),easteregg.class);
                            startActivity(k);
                            break;
                        default: Toast.makeText(getBaseContext(),"You have Clicked "+click+" Times",Toast.LENGTH_SHORT).show(); break;
                    }
                }
            }
        });
    }
}
