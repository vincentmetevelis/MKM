package com.vincentmet.mkm;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.vincentmet.mkm.BaseClass.MODID;
import static com.vincentmet.mkm.Keybinds.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(OPEN_EDITOR.get());
        event.register(PREV_MACROSET.get());
        event.register(NEXT_MACROSET.get());
        for (Lazy<MacroKeybindWrapper> macroKeybindWrapperLazy : getAllMacros()) {
            event.register(macroKeybindWrapperLazy.get());
        }
    }
}