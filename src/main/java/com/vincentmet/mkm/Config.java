package com.vincentmet.mkm;

import com.google.gson.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    public static void processJson(JsonObject json){
        MacroManager.clear();
        if(json.has("macro_sets")){
            JsonElement jsonElementMacroSets = json.get("macro_sets");
            if(jsonElementMacroSets.isJsonArray()){
                JsonArray jsonArrayMacroSets = jsonElementMacroSets.getAsJsonArray();
                jsonArrayMacroSets.forEach(jsonArrayEntryElement -> {
                    if(jsonArrayEntryElement.isJsonObject()){
                        MacroSet newSet = new MacroSet();
                        newSet.processJson(jsonArrayEntryElement.getAsJsonObject());
                        MacroManager.addMacroSet(newSet);
                    }
                });
            }
        }
        MacroManager.setCurrentMacroSetId(0);
    }

    public static JsonObject getJson(){
        JsonObject json = new JsonObject();
        JsonArray jsonArrayMacroSets = new JsonArray();
        MacroManager.getAllMacros().forEach(set -> {
            jsonArrayMacroSets.add(set.getJson());
        });
        json.add("macro_sets", jsonArrayMacroSets);
        return json;
    }

    public static void readConfigToMemoryWithModFiles(){
        readConfigToMemory(BaseClass.PATH_CONFIG, "config.json");
    }

    public static void readConfigToMemory(Path path, String file){
        processJson(loadConfig(path, file));
        writeConfigToDisk(path, file);
    }

    public static void writeConfigToDiskWithModFiles(){
        writeConfigToDisk(BaseClass.PATH_CONFIG, "config.json");
    }

    public static void writeConfigToDisk(Path path, String file){
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
}