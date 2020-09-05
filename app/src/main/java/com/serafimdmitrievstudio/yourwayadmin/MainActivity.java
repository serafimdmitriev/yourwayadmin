package com.serafimdmitrievstudio.yourwayadmin;

//219815916695-pjbfnnpdn2ot1gr6ba4ucgp1fl8jo6td.apps.googleusercontent.com

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //private final static String G_PLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/plus.me";
    //private final static String USERINFO_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
    //private final static String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    //private final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE + " " + EMAIL_SCOPE;

    LinearLayout newAdministratorsLinearLayout;
    LinearLayout actionsLinearLayout;
    RelativeLayout actionsRelativeLayout;
    RelativeLayout authLayout;
    RelativeLayout newAdministratorsRelativeLayout;
    RelativeLayout selectorRelativeLayout;
    RelativeLayout authBadLayout;
    RelativeLayout registerLayout;
    RelativeLayout registrationEndLayout;
    RelativeLayout noConnectionLayout;
    RelativeLayout greetingLayout;
    RelativeLayout backgroundLayout;

    TextView registerTextView;
    TextView greetingTextView;

    Button openNewAdminsButton;
    Button openMapActionsButton;

    Animation removeLeftward;
    Animation appearLeftward;
    Animation shakeAnimation;
    Animation appearAndRemove;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    Integer CurrentViewGroupId;

    Retrofit retrofit;
    ServerApi serverApi;

    Activity activity;

    FullActionsHandler fullActionsHandler;
    NewAdminsHandler newAdminsHandler;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.initialize();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("219815916695-rjr3sbkf5np2r1g56qs4j5c3fap9r6t0.apps.googleusercontent.com")
                .requestId()
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();


        newAdministratorsLinearLayout  = (LinearLayout) findViewById(R.id.newAdministratorsLinearLayout);
        actionsLinearLayout = (LinearLayout) findViewById(R.id.actionsLinearLayout);
        actionsRelativeLayout  = (RelativeLayout) findViewById(R.id.actionsRelativeLayout);
        authLayout = (RelativeLayout) findViewById(R.id.authRelativeLayout);
        newAdministratorsRelativeLayout  = (RelativeLayout) findViewById(R.id.newAdministratorsRelativeLayout);
        selectorRelativeLayout = (RelativeLayout) findViewById(R.id.selectorRelativeLayout);
        authBadLayout = (RelativeLayout) findViewById(R.id.authBadRelativeLayout);
        registerLayout = (RelativeLayout) findViewById(R.id.registerRelativeLayout);
        registrationEndLayout = (RelativeLayout) findViewById(R.id.registrationEndRelativeLayout);
        noConnectionLayout = (RelativeLayout) findViewById(R.id.noInternetRelativeLayout);
        greetingLayout = (RelativeLayout) findViewById(R.id.greetingRelativeLayout);
        backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);

        registerTextView = (TextView) findViewById(R.id.registerTextView);
        greetingTextView = (TextView) findViewById(R.id.greetingTextView);

        openNewAdminsButton = (Button) findViewById(R.id.openNewAdminsButton);
        openMapActionsButton = (Button) findViewById(R.id.openMapActionsButton);

        removeLeftward = AnimationUtils.loadAnimation(this, R.anim.remove_leftward);
        appearLeftward = AnimationUtils.loadAnimation(this, R.anim.appear_leftward);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        appearAndRemove = AnimationUtils.loadAnimation(this, R.anim.appear_and_remove_greeting);

        CurrentViewGroupId = authLayout.getId();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://accesspassed.com:8080/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();

        serverApi = retrofit.create(ServerApi.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.write("1");
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), 101);
            }
        });

        openNewAdminsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //LinearLayout authLayout;
                        //LinearLayout authBadLayout;
                        //RelativeLayout registerLayout;
                        //RelativeLayout registrationEndLayout;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                //LinearLayout authLayout;
                                //LinearLayout authBadLayout;
                                //RelativeLayout registerLayout;
                                //RelativeLayout registrationEndLayout;
                                newAdministratorsRelativeLayout.setVisibility(View.VISIBLE);
                                CurrentViewGroupId = newAdministratorsRelativeLayout.getId();
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        newAdministratorsRelativeLayout.startAnimation(appearLeftward);

                        selectorRelativeLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                selectorRelativeLayout.startAnimation(removeLeftward);
            }
        });

        openMapActionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //LinearLayout authLayout;
                        //LinearLayout authBadLayout;
                        //RelativeLayout registerLayout;
                        //RelativeLayout registrationEndLayout;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                //LinearLayout authLayout;
                                //LinearLayout authBadLayout;
                                //RelativeLayout registerLayout;
                                //RelativeLayout registrationEndLayout;
                                actionsRelativeLayout.setVisibility(View.VISIBLE);
                                CurrentViewGroupId = actionsRelativeLayout.getId();
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        actionsRelativeLayout.startAnimation(appearLeftward);

                        selectorRelativeLayout.setVisibility(View.INVISIBLE);
                        backgroundLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                selectorRelativeLayout.startAnimation(removeLeftward);
                backgroundLayout.startAnimation(removeLeftward);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.write("2");
        if (requestCode == 101) {
            Log.write("3");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.write("4");
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);

            try {

                Call<ServerCheckAdminIdResponse> call = serverApi.isExist("checkAdminId",
                        "GOOGLE", account.getId());

                call.enqueue(new Callback<ServerCheckAdminIdResponse>() {
                    @Override
                    public void onResponse(Call<ServerCheckAdminIdResponse> call, Response<ServerCheckAdminIdResponse> response) {
                        try {
                            //Log.write(Integer.toString(response.body().getResponse().size()));

                            removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    //LinearLayout authLayout;
                                    //LinearLayout authBadLayout;
                                    //RelativeLayout registerLayout;
                                    //RelativeLayout registrationEndLayout;
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    authLayout.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            authLayout.startAnimation(removeLeftward);

                            if (response.body().getResponse().get("exist").getAsShort() == -1) {
                                appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        authBadLayout.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                authBadLayout.startAnimation(appearLeftward);

                                registerTextView.setText(account.getGivenName() + getGreeting() + "\n\n Unfortunately, your account hasn't " +
                                        "been found. If you want to be a part of YourWay team, click \"" +
                                        getApplicationContext().getResources().getString(R.string.registerButton) + "\"");

                                findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {
                                                //LinearLayout authLayout;
                                                //LinearLayout authBadLayout;
                                                //RelativeLayout registerLayout;
                                                //RelativeLayout registrationEndLayout;
                                            }

                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                authBadLayout.setVisibility(View.INVISIBLE);
                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });
                                        authBadLayout.startAnimation(removeLeftward);
                                        appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {
                                                registerLayout.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });
                                        registerLayout.startAnimation(appearLeftward);

                                        findViewById(R.id.sendRegisterButton).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                boolean editTextsFilled = true;
                                                if (((EditText) findViewById(R.id.contactsRegisterEditText)).getText().toString().length() < 3 ||
                                                        ((EditText) findViewById(R.id.contactsRegisterEditText)).getText().toString().contains(";") ||
                                                        ((EditText) findViewById(R.id.contactsRegisterEditText)).getText().toString().contains("'")) {
                                                    findViewById(R.id.contactsRegisterEditText).startAnimation(shakeAnimation);
                                                    editTextsFilled = false;
                                                }
                                                if (((EditText) findViewById(R.id.messageRegisterEditText)).getText().toString().length() < 10 ||
                                                        ((EditText) findViewById(R.id.messageRegisterEditText)).getText().toString().contains(";") ||
                                                        ((EditText) findViewById(R.id.messageRegisterEditText)).getText().toString().contains("'")) {
                                                    findViewById(R.id.messageRegisterEditText).startAnimation(shakeAnimation);
                                                    editTextsFilled = false;
                                                }

                                                if (editTextsFilled) {
                                                    try {
                                                        Call<ServerSimpleResponse> call = serverApi.requestNewAdministrator("newAdministratorRequest",
                                                                "GOOGLE"
                                                                , account.getId()
                                                                , account.getEmail()
                                                                , URLEncoder.encode(account.getDisplayName(), "UTF-8")
                                                                , URLEncoder.encode(((EditText) findViewById(R.id.contactsRegisterEditText)).getText().toString(), "UTF-8")
                                                                , URLEncoder.encode(((EditText) findViewById(R.id.messageRegisterEditText)).getText().toString(), "UTF-8")
                                                                , MyFirebaseInstanceIDService.getToken());

                                                        call.enqueue(new Callback<ServerSimpleResponse>() {
                                                            @Override
                                                            public void onResponse(Call<ServerSimpleResponse> call, Response<ServerSimpleResponse> response) {
                                                                Log.write(response.body().getResponse());
                                                                removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                                                                    @Override
                                                                    public void onAnimationStart(Animation animation) {
                                                                        //LinearLayout authLayout;
                                                                        //LinearLayout authBadLayout;
                                                                        //RelativeLayout registerLayout;
                                                                        //RelativeLayout registrationEndLayout;
                                                                    }

                                                                    @Override
                                                                    public void onAnimationEnd(Animation animation) {
                                                                        registerLayout.setVisibility(View.INVISIBLE);
                                                                    }

                                                                    @Override
                                                                    public void onAnimationRepeat(Animation animation) {

                                                                    }
                                                                });
                                                                registerLayout.startAnimation(removeLeftward);
                                                                appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                                                                    @Override
                                                                    public void onAnimationStart(Animation animation) {
                                                                        registrationEndLayout.setVisibility(View.VISIBLE);
                                                                    }

                                                                    @Override
                                                                    public void onAnimationEnd(Animation animation) {
                                                                    }

                                                                    @Override
                                                                    public void onAnimationRepeat(Animation animation) {

                                                                    }
                                                                });
                                                                registrationEndLayout.startAnimation(appearLeftward);

                                                                findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        finish();
                                                                    }
                                                                });

                                                            }

                                                            @Override
                                                            public void onFailure(Call<ServerSimpleResponse> call, Throwable t) {
                                                                Log.write("RETROFIT 9");
                                                                Log.write(call.toString());
                                                                Log.write(t.toString());
                                                            }
                                                        });
                                                    } catch (Exception e) {
                                                        Log.write("RETROFIT 10");
                                                        Log.write(e.getMessage());
                                                        for (StackTraceElement el : e.getStackTrace()) {
                                                            Log.write(el.toString());
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                            switch (response.body().getResponse().get("exist").getAsShort()) {
                                case 0: {
                                    Log.write(Short.toString(response.body().getResponse().get("exist").getAsShort()));
                                    openNewAdminsButton.setVisibility(View.VISIBLE);

                                    JsonArray newAdministratorsJsonArray = response.body().getResponse().get("newAdministrators").getAsJsonArray();
                                    Log.write(newAdministratorsJsonArray.toString());

                                    newAdminsHandler = new NewAdminsHandler(activity, newAdministratorsLinearLayout, newAdministratorsJsonArray, shakeAnimation, removeLeftward, serverApi);


                                }
                                case 1: {
                                    openMapActionsButton.setVisibility(View.VISIBLE);

                                    JsonArray actionsJsonArray = response.body().getResponse().get("actions").getAsJsonArray();
                                    Log.write(actionsJsonArray.toString());

                                    fullActionsHandler = new FullActionsHandler(activity, actionsLinearLayout, actionsJsonArray, mMap, removeLeftward, shakeAnimation, serverApi);


                                }
                                case 2: {
                                    appearAndRemove.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                            greetingLayout.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {

                                            appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                                                @Override
                                                public void onAnimationStart(Animation animation) {
                                                    selectorRelativeLayout.setVisibility(View.VISIBLE);
                                                    CurrentViewGroupId = selectorRelativeLayout.getId();
                                                }

                                                @Override
                                                public void onAnimationEnd(Animation animation) {
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animation animation) {

                                                }
                                            });
                                            selectorRelativeLayout.startAnimation(appearLeftward);

                                            greetingLayout.setVisibility(View.INVISIBLE);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    greetingTextView.setText(account.getGivenName() + getGreeting());
                                    greetingLayout.startAnimation(appearAndRemove);
                                }
                            }
                        } catch (Exception e) {
                            Log.write("RETROFIT 8");
                            Log.write(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerCheckAdminIdResponse> call, Throwable t) {
                        Log.write("RETROFIT 9");
                        Log.write(call.toString());
                        Log.write(t.toString());
                    }
                });
            } catch (Exception e) {
                Log.write("RETROFIT 10");
                Log.write(e.getMessage());
                for (StackTraceElement el : e.getStackTrace()) {
                    Log.write(el.toString());
                }
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //authLayout.startAnimation(removeToVoid);

            if (e.getStatusCode() == CommonStatusCodes.NETWORK_ERROR) { //user canceled request
                showNoConnectionLayout();
            }

            Log.write("signInResult:failed code=" + e.getStatusCode());
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DatabaseHandler.loadDatabase(mMap);
    }
    //<-- compile 'com.google.android.gms:play-services-auth:11.8.0' -->


    @Override
    protected void onStop() {
        super.onStop();
        try {
            mGoogleSignInClient.revokeAccess();
            Log.write("success deleted");
        } catch (Exception e) {
            Log.write(e.getMessage());
        }

    }

    String getGreeting() {
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting = "error";
        if (hour > 0 && hour <= 5 || hour > 23) {
            greeting = ", good night!";
        }
        if (hour > 5 && hour <= 10) {
            greeting = ", good morning!";
        }
        if (hour > 10 && hour <= 17) {
            greeting = ", good day!";
        }
        if (hour > 17 && hour <= 24) {
            greeting = ", good evening!";
        }

        return greeting;
    }

    void showNoConnectionLayout() {
        removeLeftward.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                authLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        authLayout.startAnimation(removeLeftward);
        appearLeftward.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                noConnectionLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        noConnectionLayout.startAnimation(appearLeftward);
        findViewById(R.id.exitButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (CurrentViewGroupId == selectorRelativeLayout.getId()) {
            removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    try {
                        FirebaseAuth.getInstance().signOut();
                        Log.write("success deleted 1");
                    } catch (Exception e) {
                        Log.write(e.getMessage());
                    }

                    try {
                        mGoogleSignInClient.revokeAccess();
                        Log.write("success deleted 2");
                    } catch (Exception e) {
                        Log.write(e.getMessage());
                    }

                    openNewAdminsButton.setVisibility(View.GONE);
                    openMapActionsButton.setVisibility(View.GONE);


                    selectorRelativeLayout.setVisibility(View.INVISIBLE);

                    appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            authLayout.setVisibility(View.VISIBLE);
                            CurrentViewGroupId = authLayout.getId();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    authLayout.startAnimation(appearLeftward);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            selectorRelativeLayout.startAnimation(removeLeftward);
        }

        if (CurrentViewGroupId == newAdministratorsRelativeLayout.getId()) {
            removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    newAdministratorsRelativeLayout.setVisibility(View.INVISIBLE);

                    appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            selectorRelativeLayout.setVisibility(View.VISIBLE);
                            CurrentViewGroupId = selectorRelativeLayout.getId();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    selectorRelativeLayout.startAnimation(appearLeftward);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            newAdministratorsRelativeLayout.startAnimation(removeLeftward);
        }
        if (CurrentViewGroupId == actionsRelativeLayout.getId()) {
            if (fullActionsHandler != null) fullActionsHandler.removeDrownObjects();

            removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    actionsRelativeLayout.setVisibility(View.INVISIBLE);

                    appearLeftward.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            selectorRelativeLayout.setVisibility(View.VISIBLE);
                            backgroundLayout.setVisibility(View.VISIBLE);
                            CurrentViewGroupId = selectorRelativeLayout.getId();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    selectorRelativeLayout.startAnimation(appearLeftward);
                    backgroundLayout.startAnimation(appearLeftward);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            actionsRelativeLayout.startAnimation(removeLeftward);

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            MyFirebaseInstanceIDService.setToken(FirebaseInstanceId.getInstance().getToken());

                            Log.write(user.getEmail());
                            Log.write(user.getDisplayName());
                            Log.write(FirebaseInstanceId.getInstance().getToken());

                        } else {
                            Log.write("GOVNO && ZALUPA");

                        }
                    }
                });
    }



}

