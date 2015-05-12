package game.network;

import junit.framework.TestCase;

public class CPointerLocationResponseTest extends TestCase {

    public void testProcess() throws Exception {
        int timestep = 0;
        short playerID = 1;
        int xpos = 24;
        int ypos = 56;
        CPointerLocationResponse location = new CPointerLocationResponse(timestep, playerID, xpos, ypos);

        assertEquals("DTimestep must be 0", 0, location.DTimestep);
        assertEquals("DPlayerID must be 1", playerID, location.DPlayerID);
        assertEquals("DXCoordinate must be 24", 24, location.DXCoordinate);
        assertEquals("DYCoordinate must be 24", 56, location.DYCoordinate);
    }
}