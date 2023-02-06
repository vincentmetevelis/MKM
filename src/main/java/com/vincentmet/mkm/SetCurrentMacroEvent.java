package com.vincentmet.mkm;

import net.minecraftforge.eventbus.api.Event;

public class SetCurrentMacroEvent extends Event {
    private int oldId;
    private int newId;
    public SetCurrentMacroEvent(int oldId, int newId){
        this.oldId = oldId;
        this.newId = newId;
    }

    public int getOldId() {
        return oldId;
    }

    public int getNewId() {
        return newId;
    }
}