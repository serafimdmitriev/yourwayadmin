package com.serafimdmitrievstudio.yourwayadmin;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.JsonArray;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class FullActionsHandler {

    private ArrayList<FullAction> fullActions;
    private FullAction currentFullAction;

    private EditText streetNameEditText;

    private RadioButton showObjectsToDeleteRadioButton;
    private RadioButton showObjectsToAddRadioButton;

    private Button saveButton;
    private Button deleteButton;
    private Button reloadMapButton;

    private RelativeLayout noNewActions;

    FullActionsHandler(final Activity activity, LinearLayout actionsLinearLayout, JsonArray actionsJsonArray, final GoogleMap mMap, final Animation removeLeftward, final Animation shakeAnimation, final ServerApi serverApi) {

        showObjectsToDeleteRadioButton = (RadioButton) activity.findViewById(R.id.showObjectsToDeleteRadioButton);
        showObjectsToAddRadioButton = (RadioButton) activity.findViewById(R.id.showNewObjectsRadioButton);

        saveButton = (Button) activity.findViewById(R.id.saveActionButton);
        deleteButton = (Button) activity.findViewById(R.id.deleteActionButton);
        reloadMapButton = (Button) activity.findViewById(R.id.reloadMapButton);

        streetNameEditText = (EditText)  activity.findViewById(R.id.streetNameEditText);

        noNewActions = (RelativeLayout)  activity.findViewById(R.id.noNewActionsRelativeLayout);

        fullActions = new ArrayList<>();

        try {
            if (actionsJsonArray.size() != 0) {
                showObjectsToDeleteRadioButton.setVisibility(View.VISIBLE);
                showObjectsToAddRadioButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                reloadMapButton.setVisibility(View.VISIBLE);
                streetNameEditText.setVisibility(View.VISIBLE);

                reloadMapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMap.clear();
                        if(currentFullAction != null) {
                            currentFullAction.drawObjectsToAdd(mMap);
                            showObjectsToAddRadioButton.setChecked(true);
                            showObjectsToDeleteRadioButton.setChecked(false);
                        }
                        DatabaseHandler.loadDatabase(mMap);
                    }
                });

                noNewActions.setVisibility(View.GONE);

                long lastId = 0;
                long newId;
                for (int i = 0; i < actionsJsonArray.size(); i++) {

                    newId = actionsJsonArray.get(i).getAsJsonObject().get("actionId").getAsLong();

                    if (lastId != newId || newId == 0) {
                        FullAction action = new FullAction(actionsLinearLayout.getContext(), newId);
                        fullActions.add(action);
                        lastId = newId;

                        actionsLinearLayout.addView(action.getView());
                    }

                    try {
                        fullActions.get(fullActions.size() - 1).addPointToAdd(
                                actionsJsonArray.get(i).getAsJsonObject().get("pointToAddType").getAsShort()
                                , actionsJsonArray.get(i).getAsJsonObject().get("pointToAddLatitude").getAsDouble()
                                , actionsJsonArray.get(i).getAsJsonObject().get("pointToAddLongitude").getAsDouble()
                        );
                    } catch (Exception e) {
                        Log.write("NoPointToAdd");
                    }
                    try {
                        fullActions.get(fullActions.size() - 1).addSegmentToAdd(
                            actionsJsonArray.get(i).getAsJsonObject().get("segmentToAddType").getAsShort()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAddLatitude1").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAddLongitude1").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAddLatitude2").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAddLongitude2").getAsDouble()
                            , URLDecoder.decode(actionsJsonArray.get(i).getAsJsonObject().get("segmentToAddStreetName").getAsString(), "UTF-8")
                        );

                    } catch (Exception e) {
                        Log.write("NoSegmentToAdd");
                    }
                    try {
                        fullActions.get(fullActions.size() - 1).addSegmentToAdd(
                            actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd2Type").getAsShort()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd2Latitude1").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd2Longitude1").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd2Latitude2").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd2Longitude2").getAsDouble()
                            , ""
                        );
                    } catch (Exception e) {
                        Log.write("NoSegmentToAdd2");
                    }
                    try {
                        fullActions.get(fullActions.size() - 1).addSegmentToAdd(
                            actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd3Type").getAsShort()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd3Latitude1").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd3Longitude1").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd3Latitude2").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToAdd3Longitude2").getAsDouble()
                            , ""
                        );
                    } catch (Exception e) {
                        Log.write("NoSegmentToAdd3");
                    }
                    try {
                        fullActions.get(fullActions.size() - 1).addSegmentToDelete(
                            actionsJsonArray.get(i).getAsJsonObject().get("segmentToDeleteType").getAsShort()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToDeleteLatitude1").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToDeleteLongitude1").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToDeleteLatitude2").getAsDouble()
                            , actionsJsonArray.get(i).getAsJsonObject().get("segmentToDeleteLongitude2").getAsDouble()
                        );
                    } catch (Exception e) {
                        Log.write("NoSegmentToDelete");
                    }
                }

                for (int i = 0; i < fullActions.size(); i++) {
                    final FullAction action = fullActions.get(i);
                    action.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            currentFullAction = action;

                            closeAllActions(activity);
                            action.getView().setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
                            showObjectsToAddRadioButton.setChecked(true);
                            showObjectsToDeleteRadioButton.setChecked(false);
                            action.drawObjectsToAdd(mMap);

                            LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
                            int counter = 0;
                            for (LatLng latLng : action.getLatsAndLngs()) {
                                counter++;
                                latLngBuilder.include(latLng);
                            }
                            if (counter > 2)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), 90));

                            showObjectsToAddRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b) action.drawObjectsToAdd(mMap);
                                }
                            });

                            showObjectsToDeleteRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if (b) action.drawObjectsToDelete(mMap);
                                }
                            });
                            streetNameEditText.setText(action.getStreetName());
                        }
                    });
                }

                closeAllActions(activity);

            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptAction(true, removeLeftward, shakeAnimation, serverApi);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptAction(false, removeLeftward, shakeAnimation, serverApi);
                }
            });
        } catch (Exception e) {

            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }
    }

    private void checkLeftNewActions() {
        Log.write("10");
        boolean left = true;
        for (int i = 0; i < fullActions.size(); i++) {
            Log.write("11");
            if (fullActions.get(i).getView().getVisibility() != View.GONE) {
                Log.write("12");
                left = false;
            }
        }
        Log.write("13");
        if (left) {
            Log.write("14");
            noNewActions.setVisibility(View.VISIBLE);
            showObjectsToDeleteRadioButton.setVisibility(View.GONE);
            showObjectsToAddRadioButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            reloadMapButton.setVisibility(View.GONE);
            streetNameEditText.setVisibility(View.GONE);
        }
    }

    private void closeAllActions(Activity activity) {
        for (int i = 0; i < fullActions.size() ; i++) {
            fullActions.get(i).getView().setBackgroundColor(activity.getResources().getColor(R.color.colorLightGray));
        }
    }

    void removeDrownObjects() {
        for (FullAction action : fullActions) {
            action.removeDrownObjects();
        }
    }

    private void acceptAction(boolean accept, final Animation removeLeftward, final Animation shakeAnimation, ServerApi serverApi) {
        if (currentFullAction != null) {
            try {

                Log.write("01");
                if (streetNameEditText.getText().toString().contains(";") ||
                        streetNameEditText.getText().toString().contains("'")) {
                    streetNameEditText.startAnimation(shakeAnimation);

                    return;
                }

                if (streetNameEditText.getText() == null) streetNameEditText.setText("");

                Log.write("02");
                Call<ServerSimpleResponse> call = serverApi.acceptAction("acceptAction"
                        , currentFullAction.getActionId()
                        , URLEncoder.encode(streetNameEditText.getText().toString(), "UTF-8")
                        , accept);

                Log.write("03");
                call.enqueue(new Callback<ServerSimpleResponse>() {
                    @Override
                    public void onResponse(Call<ServerSimpleResponse> call, Response<ServerSimpleResponse> response) {

                        if (response.body().getResponse().equals("success")) {
                            Log.write("04");
                            currentFullAction.removeDrownObjects();
                            removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Log.write("06");
                                    currentFullAction.getView().setVisibility(View.GONE);
                                    currentFullAction = null;
                                    Log.write("07");
                                    checkLeftNewActions();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            currentFullAction.getView().startAnimation(removeLeftward);
                        } else {
                            Log.write("08");
                            Log.write(response.body().getResponse());
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerSimpleResponse> call, Throwable t) {
                        Log.write(call.toString());
                        Log.write(t.toString());
                    }
                });
            } catch (Exception e) {
                Log.write(e.getMessage());
                for (StackTraceElement el : e.getStackTrace()) {
                    Log.write(el.toString());
                }
            }
        }
    }
}
