package plugin.habrExamplePlugin;

import android.util.Log;

import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;


@SuppressWarnings({"WeakerAccess", "unused"})
public class LuaLoader implements JavaFunction {
    @Override
    public int invoke(LuaState luaState) {
        Log.d("Corona native", "Lua Loader invoke called");



        String libName = luaState.toString(1); // получаем имя модуля из стека (первый параметр require)
        NamedJavaFunction[] luaFunctions = new NamedJavaFunction[]{
                new HelloHabrFunction(),
                new StringJoinFunction(),
                new SumFunction(),
                new CreateDisplayTextFunction(),
        };
        luaState.register(libName, luaFunctions); // регистрируем наш модуль, он помещается наверх стека

        luaState.register(libName, luaFunctions); // регистрируем наш модуль, он будет расположен на вершине стека
        luaState.pushString("0.1.2"); // кладём в стек строку
        luaState.setField(-2, "version"); // установка поля version у нашего модуля.
        luaState.pop(1);


        luaState.pushJavaObject(new Calculator());
        luaState.setField(-2, "calc");
        luaState.pop(1);

        // Цифра 1 показывает сколько аргументов из стека вернётся в lua код.
        // Но в случае с require это ни на что не повлияет, require вернёт только наш модуль
        return 0;
    }
}

class HelloHabrFunction implements NamedJavaFunction {
    @Override
    public String getName() {
        return "helloHabr";
    }

    @Override
    public int invoke(LuaState L) {
        Log.d("Corona native", "Hello Habr!");

        return 0;
    }
}


class StringJoinFunction implements NamedJavaFunction{
    @Override
    public String getName() {
        return "stringJoin";
    }

    @Override
    public int invoke(LuaState luaState) {
        int currentStackIndex = 1;
        StringBuilder stringBuilder = new StringBuilder();
        while (!luaState.isNone(currentStackIndex)){
            String str = luaState.toString(currentStackIndex);
            if (str != null){ //toString возвращает null для non-string и non-number, игнорируем
                stringBuilder.append(str);
            }
            currentStackIndex++;
        }

        luaState.pushString(stringBuilder.toString());

        return 1;
    }
}

class SumFunction implements NamedJavaFunction{
    @Override
    public String getName() {
        return "sum";
    }

    @Override
    public int invoke(LuaState luaState) {
        if (!luaState.isNumber(1)  || !luaState.isNumber(2)){
            luaState.pushNil();
            luaState.pushString("Arguments should be numbers!");
            return 2;
        }

        int firstNumber = luaState.toInteger(1);
        int secondNumber = luaState.toInteger(1);

        luaState.pushInteger(firstNumber + secondNumber);

        return 1;
    }
}

class CreateDisplayTextFunction implements NamedJavaFunction{

    private static String code = "local text = ...;" +
            "return display.newText({" +
            "text = text," +
            "x = 160," +
            "y = 200," +
            "});";

    @Override
    public String getName() {
        return "createText";
    }

    @Override
    public int invoke(LuaState luaState) {
        luaState.load(code,"CreateDisplayTextFunction code"); // загружаем код в стек, создавая из него функцию
        luaState.pushValue(1); // помещаем первый параметр функции на вершину стека
        luaState.call(1, 1); // вызываем нашу функцию, указываем что она должна получить 1 параметр, а также вернуть 1

        return 1;
    }
}