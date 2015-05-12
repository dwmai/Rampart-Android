package game.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class CStartGameMessage extends CNetworkMessage {
    protected CNetworkPacketFactory DPacketFactory;
    protected int DMessageLength;
    protected String DUsername;
    protected String DMapData;
    protected ByteArrayOutputStream DMessage_stream;
    protected DataOutputStream DMessage;

    public CStartGameMessage(String username, String map_data) {
        DUsername = username;
        DMapData = map_data;
        DPacketFactory = new CNetworkPacketFactory();
        DMessage_stream = new ByteArrayOutputStream();
        DMessage = new DataOutputStream(DMessage_stream);
        MakeMessage();
    }

    public void MakeMessage() {
        DMessageLength = DPacketFactory.StartGameActionPacket(DMessage, DUsername, DMapData);
    }

    public byte[] GetMessage() {
        return DMessage_stream.toByteArray();
    }

    public int GetMessageLength() {
        return DMessage.size();
    }
}
