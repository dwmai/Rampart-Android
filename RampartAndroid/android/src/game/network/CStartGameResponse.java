package game.network;

import java.util.*;

public class CStartGameResponse implements CNetworkResponse
{
  short DResult;
  protected String DMap;
  protected Vector<String> DPlayers;

  public CStartGameResponse(Vector<String> players, String map)
  {
    DResult = 1;
    DPlayers = players;
    DMap = map;
  }

  //Modify to add game class type as input and implementation.
  @Override
  public void process()
  {
      if(CNetworkConstants.TESTING){
          System.out.println("Received CStartGameResponse in queue.");
      }

  }
}
