package com.example.tourist;

import android.content.Context;
import android.content.Intent;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class OgunScreen extends Fragment {
    List<myModels.contentModel> contentData;
    int post;
    String contentGroup;
    RecyclerView recyclerView;
    private contentAdapter contentAdapter;
    private OnFragmentInteractionListener mListener;
    dbHelper dbHelper;
    public OgunScreen() {
    }

    public static OgunScreen getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        OgunScreen tabFragment = new OgunScreen();
        tabFragment.setArguments(bundle);
        return tabFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ogun_screen, container, false);
        contentData = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        contentGroup = dbColumnList.ogunTab.get(post);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new loadData().execute();
            }
        },500);
        return  view;
    }

    class loadData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            dbHelper = new dbHelper(getContext());
            Cursor cursor = dbHelper.getAllGroupData(contentGroup, dbColumnList.ogunData.TABLE_NAME);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    Cursor picsCursor = dbHelper.getAPics(cursor.getString(cursor.getColumnIndex(dbColumnList.ogunData.COLUMN_RECORDID)),
                            dbColumnList.ogunFile.TABLE_NAME);
                    if(picsCursor.getCount()>0){
                        picsCursor.moveToFirst();

                        myModels.contentModel contentList = new myModels().new contentModel(
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDTITLE)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDID)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDCONTENT)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDCONTENTGROUP)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_TRAVEL)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_FAVOURITE)),
                                picsCursor.getBlob(picsCursor.getColumnIndexOrThrow(dbColumnList.ogunFile.COLUMN_FILEDATA))
                        );
                        contentData.add(contentList);
                    }
                    picsCursor.close();

                }
            }
            cursor.close();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            contentAdapter = new contentAdapter( contentData, getContext(), new contentAdapter.OnItemClickListener() {
                @Override
                public void onVisitedClick(View v, int position) {
                    String postid =  contentData.get(position).getRecordId();
                    String placevisit =  contentData.get(position).getTitle();
                    dbHelper.UpdateVisited(postid,dbColumnList.ogunData.TABLE_NAME);
                    Toast.makeText(getContext(),  "You Have Visit " +placevisit ,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFavouriteClick(View v, int position) {
                    String postid =  contentData.get(position).getRecordId();
                    String placevisit =  contentData.get(position).getTitle();
                    dbHelper.UpdateFavourite(postid,dbColumnList.ogunData.TABLE_NAME);
                    Toast.makeText(getContext(),  "You Choose " +placevisit + " As Favourite." ,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onImageClick(View v, int position) {
                    String postid =  contentData.get(position).getRecordId();
//                    Toast.makeText(getContext(), postid + " Favourite",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), viewContent.class);
                    intent.putExtra("tipsId",postid);
                    intent.putExtra("tableData",dbColumnList.ogunData.TABLE_NAME);
                    intent.putExtra("FileData",dbColumnList.ogunFile.TABLE_NAME);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
            contentAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(contentAdapter);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
