
package game.network;

import junit.framework.TestCase;
import java.io.*;


public class CNetworkTest extends TestCase {
    String testhost = "10.0.0.4";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final static int timeout = 1000;

    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    public void testConnectToServer() throws Exception {
        CNetwork network = new CNetwork();
        network.setDHost(testhost);
        assertEquals("Client should be able to connect to the server.s",true, network.ConnectToServer());
        network.DSocket.close();
    }

    public void testThread() throws Exception {
        CNetwork network = new CNetwork();
        network.setDHost(testhost);
        network.Connect();
        Thread.sleep(timeout,0);
        assertEquals("A new thread should be created after calling connect.",true,network.DThread.isAlive());
        network.DConnected = false;
    }

    public void testConnect() throws Exception {
        CNetwork network = new CNetwork();
        network.setDHost(testhost);
        network.Connect();
        Thread.sleep(timeout,0);
        assertEquals("It should have a status of connected.",true,network.Status());
        network.DConnected = false;
        Thread.sleep(timeout,0);
    }

    public void testDisconnect() throws Exception {
        CNetwork network = new CNetwork();
        network.setDHost(testhost);
        network.Connect();
        Thread.sleep(timeout,0);
        assertEquals("It should have a status of connected.",true,network.Status());
        network.Disconnect();
        Thread.sleep(timeout,0);
        assertEquals("It should have a status of disconnected.",false,network.Status());
        network.DConnected = false;
    }

    public void testLogIn() throws Exception {
        setUpStreams();
        CNetwork network = new CNetwork();
        network.setDHost(testhost);
        network.Connect();
        Thread.sleep(timeout,0);
        assertEquals("It should have a status of connected.",true,network.Status());
        String username = "username";
        String password = "password";
        network.LogIn(username,password);
        Thread.sleep(timeout, 0);
        network.Update();
        assertEquals("Result received correctly", outContent.toString().trim());
        network.Disconnect();
        Thread.sleep(timeout, 0);
        cleanUpStreams();
    }

    public void testEverything() throws Exception{
        setUpStreams();
        //Create player1
        CNetwork network = new CNetwork();
        network.setDHost(testhost);

        //Connect player1
        network.Connect();
        Thread.sleep(timeout,0);
        assertEquals("It should have a status of connected.",true,network.Status());

        //Login player 1
        String username = "username";
        String password = "password";
        network.LogIn(username,password);
        Thread.sleep(timeout, 0);
        network.Update();
        assertEquals("Result received correctly", outContent.toString().trim());
        outContent.reset();

        //Player 1 creates room.
        short capacity = 2;
        String room_name = "room2";
        int result = 1;
        network.CreateRoom(capacity, room_name);
        Thread.sleep(timeout, 0);
        network.Update();
        assertEquals("We should get a well formed response.", "Received Correct Response", outContent.toString().trim());
        outContent.reset();

        //Crate player2
        CNetwork network2 = new CNetwork();
        network2.setDHost(testhost);

        //Connect player2
        network2.Connect();
        Thread.sleep(timeout,0);
        assertEquals("It should have a status of connected.",true,network.Status());
        outContent.reset();

        //Login player2
        String username2 = "username2";
        String password2 = "password2";
        network2.LogIn(username2,password2);
        Thread.sleep(timeout, 0);
        network2.Update();
        assertEquals("Result received correctly", outContent.toString().trim());
        outContent.reset();

        //Requesting available rooms
        network2.GetAvailableRooms();
        Thread.sleep(timeout, 0);
        network2.Update();
        assertEquals("Received rooms", outContent.toString().trim());
        outContent.reset();

        //Second Player Joins Room
        network2.JoinRoom(room_name);
        Thread.sleep(timeout, 0);
        //Let's make sure we got the right response
        network2.Update();
        assertEquals("Room received: "+room_name+" Player: "+username, outContent.toString().trim());
        outContent.reset();

        //First player should receive a player joined room packet.
        network.Update();
        assertEquals("Received well formed CPlayerJoined Response", outContent.toString().trim());
        outContent.reset();

        //Second player leaves room
        network2.LeaveRoom(room_name);
        Thread.sleep(timeout, 0);

        //First player should be notified of this.
        network.Update();
        assertEquals("Received CPlayerLeftResponse in queue.", outContent.toString().trim());
        outContent.reset();

        //Second Player Joins Room
        network2.JoinRoom(room_name);
        Thread.sleep(timeout, 0);
        //Let's make sure we got the right response
        network2.Update();
        assertEquals("Room received: "+room_name+" Player: "+username, outContent.toString().trim());
        outContent.reset();

        //First player should receive a player joined room packet.
        network.Update();
        assertEquals("Received well formed CPlayerJoined Response", outContent.toString().trim());
        outContent.reset();

        //Player1 starts game
        network.StartGame("thisisafakemap");
        Thread.sleep(timeout, 0);
        //Both players should receive a response from the server.
        network.Update();
        assertEquals("Received CStartGameResponse in queue.", outContent.toString().trim());
        outContent.reset();

        network2.Update();
        assertEquals("Received CStartGameResponse in queue.", outContent.toString().trim());
        outContent.reset();

        int time_step = 0;
        while(time_step< 100){
            //Here we pick a random game action to send.
            short action = (short) (10+Math.random()*5);
            if(action < 10 && action > 15 ){
                System.out.println("Action code out of range");
            }
            time_step++;
            short player1 = 1;
            short player2 = 2;
            network.InGameAction(action, time_step,player2,5,5);
            network2.InGameAction(action,time_step,player2,10,10);
            Thread.sleep(timeout, 0);
            network.Update();
            //Let's make sure we received the correct action response.
            assertEquals("Action: "+action+"Received Response.\r\n" +
                    "Action: "+action+"Received Response.", outContent.toString().trim());
            outContent.reset();
            network2.Update();
            assertEquals("Action: "+action+"Received Response.\r\n" +
                    "Action: "+action+"Received Response.", outContent.toString().trim());
            outContent.reset();
        }

        //Disconnect player1
        network.Disconnect();
        //Disconnect player2
        network2.Disconnect();
        Thread.sleep(timeout, 0);
        cleanUpStreams();

    }
}