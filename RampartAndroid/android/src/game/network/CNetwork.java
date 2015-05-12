
package game.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.*;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.lang.Thread;

public class CNetwork implements Runnable {
    protected ConcurrentLinkedQueue<CNetworkResponse> DEvents;
    protected ConcurrentLinkedQueue<CNetworkMessage> DEventsOut;
    private String DHost;
    private String DPort;
    protected String DUsername;
    CNetworkReceive DReceiver;
    protected boolean DConnected;

    public Socket DSocket;
    int IncomingProcessedCount = 0;
  
    public Thread DThread;

    //constructor
    public CNetwork() {
        DHost = "optical.cs.ucdavis.edu";
        DPort = "49999";
        DUsername = "";
        DConnected = false;
        DEvents = new ConcurrentLinkedQueue<CNetworkResponse>();
        DEventsOut = new ConcurrentLinkedQueue<CNetworkMessage>();
        DReceiver = new CNetworkReceive(DEvents);
        DThread = null;
    }

    public int ntohl(int value) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            return value;
        }
        return Integer.reverseBytes(value);
    }

    public short ntohs(short value) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            return value;
        }
        return Short.reverseBytes(value);
    }

    /**
     *
     * @return bool true if the connection was successful, false otherwise
     */
    protected boolean ConnectToServer() {
        try {
            //Let's get all ips this name resolves to.
            //and iterate through them to see which one works.
            InetAddress[] addresses = InetAddress.getAllByName(getDHost());
            for (InetAddress addr : addresses) {
                try {
                    DSocket = new Socket(addr, Integer.parseInt(getDPort()));
                    //We break if we didn't get an exception, which would mean
                    //something didn't work.
                    break;
                } catch(ConnectException ex){
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        try {
            DSocket.setTcpNoDelay(true);
        } catch (NullPointerException ex){
            ex.printStackTrace();
            return false;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void run() {
        DConnected = ConnectToServer();
        int bytes_sent = 0;
        DataOutputStream to_server = null;
        DataInputStream from_server = null;
        int bytes_available = 0;
        int msg_size = 0;
        byte[] buffer = null;
        try {
            to_server = new DataOutputStream(DSocket.getOutputStream());
            from_server = new DataInputStream(DSocket.getInputStream());
        } catch (Exception e) {
            //TODO log file for errors
            System.out.println("Failed to write.");
            DConnected = false;
        }
        while(DConnected)
        {
            int batch_size = 3;
            while(DEventsOut.size() > 0)
            {

                CNetworkMessage Message = DEventsOut.poll(); //look at front item but don't remove
                try {
                    to_server.write(Message.GetMessage(), 0, Message.GetMessageLength());
                    bytes_sent = to_server.size();
                } catch (Exception e) {
                    //TODO log file for errors
                    System.out.println("Failed to write.");
                    DConnected = false;
                    break;
                }
            }//while(DEventsOut.Size > 0)
            int i = 0;
            while(DConnected)
            {
                try {
                    bytes_available = from_server.available();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(bytes_available>0){
                    try {
                        buffer = new byte[CNetworkConstants.MAX_MESSAGE_SIZE];
                        msg_size = from_server.readInt();
                        from_server.read(buffer,0,msg_size-4);
                        DataInputStream msg = new DataInputStream(new ByteArrayInputStream(buffer));
                        DReceiver.ReceivePacket(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                } else {
                    bytes_available = 0;
                    break;
                }
                bytes_available = 0;

            }//while(DConnected)

        }//while(Dconnected)

        DConnected = false;
        try{
            DSocket.close();
            //join the thread
            DThread.interrupt();
            DThread.join();
            while(DEvents.size()>0){
                DEvents.poll();
            }
            while(DEventsOut.size()>0){
                DEventsOut.poll();
            }
        } catch (Exception e){
            //TODO add to log file for exceptions
            //problems closing socket
        }
    }

    protected boolean Status() {
        return DConnected;
    }

    public boolean Connect() {
        if (!DConnected) {
          //Either we haven't initialized it or it's dead.
          if(DThread == null || DThread != null && !DThread.isAlive())
          {
              try {
                  DThread = new Thread(this);
                  DThread.start();
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
        }
        return DConnected;
    }

    public void Disconnect() {
        if (DConnected) {
            try {
                DConnected = false;
                Thread.sleep(200,0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void LogIn(String username, String password) {
        DUsername = username;
        DEventsOut.add(new CAuthorizationMessage(username, password));
    }


    public void GetAvailableRooms() {
        DEventsOut.add(new CGetAvailableRoomsMessage(DUsername));
    }

    /**
     *
     * @param capacity capacity of the room
     * @param room_name name of the room
     */
    public void CreateRoom(short capacity, String room_name) {
        DEventsOut.add(new CCreateRoomMessage(capacity, DUsername, room_name));
    }

    public void JoinRoom(String room_name) {
        DEventsOut.add(new CJoinRoomMessage(DUsername, room_name));
    }

    public void LeaveRoom(String room_name) {
        DEventsOut.add(new CLeaveRoomMessage(DUsername, room_name));
    }

    public void StartGame(String map_data) {
        DEventsOut.add(new CStartGameMessage(DUsername, map_data));
    }

    public void InGameAction(short action, int time_step, short player_id, int x, int y){
        DEventsOut.add(new CInGameActionMessage(action, time_step, player_id, x, y));
    }

    public void Update(){
        while(DEvents.size() > 0){
            CNetworkResponse response = DEvents.poll();
            response.process();
        }
    }

    public String getDHost() {
        return DHost;
    }

    public void setDHost(String DHost) {
        this.DHost = DHost;
    }

    public String getDPort() {
        return DPort;
    }

    public void setDPort(String DPort) {
        this.DPort = DPort;
    }
}
