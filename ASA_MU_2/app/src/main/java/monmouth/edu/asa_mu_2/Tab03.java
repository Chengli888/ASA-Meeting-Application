package monmouth.edu.asa_mu_2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.performance.slidedeletelistview.SlideDeleteListView;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import monmouth.edu.asa_mu_2.Sql.DatabaseHelper;
import monmouth.edu.asa_mu_2.Utils.MeetingUtils;
import monmouth.edu.asa_mu_2.po.Json_Meeting;
import monmouth.edu.asa_mu_2.po.Meeting;

/**
 * Created by chengli on 2017/11/1.
 */

public class Tab03 extends Fragment{
    private static final String TAG = "Tab03Activity";
    private Json_Meeting fromJson;
    private List<Meeting>  Interesting_Meeting_list  = new ArrayList<Meeting>(100);;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View ContentView = inflater.inflate(R.layout.tab3, container, false);
        meetingdatas();
        final SlideAndDragListView listView = ContentView.findViewById(R.id.listview);
        Menu menu = new Menu(false, 0);

        menu.addItem(new MenuItem.Builder().setWidth(500)
                .setBackground(new ColorDrawable(Color.RED))
                .setDirection(MenuItem.DIRECTION_RIGHT)//set direction (default DIRECTION_LEFT)
                .setText("add to event tab")
                .build());
//set in sdlv

        listView.setMenu(menu);
        final Myadapter myadapter = new Myadapter();
        listView.setAdapter(myadapter);


        listView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                Log.d("fnejrbfuerbg", String.valueOf(itemPosition));
                int id=-1;
//                Log.d("fnejrbfuerbg", Interesting_Meeting_list.get(itemPosition).getDATE());
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                final SQLiteDatabase db = databaseHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from imeet where id=?",new String[]{itemPosition+""});
                while (cursor.moveToNext()){
                    id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                    Log.d("id", String.valueOf(id));
                }
                if (id==-1){
                    db.execSQL("insert into imeet(id,date,time,session) values(?,?,?,?)",new Object[]{
                            Interesting_Meeting_list.get(itemPosition).getId(),
                            Interesting_Meeting_list.get(itemPosition).getDATE(),Interesting_Meeting_list.get(itemPosition).getTIME(),
                            Interesting_Meeting_list.get(itemPosition).getSESSION()
                    });
                    Log.d("id+++", String.valueOf(Interesting_Meeting_list.get(itemPosition).getId()));
                    Intent intent = new Intent("refresh");
                    intent.putExtra("change", "yes");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                }
                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
            }
        });


        return ContentView;
    }
    private void meetingdatas() {
        try {
            // 调用
            MeetingUtils meetingUtils = new MeetingUtils();
            String json = meetingUtils.getJson("meeting",getContext());
            System.out.print(json);
            Log.d("json",json);
            Gson gson = new Gson();
            fromJson = gson.fromJson(json, Json_Meeting.class);
            for(int i=0;i<fromJson.getMeeting().size();i++){
                Meeting meeting  =new Meeting();
                meeting.setId(i);
                meeting.setDATE(fromJson.getMeeting().get(i).getDATE());
                meeting.setSESSION(fromJson.getMeeting().get(i).getSESSION());
                meeting.setTIME(fromJson.getMeeting().get(i).getTIME());
                Interesting_Meeting_list.add(meeting);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class Myadapter extends BaseAdapter {


        @Override
        public int getCount() {
            return fromJson.getMeeting().size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getContext(), R.layout.meeting_adapter, null);
            }
            TextView Date = (TextView) view.findViewById(R.id.Date);
            TextView Session = (TextView) view.findViewById(R.id.Session);
            TextView Time = (TextView) view.findViewById(R.id.Time);
            Date.setText(fromJson.getMeeting().get(i).getDATE());
            Session.setText(fromJson.getMeeting().get(i).getSESSION());
            Time.setText(fromJson.getMeeting().get(i).getTIME());
            return view;
        }
    }
}
