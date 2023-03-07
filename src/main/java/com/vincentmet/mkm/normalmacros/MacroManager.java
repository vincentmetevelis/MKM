package com.vincentmet.mkm.normalmacros;

import com.google.gson.JsonObject;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class MacroManager {
    private static int selectedMacroSet = 0;
    private static final List<MacroSet> macroSetList = new ArrayList<>();

    public static void addMacroSet(MacroSet set){
        macroSetList.add(set);
    }

    public static void clear(){
        macroSetList.clear();
    }

    public static void removeMacroSet(int id){
        macroSetList.remove(id);
    }

    public static void addNewEmptyMacroSet(){
        addMacroSet(createNewMacroSet());
    }

    public static MacroSet createNewMacroSet(){
        MacroSet newSet = new MacroSet();
        newSet.processJson(new JsonObject());
        return newSet;
    }

    private static void updateCurrentMacroSetIdToWithinPossibleRange(){
        if(macroSetList.isEmpty()){
            addNewEmptyMacroSet();
            selectedMacroSet = 0;
        }
        while (selectedMacroSet < 0) selectedMacroSet += macroSetList.size();
        selectedMacroSet %= macroSetList.size();
    }

    public static MacroSet getCurrentMacroSet(){
        final int currentId = getCurrentMacroSetId();//do this first, since the macroSetList might be manipulated in this function
        return macroSetList.get(currentId);
    }

    public static int getCurrentMacroSetId(){
        updateCurrentMacroSetIdToWithinPossibleRange();
        return selectedMacroSet;
    }

    public static void setCurrentMacroSetId(int id){
        int oldId = selectedMacroSet;
        selectedMacroSet = id;
        updateCurrentMacroSetIdToWithinPossibleRange();
        int newId = selectedMacroSet;
        MinecraftForge.EVENT_BUS.post(new SetCurrentMacroEvent(oldId, newId));
    }

    public static void useNextMacroset(){
        setCurrentMacroSetId(getCurrentMacroSetId()+1);
    }

    public static void usePreviousMacroset(){
        setCurrentMacroSetId(getCurrentMacroSetId()-1);
    }

    public static List<MacroSet> getAllMacros(){
        return macroSetList;
    }
}