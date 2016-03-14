package dam.android.ui_fragment_example;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements MyFragmentOne.OnButtonClickListener, MyFragmentTwo.OnButtonClickListener, View.OnClickListener {

    private EditText etTextToFragments;
    private Button buttonSetTextToFragments;

    private int currentNewFragment = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        setUI();


        // lifecycle:
        // activity state is preserved, included fragments backstack
        // information other than content into views must be preserved before in Bundle savedInstanceState

        // only create fragments the first time
        if (savedInstanceState == null) {
            //setFragments(new MyFragmentOne(), new MyFragmentTwo());
            // fragment one is created using newInstance with arguments
            setFragments(MyFragmentOne.newInstance("HELLO FRAGMENTS"), new MyFragmentTwo());
        }
        else
            Toast.makeText(this,"ActivOnCreate-#Fragments IN BACKSTACK = " + getSupportFragmentManager().getBackStackEntryCount(), Toast.LENGTH_LONG).show();
    }

    private void setUI() {
        etTextToFragments = (EditText) findViewById(R.id.editTextToFragments);
        buttonSetTextToFragments = (Button) findViewById(R.id.buttonSetTextToFragments);
        buttonSetTextToFragments.setOnClickListener(this);

    }

    private void setFragments(Fragment one, Fragment two) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.add(R.id.frameOne, one, "ONE");  // no need tag if not going to find it by name to get again access to this transaction
        ft.add(R.id.frameTwo, two, "TWO");

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        // all added fragments live within activity and are active meanwhile activity is.
        ft.commit();
    }


    private void replaceFragments(Fragment one, Fragment two) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // replace --> removes (destroys) current fragment in container and then adds a new one
        // if added to backstack, then fragment is paused.
        // add --> adds a fragment to activity, the other fragments continue active, not paused.

        //replace all fragments in the container by the new ones, (if added to backstack then current fragments are paused)
        currentNewFragment++;
        ft.replace(R.id.frameOne, one, "NEW ONE_" + currentNewFragment);
        ft.replace(R.id.frameTwo, two, "NEW TWO_" + currentNewFragment);

        ft.addToBackStack(null);   // add transaction to backstack, just in case we want to go back (to implement user undo).
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            replaceFragments(MyFragmentOne.newInstance("HELLO FRAGMENTS!!!!"), new MyFragmentTwo());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // activity events
    @Override
    public void onClick(View v) {
        // set same text to both fragments
        setTextToFragment(R.id.frameOne, R.id.tvFragmentOne, etTextToFragments.getText().toString());
        setTextToFragment(R.id.frameTwo, R.id.tvFragmentTwo, etTextToFragments.getText().toString());
    }


    private void setTextToFragment(int frameId, int viewId, String text) {
        FragmentManager fm = getSupportFragmentManager();
        TextView tv;
        // try..catch just in case  fragment is missing
        try {
            // set text to fragment textView
            tv = (TextView) fm.findFragmentById(frameId).getView().findViewById(viewId);
            tv.setText(text);
            /////
            // it would be better to call a method in the fragment and do not access to the views directly!!
            /////
        } catch (Exception ex) {
            Snackbar.make(etTextToFragments, "Fragment missing", Snackbar.LENGTH_LONG).show();
        }
    }


    // events from fragments
    @Override
    public void onButtonClickToActivity(String text) {
        // set text from fragment
        etTextToFragments.setText(text);
    }

    @Override
    public void onButtonClickToFragmentOne(String text) {
       setTextToFragment(R.id.frameOne, R.id.tvFragmentOne, text);
    }

    @Override
    public void onButtonClickToFragmentTwo(String text) {
        setTextToFragment(R.id.frameTwo, R.id.tvFragmentTwo, text);
    }



}
