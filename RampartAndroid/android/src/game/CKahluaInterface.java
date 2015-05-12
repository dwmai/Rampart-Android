package game;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.converter.KahluaNumberConverter;
import se.krka.kahlua.converter.KahluaTableConverter;
import se.krka.kahlua.converter.LuaToJavaConverter;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.LuaClosure;

/**
 * Created by Qingwei Zeng on 2/25/2015.
 * Singleton Class for interfacing with Kahlua */
public class CKahluaInterface {
    private final J2SEPlatform platform = new J2SEPlatform();
    private final KahluaTable env = platform.newEnvironment();
    private KahluaConverterManager converterManager = new KahluaConverterManager();
    private final KahluaThread thread = new KahluaThread(platform, env);
    private LuaCaller caller = new LuaCaller(converterManager);
    private LuaJavaClassExposer exposer = new LuaJavaClassExposer(converterManager, platform, env);
    private static CKahluaInterface instance = null;

    private CKahluaInterface(){
        KahluaNumberConverter.install(converterManager);
        new KahluaTableConverter(platform).install(converterManager);
    }

    public static CKahluaInterface getInstance(){
        if(instance == null) {
            instance = new CKahluaInterface();
        }
        return instance;
    }

    public KahluaTable getEnvironment(){return env;}
    public LuaJavaClassExposer getExposer() {return exposer;}
    public KahluaConverterManager getConverterManager() {return converterManager;}

    /* Builds a string from a file in assets and runs that string as a script.
    * filename - name of the file under assets
    * act - the activity the function is being calling from (should be main activity all the time)
    */
    public void runFileScript(String fileName)
    {
        String script = "";

        //builds a string from the file in assets
        StringBuilder scriptData = new StringBuilder();
        try{
            String scriptFileName = "scripts/" + fileName;
            FileHandle file = Gdx.files.internal(scriptFileName);
            String fileContents = file.readString();
            BufferedReader br = new BufferedReader(new StringReader(fileContents));
            String line = null;

            while ((line = br.readLine()) != null){
                scriptData.append(line + "\n");
            }

            br.close();

            script = scriptData.toString();

        }catch(IOException ex){ game.utils.Log.critical(ex, "Failed to load/read file");}

        //run the script
        runStringScript(script);
    }

    /* Creates a closure and loads the script into the Lua Compiler. It then runs the string as a
     * script.
     * script - the string that will be ran as a script
     */
    public void runStringScript(String script){
        try {
            LuaClosure closure = LuaCompiler.loadstring(script, "", env);
            //caller.protectedCall(thread, closure, new StringTest());
            caller.protectedCall(thread, closure);
        }catch(IOException ep){Log.e("FailLuaCall", "Couldn't run script.");}
    }
}
