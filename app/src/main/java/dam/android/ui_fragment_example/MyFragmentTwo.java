package dam.android.ui_fragment_example;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyFragmentTwo extends Fragment implements View.OnClickListener {

    private Button toActivity;
    private Button toFragment;
    private OnButtonClickListener buttonActivityListener;

    private TextView tv;
    private Button buttonTextToActivity;
    private Button buttonTextToFragment;
    private EditText etText;

    private int numberButtonClicks = 0;

    public MyFragmentTwo() {
        // works always, to preserve all fragment state (no need to use savedinstancestate
        setRetainInstance(true);   // fragment instance is not destroyed when activity recreated
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            buttonActivityListener = (OnButtonClickListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnButtonClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        setUI();
    }

    private void setUI() {
        etText = (EditText) getView().findViewById(R.id.etFragmentTwo);
        buttonTextToActivity = (Button) getView().findViewById(R.id.buttonFragmentTwoToActivity);
        buttonTextToActivity.setOnClickListener(this);
        buttonTextToFragment = (Button) getView().findViewById(R.id.buttonFragmentTwoToFragmentOne);
        buttonTextToFragment.setOnClickListener(this);
        tv = (TextView)getView().findViewById(R.id.tvFragmentTwo);
        tv.setText("# button clicks = " + numberButtonClicks);
    }

    @Override
    public void onClick(View v) {
        // call to callback activity method
        switch (v.getId()) {
            case R.id.buttonFragmentTwoToActivity:
                buttonActivityListener.onButtonClickToActivity(etText.getText().toString());
                break;
            case R.id.buttonFragmentTwoToFragmentOne:
                buttonActivityListener.onButtonClickToFragmentOne(etText.getText().toString());
        }
        numberButtonClicks++;
        tv.setText("# button clicks = " + numberButtonClicks);
    }

    // interface that activity must implement
    public interface OnButtonClickListener {
        public void onButtonClickToActivity(String text);
        public void onButtonClickToFragmentOne(String text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getActivity(), "onDESTROY-" + getTag(), Toast.LENGTH_LONG).show();
    }


}
