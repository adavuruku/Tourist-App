package com.example.tourist;

import android.provider.BaseColumns;

import java.util.List;

/**
 * Created by sherif146 on 03/01/2018.
 */

public class dbColumnList {
    public static List <myModels.contentModel> oyoContent,ogunContent;
    public static List <String> oyoTab,ogunTab;

    public static int TabPostion;
    public static String TabText;


    public static class oyoData implements BaseColumns{
        public static final String TABLE_NAME = "oyo_data";
        public static final String COLUMN_RECORDID= "id";
        public static final String COLUMN_RECORDTITLE = "title";
        public static final String COLUMN_RECORDCONTENT = "content";
        public static final String COLUMN_RECORDCONTENTGROUP = "contentgroup";
        public static final String COLUMN_TRAVEL = "travel";
        public static final String COLUMN_FAVOURITE = "favourite";
    }

    public static class ogunData implements BaseColumns{
        public static final String TABLE_NAME = "ogun_data";
        public static final String COLUMN_RECORDID= "id";
        public static final String COLUMN_RECORDTITLE = "title";
        public static final String COLUMN_RECORDCONTENT = "content";
        public static final String COLUMN_RECORDCONTENTGROUP = "contentgroup";
        public static final String COLUMN_TRAVEL = "travel";
        public static final String COLUMN_FAVOURITE = "favourite";
    }


    public static class oyoFile implements BaseColumns{
        public static final String TABLE_NAME = "oyo_file";
        public static final String COLUMN_RECORDID = "id";
        public static final String COLUMN_FILEDATA = "filedata";
    }

    public static class ogunFile implements BaseColumns{
        public static final String TABLE_NAME = "ogun_file";
        public static final String COLUMN_RECORDID = "id";
        public static final String COLUMN_FILEDATA = "filedata";
    }

}
