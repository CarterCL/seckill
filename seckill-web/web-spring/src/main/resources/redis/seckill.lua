local stock = redis.call('get',KEYS[2])
if tonumber(stock) <= 0 then
        return 0
end
stock = redis.call('decr',KEYS[2])
if tonumber(stock) < 0 then
        return 0
end
redis.call('set',KEYS[3],'0')
return 1
