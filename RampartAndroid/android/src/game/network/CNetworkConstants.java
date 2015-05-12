package game.network;

public class CNetworkConstants {
    //Packets sent by client only
    public static final int CLIENT_AUTH = 0;
    public static final int GET_AVAILABLE_ROOMS = 1;
    public static final int CREATE_ROOM = 2;
    public static final int JOIN_ROOM = 3;
    public static final int DISCONNECT = 4;
    public static final int LEAVE_ROOM = 6;
    public static final int START_GAME = 7;

    //Packets sent by Server only
    public static final int SERVER_AUTH_ACTION = 0;
    public static final int SERVER_SEND_AVAILABLE_ROOMS_ACTION = 1;
    public static final int SERVER_CREATE_ROOM_ACTION = 2;
    public static final int SERVER_ALLOW_JOIN_ROOM_ACTION = 3;
    public static final int SERVER_DENY_JOIN_ROOM_ACTION = 4;
    public static final int SERVER_PLAYER_JOINED_ROOM_ACTION = 5;
    public static final int SERVER_LEAVE_ROOM_ACTION = 6;
    public static final int SERVER_OWNER_LEFT_ROOM_ACTION = 7;
    public static final int SERVER_START_GAME_ACTION = 8;

    //Packets sent by both the client and the server
    public static final int POINTER_LOCATION = 10;
    public static final int SELECTED_CASTLE = 11;
    public static final int PLACED_CANNON = 12;
    public static final int FIRED_CANNON = 13;
    public static final int ROTATED_WALL = 14;
    public static final int PLACED_WALL = 15;
    public static final int END_GAME = 16;
    public static final int MAX_MESSAGE_SIZE = 4096;
    public static final boolean TESTING = true;

}
