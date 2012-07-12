package com.yyl.myrmex.tracking;

import com.yyl.myrmex.tracking.database.DatabaseExporter;
import com.yyl.myrmex.tracking.database.LocDatabaseHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class DbExportTask extends AsyncTask<Void, Void, Boolean> {

	private static final String EXPORT_FILENAME = "trackingmap";
	private static final String LOCTABLE_PATH = "/data/com.yyl.myrmex.trackingmap/databases/loctable.db";
	private Context ctx;
	private Calendar calendar;
	
	public DbExportTask(Context c) {
		ctx = c;
	}

	
    // automatically done on worker thread (separate from UI thread)
        protected Boolean doInBackground(final Void... args) {
        	LocDatabaseHelper database = new LocDatabaseHelper(ctx);
        	DatabaseExporter dbe = new DatabaseExporter(database.getReadableDatabase());
        	try {
        		calendar = Calendar.getInstance();
        		String time = parseTime(calendar.getTimeInMillis());
				dbe.export(Environment.getDataDirectory()+LOCTABLE_PATH, EXPORT_FILENAME + time);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
        }

        // can use UI thread here
        protected void onPostExecute(final Boolean success) {
           if (success) {
              Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT).show();
           } else {
              Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();
           }
        }
        
    	private String parseTime(long t) {
    		String format = "yyyy-MM-dd-HH-mm";
    		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
//    		DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
    		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
    		String gmtTime = sdf.format(t);
    		return gmtTime;
    	}
    }