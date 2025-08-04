-- 库存操作Lua脚本
-- 参数：KEYS[1] = 库存key, KEYS[2] = 分布式锁key
-- 参数：ARGV[1] = 用户ID, ARGV[2] = 购买数量

-- 获取当前库存
local stock = redis.call('GET', KEYS[1])
if not stock then
    return -1  -- 商品不存在
end

-- 检查库存是否足够
if tonumber(stock) < tonumber(ARGV[2]) then
    return -2  -- 库存不足
end

-- 尝试获取分布式锁
local lockResult = redis.call('SET', KEYS[2], ARGV[1], 'NX', 'EX', 10)
if not lockResult then
    return -3  -- 获取锁失败，用户重复下单
end

-- 执行库存扣减
local newStock = redis.call('DECRBY', KEYS[1], ARGV[2])
if newStock < 0 then
    -- 库存不足，回滚
    redis.call('INCRBY', KEYS[1], ARGV[2])
    redis.call('DEL', KEYS[2])
    return -2  -- 库存不足
end

-- 返回成功状态码
return 1 