package com.easyapp.mobilepad;

import java.util.HashMap;

/**
 * Created by ihor.dutchak on 14.12.2015.
 */
public class RemoteInputEmulator {
    private TCPSocketConnection connection;

    public RemoteInputEmulator (TCPSocketConnection new_connection) {
        connection = new_connection;
    }

    enum KeyboardButton {
        ESC,
        ENTER,
        SPACE,
        TAB,
        CTRL,
        ALT,
        SHIFT,
        SUPER,
        CAPS_LOCK,

        LEFT,
        RIGHT,
        UP,
        DOWN,

        HOME,
        END,
        PAGE_UP,
        PAGE_DOWN,
        INSERT,
        DELETE,
        BACKSPACE,

        TILDE,
        MINUS,
        PLUS,
        COMMA,
        DOT,
        SLASH,
        BACK_SLASH,

        LEFT_BRACKET,
        RIGHT_BRACKET,
        SEMICOLON,
        APOSTROPHE,

        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H,
        I,
        J,
        K,
        L,
        M,
        N,
        O,
        P,
        Q,
        R,
        S,
        T,
        U,
        V,
        W,
        X,
        Y,
        Z,

        KEY_0,
        KEY_1,
        KEY_2,
        KEY_3,
        KEY_4,
        KEY_5,
        KEY_6,
        KEY_7,
        KEY_8,
        KEY_9,
    }

    static HashMap<KeyboardButton, String> keyboardButtonCodes;
    static {
        keyboardButtonCodes = new HashMap<>();
        keyboardButtonCodes.put(KeyboardButton.A, "A");
        keyboardButtonCodes.put(KeyboardButton.B, "B");
        keyboardButtonCodes.put(KeyboardButton.C, "C");
        keyboardButtonCodes.put(KeyboardButton.D, "D");
        keyboardButtonCodes.put(KeyboardButton.E, "E");
        keyboardButtonCodes.put(KeyboardButton.F, "F");
        keyboardButtonCodes.put(KeyboardButton.G, "G");
        keyboardButtonCodes.put(KeyboardButton.H, "H");
        keyboardButtonCodes.put(KeyboardButton.I, "I");
        keyboardButtonCodes.put(KeyboardButton.J, "J");
        keyboardButtonCodes.put(KeyboardButton.K, "K");
        keyboardButtonCodes.put(KeyboardButton.L, "L");
        keyboardButtonCodes.put(KeyboardButton.M, "M");
        keyboardButtonCodes.put(KeyboardButton.N, "N");
        keyboardButtonCodes.put(KeyboardButton.O, "O");
        keyboardButtonCodes.put(KeyboardButton.P, "P");
        keyboardButtonCodes.put(KeyboardButton.Q, "Q");
        keyboardButtonCodes.put(KeyboardButton.R, "R");
        keyboardButtonCodes.put(KeyboardButton.S, "S");
        keyboardButtonCodes.put(KeyboardButton.T, "T");
        keyboardButtonCodes.put(KeyboardButton.U, "U");
        keyboardButtonCodes.put(KeyboardButton.V, "V");
        keyboardButtonCodes.put(KeyboardButton.W, "W");
        keyboardButtonCodes.put(KeyboardButton.X, "X");
        keyboardButtonCodes.put(KeyboardButton.Y, "Y");
        keyboardButtonCodes.put(KeyboardButton.Z, "Z");

        keyboardButtonCodes.put(KeyboardButton.KEY_0, "0");
        keyboardButtonCodes.put(KeyboardButton.KEY_1, "1");
        keyboardButtonCodes.put(KeyboardButton.KEY_2, "2");
        keyboardButtonCodes.put(KeyboardButton.KEY_3, "3");
        keyboardButtonCodes.put(KeyboardButton.KEY_4, "4");
        keyboardButtonCodes.put(KeyboardButton.KEY_5, "5");
        keyboardButtonCodes.put(KeyboardButton.KEY_6, "6");
        keyboardButtonCodes.put(KeyboardButton.KEY_7, "7");
        keyboardButtonCodes.put(KeyboardButton.KEY_8, "8");
        keyboardButtonCodes.put(KeyboardButton.KEY_9, "9");
        keyboardButtonCodes.put(KeyboardButton.ESC, "ESC");
        keyboardButtonCodes.put(KeyboardButton.ENTER, "ENTER");
        keyboardButtonCodes.put(KeyboardButton.SPACE, "SPACE");
        keyboardButtonCodes.put(KeyboardButton.TAB, "TAB");
        keyboardButtonCodes.put(KeyboardButton.CTRL, "CTRL");
        keyboardButtonCodes.put(KeyboardButton.ALT, "ALT");
        keyboardButtonCodes.put(KeyboardButton.SHIFT, "SHIFT");
        keyboardButtonCodes.put(KeyboardButton.SUPER, "SUPER");
        keyboardButtonCodes.put(KeyboardButton.CAPS_LOCK, "CAPS_LOCK");

        keyboardButtonCodes.put(KeyboardButton.LEFT, "LEFT");
        keyboardButtonCodes.put(KeyboardButton.RIGHT, "RIGHT");
        keyboardButtonCodes.put(KeyboardButton.UP, "UP");
        keyboardButtonCodes.put(KeyboardButton.DOWN, "DOWN");

        keyboardButtonCodes.put(KeyboardButton.PAGE_UP, "PAGE_UP");
        keyboardButtonCodes.put(KeyboardButton.PAGE_DOWN, "PAGE_DOWN");
        keyboardButtonCodes.put(KeyboardButton.HOME, "HOME");
        keyboardButtonCodes.put(KeyboardButton.END, "END");
        keyboardButtonCodes.put(KeyboardButton.INSERT, "INSERT");
        keyboardButtonCodes.put(KeyboardButton.DELETE, "DEL");
        keyboardButtonCodes.put(KeyboardButton.BACKSPACE, "BACKSPACE");

        keyboardButtonCodes.put(KeyboardButton.TILDE, "~");
        keyboardButtonCodes.put(KeyboardButton.MINUS, "-");
        keyboardButtonCodes.put(KeyboardButton.PLUS, "=");
        keyboardButtonCodes.put(KeyboardButton.COMMA, ",");
        keyboardButtonCodes.put(KeyboardButton.DOT, ".");
        keyboardButtonCodes.put(KeyboardButton.SLASH, "/");
        keyboardButtonCodes.put(KeyboardButton.BACK_SLASH, "\\");

        keyboardButtonCodes.put(KeyboardButton.LEFT_BRACKET, "[");
        keyboardButtonCodes.put(KeyboardButton.RIGHT_BRACKET, "]");
        keyboardButtonCodes.put(KeyboardButton.SEMICOLON, ";");
        keyboardButtonCodes.put(KeyboardButton.APOSTROPHE, "'");
    }

    public void pressButton(KeyboardButton button) {
        connection.send("KEY_DOWN:"+keyboardButtonCodes.get(button));
    }

    public void releaseButton(KeyboardButton button) {
        connection.send("KEY_UP:"+keyboardButtonCodes.get(button));
    }
}