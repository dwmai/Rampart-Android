package game.network;

import java.util.*;

public class CRoomsAvailableResponse implements CNetworkResponse
{
  short DResult;

  protected Vector<String> DRooms;

  public CRoomsAvailableResponse(Vector <String> rooms)
  {
    DRooms = rooms;
  }

  //Modify to add game class type as input and implementation
  @Override
  public void process()
  {
      if(CNetworkConstants.TESTING){
          if(DRooms.size() >0){
            System.out.println("Received rooms");
          }

      }

  }
}
