package com.example.riane.hanbaoreader_app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.cache.BookTagDao;
import com.example.riane.hanbaoreader_app.modle.BookTag;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Riane on 2016/4/14.
 */
public class PlaceBookActivity extends BaseActivity {

    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.toolbar_place)
    Toolbar toolbarPlace;
    private BookTagDao bookTagDao;
    private List<BookTag> bookTagList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placebook);
        ButterKnife.bind(this);

        toolbarPlace = (Toolbar) findViewById(R.id.toolbar_place);
        setSupportActionBar(toolbarPlace);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarPlace.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bookTagDao = new BookTagDao(PlaceBookActivity.this);
        bookTagList = bookTagDao.getAll();
        if (bookTagList == null || bookTagList.size() < 1) {
            String[] res = new String[]{"玄幻","言情","武侠","科幻","历史","科学"};
            List<String> list = Arrays.asList(res);
            arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.place_contain, PlaceBookFragment
//        .newInstance()).commit();

    }
}
