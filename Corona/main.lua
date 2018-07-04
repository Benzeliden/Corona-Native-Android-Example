local habrPlugin = require("plugin.habrExamplePlugin")

habrPlugin.helloHabr()

print("version", habrPlugin.version)


local joinedString = habrPlugin.stringJoin("this", " ", "was", " ", "concated", " ", {}, nil, "by", " ", "Java", "!", " ", "some", " ", "number", " : ", 42);

print(joinedString)


local res, err = habrPlugin.sum("test", 4)

print("result 1:", res, err)

res, err = habrPlugin.sum(3,2)

print("result 2:", res, err)

local calc = habrPlugin.calc

print("call method of java object", calc:sum(3,4))
--print("hmm", calc:someStaticMethod())
print("call static method of java object", calc:getClass():someStaticMethod())

print("hmm", calc:someStaticMethod())
print("call method of java object", calc:sum(3,6))

local newInstance = calc:getClass():new()
print("call method of java object", newInstance:sum(3,6))


habrPlugin.createText("Hello Habr!")