package dam.android.ui_fragment_example;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by empar
 * THIS FRAGMENT GET ARGUMENTS WHEN CREATED!!!!
 */
public class MyFragmentOne extends Fragment implements View.OnClickListener {

    private Button toActivity;
    private Button toFragment;
    private OnButtonClickListener buttonActivityListener;

    private Button buttonTextToActivity;
    private Button buttonTextToFragment;
    private EditText etText;

    private int totalButtonCLicks = 0;
    private String helloString = "NO MESSAGE";
    TextView tv;



    // interface per a què l'activity la implemente
    public interface OnButtonClickListener {
        public void onButtonClickToActivity(String text);
        public void onButtonClickToFragmentTwo(String text);
    }

    public MyFragmentOne() {

    }


    // STATIC METHOD TO SEND ARGUMENTS TO ACTIVITY
    public static MyFragmentOne newInstance(String helloMessage) {
        MyFragmentOne fragment = new MyFragmentOne();

        Bundle args = new Bundle();
        args.putString("Hello", helloMessage);
        // set immediately arguments before fragment is attached to activity
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            // get activity
            buttonActivityListener = (OnButtonClickListener) context;

        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnButtonClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // the first time fragment is created, get arguments
            helloString = getArguments().getString("Hello");
            Toast.makeText(getActivity(), "onCreate " + getTag() + " hello=" + helloString, Toast.LENGTH_LONG).show();
        }
        else {  // this code  is necessary when fragment was in backstack when destroyed
            totalButtonCLicks = savedInstanceState.getInt("totalButtonClicks");
            helloString = savedInstanceState.getString("Hello");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // lifecycle: show data if saved previously
        if (savedInstanceState != null) {
            totalButtonCLicks = savedInstanceState.getInt("totalButtonClicks");
            helloString = savedInstanceState.getString("Hello");
            //Toast.makeText(getActivity(), "onCreatedView-" + getTag() + "n=" + totalButtonCLicks, Toast.LENGTH_LONG).show();
       }
       return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        setUI();
    }

    private void setUI() {
        etText = (EditText) getView().findViewById(R.id.etFragmentOne);
        buttonTextToActivity = (Button) getView().findViewById(R.id.buttonFragmentOneToActivity);
        buttonTextToActivity.setOnClickListener(this);
        buttonTextToFragment = (Button) getView().findViewById(R.id.buttonFragmentOneToFragmentTwo);
        buttonTextToFragment.setOnClickListener(this);

        tv = ((TextView)getView().findViewById(R.id.tvFragmentOne));

        tv.setText(helloString);

    }

    @Override
    public void onClick(View v) {
        // call to callback activity method
        switch (v.getId()) {
            case R.id.buttonFragmentOneToActivity:
                buttonActivityListener.onButtonClickToActivity(etText.getText().toString());
                break;
            case R.id.buttonFragmentOneToFragmentTwo:
                buttonActivityListener.onButtonClickToFragmentTwo(etText.getText().toString());
        }

        totalButtonCLicks++;

        tv.setText(" Total Button Clicks: " + totalButtonCLicks);
    }

    @Override
    public void onPause() {
        super.onPause();
        // set current string, unless, the value contained is the initial when the fragment was created
        helloString = tv.getText().toString();
    }

    //lifecycle: save fragment data
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save fragment data

        outState.putInt("totalButtonClicks", totalButtonCLicks);
        outState.putString("Hello", helloString);
        Toast.makeText(getActivity(), "saveInstaceState " + getTag(), Toast.LENGTH_LONG).show();

        // we could also save data to arguments as they are preserved always (also if destroyed)
        //getArguments().putString("Hello", helloString);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        Toast.makeText(getActivity(), "onDESTROY-" + getTag(), Toast.LENGTH_LONG).show();
    }
}
