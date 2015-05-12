package game.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class CGetAvailableRoomsMessage extends CNetworkMessage {
    protected CNetworkPacketFactory DPacketFactory;
    protected String DUsername;
    protected ByteArrayOutputStream DMessage_stream;
    protected DataOutputStream DMessage;

    public CGetAvailableRoomsMessage(String username) {
        DUsername = username;
        DPacketFactory = new CNetworkPacketFactory();
        DMessage_stream = new ByteArrayOutputStream();
        DMessage = new DataOutputStream(DMessage_stream);
        MakeMessage();
    }

    public void MakeMessage() {
        DPacketFactory.GetAvailableRoomsActionPacket(DMessage, DUsername);
    }

    public byte[] GetMessage() {
        return DMessage_stream.toByteArray();
    }

    public int GetMessageLength() {
        return DMessage.size();
    }
}
