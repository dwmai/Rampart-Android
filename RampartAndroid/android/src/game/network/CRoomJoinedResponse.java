package game.network;

import java.util.*;

public class CRoomJoinedResponse implements CNetworkResponse
{
  short DResult;
  protected Vector<String> DPlayers = null;
  protected int DCapacity = -1;
  protected String DRoomName = "";

  public CRoomJoinedResponse(Vector<String> players, int player_count, String roomname)
  {
    DResult = 1;
    DPlayers = players;
    DCapacity = player_count;
    DRoomName = roomname;
  }

  //Modify to add game class type as input and implementation.
  @Override
  public void process()
  {
      if(CNetworkConstants.TESTING){
          System.out.println("Room received: "+DRoomName+" Player: "+DPlayers.firstElement());
      }


  }
}
