package com.vincentmet.mkm;

import com.google.gson.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class Config {
    private static String macro0 = "";
    private static String macro1 = "";
    private static String macro2 = "";
    private static String macro3 = "";
    private static String macro4 = "";
    private static String macro5 = "";
    private static String macro6 = "";
    private static String macro7 = "";
    private static String macro8 = "";
    private static String macro9 = "";

    public static void processJson(JsonObject json){
        if(json.has("macro0")){
            JsonElement jsonElement = json.get("macro0");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro0 = jsonPrimitive.getAsString();
                }else{macro0 = "";}
            }else{macro0 = "";}
        }else{macro0 = "";}
        
        if(json.has("macro1")){
            JsonElement jsonElement = json.get("macro1");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro1 = jsonPrimitive.getAsString();
                }else{macro1 = "";}
            }else{macro1 = "";}
        }else{macro1 = "";}
        
        if(json.has("macro2")){
            JsonElement jsonElement = json.get("macro2");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro2 = jsonPrimitive.getAsString();
                }else{macro2 = "";}
            }else{macro2 = "";}
        }else{macro2 = "";}
        
        if(json.has("macro3")){
            JsonElement jsonElement = json.get("macro3");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro3 = jsonPrimitive.getAsString();
                }else{macro3 = "";}
            }else{macro3 = "";}
        }else{macro3 = "";}
        
        if(json.has("macro4")){
            JsonElement jsonElement = json.get("macro4");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro4 = jsonPrimitive.getAsString();
                }else{macro4 = "";}
            }else{macro4 = "";}
        }else{macro4 = "";}
        
        if(json.has("macro5")){
            JsonElement jsonElement = json.get("macro5");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro5 = jsonPrimitive.getAsString();
                }else{macro5 = "";}
            }else{macro5 = "";}
        }else{macro5 = "";}
        
        if(json.has("macro6")){
            JsonElement jsonElement = json.get("macro6");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro6 = jsonPrimitive.getAsString();
                }else{macro6 = "";}
            }else{macro6 = "";}
        }else{macro6 = "";}
        
        if(json.has("macro7")){
            JsonElement jsonElement = json.get("macro7");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro7 = jsonPrimitive.getAsString();
                }else{macro7 = "";}
            }else{macro7 = "";}
        }else{macro7 = "";}
        
        if(json.has("macro8")){
            JsonElement jsonElement = json.get("macro8");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro8 = jsonPrimitive.getAsString();
                }else{macro8 = "";}
            }else{macro8 = "";}
        }else{macro8 = "";}
        
        if(json.has("macro9")){
            JsonElement jsonElement = json.get("macro9");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro9 = jsonPrimitive.getAsString();
                }else{macro9 = "";}
            }else{macro9 = "";}
        }else{macro9 = "";}
    }

    public static JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("macro0", macro0);
        json.addProperty("macro1", macro1);
        json.addProperty("macro2", macro2);
        json.addProperty("macro3", macro3);
        json.addProperty("macro4", macro4);
        json.addProperty("macro5", macro5);
        json.addProperty("macro6", macro6);
        json.addProperty("macro7", macro7);
        json.addProperty("macro8", macro8);
        json.addProperty("macro9", macro9);
        return json;
    }

    public static void readConfigToMemory(Path path, String file){
        System.out.println("Loading Macros from Config!");
        processJson(loadConfig(path, file));
        writeConfigToDisk(path, file);
    }

    public static void writeConfigToDisk(Path path, String file){
        System.out.println("Saving Macros to Config!");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String out = gson.toJson(getJson());
        writeTo(path, file, out);
    }

    private static JsonObject loadConfig(Path path, String filename){
        try {
            StringBuilder res = new StringBuilder();
            Files.readAllLines(path.resolve(filename), StandardCharsets.UTF_8).forEach(res::append);
            return JsonParser.parseString(res.toString()).getAsJsonObject();
        }catch (IOException e) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String out = gson.toJson(new JsonObject());
            writeTo(path, filename, out);
            return loadConfig(path, filename);
        }
    }

    private static void writeTo(Path location, String filename, Object text){
        try{
            if(!location.toFile().exists()){
                location.toFile().mkdirs();
            }
            Files.write(location.resolve(filename), text.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Supplier<String> getMacro0() {
        return ()->macro0;
    }

    public static Supplier<String> getMacro1() {
        return ()->macro1;
    }

    public static Supplier<String> getMacro2() {
        return ()->macro2;
    }

    public static Supplier<String> getMacro3() {
        return ()->macro3;
    }

    public static Supplier<String> getMacro4() {
        return ()->macro4;
    }

    public static Supplier<String> getMacro5() {
        return ()->macro5;
    }

    public static Supplier<String> getMacro6() {
        return ()->macro6;
    }

    public static Supplier<String> getMacro7() {
        return ()->macro7;
    }

    public static Supplier<String> getMacro8() {
        return ()->macro8;
    }

    public static Supplier<String> getMacro9() {
        return ()->macro9;
    }

    public static void setMacro0(String macro0) {
        Config.macro0 = macro0;
    }

    public static void setMacro1(String macro1) {
        Config.macro1 = macro1;
    }

    public static void setMacro2(String macro2) {
        Config.macro2 = macro2;
    }

    public static void setMacro3(String macro3) {
        Config.macro3 = macro3;
    }

    public static void setMacro4(String macro4) {
        Config.macro4 = macro4;
    }

    public static void setMacro5(String macro5) {
        Config.macro5 = macro5;
    }

    public static void setMacro6(String macro6) {
        Config.macro6 = macro6;
    }

    public static void setMacro7(String macro7) {
        Config.macro7 = macro7;
    }

    public static void setMacro8(String macro8) {
        Config.macro8 = macro8;
    }

    public static void setMacro9(String macro9) {
        Config.macro9 = macro9;
    }
}