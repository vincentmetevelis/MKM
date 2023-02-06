package com.vincentmet.mkm;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.vincentmet.mkm.BaseClass.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler{
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if (Minecraft.getInstance().player != null){
            if(Keybinds.OPEN_EDITOR.consumeClick()) Minecraft.getInstance().setScreen(new MacroScreen());
            if(Keybinds.PREV_MACROSET.consumeClick()) MacroManager.usePreviousMacroset();
            if(Keybinds.NEXT_MACROSET.consumeClick()) MacroManager.useNextMacroset();
            Keybinds.getAllMacros().forEach(macroKeybindWrapper -> {if(macroKeybindWrapper.consumeClick()) Minecraft.getInstance().player.chat("/" + macroKeybindWrapper.getMacroGetterValue());});
        }
    }

    @SubscribeEvent
    public static void logoutEvent(PlayerEvent.PlayerLoggedOutEvent event){
        Config.writeConfigToDiskWithModFiles();
    }

    @SubscribeEvent
    public static void loginEvent(PlayerEvent.PlayerLoggedInEvent event){
        Config.readConfigToMemoryWithModFiles();
    }

    @SubscribeEvent
    public static void selectedMacroChangeEvent(SetCurrentMacroEvent event){
        if(Minecraft.getInstance().player != null){
            if(Minecraft.getInstance().screen instanceof MacroScreen){
                Minecraft.getInstance().setScreen(new MacroScreen());
            }
            Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("mkm.text.switched_to_macro_set", MacroManager.getCurrentMacroSetId()), false);
        }
    }
}