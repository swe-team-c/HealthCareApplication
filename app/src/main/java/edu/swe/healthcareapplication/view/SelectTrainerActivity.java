package edu.swe.healthcareapplication.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import edu.swe.healthcareapplication.R;
import edu.swe.healthcareapplication.model.ChatChannel;
import edu.swe.healthcareapplication.model.Trainer;
import edu.swe.healthcareapplication.view.adapter.SelectTrainerAdapter;
import edu.swe.healthcareapplication.view.listener.OnItemSelectListener;
import java.util.ArrayList;
import java.util.List;

public class SelectTrainerActivity extends AppCompatActivity implements
    OnItemSelectListener<Pair<String, Trainer>> {

  private static final String TAG = SelectTrainerActivity.class.getSimpleName();

  private RecyclerView mTrainerList;
  private SelectTrainerAdapter mAdapter;
  private FirebaseDatabase mDatabase;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_trainer);
    mDatabase = FirebaseDatabase.getInstance();
    initView();
    readTrainers();
  }

  private void initView() {
    mAdapter = new SelectTrainerAdapter();
    mAdapter.setOnItemSelectListener(this);
    mTrainerList = findViewById(R.id.trainer_list);
    mTrainerList.setHasFixedSize(true);
    mTrainerList.setLayoutManager(new LinearLayoutManager(this));
    mTrainerList.setAdapter(mAdapter);
  }

  private void readTrainers() {
    DatabaseReference reference = mDatabase.getReference().child("trainers");
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> trainersSnapshot = dataSnapshot.getChildren();
        List<Pair<String, Trainer>> trainerList = new ArrayList<>();
        for (DataSnapshot trainerSnapshot : trainersSnapshot) {
          String trainerId = trainerSnapshot.getKey();
          Trainer trainer = trainerSnapshot.getValue(Trainer.class);
          trainerList.add(new Pair<>(trainerId, trainer));
        }
        mAdapter.setTrainerList(trainerList);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Log.e(TAG, "onCancelled: " + databaseError.toString());
      }
    });
  }

  private void writeChatChannel(String trainerId) {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    if (currentUser != null) {
      String userUid = currentUser.getUid();
      ChatChannel channel = new ChatChannel(userUid, trainerId);
      DatabaseReference reference = mDatabase.getReference().child("chat_channel");
      reference.push().setValue(channel);
    }
  }

  @Override
  public void onItemSelected(Pair<String, Trainer> trainerPair) {
    Trainer trainer = trainerPair.second;
    Toast.makeText(this, trainer.name + " selected", Toast.LENGTH_SHORT).show();
    writeChatChannel(trainerPair.first);
  }
}
