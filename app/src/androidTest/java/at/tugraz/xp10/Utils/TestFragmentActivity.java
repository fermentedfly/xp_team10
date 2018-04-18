package at.tugraz.xp10.Utils;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import at.tugraz.xp10.R;
import at.tugraz.xp10.fragments.ListViewFragment;
import at.tugraz.xp10.fragments.TestFragment;

@RestrictTo(RestrictTo.Scope.TESTS)
public class TestFragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ListViewFragment.OnFragmentInteractionListener, TestFragment.OnFragmentInteractionListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content = new FrameLayout(this);
        content.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        content.setId(R.id.content_frame);

        Toolbar toolbar = new Toolbar(content.getContext());
        setSupportActionBar(toolbar);

        setContentView(content);
    }

    public void setFragment(Fragment fragment) {
        /*
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragment, "TEST")
                .commit();
                */

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment,fragment.getTag());
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
