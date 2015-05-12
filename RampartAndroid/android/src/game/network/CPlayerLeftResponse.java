package game.network;

public class CPlayerLeftResponse implements CNetworkResponse 
{
  short DResult;
  protected String DUsername;
  protected String DRoomname;

  public CPlayerLeftResponse(String username, String roomname)
  {
    DResult = 1;
    DUsername = username;
    DRoomname = roomname;
  }

  //Modify to add game class type as input and implementation
  @Override
  public void process() {
      if(CNetworkConstants.TESTING){
          System.out.println("Received CPlayerLeftResponse in queue.");
      }
  }
}
