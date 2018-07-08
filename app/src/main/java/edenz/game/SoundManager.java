package edenz.game;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by Nishshanka on 6/21/17.
 */

public class SoundManager {
    private AudioAttributes audioAttributes;
    private final int SOUND_POOL_SIZE = 2;
    private static SoundPool soundPool;
    private static int backgroundSound;
    private static int backgroundSoundStream;
    private static int hitSound;
    private static int explosionSound;
    private static int pipedSound;

    public SoundManager(Context context)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_SIZE)
                    .build();
        }
        else
        {
            soundPool = new SoundPool(SOUND_POOL_SIZE, AudioManager.STREAM_MUSIC, 0);
        }

        explosionSound = soundPool.load(context, R.raw.explosion, 1);
        hitSound = soundPool.load(context, R.raw.hit, 1);
        backgroundSound = soundPool.load(context, R.raw.background, 1);
        pipedSound = soundPool.load(context, R.raw.piped, 1);
    }

    public void playExplosion()
    {
        stopBackground();
        soundPool.play(explosionSound,1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHit()
    {
        soundPool.play(hitSound,1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playPiped()
    {
        stopBackground();
        soundPool.play(pipedSound,1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playBackground()
    {
        backgroundSoundStream = soundPool.play(backgroundSound,1.0f, 1.0f, 1, -1, 1.0f);
    }

    public void stopBackground() {
        soundPool.stop(backgroundSoundStream);
    }



}
