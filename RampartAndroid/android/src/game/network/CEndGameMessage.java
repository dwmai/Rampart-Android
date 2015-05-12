package game.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CEndGameMessage extends CNetworkMessage
{
  protected CNetworkPacketFactory DPacketFactory;
  protected int DPlayerID;
  protected int DWinnerID;
  protected ByteArrayOutputStream DMessage_stream;
  protected DataOutputStream DMessage;

  public CEndGameMessage(int playerID, int winnerID)
  {
    DPlayerID = playerID;
    DWinnerID = winnerID;

    DPacketFactory = new CNetworkPacketFactory();
    
    DMessage_stream = new ByteArrayOutputStream();
    DMessage = new DataOutputStream(DMessage_stream);
  
    DPacketFactory.EndGameActionPacket(DMessage, DPlayerID, DWinnerID);
  }

  public byte[] GetMessage()
  {
    return DMessage_stream.toByteArray();
  }

  public int GetMessageLength()
  {
    return DMessage_stream.size();
  }

//  protected void MakeMessage()
//  {
//  }
}
