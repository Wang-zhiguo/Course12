package cn.androidstudy.course12;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();
        cr.registerContentObserver(uri,true,new MyObserver(new Handler()));
    }

    public void readSMS(View view){
        Uri uri = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(uri,
                new String[]{"_id","address","type","body","date"},
                null,null,null);
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String address = cursor.getString(1);
            String type = cursor.getString(2);
            String body = cursor.getString(3);
            String date = cursor.getString(4);
            System.out.println("address:"+address+",body:"+body);
        }
    }

    class MyObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            System.out.println("111111111111---"+uri.toString());
            try{
                long i = ContentUris.parseId(uri);
            }catch (Exception e){
                return;
            }
            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(uri,new String[]{"_id","address","type","body","date"},null,null,null);
            while(cursor.moveToNext()){
                String id = cursor.getString(0);
                String address = cursor.getString(1);
                String type = cursor.getString(2);
                String body = cursor.getString(3);
                String date = cursor.getString(4);
                System.out.println("----address:"+address+",body:"+body);
            }
        }
    }
}
