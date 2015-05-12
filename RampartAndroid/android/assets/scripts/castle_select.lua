--local myrandom = newrandom()

function getCastleLocation(numberCastleLocations)
    --myrandom:seed(os.time())
    number = myrandom:random(0, numberCastleLocations - 1)

    return(number)
end

