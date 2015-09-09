package com.cheheihome;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.cheheihome.supercalendar.R;
import com.cheheihome.views.SuperGridView;

import java.util.ArrayList;
import java.util.Calendar;

import lecalendar.model.DayModel;
import lecalendar.views.DayView;

/**
 * Created by chanlevel on 15/9/8.
 */
public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    SuperGridView gridView;
    DayView dayView;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (SuperGridView) findViewById(R.id.gridView);
        dayView = (DayView) findViewById(R.id.dayView);
        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayModel dayModel = days.get(10);
                dayModel.setRoom_num(dayModel.getRoom_num() > 0 ? 0 : 1);

                DayView daycell = (DayView) gridView.getChildAt(10);
                daycell.setStatus(flag = !flag);
                // dayView.setDayModel(dayModel);

            }
        });
        setGridView();

        setGrid();
    }

    private void setGrid() {

        gridView.setSuperCallBack(new SuperGridView.SuperCallBack() {
            @Override
            public void onDown(int postion) {

                DayModel startModel = (DayModel) gridView.getAdapter().getItem(postion);
                startModel.setRoom_num(startModel.getRoom_num() == 0 ? 1 : 0);
                open = startModel.getRoom_num() != 0;

                //       DayView dayView = (DayView) gridView .getChildAt(postion - gridView.getFirstVisiblePosition());
                // dayView.setDayModel(startModel);
            }

            @Override
            public void onMove(int startpostion, int endpostion) {
                Log.d("super-callback", "onMove:start:" + startpostion + "--end:" + endpostion);
                update(startpostion, endpostion, false);

            }

            @Override
            public void onUp(int startpostion, int endpostion) {

                Log.d("super-callback", "onUp:start:" + startpostion + "--end:" + endpostion);
                update(startpostion, endpostion, true);

            }
        });
    }

    boolean open;
    int tempB, tempE;

    private void update(int startpostion, int endpostion, boolean update) {

        //  if (tempB == startpostion && tempE == endpostion) return;
        Log.d("super-upadte", startpostion + "---" + endpostion);
        tempB = startpostion;
        tempE = endpostion;

        if (startpostion > endpostion) {
            int t = startpostion;
            startpostion = endpostion;
            endpostion = t;
        }

        for (int i = 0; gridView.getFirstVisiblePosition() <= i && i < gridView.getLastVisiblePosition(); i++) {
            DayView dayView = (DayView) gridView.getChildAt(i - gridView.getFirstVisiblePosition());

            if (i >= startpostion && i <= endpostion) {
                //    int color = open ? Color.GRAY : Color.WHITE;
                if (dayView != null && dayView.getOpen() != open) dayView.setStatus(open);
            } else {
                Log.d("SUPER-reset", i + "");
                DayModel dayModel = days.get(i);
                // dayView.setOpen(dayModel.getRoom_num()>0);
                Log.d("SUPER-reset", i + "--room_num:" + dayModel.getRoom_num());
                dayView.setStatus(dayModel.getRoom_num() > 0);
                // dayView.invalidate();

            }

        }
        if (update) {
            for (int i = startpostion; i <= endpostion; i++) {
                // DayView dayView = (DayView) gridView.getChildAt(i - gridView.getFirstVisiblePosition());

                //    int color = open ? Color.GRAY : Color.WHITE;
                // if (dayView != null) dayView.setOpen(open);
                //  Log.d("SUPER-get", i + "");

                days.get(i).setRoom_num(open ? 1 : 0);
                // dayView.setDayModel(dayModel);
                //  (days.get(i)).setRoom_num(open ? 1 : 0);
            }
          //  adapter.notifyDataSetChanged();

        }
    }


    void getDates(Calendar begin, Calendar end) {
        Calendar temp = begin;
        // if (this.days == null) this.days = ArrayList<DayModel>();
        while (temp.before(end)) {
            DayModel dayModel = new DayModel(false, false, null, 0, 0, null, temp.getTime(), false, false, false, false);

            dayModel.setRoom_num((int) (Math.random() * 5));
            dayModel.setPrice(dayModel.getRoom_num());
            dayModel.init();
            this.days.add(dayModel);

            temp.add(Calendar.DATE, 1);
        }


    }

    ArrayList<DayModel> days = new ArrayList<>();
    MAapter adapter;

    private void setGridView() {
        Calendar b = Calendar.getInstance();
        b.add(Calendar.MONTH, -1);
        b.set(Calendar.DAY_OF_WEEK, 1);
        Calendar e = Calendar.getInstance();
        e.add(Calendar.YEAR, 1);
        e.set(Calendar.DAY_OF_WEEK, 7);
        getDates(b, e);
        adapter = new MAapter(MainActivity.this);
        gridView.setAdapter(adapter);

        //gridView.setOnItemClickListener { view, parent, i, l ->System.out.print(i.toString())  }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visiblecount = visibleItemCount;
    }

    int visiblecount;

    private class MAapter extends BaseAdapter {
        Context context;

        public MAapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return days.size();
        }

        @Override
        public Object getItem(int position) {
            return days.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            DayView dayView = new DayView(MainActivity.this);
//            dayView.setHeight(150);
//            dayView.setDayModel((DayModel) getItem(position));


            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_day, null);


                holder = new ViewHolder();
                holder.dayView = (DayView) convertView.findViewById(R.id.dayView);
                convertView.setMinimumHeight(150);
                convertView.setTag(holder);
                holder.dayView.setDayModel(days.get(position));
                return convertView;
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.dayView.setDayModel(days.get(position));
                convertView.setMinimumHeight(150);
                return convertView;
            }


        }

        @Override
        public void notifyDataSetChanged() {

            Log.d("super-adapter", "notifyDataSetChanged");
            super.notifyDataSetChanged();
        }

        @Override
        public void notifyDataSetInvalidated() {
            Log.d("super-adapter", "notifyDataSetInvalidated");
            super.notifyDataSetInvalidated();
        }
    }

    private static class ViewHolder {
        DayView dayView;
    }
}
