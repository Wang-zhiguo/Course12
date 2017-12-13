package cn.androidstudy.course12;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    //这里的AUTHORITY就是我们在AndroidManifest.xml中配置的authorities
    private static final String AUTHORITY = "cn.androidstudy.course12";
    //匹配成功后的匹配码
    private static final int PERSONS = 100;
    private static final int PERSON = 101;
    private static UriMatcher uriMatcher;
    private SQLiteDatabase db;
    private DBOpenHelper openHelper;
    //数据改变后指定通知的Uri
    private static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHORITY + "/person");

    //在静态代码块中添加要匹配的 Uri
    static {
        //匹配不成功返回NO_MATCH(-1)
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /**
         * uriMatcher.addURI(authority, path, code); 其中
         * authority：主机名(用于唯一标示一个ContentProvider,这个需要和清单文件中的authorities属性相同)
         * path:路径路径(可以用来表示我们要操作的数据，路径的构建应根据业务而定)
         * code:返回值(用于匹配uri的时候，作为匹配成功的返回值)
         */
        uriMatcher.addURI(AUTHORITY, "person", PERSONS);// 匹配记录集合
        uriMatcher.addURI(AUTHORITY, "person/#", PERSON);// 匹配单条记录
    }
    public MyContentProvider() {
    }
    //content://cn.androidstudy.course12/person/12
    @Override
    public int delete(Uri uri, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                int count=db.delete("tb_person",
                        selection, selectionArgs);
                if(count>0){
                    notifyDataChanged();
                    return count;
                }
                break;
            case PERSON:
                // 这里可以做删除单条数据的操作。
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                count = db.delete("tb_person", where, selectionArgs);
                return count;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                return "vnd.android.cursor.dir/person";

            case PERSON:
                return "vnd.android.cursor.item/person";

            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match=uriMatcher.match(uri);
        if(match!=PERSONS){
            throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }

        long rawId = db.insert("tb_person",
                null, values);
        Uri insertUri = ContentUris.withAppendedId(uri, rawId);
        if(rawId>0){
            notifyDataChanged();
            return insertUri;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        openHelper = new DBOpenHelper(getContext());
        db = openHelper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                return db.query("tb_person", projection, selection, selectionArgs,
                        null, null, sortOrder);

            case PERSON:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.query("tb_person", projection, where, selectionArgs, null,
                        null, sortOrder);

            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                count = db.update("tb_person", values, selection, selectionArgs);
                return count;
            case PERSON:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                count = db.update("tb_person", values, where, selectionArgs);
                return count;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }
    //通知指定URI数据已改变
    private void notifyDataChanged() {
        getContext().getContentResolver().notifyChange(NOTIFY_URI, null);
    }
}
