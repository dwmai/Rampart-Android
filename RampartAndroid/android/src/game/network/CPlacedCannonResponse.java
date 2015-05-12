package game.network;

public class CPlacedCannonResponse extends CPointerLocationResponse {

    CPlacedCannonResponse(int time_step, short player_id, int x, int y) {
        super(time_step, player_id, x, y);
    }

    @Override
    //Modify to add game class type as input and implementation.
    public void process() {
        if(CNetworkConstants.TESTING){
            System.out.println("Action: "+CNetworkConstants.PLACED_CANNON+"Received Response.");
        }
    }
}
