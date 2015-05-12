package game.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class CAuthorizationMessage extends CNetworkMessage {
    protected CNetworkPacketFactory DPacketFactory;
    protected String DUsername;
    protected String DPassword;
    protected ByteArrayOutputStream DMessage_stream;
    protected DataOutputStream DMessage;

    public CAuthorizationMessage(String username, String password) {
        DUsername = username;
        DPassword = password;
        DPacketFactory = new CNetworkPacketFactory();
        DMessage_stream = new ByteArrayOutputStream();
        DMessage = new DataOutputStream(DMessage_stream);
        MakeMessage();
    }

    public void MakeMessage() {
        DPacketFactory.AuthActionPacket(DMessage, DUsername, DPassword);
    }

    public byte[] GetMessage() {
        return DMessage_stream.toByteArray();
    }

    public int GetMessageLength() {
        return DMessage_stream.size();
    }

}
