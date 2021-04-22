package cf.developerguy.tqd;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public LottieAnimationView lottieAnimationView2;
    boolean islottieAnimationView2 = false;

    private String TAG;
    private SpeechRecognizer recognizer;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lottieAnimationView2 = findViewById(R.id.lottieAnimationView2);
        lottieAnimationView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islottieAnimationView2) {
                    lottieAnimationView2.playAnimation();
                    islottieAnimationView2 = false;
                } else {
                    lottieAnimationView2.pauseAnimation();
                    islottieAnimationView2 = true;
                }
            }
        });
        initioalizeTexttospeech();
        initializeresult();
    }

    private void initioalizeTexttospeech() {
        tts = new TextToSpeech(this, status -> {
            if (tts.getEngines().size() == 0) {
                Toast.makeText(MainActivity.this, "Engine is not available ", Toast.LENGTH_SHORT).show();
            } else {
//                    if (Manifest.permission.RECORD_AUDIO)
                speak("hello i am edith !");


            }
        });
    }

    public void speak(String msg) {
        tts.setSpeechRate(-1);
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
    }

//    private void findbyid() {
//        tvResult = (TextView) findViewById(R.id.tv_result);
//    }

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
                    Toast.makeText(MainActivity.this, "Stop speaking", Toast.LENGTH_SHORT).show();
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
                        String text = result.get(0).toLowerCase();
//                        tvResult.setText(result.get(0));
                        response(text);

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

    public void startrecording(View view) {
        Intent dent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        dent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        dent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        System.out.println("in start recording function");

        recognizer.startListening(dent);

    }

    private void response(String convtxt) {
        if (convtxt.contains("hello")||convtxt.contains("hello edith")) {
            speak("Hello i am edith.   what can i help you with ?");
        } if (convtxt.contains("how can you help me")) {
            speak("Hello i am edith.   what can i help you with ?");
        } else if (convtxt.contains("i") && (convtxt.contains("want") || convtxt.contains("wanna")) || ((convtxt.contains("to"))) && (convtxt.contains("watch") || convtxt.contains("see")) && (convtxt.contains("movie") || convtxt.contains("movies") || convtxt.contains("youtube") || convtxt.contains("tv series") || convtxt.contains("videos") || convtxt.contains("season") || convtxt.contains("cartoon"))) {
            speak("ok ! opening youtube");
            youtube();
        } else if ((convtxt.contains("open") || convtxt.contains("start") || convtxt.contains("set") || convtxt.contains("setup") || convtxt.contains("create")) && (convtxt.contains("alarm") || convtxt.contains("reminder"))) {
            speak("We are currently working on that feature . we apologize for inconvenience! you can set a alarm ");
            openclock();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("whatsapp")) {
            speak("opening whatsapp");
            whatsapp();
        } else if (convtxt.contains("call") || convtxt.contains("contact")) {

            mkcall(convtxt);
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("youtube")) {
            speak("opening youtube");
            youtube();
        } else if (convtxt.contains("who")) {
            if (convtxt.contains("are") && convtxt.contains("you")) {
                speak("my name is edith i am an artificial assistant");
            } else if (convtxt.contains("is your creator")) {
                speak("i am edith and my creator is dev");
            } else if (convtxt.contains("who") && convtxt.contains("is")) {
                speak("opening wikipedia ");
                String urldata = findw(convtxt);
                newwiki(urldata);

            }
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("camera")) {
            speak("sure !  Opening camera.... ");
            opencam();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("google")) {
            speak("sure !  Opening google.... ");
            google();
        } else if ((convtxt.contains("take") || convtxt.contains("capture")) && (convtxt.contains("photo") || convtxt.contains("picture"))) {
            speak("sure !  Opening camera.... ");
            dispatchTakePictureIntent();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("play store")) {
            speak("sure ! opening playstore");
            playstore();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("spotify")) {
            speak("sure!   opening spotify");
            spotify();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("facebook")) {
            speak("opening facebook");
            facebook();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("gmail")) {
            speak("opening gmail....");
            gmail();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && (convtxt.contains("gallery") || convtxt.contains("photo") || convtxt.contains("photos"))) {
            speak("opening gallary....");
            gallery();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && (convtxt.contains("drive") || convtxt.contains("file") || convtxt.contains("storage"))) {
            speak("opening Drive....");
            drive();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && (convtxt.contains("browser") || convtxt.contains("chrome") || convtxt.contains("search engine"))) {
            speak("opening Chrome....");
            chrome();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && (convtxt.contains("map") || convtxt.contains("maps"))) {
            speak("opening maps....");
            maps();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("playstore")) {
            speak("opening playstore....");
            playstore();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("instagram")) {
            speak("opening Instagram....");
            insta();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("snapchat")) {
            speak("opening Snapchat....");
            snap();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("linkedin")) {
            speak("opening Linkedin....");
            linkedin();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("twitter")) {
            speak("opening twitter....");
            twitter();
        } else if ((convtxt.contains("open") || convtxt.contains("start")) && convtxt.contains("my website")) {
            speak("opening your website dev sir....");
            browsera();

        } else {
            boolean b = convtxt.contains("you") && (convtxt.contains("doing") || convtxt.contains("do"));
            if (convtxt.contains("how")) {
                if (b) {
                    speak("i am good baby how you doin ?");
                } else if (convtxt.contains("mother")) {
                    speak("i don't have mother but than you for asking");
                } else if (convtxt.contains("father")) {
                    speak("i don't have father but than you for asking");
                } else if (convtxt.contains("are you")) {
                    speak("i am fine thank you for asking");
                } else if (convtxt.contains("film")) {
                    speak("film? hmmmm what is film ? ");
                }
            } else if (convtxt.contains("what")) {
                if (convtxt.contains("your") && convtxt.contains("name")) {
                    speak("my name is edith");
                } else if (convtxt.contains("do you do ")) {
                    speak("i am assistant i assist you with what ever you need ");
                } else if (convtxt.contains("what") && convtxt.contains("is")) {
                    speak("opening Wikipedia ");
                    String urldata = findh(convtxt);
                    newwiki(urldata);
                } else if (convtxt.contains("are you") && (!convtxt.contains("doing"))) {
                    speak("i am edith . i am an assistant");
                } else if (convtxt.contains("time")) {
                    time();
                } else if (convtxt.contains("date")) {
                    date();
                } else if (convtxt.contains("day")) {
                    day();
                } else if (convtxt.contains("year")) {
                    year();
                } else if (convtxt.contains("month")) {
                    month();
                } else if (convtxt.contains("film") || convtxt.contains("movie")) {
                    speak("film? what is film ? ");
                } else if (b) {
                    speak("i am good baby how you doin ?");
                }
            } else {
                speak("sorry what was that ? can you say that again please ?");

            }
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////other testing

//    public void open(View view) {
//
//
//
//        final PackageManager pm = getPackageManager();
//
//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//        for (ApplicationInfo packageInfo : packages) {
//////            all packages installed
//            if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
////            only those packages which return runnable intent
//                Log.d(TAG, "Installed package :" + packageInfo.packageName);
//                Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
//                Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName) + "\n");
//            }
////            if (packageInfo.packageName.contains("facebook")) {
////                Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
////                startActivity(intent);
////            }
//        }
//
//    }
/////////////////////////////////////////////////////////////////////////////////////////other testing

    ///////////////////////////////////////////////////////////////////////////////////////////open third-party apps
    public void opencam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    public void whatsapp() {
        boolean f = false;
        f = true;
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.contains("whatsapp")) {
                Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                startActivity(intent);

            }
        }
        if (!f) {
            speak("please install the app first");
        }
    }

    public void facebook() {
        openappsforyou("com.facebook.katana");
    }

    public void snap() {
        openappsforyou("com.snapchat.android");
    }

    public void insta() {
        openappsforyou("com.instagram.android");
    }

    public void twitter() {
        openappsforyou("com.twitter.android");
    }

    public void linkedin() {
        openappsforyou("com.linkedin.android");
    }

    public void gmail() {
        openappsforyou("com.google.android.gm");
    }

    public void gallery() {
        openappsforyou("com.google.android.apps.photos");
    }

    public void maps() {
        openappsforyou("com.google.android.apps.maps");
    }

    public void chrome() {
        openappsforyou("com.android.chrome");

    }

    public void drive() {
        openappsforyou("com.google.android.apps.docs");
    }

    public void playstore() {
        openappsforyou("com.android.vending");
    }

    public void google() {
        openappsforyou("com.google.android.googlequicksearchbox");
    }

    public void youtube() {
        openappsforyou("com.google.android.youtube");
    }

    public void spotify() {
        openappsforyou("com.spotify.music");
    }

    public void openclock() {
        openappsforyou("com.android.deskclock");
    }

    public void browsera() {
        Uri webpage = Uri.parse("https://developerguy.cf");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private void openappsforyou(String s) {

        final PackageManager pm = getPackageManager();
        boolean f = false;
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.matches(s)) {
                Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                startActivity(intent);
                f = true;
            }
        }
        if (!f) {
            speak("please install the app first");
        }

    }
/////////////////////////////////////////////////////////////////////////////////////////open third-party apps


//////////////////////////////////////////////////////////////////////////////////////////time and date

    private void time() {
        Date date = new Date();
        String time = DateUtils.formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
        speak("current time is" + time);
    }

    private void date() {
        SimpleDateFormat date = new SimpleDateFormat("dd MM yyyy");
        Calendar cal = Calendar.getInstance();
        String currenttime = date.format(cal.DATE);
        speak("current date is" + currenttime);
    }

    private void day() {
        SimpleDateFormat date = new SimpleDateFormat("dd mm yyyy");
        Calendar cal = Calendar.getInstance();
        String currenttime = date.format(cal.getTime());
        speak("current date is" + currenttime);
    }

    private void month() {
        SimpleDateFormat month = new SimpleDateFormat("MM");
        String currenttime = month.format(new Date());
        speak("current month is " + currenttime);
    }

    private void year() {
        SimpleDateFormat y = new SimpleDateFormat("yyyy");
        String currenttime = y.format(new Date());
        speak("current year is " + currenttime);
    }

    private void newwiki(String txt) {

        Uri webpage = Uri.parse("https://en.wikipedia.org/wiki/" + txt);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////time and date


    ////////////////////////////////////////////////////////////////////////////////////////////make calls
    private String recname(String msg) {
        String name = "";
        boolean flage = false;
        String[] data = msg.split(" ");
        for (int i = 0; i < data.length; i++) {
            String d = data[i];
            if (d.equals("call")) {
                if (data[(i + 1)].equals("to")) {
                    flage = false;
                } else {
                    flage = true;
                }
            } else if (d.equals("and") || d.equals(".")) {
                flage = false;
            } else if (data[(i - 1)].equals("call")) {
                if (d.equals("to")) {
                    flage = true;
                }
            }
            if (flage) {
                if (!d.equals("call") && !d.equals("to")) {
                    name = name.concat(" " + d);
                }
            }

        }
        Log.d("Name", name);
        return name;
    }

    private String findw(String msg) {
        String name = "";
        boolean flage = false;
        String[] data = msg.split(" ");
        for (int i = 0; i < data.length; i++) {
            String d = data[i];
            if (d.equals("who")) {
                if (data[(i + 1)].equals("is")) {
                    flage = false;
                } else {
                    flage = true;
                }
            } else if (d.equals("and") || d.equals(".")) {
                flage = false;
            } else if (data[(i - 1)].equals("who")) {
                if (d.equals("is")) {
                    flage = true;
                }
            }
            if (flage) {
                if (!d.equals("who") && !d.equals("is")) {
                    name = name.concat(" " + d);
                }
            }

        }
        Log.d("Name", name);
        return name;
    }

    private String findh(String msg) {
        String name = "";
        boolean flage = false;
        String[] data = msg.split(" ");
        for (int i = 0; i < data.length; i++) {
            String d = data[i];
            if (d.equals("what")) {
                if (data[(i + 1)].equals("is")) {
                    flage = false;
                } else {
                    flage = true;
                }
            } else if (d.equals("and") || d.equals(".")) {
                flage = false;
            } else if (data[(i - 1)].equals("what")) {
                if (d.equals("is")) {
                    flage = true;
                }
            }
            if (flage) {
                if (!d.equals("what") && !d.equals("is")) {
                    name = name.concat(" " + d);
                }
            }

        }
        Log.d("Name", name);
        return name;
    }

    private String getnum(String names) {
        names = names.trim();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE.toLowerCase()},
                "DISPLAY_NAME = '" + names + "'", null, null);
        cursor.moveToFirst();
        String s = cursor.getString(0);
        return s;
    }

    private void mkcall(String msgs) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.CALL_PHONE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();
        } else {
            try {
                final String callername = recname(msgs);
                Log.d("name", callername);
                System.out.println("1");
                String number = getnum(callername);
                System.out.println("2");
                String dial = "tel:" + number;
                speak("calling " + callername);
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } catch (Exception e) {
                speak("we are facing an issue. please recheck the contact details");
            }


        }
    }
////////////////////////////////////////////////////////////////////////////////////////////make calls

}