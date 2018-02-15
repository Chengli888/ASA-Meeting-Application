package monmouth.edu.asa_mu_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.List;

import monmouth.edu.asa_mu_2.Sql.DatabaseHelper;
import monmouth.edu.asa_mu_2.po.Meeting;

/**
 * Created by chengli on 2017/11/1.
 */

public class Tab04 extends Fragment {
    private List<Meeting> Interesting_Meeting_list = new ArrayList<>(100);
    private LocalBroadcastManager broadcastManager;
    private SlideAndDragListView listView;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity mActivity = (MainActivity) getActivity();
        //注册广播
        registerReceiver();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View ContentView = inflater.inflate(R.layout.tab3, container, false);inflater.inflate(R.layout.tab4, container, false);
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Interesting_Meeting_list.clear();
        listView = ContentView.findViewById(R.id.listview);
        Cursor cursor = db.query("imeet", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String session = cursor.getString(cursor.getColumnIndex("session"));
            Meeting meeting = new Meeting();
            meeting.setId(id);
            meeting.setDATE(date);
            meeting.setTIME(time);
            meeting.setSESSION(session);
            Interesting_Meeting_list.add(meeting);
        }

        menu = new Menu(false, 0);

        menu.addItem(new MenuItem.Builder().setWidth(500)
                .setBackground(new ColorDrawable(Color.RED))
                .setDirection(MenuItem.DIRECTION_RIGHT)//set direction (default DIRECTION_LEFT)
                .setText("remove from tab")
                .build());
//set in sdlv
        listView.setMenu(menu);
        listView.setAdapter(new Myadapter());
        db.close();
        listView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                Interesting_Meeting_list.remove(itemPosition);
                db.execSQL("delete from imeet where 1=1");
                for(int i = 0;i<Interesting_Meeting_list.size();i++) {
                    db.execSQL("insert into imeet(id,date,time,session) values(?,?,?,?)", new Object[]{
                            Interesting_Meeting_list.get(i).getId(),
                            Interesting_Meeting_list.get(i).getDATE(),Interesting_Meeting_list.get(i).getTIME(),
                            Interesting_Meeting_list.get(i).getSESSION()
                    });
                }
                Interesting_Meeting_list.clear();
                Cursor cursor = db.query("imeet", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String session = cursor.getString(cursor.getColumnIndex("session"));
                    Meeting meeting = new Meeting();
                    meeting.setDATE(date);
                    meeting.setTIME(time);
                    meeting.setSESSION(session);
                    Interesting_Meeting_list.add(meeting);
                }

                menu = new Menu(false, 0);

                menu.addItem(new MenuItem.Builder().setWidth(500)
                        .setBackground(new ColorDrawable(Color.RED))
                        .setDirection(MenuItem.DIRECTION_RIGHT)//set direction (default DIRECTION_LEFT)
                        .setText("remove from tab")
                        .build());
//set in sdlv
                listView.setMenu(menu);
                listView.setAdapter(new Myadapter());

                db.close();
                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
            }
        });

        return ContentView;
    }
    private void registerReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refresh");
        broadcastManager.registerReceiver(MeetingReceiver, intentFilter);
    }
    private BroadcastReceiver MeetingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("change");

            if ("yes".equals(change)) {
                // 这地方只能在主线程中刷新UI,子线程中无效，因此用Handler来实现
                new Handler().post(new Runnable() {
                    public void run() {
                        //在这里来写你需要刷新的地方
                        //例如：testView.setText("恭喜你成功了");
                        Interesting_Meeting_list.clear();
                        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        Cursor cursor = db.query("imeet", null, null, null, null, null, null);
                        while (cursor.moveToNext()) {
                            int id  = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                            String date = cursor.getString(cursor.getColumnIndex("date"));
                            String time = cursor.getString(cursor.getColumnIndex("time"));
                            String session = cursor.getString(cursor.getColumnIndex("session"));
                            Meeting meeting = new Meeting();
                            meeting.setId(id);
                            meeting.setDATE(date);
                            meeting.setTIME(time);
                            meeting.setSESSION(session);
                            Interesting_Meeting_list.add(meeting);
                        }

                        menu = new Menu(false, 0);

                        menu.addItem(new MenuItem.Builder().setWidth(500)
                                .setBackground(new ColorDrawable(Color.RED))
                                .setDirection(MenuItem.DIRECTION_RIGHT)//set direction (default DIRECTION_LEFT)
                                .setText("remove from tab")
                                .build());
//set in sdlv
                        listView.setMenu(menu);
                        listView.setAdapter(new Myadapter());
                        db.close();
                    }
                });
            }
        }
    };

    public class Myadapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Interesting_Meeting_list.size();
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
            Date.setText(Interesting_Meeting_list.get(i).getDATE());
            Session.setText(Interesting_Meeting_list.get(i).getSESSION());
            Time.setText(Interesting_Meeting_list.get(i).getTIME());
            return view;
        }
    }
}
