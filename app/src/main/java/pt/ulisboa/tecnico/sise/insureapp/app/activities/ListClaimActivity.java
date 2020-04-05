package pt.ulisboa.tecnico.sise.insureapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import pt.ulisboa.tecnico.sise.insureapp.R;

public class ListClaimActivity extends AppCompatActivity {
    private ListView _listView;
    private Button _backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_claim);

        _backButton = (Button) findViewById(R.id.backButtonListView);
    }
}
