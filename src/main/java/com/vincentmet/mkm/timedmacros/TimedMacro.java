package com.vincentmet.mkm.timedmacros;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TimedMacro {
    private String name = "A new timed macro";
    private String macro = "say This is a new timed macro";
    private int timingInTicks = 100;

    public void processJson(JsonObject json){
        if(json.has("name")){
            JsonElement jsonElement = json.get("name");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    name = jsonPrimitive.getAsString();
                }else{name = "A new timed macro";}
            }else{name = "A new timed macro";}
        }else{name = "A new timed macro";}

        if(json.has("macro")){
            JsonElement jsonElement = json.get("macro");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    macro = jsonPrimitive.getAsString();
                }else{macro = "say This is a new timed macro";}
            }else{macro = "say This is a new timed macro";}
        }else{macro = "say This is a new timed macro";}

        if(json.has("timing")){
            JsonElement jsonElement = json.get("timing");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isNumber()){
                    timingInTicks = jsonPrimitive.getAsInt();
                }else{timingInTicks = 500;}
            }else{timingInTicks = 500;}
        }else{timingInTicks = 500;}
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("macro", macro);
        json.addProperty("timing", timingInTicks);
        return json;
    }

    public String getName() {
        return name;
    }

    public String getMacro() {
        return macro;
    }

    public int getTimingInTicks() {
        return timingInTicks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMacro(String macro) {
        this.macro = macro;
    }

    public void setTimingInTicks(int timingInTicks) {
        this.timingInTicks = timingInTicks;
    }
}