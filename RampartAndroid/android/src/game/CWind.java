package game;

import com.badlogic.gdx.math.Vector2;

import game.animations.DefineConstants;

/**
 * @brief Wind state of game
 */
public class CWind {

	// Stores the WindType Setting
	public EWindType DWindType;
	// Stores the current Windspeed and Wind Direction
	public int DWindSpeed;
	public int DWindDirection;
    //Stores whether the wind changes direction next update
    public boolean DWindDirectionChange;

	/**
	 * @brief Makes wind with basic settings
	 */
	public CWind() {
		DWindType = EWindType.wtMild;
		DWindSpeed = 1;
		DWindDirection = 0;
	}


	public CWind(CWind wind) {
		this.DWindType = wind.DWindType;
		this.DWindSpeed = wind.DWindSpeed;
		this.DWindDirection = wind.DWindDirection;
	}

    /**
*
* @brief Update DWindSpeed and DWindDirection
*
* @param The game that is running

*/
    public final void WindUpdate(int Change, int DirChange, int SpeedChange) {

        if(EWindType.wtNone == DWindType) {
            DWindDirection = 0;
            DWindSpeed = 1;
        }
        else {
            int ChangeProbability;
            int DirectionProbability;
            int SpeedProbability;
            int MinWindSpeed;
            int MaxWindSpeed;

            if(EWindType.wtMild == DWindType) {
                ChangeProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.01);
                DirectionProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.01);
                SpeedProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.1);
                MinWindSpeed = 1;
                MaxWindSpeed = DefineConstants.WINDSPEED_COUNT / 2;
            }
            else if(EWindType.wtModerate == DWindType) {
                ChangeProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.025);
                DirectionProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.025);
                SpeedProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.2);
                MinWindSpeed = 2;
                MaxWindSpeed = DefineConstants.WINDSPEED_COUNT - 1;
            }
            else {
                ChangeProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.1);
                DirectionProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.1);
                SpeedProbability = (int)(DefineConstants.RANDOM_NUMBER_MAX * 0.3);
                MinWindSpeed = DefineConstants.WINDSPEED_COUNT / 2;
                MaxWindSpeed = DefineConstants.WINDSPEED_COUNT - 1;
            }

            if((Change % DefineConstants.RANDOM_NUMBER_MAX) < ChangeProbability) {
                DirChange = DirChange % DefineConstants.RANDOM_NUMBER_MAX;
                SpeedChange = SpeedChange % DefineConstants.RANDOM_NUMBER_MAX;

                if(DirChange < DirectionProbability) {
                    DWindDirection += DefineConstants.WINDDIRECTION_COUNT-1;
                    DWindDirection %= DefineConstants.WINDDIRECTION_COUNT;
                    if (DWindDirection == 0) {
                        DWindDirection = DefineConstants.WINDDIRECTION_COUNT-1;
                    }
                    DWindDirectionChange = true;
                }
                else if(DirChange < DirectionProbability * 2) {
                    DWindDirection++;
                    DWindDirection %= DefineConstants.WINDDIRECTION_COUNT;
                    if (DWindDirection == 0) {
                        DWindDirection = 1;
                    }
                    DWindDirectionChange = true;
                }
                if(SpeedChange < DirectionProbability) {
                    DWindSpeed += DefineConstants.WINDSPEED_COUNT-1;
                    DWindSpeed %= DefineConstants.WINDSPEED_COUNT;
                }
                else if(SpeedChange < DirectionProbability * 2) {
                    DWindSpeed++;
                    DWindSpeed %= DefineConstants.WINDSPEED_COUNT;
                }

            }
            if (DWindDirection == 0) {
                DWindDirection = 1;
                DWindDirectionChange = true;
            }
            if(DWindSpeed < MinWindSpeed) {
                DWindSpeed = MinWindSpeed;
            }
            if(DWindSpeed > MaxWindSpeed) {
                DWindSpeed = MaxWindSpeed;
            }
        }
    }


    public final Vector2 GetVector() {
		double WindX = 0;
		double WindY = 0;
		switch (DWindDirection) {
		case 1:
			WindY = -DWindSpeed;
			break;
		case 2:
			WindX = DWindSpeed;
			WindY = -DWindSpeed;
			break;
		case 3:
			WindX = DWindSpeed;
			break;
		case 4:
			WindX = DWindSpeed;
			WindY = DWindSpeed;
			break;
		case 5:
			WindY = DWindSpeed;
			break;
		case 6:
			WindX = -DWindSpeed;
			WindY = DWindSpeed;
			break;
		case 7:
			WindX = -DWindSpeed;
			break;
		case 8:
			WindX = -DWindSpeed;
			WindY = -DWindSpeed;
			break;
		default:
			break;
		}
		WindX /= 10;
		WindY /= 10;
		WindX *= (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;
		WindY *= (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;
		return new Vector2((float) WindX, (float) WindY);
	}

	/**
	 * @brief Type of wind
	 */
	public enum EWindType {
		wtNone,
		wtMild,
		wtModerate,
		wtErratic;

		public static EWindType forValue(int value) {
			return values()[value];
		}

		public int getValue() {
			return this.ordinal();
		}
	}
}
