function findBestPlacement(ColorIndex, XTile, YTile)
    Best = {
  	X = -1,	
  	Y = -1,
  	Rotation = 0,
  	BCoverage = 0,
  	BInterference = 0
    }

	for RotationIndex = 0, 3, 1 do
		for YOffset = -2-test:getWallShapeHeight(ColorIndex), 4, 1 do
			TargetY = YTile + YOffset
			if TargetY < 0 or TargetY >= test:getDMapHeight_rebuild() then
				--go to ycontinue
			else
				for XOffset = -2-test:getWallShapeWidth(ColorIndex), 4, 1 do
					TargetX = XTile + XOffset
					
					if 0 > TargetX or TargetX >= test:getDMapWidth_rebuild() then
						--go to xcontinue
					else
						if test:getValidWallPlacement(ColorIndex, TargetX, TargetY) then
							Coverage = 0
							Interference = 0
							BetterSolution = false
							
							for WallYPos = 0, test:getWallShapeHeight(ColorIndex)-1, 1 do
								for WallXPos = 0, test:getWallShapeWidth(ColorIndex)-1, 1 do
									if test:getWallShapeIsBlock(ColorIndex, WallXPos, WallYPos) then
										WallX = TargetX + WallXPos - XTile
										WallY = TargetY + WallYPos - YTile
										if ((WallX == -3 or WallX == 4) and (WallY >= -2 and WallY <= 3)) then
											Coverage = Coverage+1
										elseif ((WallY == -3 or WallY == 4) and (WallX >= -3 and WallX <= 4)) then
											Coverage = Coverage+1
										elseif ((WallX >= -2 and WallX <= 3) and (WallY >= -2 and WallY <= 3)) then
											Interference = Interference+1
										end
									end
								end
							end
							if Best.X < 0 then
								BetterSolution = true
							end
						
							if Coverage > Best.BCoverage and Interference < Best.BInterference then
								BetterSolution = true
							end
						
							if BetterSolution then
								if test:getDifficultyLevel() == 1 then
									if myrandom:random() > 0.15 then
										randVal = (myrandom:random(1,2) * 2) - 3
									else
										randVal = 0
									end
								elseif test:getDifficultyLevel() == 2 then
									if myrandom:random() > 0.667 then
										randVal = (myrandom.random(1,2) * 2) - 3
									else
										randVal = 0
									end
								else
									randVal = 0
								end
								Best.X = TargetX + randVal
								Best.Y = TargetY + randVal
								Best.Rotation = RotationIndex
								Best.BCoverage = Coverage
								Best.BInterference = Interference
							end
						end
					end
					--xcontinue
				end
            end
			--ycontinue
		end
		test:rotateWallShape(ColorIndex)
	end
	return Best
end