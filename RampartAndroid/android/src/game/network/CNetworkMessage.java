package game.network;

import java.util.*;
import java.io.*;

public abstract class CNetworkMessage {
    private short DAction = -1;

    public CNetworkMessage() {
    }

    public void MakeMessage() {
    }

    public byte[] GetMessage() {
        return null;
    }

    public int GetMessageLength() {
        return 0;
    }
}
