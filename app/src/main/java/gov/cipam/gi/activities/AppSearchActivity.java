package gov.cipam.gi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import gov.cipam.gi.R;

public class AppSearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_search);

        Toast.makeText(this, "Inside Search Activity now !!", Toast.LENGTH_SHORT).show();

        setUpToolbar(this);
    }

    @Override
    protected int getToolbarID() {
        return R.id.app_search_list_toolbar;
    }
}
