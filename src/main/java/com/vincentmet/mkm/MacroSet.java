package com.vincentmet.mkm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class MacroSet {
    private String macro0 = "";
    private String macro1 = "";
    private String macro2 = "";
    private String macro3 = "";
    private String macro4 = "";
    private String macro5 = "";
    private String macro6 = "";
    private String macro7 = "";
    private String macro8 = "";
    private String macro9 = "";

    public void processJson(JsonObject json){
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

    public JsonObject getJson(){
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

    public String getMacro0() {
        return macro0;
    }

    public String getMacro1() {
        return macro1;
    }

    public String getMacro2() {
        return macro2;
    }

    public String getMacro3() {
        return macro3;
    }

    public String getMacro4() {
        return macro4;
    }

    public String getMacro5() {
        return macro5;
    }

    public String getMacro6() {
        return macro6;
    }

    public String getMacro7() {
        return macro7;
    }

    public String getMacro8() {
        return macro8;
    }

    public String getMacro9() {
        return macro9;
    }

    public void setMacro0(String macro0) {
        this.macro0 = macro0;
    }

    public void setMacro1(String macro1) {
        this.macro1 = macro1;
    }

    public void setMacro2(String macro2) {
        this.macro2 = macro2;
    }

    public void setMacro3(String macro3) {
        this.macro3 = macro3;
    }

    public void setMacro4(String macro4) {
        this.macro4 = macro4;
    }

    public void setMacro5(String macro5) {
        this.macro5 = macro5;
    }

    public void setMacro6(String macro6) {
        this.macro6 = macro6;
    }

    public void setMacro7(String macro7) {
        this.macro7 = macro7;
    }

    public void setMacro8(String macro8) {
        this.macro8 = macro8;
    }

    public void setMacro9(String macro9) {
        this.macro9 = macro9;
    }
}