package game.network;

public class CRoomCreatedResponse implements CNetworkResponse
{
  short DResult =-1;
  public String DResponseRoomName = null;

  CRoomCreatedResponse(int respresult, String resproom_name)
  {
    DResult = (short) respresult;
    DResponseRoomName = resproom_name;
  }

  //Modify to add game class type as input and implementation
  @Override
  public void process()
  {
      if(CNetworkConstants.TESTING){
          if(DResult == 0 || DResult == 1 && DResponseRoomName != null)
              System.out.println("Received Correct Response");
      }

  }
}
