--local math = require "math"
getenemyWall = function(colorIndex)
	if colorIndex == 1 then
		return 5
	else
		return 2
	end
end
getxy = function(colorIndex)
  --math.randomseed(os.time())
  coord =
  {
  xpos = 0,
  ypos = 0
  }
  BlueArrX = {}
  BlueArrY = {}
  i = 1
  enemyWall = getenemyWall(colorIndex)
  for y=0, 23, 1 do
    for x=0, 39, 1 do
      if test:getWallType(x,y) == enemyWall then
        BlueArrX[i] = x
        BlueArrY[i] = y
        i = i+1
      end
    end
  end
  j = (math.floor((i-1) * myrandom:random())) + 1
  -- easy is level 1, medium is level 2, hard is level 3
  if test:getDifficultyLevel() == 1 then
		if myrandom:random() < .5 then
      x = BlueArrX[j] + myrandom:random(-2,2) -- add a random miss factor of up to 3 squares
      y = BlueArrY[j] + myrandom:random(-2,2)
		else
      x = BlueArrX[j]
      y = BlueArrY[j]
    end
  elseif test:getDifficultyLevel() == 2 then
    if myrandom:random() < .2 then
      x = BlueArrX[j] + myrandom:random(-1,1) -- add a random miss factor of up to 1.5 squares
      y = BlueArrY[j] + myrandom:random(-1,1)
    else
      x = BlueArrX[j]
      y = BlueArrY[j]
    end
  elseif test:getDifficultyLevel() == 3 then
    x = BlueArrX[j]
    y = BlueArrY[j]
  end
  coord.xpos = x
  coord.ypos = y
  return coord
end
getShipMovementLocation = function(colorIndex)
--math.randomseed(os.time())
    shipMovementLocation =
    {
        xpos = 0,
        ypos = 0
    }
    wallArrX = {}
    wallArrY = {}
    i = 1
    enemyWall = getenemyWall(colorIndex)
    for y=0, 23, 1 do
        for x=0, 39, 1 do
            if test.getWallType(x,y) == enemyWall then
                wallArrX[i] = x
                wallArrY[i] = y
                i = i+1
            end
        end
    end
    j = (math.floor((i-1) * math.random())) + 1
    -- selected a random enemy wall to move towards
    shipMovementLocation.xpos = wallArrX[j]
    shipMovementLocation.ypos = wallArrY[j]
    return(shipMovementLocation)
end