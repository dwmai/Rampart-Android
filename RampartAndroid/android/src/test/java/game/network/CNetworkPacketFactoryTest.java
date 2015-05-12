package game.network;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.nio.ByteOrder;

public class CNetworkPacketFactoryTest extends TestCase {

    CNetworkPacketFactory factory = new CNetworkPacketFactory();
    protected ByteArrayOutputStream DMessage_stream;
    protected DataOutputStream DMessage;

    public void testAuthActionPacket() throws Exception {

        DMessage_stream = new ByteArrayOutputStream();
        DMessage = new DataOutputStream(DMessage_stream);
        short action = CNetworkConstants.CLIENT_AUTH;
        String username = "user";
        String password = "password";
        //Making packet
        factory.AuthActionPacket(DMessage, username, password);

        InputStream inputStream = new ByteArrayInputStream(DMessage_stream.toByteArray());
        DataInputStream reader = new DataInputStream(inputStream);
        //Testing packet's length is correct.
        int packet_length = (4 + 2 + username.length() + 1 + password.length() + 1);
        assertEquals("size must be: " + packet_length, packet_length, reader.readInt());
        //Testing packet's action number is correct.
        assertEquals("Action must be: " + ntohs(action), ntohs(action), reader.readShort());
        byte[] user = new byte[username.length()];
        reader.read(user, 0, username.length());
        String us = new String(user);
        //testing the username in the packet is correct
        assertEquals(us, username);

    }

    public void testGetAvailableRoomsActionPacket() throws Exception {

    }

    public void testCreateRoomActionPacket() throws Exception {

    }

    public void testJoinRoomActionPacket() throws Exception {

    }

    public void testDisconnectActionPacket() throws Exception {

    }

    public void testLeaveRoomActionPacket() throws Exception {

    }

    public void testStartGameActionPacket() throws Exception {

    }

    public void testActionPacket() throws Exception {
        DMessage_stream = new ByteArrayOutputStream();
        DMessage = new DataOutputStream(DMessage_stream);
        short action = 10;
        int time_step = 32;
        short player_id = 1;
        int x = 256;
        int y = 450;
        factory.ActionPacket(DMessage, action, time_step, player_id, x, y);
        InputStream inputStream = new ByteArrayInputStream(DMessage_stream.toByteArray());
        DataInputStream reader = new DataInputStream(inputStream);
        assertEquals(4 + 2 + 4 + 2 + 4 + 4, reader.readInt());
        assertEquals(action, reader.readShort());
        assertEquals(time_step, reader.readInt());
        assertEquals(player_id, reader.readShort());
        assertEquals(x, reader.readInt());
        assertEquals(y, reader.readInt());
    }

    public void testSetUpHeader() throws Exception {

    }

    public void testHtonl() throws Exception {

    }

    public void testHtons() throws Exception {

    }

    public int ntohl(int value) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            return value;
        }
        return Integer.reverseBytes(value);
    }

    public short ntohs(short value) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            return value;
        }
        return Short.reverseBytes(value);
    }
}