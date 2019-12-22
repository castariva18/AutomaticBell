package org.ganymede.automaticbell;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ganymede.automaticbell.Model.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<History> listHistory;
    private ValueEventListener mDBListener;
    private HistoryAdapter adapter;

    int distance;
    TextView tvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.rv_keterangan);
        tvTime = (TextView) findViewById(R.id.tv_time);
        listHistory = new ArrayList<>();
        adapter = new HistoryAdapter(listHistory);
        LinearLayoutManager lm = new LinearLayoutManager(HistoryActivity.this);

        DividerItemDecoration itemDecor = new DividerItemDecoration(HistoryActivity.this, DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        updateDistance();

    }
    private void updateDistance() {
        reference= FirebaseDatabase.getInstance().getReference().child("histori");
        mDBListener = reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listHistory.clear();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                History history = postSnapshot.getValue(History.class);
                listHistory.add(history);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.hapus_item_keranjang:

                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this)
                        .setMessage("Hapus semua histori??")
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference= FirebaseDatabase.getInstance().getReference().child("histori");
                                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(HistoryActivity.this, "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                builder.create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
