package org.ganymede.automaticbell;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.library.PulseView;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import org.ganymede.automaticbell.Model.History;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    private FloatingActionButton fabHistory;
    PulseView pulseView;
    Vibrator vibrator;
    private Button mButton;

    TextView tvTime, tvStatus;
    private Boolean isFirst;


    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onStop() {
        super.onStop();
        isFirst = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFirst = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        tvTime = findViewById(R.id.tv_time);
        tvStatus = findViewById(R.id.tv_status);

        fabHistory = (FloatingActionButton) findViewById(R.id.fab_history);
        mButton = findViewById(R.id.button);

        final NotificationHelper notificationHelper = new NotificationHelper(MainActivity.this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("status");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("getaran");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue().toString();
                if (status.equals("off")){
                    mButton.setText("Nyalakan");
                    mButton.setBackground(getDrawable(R.drawable.curve_blue));
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReference.setValue("on");
                        }
                    });
                    vibrator.cancel();
//                    pulseView.finishPulse();

                }
                else if (status.equals("on")){
                    mButton.setText("Matikan");
                    mButton.setBackground(getDrawable(R.drawable.half_curve));
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReference.setValue("off");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status1 = dataSnapshot.getValue().toString();
                if (status1.equals("on")){
                    pulseView = (PulseView) findViewById(R.id.pv);

                    notificationHelper.notify("Automatic Bell", "[NOTICE] Anda kedatangan tamu", true);
                    long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
                    pulseView.startPulse();
                    vibrator.vibrate(pattern,0);
                    pulseView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (pulseView.isClickable()){
                                pulseView.finishPulse();
                                saveTime();
                                vibrator.cancel();
                                databaseReference2.setValue("off");
                            }else {
                                Toast.makeText(MainActivity.this,"Something is missing",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
//                if (dataSnapshot.getValue().toString().isEmpty()) {
//                    Toast.makeText(MainActivity.this,"Data kosong",Toast.LENGTH_SHORT).show();
//                    }else {
//                    pulseView = (PulseView) findViewById(R.id.pv);
//                    long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
//                    pulseView.startPulse();
//                    vibrator.vibrate(pattern, 0);
//                    notificationHelper.notify("Automatic Bell", "[NOTICE] Anda kedatangan tamu", true);
//
//                }
//                    pulseView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (pulseView.isClickable()) {
//                                pulseView.finishPulse();
//                                saveTime();
//                                vibrator.cancel();
//                            } else {
//                                Toast.makeText(MainActivity.this, "Something is missing", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fabHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(MainActivity.this,HistoryActivity.class);

                startActivity(history);
            }
        });
    }

    private void saveTime() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("histori");
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);
        String tanggal = sdf1.format(c1.getTime());
        String dataID = mDatabaseRef.push().getKey();
        History upload = new History(tanggal);

        mDatabaseRef.child(dataID).setValue(upload);
    }

}
