package game.network;

public class CLoginResponse implements CNetworkResponse {
  short DResult = -1;

  public CLoginResponse(short result)
  {
    DResult = result;
  }

  //Modify to add game class type as input and implementation.
  @Override
  public void process() {
      if(CNetworkConstants.TESTING){
          if(DResult == 1 || DResult == 0){
              System.out.println("Result received correctly");
          } else {
              System.out.println("Incorrect result received");
          }
      }



  }
}
