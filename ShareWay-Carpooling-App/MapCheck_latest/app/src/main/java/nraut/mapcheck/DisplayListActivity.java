package nraut.mapcheck;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naina on 5/11/2016.
 */
public class DisplayListActivity extends AppCompatActivity {
    private List<ListRowStruct> availableList;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display);

//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.availableList);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(manager);
//
//        availableList = new ArrayList<ListRowStruct>();
//        adapter = new Adapter(availableList);
//        recyclerView.setAdapter(adapter);
//        String defaultPic = "defaultpic.png";
//        availableList.add(new ListRowStruct(defaultPic,"Naina","Santa Clara","San Fransisco"));//Drawable profile_pic, String personName, String origin, String destination
//
//        TouchHelperCallback callback = new TouchHelperCallback(adapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(recyclerView);
    }
}
