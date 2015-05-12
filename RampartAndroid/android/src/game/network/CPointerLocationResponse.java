package game.network;

public class CPointerLocationResponse implements CNetworkResponse {
    protected int DTimestep;
    protected int DPlayerID;
    protected int DXCoordinate;
    protected int DYCoordinate;

    CPointerLocationResponse(int time_step, short player_id, int x, int y) {
        super();
        DTimestep = time_step;
        DPlayerID = player_id;
        DXCoordinate = x;
        DYCoordinate = y;
    }

    @Override
    //Modify to add game class type as input and implementation.
    public void process() {
        if(CNetworkConstants.TESTING){
            System.out.println("Action: "+CNetworkConstants.POINTER_LOCATION+"Received Response.");
        }

    }

}
