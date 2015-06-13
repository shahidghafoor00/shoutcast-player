package byteshaft.com.shoutcast;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;

import wseemann.media.FFmpegMediaPlayer;

public class StreamService extends Service implements FFmpegMediaPlayer.OnPreparedListener {

    final int ID = 404;
    private FFmpegMediaPlayer mMediaPlayer;
    private static StreamService sService;

    private Notification mNotification;

    static StreamService getInstance() {
        return sService;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sService = this;
        start();
        mNotification = new Notification(getApplicationContext());
        android.app.Notification notification = mNotification.getNotification();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(ID, notification);
        return START_NOT_STICKY;
    }

    private void start() {
        mMediaPlayer = new FFmpegMediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        String url = getString(R.string.shoutcast_url);
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        sService = null;
    }

    @Override
    public void onPrepared(FFmpegMediaPlayer fFmpegMediaPlayer) {
        mMediaPlayer.start();
    }
}
