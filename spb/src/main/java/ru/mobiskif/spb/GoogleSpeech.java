package ru.mobiskif.spb;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public class GoogleSpeech extends TextToSpeech {

    public GoogleSpeech(Context context, OnInitListener listener) {
        super(context, listener);
        //selectFile("*/*"); break;
        if (TextToSpeech.Engine.CHECK_VOICE_DATA_PASS != 1) {
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            context.startActivity(installIntent);
        }
        else {
            Toast.makeText(context, "Нет голоса", Toast.LENGTH_SHORT).show();
        }
    }
}