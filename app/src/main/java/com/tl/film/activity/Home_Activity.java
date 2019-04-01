package com.tl.film.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.tl.film.R;
import com.tl.film.adapter.RecyclerCoverFlow_Adapter;
import com.tl.film.view.CoverFlowLayoutManger;
import com.tl.film.view.RecyclerCoverFlow;

/**
 * @author jiangyao
 * date: 2019/3/22
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:
 */
public class Home_Activity extends Base_Activity {
    private static final String TAG = "Home_Activity";

    RecyclerCoverFlow mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mList = findViewById(R.id.list);
//        mList.setFlatFlow(true); //平面滚动

        mList.setAdapter(new RecyclerCoverFlow_Adapter(this));
        mList.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                Toast.makeText(Home_Activity.this, (position + 1) + "/" + mList.getLayoutManager().getItemCount(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
