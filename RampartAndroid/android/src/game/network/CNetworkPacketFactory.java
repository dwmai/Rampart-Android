package game.network;

import java.nio.ByteOrder;
import java.io.*;

public class CNetworkPacketFactory {
    private static DataOutputStream CurrentMessage;

    public CNetworkPacketFactory() {
        //ctor
    }

    public int AuthActionPacket(DataOutputStream DMessage, String username, String password) {
        CurrentMessage = DMessage;
        int packet_size;
        short action = (short) CNetworkConstants.CLIENT_AUTH;
        int length = 0;

        //4 = size of packet_size, 2 = size of action, +2 = the two commas
        //Strings count the null terminating character so this should be the right sum.
        packet_size = 4 + 2 + username.length() + password.length()+2;

        if (packet_size > CNetworkConstants.MAX_MESSAGE_SIZE) {
            System.out.println("Buffer provided is too small\n");
            return -1;
        }
        SetUpHeader(packet_size, action);
        try {
            CurrentMessage.writeBytes(username);
            CurrentMessage.writeByte(0x2c);
            CurrentMessage.writeBytes(password);
            CurrentMessage.writeByte(0x2c);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Finishd template doesn't match size or an error occured creating it
        if (CurrentMessage.size() != packet_size) return -1;

        return CurrentMessage.size();
    }

    public int GetAvailableRoomsActionPacket(DataOutputStream DMessage, String username) {
        CurrentMessage = DMessage;
        int packet_size;
        short action = (short) CNetworkConstants.GET_AVAILABLE_ROOMS;

        //4 = size of packet_size, 2 = size of action, +1 = the comma
        //Strings count the null terminating character so this should be the right sum.
        packet_size = 4 + 2 + username.length()+1;

        if (packet_size > CNetworkConstants.MAX_MESSAGE_SIZE) {
            System.out.println("Buffer provided is too small\n");
            return -1;
        }

        SetUpHeader(packet_size, action);
        try {
            CurrentMessage.writeBytes(username);
            CurrentMessage.writeByte(0x2c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Finished packet doesn't match expected size.
        if (CurrentMessage.size() != packet_size) return -1;

        return packet_size;
    }

    public int CreateRoomActionPacket(DataOutputStream DMessage, short capacity, String username, String room_name) {
        CurrentMessage = DMessage;
        int packet_size;
        short action = (short) CNetworkConstants.CREATE_ROOM;

        //4 = size of packet_size, 2 = size of action, 2 = size of capacity, +2 = the commas
        //Strings count the null terminating character so this should be the right sum.
        packet_size = 4 + 2 + 4 + username.length() + room_name.length();

        SetUpHeader(packet_size, action);
        try {
            CurrentMessage.writeShort(capacity);
            CurrentMessage.writeBytes(username);
            CurrentMessage.writeByte(0x2c);
            CurrentMessage.writeBytes(room_name);
            CurrentMessage.writeByte(0x2c);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to buffer\n");
            return -1;
        }
        //TODO htons here working?

        //Finished template doesn't match size or an error occured creating it.
        if (packet_size != CurrentMessage.size()) {
            System.out.println("Incorrectly sized packet."+packet_size+","+CurrentMessage.size()+"\n");
            return -1;
        }
        return CurrentMessage.size();
    }

    public int JoinRoomActionPacket(DataOutputStream DMessage, String username, String room_name) {
        CurrentMessage = DMessage;
        int packet_size;
        short action = (short) CNetworkConstants.JOIN_ROOM;

        //4 = size of packet_size, 2 = size of action, +2 = the commas
        //Strings count the null terminating character so this should be the right sum.
        packet_size = 4 + 2 + username.length() + room_name.length()+2;

        SetUpHeader(packet_size, action);

        try {
            CurrentMessage.writeBytes(username);
            CurrentMessage.writeByte(0x2c);
            CurrentMessage.writeBytes(room_name);
            CurrentMessage.writeByte(0x2c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Finished template doesn't match size or an error occured creating it.
        if (CurrentMessage.size() != packet_size) return -1;

        return CurrentMessage.size();
    }

    public int DisconnectActionPacket(DataOutputStream DMessage) {
        CurrentMessage = DMessage;
        int packet_size;
        short action = (short) CNetworkConstants.DISCONNECT;
        int length = 0;

        //4 = size of packet_size, 2 = size of action
        packet_size = 4 + 2;

        SetUpHeader(packet_size, action);

        if (CurrentMessage.size() != packet_size) return -1;

        return packet_size;
    }

    public int LeaveRoomActionPacket(DataOutputStream DMessage, String username, String room_name) {
        CurrentMessage = DMessage;
        int packet_size;
        short action = (short) CNetworkConstants.LEAVE_ROOM;

        //4 = size of packet_size, 2 = size of action, +2 = commas
        //Strings count the null terminating character so this should be the right sum.
        packet_size = 4 + 2 + username.length() + room_name.length()+2;



        SetUpHeader(packet_size, action);

        try {
            CurrentMessage.writeBytes(username);
            CurrentMessage.writeByte(0x2c);
            CurrentMessage.writeBytes(room_name);
            CurrentMessage.writeByte(0x2c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Finished template doesn't match size or an error occured creating it.
        if (CurrentMessage.size() != packet_size) return -1;

        return CurrentMessage.size();
    }

    public int EndGameActionPacket(DataOutputStream DMessage, int playerID, int winnerID)
    {
      CurrentMessage = DMessage;
      int packet_size;
      short action = (short) CNetworkConstants.END_GAME;
      short player_ID = (short) playerID;
      short winner_ID = (short) winnerID;
      int length = 0;
 
      packet_size = 4 + 3*2;

      if(packet_size > CNetworkConstants.MAX_MESSAGE_SIZE)
      {
        return -1;
      }
      length += SetUpHeader(packet_size, action);
      length += 2;        
 
      try
      {
        CurrentMessage.writeInt(player_ID);
        CurrentMessage.writeInt(winner_ID);
      } catch (IOException e) {
        e.printStackTrace();
      }

      if(length != packet_size) return -1;
      
      return packet_size;
    }

    public int StartGameActionPacket(DataOutputStream DMessage, String username, String map_data) {
        CurrentMessage = DMessage;
        int packet_size;
        short action = (short) CNetworkConstants.START_GAME;

        //4 = size of packet_size, 2 = size of action, +2 for commas
        //Strings count the null terminating character so this should be the right sum.
        packet_size = 4 + 2 + username.length() + map_data.length()+2;

        SetUpHeader(packet_size, action);
        try {
            CurrentMessage.writeBytes(username);
            CurrentMessage.writeByte(0x2c);
            CurrentMessage.writeBytes(map_data);
            CurrentMessage.writeByte(0x2c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Finished template doesn't match size or an error occured creating it/
        if (CurrentMessage.size() != packet_size) return -1;

        return CurrentMessage.size();
    }

    public int ActionPacket(DataOutputStream DMessage, short action, int time_step, short player_id, int x_coordinate, int y_coordinate) {
        CurrentMessage = DMessage;
        //calculated packet_size
        int packet_size;
        //Actual packet length

        //4 = size of packet_size, 2 = size of action, 4 = size of time_step, 2 = size of player_id, 4 = size of x_coordinate, 4 = size of y_coordinate
        packet_size = 4 + 2 + 4 + 2 + 4 + 4;

        SetUpHeader(packet_size, action);

        //TODO check that these htonl's work
        //4 = size of time_step

        try {
            CurrentMessage.writeInt(time_step);
            CurrentMessage.writeShort(player_id);
            CurrentMessage.writeInt(x_coordinate);
            CurrentMessage.writeInt(y_coordinate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Let's make sure the size we wrote in the packet is correct.
        if (CurrentMessage.size() != packet_size) {
            return -1;
        }
        return CurrentMessage.size();
    }

    public int SetUpHeader(int packet_size, short action) {
        int length = 0;
        //change byte order to big endian
        try {
            CurrentMessage.writeInt(packet_size);
            CurrentMessage.writeShort(action);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CurrentMessage.size();
    }

}
