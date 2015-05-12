package game.network;

public class COwnerLeftRoomResponse implements CNetworkResponse
{
  short DResult;
  protected String DUsername;
  protected String DRoomName;

  public COwnerLeftRoomResponse(String username, String roomname)
  {
    DResult = 1;
    DUsername = username;
    DRoomName = roomname;
  }

  //Modify to add game class type as input and implementation.
  @Override
  public void process() {
      if(CNetworkConstants.TESTING){
          System.out.println("Received Response.");
      }
  }
}
