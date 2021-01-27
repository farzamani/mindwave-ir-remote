package com.zamani.mindwaveremote;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "NeuroSky";

    private static final String CMD_TV_POWER =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    private static final String CMD_TV_OK =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    private static final String CMD_TV_LEFT =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    private static final String CMD_TV_RIGHT =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    private static final String CMD_TV_UP =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    private static final String CMD_TV_DOWN =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0016 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    private static final String CMD_TV_BACK =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    public static final String CMD_TV_VOLUME_DOWN =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    public static final String CMD_TV_VOLUME_UP =
            "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0016 0016 05F7 015B 0057 0016 0E6C";

    private ConsumerIrManager irManager;

    private Button btnConnect, btnStart, btnStop, btnDisconnect;
    private ImageView imagePower, imageLeft, imageRight, imageUp, imageDown, imageIdle, imageBack, imageOk, imageVolumeUp, imageVolumeDown;
    private TextView textState, textAttention, textMeditation, textBlink;

    private NeuroSky neuroSky;
    private States imageState;

    private volatile boolean isReady = true;

    private long previous_time_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        irManager = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);

        initView();

        neuroSky = createNeuroSky();
        imageState = new States(1);

        btnConnect.setOnClickListener(v -> {
            try {
                neuroSky.connect();
                Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show();
            } catch (BluetoothNotEnabledException e) {
                Log.d(TAG, "onCreate: " + e.getMessage());
            }
        });

        btnDisconnect.setOnClickListener(v -> {
            neuroSky.disconnect();
            Toast.makeText(this, "Disconnecting...", Toast.LENGTH_SHORT).show();
        });

        btnStart.setOnClickListener(v -> {
            neuroSky.startMonitoring();
            Toast.makeText(this, "Start monitoring...", Toast.LENGTH_SHORT).show();

            isReady = true;
        });

        btnStop.setOnClickListener(v -> {
            neuroSky.stopMonitoring();
            Toast.makeText(this, "Monitoring Paused...", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private NeuroSky createNeuroSky() {
        return new NeuroSky(new ExtendedDeviceMessageListener() {
            @Override
            public void onStateChange(State state) {
                handleStateChange(state);
            }

            @Override
            public void onSignalChange(Signal signal) {
                handleSignalChange(signal);
            }

            @Override
            public void onBrainWavesChange(Set<BrainWave> brainWaves) {
                handleBrainwavesChange(brainWaves);
            }
        });
    }

    private void handleBrainwavesChange(Set<BrainWave> brainWaves) {
        for (BrainWave brainWave: brainWaves) {
            Log.d(TAG, String.format("%s: %d", brainWave.toString(), brainWave.getValue()));
        }
    }

    private void handleSignalChange(Signal signal) {
        switch (signal) {
            case ATTENTION:
                textAttention.setText(getFormattedMessage("ATTENTION: %d", signal));

                long temp = System.currentTimeMillis();

                if (!isReady) {
                    if (temp - previous_time_click > 7500) {
                        isReady = true;
                        Log.d(TAG, "handleSignalChange: Ready to click");
                        Toast.makeText(this, "Ready to click", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "handleSignalChange: temp = " + (temp - previous_time_click));
                    }
                } else { // if isReady
                    if (signal.getValue() > 65) {
                        switch (imageState.getState()) {
                            case 1:
                                TransmitSignal(hex2ir(CMD_TV_POWER));
                                Toast.makeText(this, "Power Transmitted", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                TransmitSignal(hex2ir(CMD_TV_LEFT));
                                Toast.makeText(this, "Left Transmitted", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                TransmitSignal(hex2ir(CMD_TV_RIGHT));
                                Toast.makeText(this, "Right Transmitted", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                TransmitSignal(hex2ir(CMD_TV_DOWN));
                                Toast.makeText(this, "Down Transmitted", Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                TransmitSignal(hex2ir(CMD_TV_UP));
                                Toast.makeText(this, "Up Transmitted", Toast.LENGTH_SHORT).show();
                                break;
                            case 6:
                                TransmitSignal(hex2ir(CMD_TV_OK));
                                Toast.makeText(this, "OK Transmitted", Toast.LENGTH_SHORT).show();
                                break;
                            case 7:
                                TransmitSignal(hex2ir(CMD_TV_BACK));
                                Toast.makeText(this, "Back Transmitted", Toast.LENGTH_SHORT).show();
                                break;
                            case 8:
                                TransmitSignal(hex2ir(CMD_TV_VOLUME_DOWN));
                                Toast.makeText(this, "Volume Down Transmitted", Toast.LENGTH_SHORT).show();
                                break;
                            case 9:
                                TransmitSignal(hex2ir(CMD_TV_VOLUME_UP));
                                Toast.makeText(this, "Volume Up Transmitted", Toast.LENGTH_SHORT).show();
                            default:
                                break;
                        }
                        Log.d(TAG, "handleSignalChange: Idle for 5 seconds after click");
                        Toast.makeText(this, "Idle for 5 seconds after click", Toast.LENGTH_SHORT).show();
                        isReady = false;
                        previous_time_click = temp;
                    }
                }

                break;
            case MEDITATION:
                textMeditation.setText(getFormattedMessage("MEDITATION: %d", signal));
                break;
            case BLINK:
                textBlink.setText(getFormattedMessage("BLINK STRENGTH: %d", signal));

                if (signal.getValue() > 80) {
                    Log.d(TAG, "handleSignalChange: Blink Detected");
                    Toast.makeText(this, "Blink Detected", Toast.LENGTH_SHORT).show();

                    switch (imageState.getState()) {
                        case 1:
                            imagePower.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageLeft.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(2);
                            break;
                        case 2:
                            imageLeft.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageRight.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(3);
                            break;
                        case 3:
                            imageRight.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageDown.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(4);
                            break;
                        case 4:
                            imageDown.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageUp.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(5);
                            break;
                        case 5:
                            imageUp.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageOk.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(6);
                            break;
                        case 6:
                            imageOk.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageBack.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(7);
                            break;
                        case 7:
                            imageBack.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageVolumeDown.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(8);
                            break;
                        case 8:
                            imageVolumeDown.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageVolumeUp.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(9);
                            break;
                        case 9:
                            imageVolumeUp.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imageIdle.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(10);
                            break;
                        case 10:
                            imageIdle.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            imagePower.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                            imageState.setState(1);
                            break;
                        default:
                            break;
                    }
                }
                break;
        }

        Log.d(TAG, String.format("%s: %d", signal.toString(), signal.getValue()));
    }

    private String getFormattedMessage(String messageFormat, Signal signal) {
        return String.format(Locale.getDefault(), messageFormat, signal.getValue());
    }

    private void handleStateChange(State state) {
        if (neuroSky != null && state.equals(State.CONNECTED)) {
            Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
        }

        if (neuroSky != null && state.equals(State.DISCONNECTED)) {
            Toast.makeText(this, "Disconnected...", Toast.LENGTH_SHORT).show();
        }

        if (neuroSky != null && state.equals(State.IDLE)) {
            Toast.makeText(this, "State Idle", Toast.LENGTH_SHORT).show();
        }

        textState.setText("STATE: " + state.toString());
        Log.d(TAG, "handleStateChange: " + state.toString());
    }

    //Function to Transmit IR Signal from Phone
    private void TransmitSignal (IRCommand cmd) {
        Log.d(TAG, "TransmitSignal: ");
        irManager.transmit(cmd.freq, cmd.pattern);
    }

    // based on code from http://stackoverflow.com/users/1679571/randy (http://stackoverflow.com/a/25518468)
    //Function to convert Pronto Hex to Ready-to-Transmit IR Signal
    private IRCommand hex2ir(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData.split(" ")));
        list.remove(0);
        int frequency = Integer.parseInt(list.remove(0), 16);
        list.remove(0);
        list.remove(0);

        frequency = (int) (1000000 / (frequency * 0.241246));
        int pulses = 1000000 / frequency;
        int count;

        int[] pattern = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            count = Integer.parseInt(list.get(i), 16);
            pattern[i] = count * pulses;
        }

        return new IRCommand(frequency, pattern);
    }

    //
    private static class IRCommand {
        private final int freq;
        private final int[] pattern;

        private IRCommand (int freq, int[] pattern) {
            this.freq = freq;
            this.pattern = pattern;
        }
    }

    private void initView() {
        btnConnect = findViewById(R.id.btn_connect);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnDisconnect = findViewById(R.id.btn_disconnect);

        imagePower = findViewById(R.id.image_power);
        imageLeft = findViewById(R.id.image_left);
        imageRight = findViewById(R.id.image_right);
        imageUp = findViewById(R.id.image_up);
        imageDown = findViewById(R.id.image_down);
        imageOk = findViewById(R.id.image_ok);
        imageBack = findViewById(R.id.image_back);
        imageVolumeDown = findViewById(R.id.image_volume_down);
        imageVolumeUp = findViewById(R.id.image_volume_up);
        imageIdle = findViewById(R.id.image_idle);

        textState = findViewById(R.id.text_state);
        textAttention = findViewById(R.id.text_attention);
        textMeditation = findViewById(R.id.text_meditation);
        textBlink = findViewById(R.id.text_blink);
    }
}