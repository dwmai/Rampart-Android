package game.network;

public class CFiredCannonResponse extends CPointerLocationResponse {

    CFiredCannonResponse(int time_step, short player_id, int x, int y) {
        super(time_step, player_id, x, y);
    }

    @Override
    //Modify to add game class type as input and implementation.
    public void process() {
        System.out.println("Action: "+CNetworkConstants.FIRED_CANNON+"Received Response.");
    }
}
