package com.example.listview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;

public class WorkoutPlaylistFragment extends Fragment{

    //Interface
    private MyInterface2 myInterface2;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myInterface2 = (MyInterface2) context;

    }

    //Bundle
    private ArrayList<String> mExerciseNameArray;
    private ArrayList<String> mExerciseRepArray;
    private ArrayList<String> mExerciseImageArray;
    private String mExerciseName;
    private String mExerciseReps;
    private int i = 0;

    //Views
    private TextView mTxtVwCountDown;
    private TextView mTxtVwExerciseName;
    private TextView mTxtVwExerciseReps;
    private Button mBtnComplete;
    private Button mBtnSkip;
    private Button mBtnAddTime;
    private ImageView mImgVwWorkout;

    //Timer
    private static final long mStartTime = 20000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeft = mStartTime;

    private View view;

    //Shared Preferences
    public static final String mSPWorkOutsCompleted = "text5";
    private static int mWorkoutCompletedCount;
    private boolean mWorkoutCompleted = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.workoutplaylist, container, false);

        //Views
        mTxtVwCountDown = view.findViewById(R.id.text_view_countdown);
        mTxtVwExerciseName = view.findViewById(R.id.text_view_exercise_name);
        mTxtVwExerciseReps = view.findViewById(R.id.text_view_exercise_reps);
        mBtnComplete = view.findViewById(R.id.button_complete);
        mBtnSkip = view.findViewById(R.id.button_skip);
        mBtnAddTime = view.findViewById(R.id.button_add);
        mImgVwWorkout = view.findViewById(R.id.gif_image_view_workout);

        //Bundle
        mExerciseNameArray = this.getArguments().getStringArrayList("exercises");
        mExerciseRepArray = this.getArguments().getStringArrayList("exerciseReps");
        mExerciseName = mExerciseNameArray.get(0);
        mExerciseReps = mExerciseRepArray.get(0);

        mTxtVwExerciseName.setText(mExerciseName);
        mTxtVwExerciseReps.setText(mExerciseReps);

        //Buttons
        mBtnSkip.setOnClickListener(v -> skipTimer());
        mBtnComplete.setOnClickListener(v -> completeSet());
        mBtnAddTime.setOnClickListener(v -> addTime());

        updateCountDownText();

        return view;
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeft, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                //Rest
                mBtnAddTime.setVisibility(View.INVISIBLE);
                mBtnSkip.setVisibility(View.INVISIBLE);
                mTxtVwCountDown.setVisibility(View.INVISIBLE);

                //Work out
                mBtnComplete.setVisibility(View.VISIBLE);
                mImgVwWorkout.setVisibility(View.VISIBLE);
                mTxtVwExerciseName.setVisibility(View.VISIBLE);
                mTxtVwExerciseReps.setVisibility(View.VISIBLE);
                view.setBackgroundColor(WHITE);
                mTimeLeft = mStartTime;
            }
        }.start();

        //Work out
        mTxtVwExerciseName.setVisibility(View.INVISIBLE);
        mTxtVwExerciseReps.setVisibility(View.INVISIBLE);
        mBtnComplete.setVisibility(View.INVISIBLE);
        mImgVwWorkout.setVisibility(View.INVISIBLE);

        //Rest
        mTxtVwCountDown.setVisibility(View.VISIBLE);
        mBtnSkip.setVisibility(View.VISIBLE);
        mBtnAddTime.setVisibility(View.VISIBLE);
        view.setBackgroundColor(BLUE);

        //Shared Pref
//        loadData();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeft /1000)/60;
        int seconds = (int) (mTimeLeft /1000)%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        mTxtVwCountDown.setText(timeLeftFormatted);
    }

    private void skipTimer() {
        resetTimer();
        pauseTimer();
        updateCountDownText();

        //Rest
        mTxtVwCountDown.setVisibility(View.INVISIBLE);
        mBtnAddTime.setVisibility(View.INVISIBLE);
        mBtnSkip.setVisibility(View.INVISIBLE);

        //Work out
        mBtnComplete.setVisibility(View.VISIBLE);
        mImgVwWorkout.setVisibility(View.VISIBLE);
        mTxtVwExerciseName.setVisibility(View.VISIBLE);
        mTxtVwExerciseReps.setVisibility(View.VISIBLE);
        view.setBackgroundColor(WHITE);
    }

    private void addTime() {
        pauseTimer();
        mTimeLeft = mTimeLeft + 20000;
        startTimer();
        updateCountDownText();
    }

    private void resetTimer() {
        mTimeLeft = mStartTime;
        updateCountDownText();
    }

    private void completeSet() {
        startTimer();

        i++;

        if(i < mExerciseNameArray.size()){
            mTxtVwExerciseName.setText(mExerciseNameArray.get(i));
            mTxtVwExerciseReps.setText(mExerciseRepArray.get(i));
        }

        if (i == mExerciseNameArray.size()) {
            pauseTimer();
            getActivity().getSupportFragmentManager().popBackStack();
            mWorkoutCompleted = true;

        }
    }

//    public void saveData(){
//        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(mSPWorkOutsCompleted, mWorkoutCompletedCount);
//        editor.apply();
//
//        Toast.makeText(getActivity(),"Workout Count Saved",Toast.LENGTH_SHORT).show();
//        return;
//    }
//    public void loadData() {
//        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
//        mWorkoutCompletedCount = sharedPreferences.getInt(mSPWorkOutsCompleted,0);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        i = 0;
        if (mWorkoutCompleted) {
//            mWorkoutCompletedCount = mWorkoutCompletedCount+1;
            mWorkoutCompleted = false;
            myInterface2.btnShow();
        }
//        pauseTimer();
        resetTimer();
//        saveData();
    }


}
