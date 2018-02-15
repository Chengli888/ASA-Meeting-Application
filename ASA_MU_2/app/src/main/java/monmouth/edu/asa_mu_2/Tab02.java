package monmouth.edu.asa_mu_2;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import monmouth.edu.asa_mu_2.Adapter.MyAdapter;
import monmouth.edu.asa_mu_2.Utils.ASAApplication;
import monmouth.edu.asa_mu_2.Utils.MyBean;

/**
 * Created by chengli on 2017/11/1.
 */

public class Tab02 extends Fragment {
    private RecyclerView rv;
    private EditText et;
    private Button btn;
    private Socket socket;
    private ArrayList<MyBean> list;
    private MyAdapter adapter;
    private String time="";
    private ASAApplication asaApplication;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        asaApplication=(ASAApplication)getActivity().getApplication();
        list= asaApplication.getList();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //list=asaApplication.getList();
        //final InputMethodManager imm = (InputMethodManager) asaApplication.getSystemService(getContext().INPUT_METHOD_SERVICE);
        adapter = new MyAdapter(getContext());
        final Handler handler = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

//                    socket = new Socket("192.168.1.111", 10010);
                    //socket.close();
//                    socket = new Socket("204.152.149.62" +
//                            "",8008);
                    socket = new Socket("opus.monmouth.edu" +
                            "",8008);
                    Log.d("this is thread", String.valueOf(socket));

                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, len);
                        // 发到主线程中 收到的数据
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = data;

                        handler.sendMessage(message);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        final View ContentView = inflater.inflate(R.layout.tab2, container, false);
        rv = (RecyclerView) ContentView.findViewById(R.id.rv);
        et = (EditText)ContentView. findViewById(R.id.et);
        btn = (Button) ContentView.findViewById(R.id.btn);
        //list = new ArrayList<>();

        adapter.setData(list);
        rv.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(manager);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data = et.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //设置日期格式
                            outputStream.write((socket.getLocalPort() + "//" + data + "//" + df.format(new Date())).getBytes("utf-8"));

                           // imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                et.setText("");
                InputMethodManager imm = (InputMethodManager) asaApplication.getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ContentView.getWindowToken(), 0);
            }
        });
        return ContentView;
    }
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int localPort = socket.getLocalPort();
                String[] split = ((String) msg.obj).split("//");
                if(time.equals(split[2])){
                    Log.d("this is thread","---------------++----------------");
                    Log.d("this is thread","repeat");
                }else {
                if (split[0].equals(localPort + "")) {
                    time=split[2];
                    MyBean bean = new MyBean(split[1],1,split[2],"My：");
                    list.add(bean);
                } else {
                    time=split[2];
                    MyBean bean = new MyBean(split[1],2,split[2],("From：" + split[0]));
                    list.add(bean);
                }}
                //lastdata = split[1];
                // 向适配器set数据
                adapter.setData(list);
                rv.setAdapter(adapter);
                LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rv.setLayoutManager(manager);
            }
        }
    }
}
