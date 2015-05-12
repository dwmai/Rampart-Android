package game.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class CLeaveRoomMessage extends CNetworkMessage {
    protected CNetworkPacketFactory DPacketFactory;
    protected String DUsername;
    protected String DRoomName;
    protected ByteArrayOutputStream DMessage_stream;
    protected DataOutputStream DMessage;

    public CLeaveRoomMessage(String username, String room_name) {
        DUsername = username;
        DRoomName = room_name;
        DPacketFactory = new CNetworkPacketFactory();
        DMessage_stream = new ByteArrayOutputStream();
        DMessage = new DataOutputStream(DMessage_stream);
        MakeMessage();
    }

    public void MakeMessage() {
        DPacketFactory.LeaveRoomActionPacket(DMessage, DUsername, DRoomName);
    }

    public byte[] GetMessage() {
        return DMessage_stream.toByteArray();
    }

    public int GetMessageLength() {
        return DMessage.size();
    }
}
