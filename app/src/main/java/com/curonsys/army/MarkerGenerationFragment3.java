package com.curonsys.army;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

/**
 * Created by ijin-yeong on 2018. 5. 21..
 */



public class MarkerGenerationFragment3 extends Fragment {

    Context thisContext;

    TextView tv;
    MaterialDialog.Builder builder = null;
    MaterialDialog materialDialog = null;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<ContentsListModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    DBManager dbManager = DBManager.getInstance();
    SampleData sampleData = SampleData.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marker_generation3, container, false);
        FragmentManager fragmentManager = this.getChildFragmentManager();

        myOnClickListener = new MyOnClickListener(thisContext);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(thisContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<ContentsListModel>();
        for (int i = 0; i < SampleData.nameArray.length; i++) {
            data.add(new ContentsListModel(
                    SampleData.nameArray[i],
                    SampleData.versionArray[i],
                    SampleData.id_[i],
                    SampleData.drawableArray[i]
            ));
        }

        removedItems = new ArrayList<Integer>();

        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        thisContext=activity;
        //https://www.journaldev.com/10024/android-recyclerview-android-cardview-example-tutorial
        //step 3는 위의 url을 이용해서 cardview로 구현할 예정임
        //컨텐츠를 단순하게 선택하는 기능을 가지는 fragment, recyclerview 이용
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_item) {
            //check if any items to add
            if (removedItems.size() != 0) {
                addRemovedItemToList();
            } else {
                Toast.makeText(thisContext, "Nothing to add", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void addRemovedItemToList() {
        int addItemAtListPosition = 3;
        data.add(addItemAtListPosition, new ContentsListModel(
                SampleData.nameArray[removedItems.get(0)],
                SampleData.versionArray[removedItems.get(0)],
                SampleData.id_[removedItems.get(0)],
                SampleData.drawableArray[removedItems.get(0)]
        ));
        adapter.notifyItemInserted(addItemAtListPosition);
        removedItems.remove(0);
    }


    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;
        DBManager dbManager = DBManager.getInstance();
        SampleData sampleData = SampleData.getInstance();
        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            //removeItem(v);
            selectedItem(v);
        }

        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();
            int selectedItemId = -1;
            for (int i = 0; i < SampleData.nameArray.length; i++) {
                if (selectedName.equals(SampleData.nameArray[i])) {
                    selectedItemId = SampleData.id_[i];
                }
            }
            removedItems.add(selectedItemId);
            data.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);
        }

        private void selectedItem(View v){
            final int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            LinearLayout linearLayout = viewHolder.itemView.findViewById(R.id.card_item_linear);

            linearLayout.setBackgroundColor(Color.BLUE);
            final TextView textView = viewHolder.itemView.findViewById(R.id.textViewName);

//            adapter.notifyItemChanged(selectedItemPosition);
            new MaterialDialog.Builder(this.context)
                    .title("해당 컨텐츠를 선택하시겠습니까?")
                    .titleColor(Color.BLACK)
                    .positiveText("예")
                    .negativeText("아니요")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Toast.makeText(context,textView.getText()+"",Toast.LENGTH_SHORT).show();
                            adapter.notifyItemChanged(selectedItemPosition);
                            //Toast.makeText(context,dbManager.generatorId,Toast.LENGTH_SHORT).show();
                            dbManager.contentFileName = sampleData.contentFileName[selectedItemPosition];
                            dbManager.contentHasAnimation = sampleData.contentHasAnimation[selectedItemPosition];
                            dbManager.contentId = sampleData.id_[selectedItemPosition];
                            dbManager.contentName = sampleData.nameArray[selectedItemPosition];
                            dbManager.contentTextureFiles = sampleData.contentTextureFiles.get(selectedItemPosition);
                            dbManager.textureCount = sampleData.contentTextureCount[selectedItemPosition];
                            dbManager.contentTextureNames = sampleData.contentTextureNames.get(selectedItemPosition);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Toast.makeText(context,"취소",Toast.LENGTH_SHORT).show();
                            adapter.notifyItemChanged(selectedItemPosition);
                        }
                    })
                    .show();
        }
    }
}
