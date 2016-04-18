package com.example.riane.hanbaoreader_app.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.riane.hanbaoreader_app.R;
import com.example.riane.hanbaoreader_app.app.BaseActivity;
import com.example.riane.hanbaoreader_app.cache.BookTagDao;
import com.example.riane.hanbaoreader_app.modle.BookTag;
import com.example.riane.hanbaoreader_app.ui.fragment.PlaceBookFragment;
import com.example.riane.hanbaoreader_app.util.LogUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
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
    @Bind(R.id.empty_view)
    RelativeLayout rl_empty_view;
    private BookTagDao bookTagDao;
    private List<BookTag> bookTagList;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> bookPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placebook);
        ButterKnife.bind(this);
        bookPaths = new ArrayList<String>();
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
        initView();
    }

    private void initView() {
        bookTagDao = new BookTagDao(PlaceBookActivity.this);
        bookTagList = bookTagDao.getAll();
        List<String> list = null;
        if (bookTagList == null || bookTagList.size() < 1) {
            String[] res = new String[]{"玄幻", "言情", "武侠", "科幻", "历史", "科学"};
            list = Arrays.asList(res);
        } else {
            List<BookTag> bookTags = bookTagDao.getAll();
            //LogUtils.d("bookTag + "bookTags.get(0).getBookTag());
            list = new ArrayList<>();
            for (BookTag tag : bookTags) {
                LogUtils.d("bookTag + " + tag.getBookTag());
                list.add(tag.getBookTag());
            }
        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
//
        //让第一项已经被选中
        spinner.setSelection(0, true);
        List<BookTag> bookTags = bookTagDao.getBookTagBytag(list.get(0));
        if (bookTags == null || bookTags.size() < 1) {
            rl_empty_view.setVisibility(View.VISIBLE);
        } else {
            rl_empty_view.setVisibility(View.GONE);
            for (BookTag booktag : bookTags) {
                LogUtils.d("书的标签" + booktag.getBookPath());
                bookPaths.add(booktag.getBookPath());
                LogUtils.d("bookPath" + bookPaths.get(0));
            }
        }
        switchFragment();
        //List<BookTag> bookTags = bookTagDao.getBookTagBytag(list.get(0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) spinner.getItemAtPosition(position);
                List<BookTag> bookTags = bookTagDao.getBookTagBytag(data);
                if (bookTags == null || bookTags.size() < 1) {
                    rl_empty_view.setVisibility(View.VISIBLE);
                } else {
                    rl_empty_view.setVisibility(View.GONE);
                    for (BookTag booktag : bookTags) {
                        LogUtils.d("书的标签" + booktag.getBookPath());
                        bookPaths.add(booktag.getBookPath());
                        LogUtils.d("bookPath" + bookPaths.get(0));
                    }
                }

                switchFragment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void switchFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.place_contain, PlaceBookFragment
                .newInstance((ArrayList<String>) bookPaths)).commit();
    }

}
