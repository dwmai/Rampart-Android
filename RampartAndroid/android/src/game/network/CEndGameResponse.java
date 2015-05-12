package game.network;

public class CEndGameResponse implements CNetworkResponse
{
  short DResult;
  protected short DWinnerID;

  public CEndGameResponse(int winnerID)
  {
    DResult = 1;
    DWinnerID = (short) winnerID;
  }

  //Modify to add game class type as input and implementation.
  @Override
  public void process()
  {

  }
}
