package game.network;

public class CRoomDeniedResponse implements CNetworkResponse 
{
  short DResult;
  public String DRoomName;

  public CRoomDeniedResponse(String respRoomName)
  {
    DResult = 1;
    DRoomName = respRoomName;
  }

  //Modifiy to add game class type as input and implementation
  @Override
  public void process() {

  }
}
