package game.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CDisconnectMessage extends CNetworkMessage
{
  protected CNetworkPacketFactory DPacketFactory;
  protected String DUsername;
  protected String DRoomName;
  protected ByteArrayOutputStream DMessage_stream;
  protected DataOutputStream DMessage;

  public CDisconnectMessage()
  {
    DPacketFactory = new CNetworkPacketFactory();
    DMessage_stream = new ByteArrayOutputStream();
    DMessage = new DataOutputStream(DMessage_stream);
    MakeMessage();
  }

  public void MakeMessage()
  {
    int result = 0;
    DPacketFactory.DisconnectActionPacket(DMessage);
  }

  public byte[] GetMessage()
  {
    return DMessage_stream.toByteArray();
  }

  public int GetMessageLength()
  {
    return DMessage_stream.size();
  }
}
