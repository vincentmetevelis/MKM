package com.vincentmet.mkm.timedmacros;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimedMacroManager {
    private static final List<TimedMacro> timedMacroList = new ArrayList<>();
    private static final List<Integer> idsToRemoveList = new ArrayList<>();

    public static void addTimedMacro(TimedMacro set){
        timedMacroList.add(set);
    }

    public static void clear(){
        timedMacroList.clear();
    }

    public static void removeTimedMacro(int id){
        timedMacroList.remove(id);
    }

    public static void addNewEmptyTimedMacro(){
        addTimedMacro(createNewTimedMacro());
    }

    public static TimedMacro createNewTimedMacro(){
        TimedMacro newSet = new TimedMacro();
        newSet.processJson(new JsonObject());
        return newSet;
    }

    public static void markIfForDeletion(int idToDelete){
        idsToRemoveList.add(idToDelete);
    }

    public static void deleteMarkedForDeletion(){
        if (hasAnyMarkedForDeletionEntries()){
            idsToRemoveList.stream().sorted().forEach(TimedMacroManager::removeTimedMacro);
            idsToRemoveList.clear();
        }
    }

    public static boolean hasAnyMarkedForDeletionEntries(){
        return !idsToRemoveList.isEmpty();
    }

    public static List<TimedMacro> getAllTimedMacros(){
        return timedMacroList;
    }
}