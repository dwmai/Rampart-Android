package game.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CInGameActionMessage extends CNetworkMessage
{
  protected CNetworkPacketFactory DPacketFactory;
  protected short DAction;
  protected int DTimeStep;
  protected short DPlayerID;
  protected int DXCoordinate;
  protected int DYCoordinate;
  protected ByteArrayOutputStream DMessage_stream;
  protected DataOutputStream DMessage;

  public CInGameActionMessage(short action, int time_step, int player_id, int x_coordinate, int y_coordinate)
  {
    DAction = action;
    DTimeStep = time_step;
    DPlayerID = (short) player_id;
    DXCoordinate = x_coordinate;
    DYCoordinate = y_coordinate;
    
    DMessage_stream = new ByteArrayOutputStream();
    DMessage = new DataOutputStream(DMessage_stream);
  
    DPacketFactory = new CNetworkPacketFactory();
    MakeMessage();
  }

  public void MakeMessage()
  {
    int result = 0;
    DPacketFactory.ActionPacket(DMessage, DAction, DTimeStep, DPlayerID, DXCoordinate, DYCoordinate);
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
