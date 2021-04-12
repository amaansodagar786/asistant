package cf.developerguy.tqd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public int checkint;
    int a = 0;
    private Button button;
    private SpeechRecognizer recognizer;
    private TextView tvResult;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findbyid();
        initioalizeTexttospeech();
        initializeresult();
        /*button = (Button) findViewById(R.id.newbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();

            }
        });*/
    }
    private void initioalizeTexttospeech() {
//        T2S= new TextToSpeech(testApp.getInstance().getApplicationContext(), this, "com.google.android.tts");
//        Set<String> a=new HashSet<>();
//        a.add("male");//here you can give male if you want to select male voice.
//        Voice v=new Voice("en-us-x-sfg#male_2-local",new Locale("en","US"),400,200,true,a);
//        T2S.setVoice(v);
//        T2S.setSpeechRate(0.8f);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0) {
                    Toast.makeText(MainActivity.this, "Engine is not available ", Toast.LENGTH_SHORT).show();
                } else {
//                    if (Manifest.permission.RECORD_AUDIO)
                    speak();

                }
            }
        });
    }

    private void speak() {
        tts.setLanguage(Locale.US);
        tts.speak(" Hi i am Edith..", TextToSpeech.QUEUE_FLUSH, null);

    }

    private void findbyid() {
        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    private void initializeresult() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            System.out.println("speach recognizer is available ");
            recognizer = SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Toast.makeText(MainActivity.this, "Speek please ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBeginningOfSpeech() {
                    System.out.println("onbegainnig of speech ");
                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {
                    System.out.println("speech endded ");
                    System.out.println("stop!");
                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (result != null) {
                        String text = result.get(0);

                        tvResult.setText(result.get(0));
                    } else {
                        System.out.println(result);
                        System.out.println(" array list(result) = null ");
                    }

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    /*public void openActivity2() {
        Intent intent = new Intent(this, ask_per.class);
        startActivity(intent);

//        if(getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
//            return 0;
//        }

    }*/
//////////////////////////////////////////////////////////////////////////////////////////open camara
    public void opencam(View view) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> pkgAppsList = getBaseContext().getPackageManager().queryIntentActivities( mainIntent, 0);
        for(ResolveInfo fruit:pkgAppsList)
            System.out.println(fruit);
    }
/////////////////////////////////////////////////////////////////////////////////////////open camara

//////////////////////////////////////////////////////////////////////////////////////////open camara
//    public void opencam(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivity(intent);
//    }
/////////////////////////////////////////////////////////////////////////////////////////open camara


    public void startrecording(View view) {
        Intent dent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        dent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        dent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        System.out.println("in start recording function");

        recognizer.startListening(dent);

    }



}