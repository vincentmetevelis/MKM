package com.vincentmet.mkm;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.vincentmet.mkm.BaseClass.MODID;
import static com.vincentmet.mkm.Keybinds.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler{
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if (Minecraft.getInstance().player != null){
            if(OPEN_EDITOR.get().consumeClick()) Minecraft.getInstance().setScreen(new MacroScreen());
            if(PREV_MACROSET.get().consumeClick()) MacroManager.usePreviousMacroset();
            if(NEXT_MACROSET.get().consumeClick()) MacroManager.useNextMacroset();
            Keybinds.getAllMacros().forEach(macroKeybindWrapper -> {if(macroKeybindWrapper.get().consumeClick()) Minecraft.getInstance().player.command(macroKeybindWrapper.get().getMacroGetterValue());});
        }
    }

    @SubscribeEvent
    public static void selectedMacroChangeEvent(SetCurrentMacroEvent event){
        if(Minecraft.getInstance().player != null){
            if(Minecraft.getInstance().screen instanceof MacroScreen){
                Minecraft.getInstance().setScreen(new MacroScreen());
            }
            Minecraft.getInstance().player.displayClientMessage(Component.translatable("mkm.text.switched_to_macro_set", MacroManager.getCurrentMacroSetId()), false);
        }
    }
}