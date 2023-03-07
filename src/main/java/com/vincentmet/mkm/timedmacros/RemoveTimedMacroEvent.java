package com.vincentmet.mkm.timedmacros;

import net.minecraftforge.eventbus.api.Event;

public class RemoveTimedMacroEvent extends Event {
    private final int removedId;
    public RemoveTimedMacroEvent(int removedId){
        this.removedId = removedId;
    }

    public int getRemovedId() {
        return removedId;
    }
}