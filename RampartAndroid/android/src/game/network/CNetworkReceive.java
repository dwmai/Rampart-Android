package game.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.nio.ByteOrder;
import java.net.Socket;


public class CNetworkReceive {
    protected Socket DSocket;
    protected ConcurrentLinkedQueue<CNetworkResponse> DEvents;
    protected boolean DInGame;

    public CNetworkReceive(ConcurrentLinkedQueue<CNetworkResponse> Events) {
        DEvents = Events;
    }

    public void ReceivePacket(DataInputStream from_server) {
        int size;
        short actionCode;
        try {
            DataInputStream messageReader = from_server;
            actionCode = messageReader.readShort();
            //TODO log
            switch (actionCode) {
                //AUTH_ACTION
                case CNetworkConstants.SERVER_AUTH_ACTION:
                    ParseAuthActionPacket(messageReader);
                    //DInGame = false;
                    break;
                //SEND_AVAILABLE_ROOMS
                case CNetworkConstants.SERVER_SEND_AVAILABLE_ROOMS_ACTION:
                    ParseSendAvailableRoomsPacket(messageReader);
                    //DInGame = false;
                    //DInGame = false;
                    break;
                //NEW_ROOM_RESPONSE
                case CNetworkConstants.SERVER_CREATE_ROOM_ACTION:
                    ParseNewRoomResponsePacket(messageReader);
                    //DInGame = false;
                    break;
                //ALLOW_JOIN_ROOM
                case CNetworkConstants.SERVER_ALLOW_JOIN_ROOM_ACTION:
                    ParseAllowJoinRoomPacket(messageReader);
                    //DInGame = false;
                    break;
                //DENY_JOIN_ROOM
                case CNetworkConstants.SERVER_DENY_JOIN_ROOM_ACTION:
                    ParseDenyJoinRoomPacket(messageReader);
                    //DInGame = false;
                    break;
                //PLAYER_JOINED_ROOM
                case CNetworkConstants.SERVER_PLAYER_JOINED_ROOM_ACTION:
                    ParsePlayerJoinedRoomPacket(messageReader);
                    break;
                //LEAVE_ROOM
                case CNetworkConstants.SERVER_LEAVE_ROOM_ACTION:
                    ParseLeaveRoomPacket(messageReader);
                    //DInGame = false;
                    break;
                case CNetworkConstants.SERVER_OWNER_LEFT_ROOM_ACTION:
                    ParseOwnerLeftRoomPacket(messageReader);
                    //DInGame = false;
                    break;
                //SERVER_START_GAME
                case CNetworkConstants.SERVER_START_GAME_ACTION:
                    ParseStartGamePacket(messageReader);
                    //DInGame = true;
                    break;
                case CNetworkConstants.POINTER_LOCATION:
                    ParseActionPacket(messageReader, actionCode);
                    //DInGame = true;
                    break;
                case CNetworkConstants.SELECTED_CASTLE:
                    ParseActionPacket(messageReader, actionCode);
                    //DInGame = true;
                    break;
                case CNetworkConstants.PLACED_CANNON:
                    ParseActionPacket(messageReader, actionCode);
                    //DInGame = true;
                    break;
                case CNetworkConstants.FIRED_CANNON:
                    ParseActionPacket(messageReader, actionCode);
                    //DInGame = true;
                    break;
                case CNetworkConstants.ROTATED_WALL:
                    ParseActionPacket(messageReader, actionCode);
                    //DInGame = true;
                    break;
                case CNetworkConstants.PLACED_WALL:
                    ParseActionPacket(messageReader, actionCode);
                    //DInGame = true;
                    break;
                case CNetworkConstants.END_GAME:
                    ParseEndGamePacket(messageReader);
                    //DInGame = false;
                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ParseAuthActionPacket(DataInputStream messageReader) {
        short result;
        try {
            result = messageReader.readShort();
            DEvents.add(new CLoginResponse(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ParseSendAvailableRoomsPacket(DataInputStream messageReader)
    {
        int numberOfRooms;
        Vector<String> Rooms = new Vector<String>();
        try {
            numberOfRooms = messageReader.readShort();
            byte[] buff = new byte[messageReader.available()];
            messageReader.read(buff);
            String rooms = new String(buff);
            String[] tokens = rooms.split(",");
            Rooms.addAll(Arrays.asList(tokens));
            DEvents.add(new CRoomsAvailableResponse(Rooms));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ParseNewRoomResponsePacket(DataInputStream messageReader)
    {
        try {
            short result = messageReader.readShort();
            byte[] buff = new byte[messageReader.available()];
            messageReader.read(buff);
            String rooms = new String(buff);
            String[] rooms_array = rooms.split(",");
            DEvents.add(new CRoomCreatedResponse(result,rooms_array[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ParseAllowJoinRoomPacket(DataInputStream messageReader)
    {
        int player_count = 0;
        Vector<String> Players = new Vector<String>();
        String room_name;
        try {
            player_count = messageReader.readShort();
            byte[] buff = new byte[messageReader.available()];
            messageReader.read(buff);
            String payload = new String(buff);
            String[] tokens = payload.split(",");
            room_name = tokens[0];
            for(int i = 1; i < tokens.length-1; i++){
                Players.add(tokens[i]);
            }
            DEvents.add(new CRoomJoinedResponse(Players, player_count,room_name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ParseDenyJoinRoomPacket(DataInputStream messageReader)
    {
        byte [] pack = new byte[CNetworkConstants.MAX_MESSAGE_SIZE];
        System.out.println("Result received: ");
        try {
            messageReader.read(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String packet = new String(pack);

        String cptr;
        cptr = packet.substring(0, packet.indexOf(","));
        packet = packet.substring(packet.indexOf(",")+1, packet.length());
        String room_name  = cptr;
        //NOTE: RoomDeniedResponse's process() not implemented
        // DNetworkInMutex->Lock();
        DEvents.add(new CRoomDeniedResponse(room_name));
        //DNetworkInMutex->Unlock();
    }

    private void ParsePlayerJoinedRoomPacket(DataInputStream messageReader)
    {
        byte [] pack = new byte[CNetworkConstants.MAX_MESSAGE_SIZE];
        try {
            messageReader.read(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String packet = new String(pack);

        String cptr;
        cptr = packet.substring(0, packet.indexOf(","));
        packet = packet.substring(packet.indexOf(",")+1, packet.length());
        String room_name = cptr;
        cptr = packet.substring(0, packet.indexOf(","));
        packet = packet.substring(packet.indexOf(",")+1, packet.length());
        String username  = cptr;
        //DNetworkInMutex->Lock();
        DEvents.add(new CPlayerJoinedResponse(username, room_name));
        //DNetworkInMutex->Unlock();
    }

    private void ParseLeaveRoomPacket(DataInputStream messageReader)
    {
        byte [] pack = new byte[CNetworkConstants.MAX_MESSAGE_SIZE];
        try {
            messageReader.read(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String packet = new String(pack);

        String cptr;
        cptr = packet.substring(0, packet.indexOf(","));
        packet = packet.substring(packet.indexOf(",")+1, packet.length());
        String username = cptr;
        cptr = packet.substring(0, packet.indexOf(","));
        packet = packet.substring(packet.indexOf(",")+1, packet.length());
        String room_name  = cptr;
        //DNetworkInMutex->Lock();
        DEvents.add(new CPlayerLeftResponse(username, room_name));
        //DNetworkInMutex->Unlock();
    }

    private void ParseOwnerLeftRoomPacket(DataInputStream messageReader)
    {
        byte [] pack = new byte[CNetworkConstants.MAX_MESSAGE_SIZE];
        try {
            messageReader.read(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String packet = new String(pack);

        String cptr;
        cptr = packet.substring(0, packet.indexOf(","));
        packet = packet.substring(packet.indexOf(",")+1, packet.length());
        String username = cptr;
        cptr = packet.substring(0, packet.indexOf(","));
        packet = packet.substring(packet.indexOf(",")+1, packet.length());
        String room_name = cptr;
        //DNetworkInMutex->Lock();
        DEvents.add(new COwnerLeftRoomResponse(username, room_name));
        //DNetworkInMutex->Unlock();
    }

    private void ParseStartGamePacket(DataInputStream messageReader) {
        int num_players = 0;
        Vector<String> players = new Vector<String>();
        //NOTE: new MAP field
        String map;
        try {
            num_players = messageReader.readShort();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){

        }
        if(num_players <= 0)
        {
            return;
        }

        //NOTE: new id fields
        int[] player_ids = new int[num_players];
        for(int i = 0; i < num_players; i++)
        {
            try {
                player_ids[0] = messageReader.readShort();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //NOTE: new username fields
        String cptr;
        byte [] pack = new byte[CNetworkConstants.MAX_MESSAGE_SIZE];
        try {
            messageReader.read(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String packet = new String(pack);
        String[] tokens = packet.split(",");
        int i = 0;
        for(i = 0; i < tokens.length-1; i++)
        {
            players.add(tokens[i]);
        }
        //don't need to substring the packet as we don't need any more info from it
        map = tokens[i];
        DEvents.add(new CStartGameResponse(players, map));
        //DNumberOfPlayers = num_players;
    }

    private void ParseEndGamePacket(DataInputStream messageReader)
    {
        int winnderID = 0;
        try {
            winnderID = messageReader.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DEvents.add(new CEndGameResponse(winnderID));
        //DNumberOfPlayers = -1;
    }

    private void ParseActionPacket(DataInputStream messageReader, short action_code)
    {
        int x_coord = 0;
        int y_coord = 0;
        int time_step = 0;
        short player_ID = 0;
        try {
            time_step = messageReader.readInt();
            player_ID = messageReader.readShort();
            x_coord = messageReader.readInt();
            y_coord = messageReader.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch(action_code)
        {
            case CNetworkConstants.POINTER_LOCATION:
                DEvents.add(new CPointerLocationResponse(time_step, player_ID, x_coord, y_coord));
                break;
            case CNetworkConstants.SELECTED_CASTLE:
                DEvents.add(new CSelectedCastleResponse(time_step, player_ID, x_coord, y_coord));
                break;
            case CNetworkConstants.PLACED_CANNON:
                DEvents.add(new CPlacedCannonResponse(time_step, player_ID, x_coord, y_coord));
                break;
            case CNetworkConstants.FIRED_CANNON:
                DEvents.add(new CFiredCannonResponse(time_step, player_ID, x_coord, y_coord));
                break;
            case CNetworkConstants.ROTATED_WALL:
                DEvents.add(new CRotatedWallResponse(time_step, player_ID, x_coord, y_coord));
                break;
            case CNetworkConstants.PLACED_WALL:
                DEvents.add(new CPlacedWallResponse(time_step, player_ID, x_coord, y_coord));
                break;
            default:
                break;
        }
    }
}
