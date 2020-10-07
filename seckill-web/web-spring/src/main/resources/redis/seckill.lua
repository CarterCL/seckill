local stock = redis.call('get',KEYS[2])
if tonumber(stock) <= 0 then
        return 0
end
stock = redis.call('decr',KEYS[2])
if tonumber(stock) < 0 then
        return 0
end
redis.call('set',KEYS[3],'0')
redis.call('xadd','ordermq','*','userId',KEYS[1],'productId',KEYS[2])
return 1
