package com.example.teachme_shapes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;

public class MainActivity extends AppCompatActivity {
    // MARK: Debug info
    private final String TAG="TEACHMESHAPES";

    // MARK: Particle Account Info
    private final String PARTICLE_USERNAME = "gurjit.babrah@gmail.com";
    private final String PARTICLE_PASSWORD = "honeykaur123";

    // MARK: Particle device-specific info
    private final String DEVICE_ID = "17002f000f47363333343437";


    private long subscriptionId;
  int counter=0;
    // MARK: Particle device
    private ParticleDevice mDevice;
    TextView View1,View2;
    int i = 0;
    int loop = 1;
    int loop2= 1;


    ImageView image;
    Button button;
    String[] Ans = new String[] {"A","B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View1 =  findViewById(R.id.result);
       View2= findViewById(R.id.textView);

       image = findViewById(R.id.imageView);
        button = findViewById(R.id.score);


       //GETTING API CONNECTION
        ParticleCloudSDK.init(this.getApplicationContext());

       //SETUP VARIABLE DEVICE
        getDeviceFromCloud();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {
                        View1.setText("" + counter);
                        showScore("" + counter);

                    }
                });


            }
        });

    }


   //GETTING VARIABLE FROM CLOUD


    public void getDeviceFromCloud() {
        // This function runs in the background
        // It tries to connect to the Particle Cloud and get your device

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(PARTICLE_USERNAME, PARTICLE_PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);
                return -1;

            }

            @Override
            public void onSuccess(Object o) {

                Log.d(TAG, "Successfully got device from Cloud");

                // if you get the device, then go subscribe to events
                subscribeToParticleEvents();
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }



    public void result(String answer){
        runOnUiThread(new Runnable() {


            @Override
            public void run() {
                try{
                    if (answer.equals(Ans[i])){
                        View1.setText("You are correct");
                        if (Ans[1].equals(Ans[i])){


                            turnParticleGreen("pentagon");
                                      if (loop == 1) {
                                                 counter = counter + 1;
                                loop++;
                            }

                        } else if (Ans[0].equals(Ans[i])){
                            turnParticleGreen("square");
                            if (loop2 == 1) {
                               counter = counter + 1;
                                loop2++;
                            }
                        }


                    }
                    else {
                        View1.setText("Sorry you could'nt make it");
                        turnParticleRed();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                }

            }
        });



        Log.d(TAG, "I'm inside the result() function");

    }
    public void nextQue(){


        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(i<1){
                    i++;
                    image.setImageResource(R.drawable.pentagon);

                    View2.setText("How many Sides 4 or 5? " +
                            "\n A. 4 \n B. 5\n\n" +

                            "Use Particle to answer.\n\n" +

                            "(A = Button 4, B = Button 2)");
                                  View1.setText("Answer");

                }else{


                    i++;
                    image.setImageResource(R.drawable.smiley);
                    View2.setText("Quiz Finished");
                    View1.setText("");
                }

            }
        });

    }



    public void turnParticleGreen(String green) {

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add(green);
                try {
                    mDevice.callFunction("answer", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success: Turned light green!!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());


            }
        });
    }



    public void showScore(String score) {

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add(score);
                try {
                    mDevice.callFunction("score", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success: Turned light green!!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }




    public void turnParticleRed() {

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add("red");
                try {
                    mDevice.callFunction("answer", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success: Turned lights red!!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });

    }




    public void subscribeToParticleEvents() {
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                subscriptionId = ParticleCloudSDK.getCloud().subscribeToAllEvents(
                        "playerChoice",  // the first argument, "eventNamePrefix", is optional
                        new ParticleEventHandler() {
                            public void onEvent(String eventName, ParticleEvent event) {
                               // Log.i(TAG, "Received event with payload: " + event.dataPayload);
                                String choice = event.dataPayload;
                                if (choice.contentEquals("A")) {

                                    result("A");
                                }
                                else if (choice.contentEquals("B")) {

                                    result("B");
                                }
                                else if(choice.contentEquals("true")) {
                                    nextQue();
                                }
                                else if(choice.contentEquals("C")){
                                    showScore(""+ counter);
                                }

                            }

                            public void onEventError(Exception e) {
                                Log.e(TAG, "Event error: ", e);
                            }
                        });
                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Success: Subscribed to events!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }
}
