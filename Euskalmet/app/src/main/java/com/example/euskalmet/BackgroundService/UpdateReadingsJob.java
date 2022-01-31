package com.example.euskalmet.BackgroundService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.euskalmet.Room.MeteoController;

public class UpdateReadingsJob extends JobService {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters params) {
        Context context = getApplicationContext();
        MeteoController meteoController = MeteoController.getMeteoController(context);
        meteoController.updateReadings();
        UpdateReadingsUtil.scheduleJob(context);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}
