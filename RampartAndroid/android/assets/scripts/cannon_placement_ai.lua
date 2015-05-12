--Written by: Christopher May
--Notes: size of a map tile currently set to 12 pixels
getCannonLocs = function(colorIndex, xTile, yTile, xDir, yDir, dMapWidth, dMapHeight, numCannons)
    tileSize = 12
    for yOff = 0, dMapHeight, 1 do
        for xOff = dMapWidth, 0, -1 do

            if xDir == 1 then
                xAttempt = xTile + xOff
            else
                xAttempt = dMapWidth + xTile - xOff
            end

            if yDir == 1 then
                yAttempt = yTile + yOff
            else
                yAttempt = dMapHeight + yTile - yOff
            end

            xAttempt = xAttempt % dMapWidth
                yAttempt = yAttempt % dMapHeight

            if test:isValidCannonLoc(colorIndex, numCannons,xAttempt, yAttempt) then
                xPos = xAttempt * tileSize  --dMapWidth + dMapWidth / 2
                yPos = yAttempt * tileSize  --dMapHeight + dMapHeight / 2
                test:setDAITarget(colorIndex, xPos, yPos)
            	return true
            end
        end
    end
	return false
end

