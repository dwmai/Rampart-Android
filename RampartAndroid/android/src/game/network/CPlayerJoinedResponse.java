package game.network;

public class CPlayerJoinedResponse implements CNetworkResponse
{
  short DResult;
  protected String DUsername = null;
  protected String DRoomName = null;
 
  public CPlayerJoinedResponse(String username, String roomname)
  {
    DResult = 1;
    DUsername = username;  
    DRoomName = roomname;
  }

  //Modify to add game class type as input and implementation.
  @Override
  public void process() {
      if(CNetworkConstants.TESTING){
          if(DUsername != null && DRoomName != null){
            System.out.println("Received well formed CPlayerJoined Response");
          }
      }
  }
}
