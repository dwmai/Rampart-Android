package game.animations;

public final class DefineConstants {
    public static final int CASTLE_ANIMATION_TIMESTEPS = 4;
	public static final int TERRAIN_ANIMATION_TIMESTEPS = 4;
    public static final int TIMEOUT_INTERVAL = 50;
    public static final int RANDOM_NUMBER_MAX = 1000000;
    public static final int WINDSPEED_COUNT = 8;
    public static final int WINDDIRECTION_COUNT = 9;
	public static final int CLIENT_AUTH = 0;
	public static final int GET_AVAILABLE_ROOMS = 1;
	public static final int CREATE_ROOM = 2;
	public static final int JOIN_ROOM = 3;
	public static final int DISCONNECT = 4;
	public static final int LEAVE_ROOM = 6;
	public static final int START_GAME = 7;
	public static final int SERVER_AUTH_ACTION = 0;
	public static final int SERVER_SEND_AVAILABLE_ROOMS_ACTION = 1;
	public static final int SERVER_CREATE_ROOM_ACTION = 2;
	public static final int SERVER_ALLOW_JOIN_ROOM_ACTION = 3;
	public static final int SERVER_DENY_JOIN_ROOM_ACTION = 4;
	public static final int SERVER_PLAYER_JOINED_ROOM_ACTION = 5;
	public static final int SERVER_LEAVE_ROOM_ACTION = 6;
	public static final int SERVER_OWNER_LEFT_ROOM_ACTION = 7;
	public static final int SERVER_START_GAME_ACTION = 8;
	public static final int POINTER_LOCATION = 10;
	public static final int SELECTED_CASTLE = 11;
	public static final int PLACED_CANNON = 12;
	public static final int FIRED_CANNON = 13;
	public static final int ROTATED_WALL = 14;
	public static final int PLACED_WALL = 15;
    public static final int END_GAME = 16;
	public static final int MAX_MESSAGE_SIZE = 4096;
    public static final int SHIP_ANIMATION_TIMESTEPS = 4;
    public static final int SHIP_SINKING_ANIMATION_TIMESTEPS = 16;
	public static final int LUABRIDGE_MAJOR_VERSION = 2;
	public static final int LUABRIDGE_MINOR_VERSION = 0;
	public static final int LUABRIDGE_VERSION = 200;
	public static final int LUA_OPEQ = 1;
	public static final int LUA_OPLT = 2;
	public static final int LUA_OPLE = 3;
	public static final int LUABRIDGE_LUA_OK = 0;
	public static final int GAME_WIDTH = 480;
	public static final int GAME_HEIGHT = 288;
	public static final double STANDARD_GRAVITY = 9.80665;
    public static final int MAX_LIST_SIZE = 3;
	public static final String BROADCAST_OPTION = "Broadcast";
	public static final String REGULAR_OPTION = "Regular";
	public static final double M_PI = 3.141592653589793;
    public static final String ROOM_DENIED_TEXT = "Could not join game";
	public static final int CANVAS_WIDTH = 480;
	public static final int CANVAS_HEIGHT = 288;

    /** Note: The following values are not in the Linux code */
    public static int MUSIC_VOLUME = 10;
    public static int SOUNDEFFECT_VOLUME = 10;
}
