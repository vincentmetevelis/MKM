package com.vincentmet.mkm;

import com.vincentmet.mkm.normalmacros.Keybinds;
import com.vincentmet.mkm.normalmacros.MacroManager;
import com.vincentmet.mkm.normalmacros.MacroScreen;
import com.vincentmet.mkm.normalmacros.SetCurrentMacroEvent;
import com.vincentmet.mkm.timedmacros.AddNewTimedMacroEvent;
import com.vincentmet.mkm.timedmacros.RemoveTimedMacroEvent;
import com.vincentmet.mkm.timedmacros.TimedMacroManager;
import com.vincentmet.mkm.timedmacros.TimedMacroScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.vincentmet.mkm.BaseClass.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler{
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if (Minecraft.getInstance().player != null && event.phase.equals(TickEvent.Phase.END)){
            if(Keybinds.OPEN_EDITOR.consumeClick()) Minecraft.getInstance().setScreen(new MacroScreen());
            if(Keybinds.PREV_MACROSET.consumeClick()) MacroManager.usePreviousMacroset();
            if(Keybinds.NEXT_MACROSET.consumeClick()) MacroManager.useNextMacroset();
            Keybinds.getAllMacros().forEach(macroKeybindWrapper -> {
                if(macroKeybindWrapper.consumeClick()){
                    Minecraft.getInstance().player.chat("/" + macroKeybindWrapper.getMacroGetterValue());
                }
            });
            TimedMacroManager.getAllTimedMacros().forEach(timedMacro -> {
                if(System.currentTimeMillis()/50 % timedMacro.getTimingInTicks() == 0 && !Minecraft.getInstance().isPaused()){
                    Minecraft.getInstance().player.chat("/" + timedMacro.getMacro());
                }
            });
        }
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

    @SubscribeEvent
    public static void removeTimedMacroEvent(RemoveTimedMacroEvent event){
        if(Minecraft.getInstance().player != null){
            if(Minecraft.getInstance().screen instanceof TimedMacroScreen){
                Minecraft.getInstance().setScreen(new TimedMacroScreen());
            }
        }
    }

    @SubscribeEvent
    public static void newTimedMacroEvent(AddNewTimedMacroEvent event){
        if(Minecraft.getInstance().player != null){
            if(Minecraft.getInstance().screen instanceof TimedMacroScreen screen){
                screen.setScrollDistance(screen.getMaxScroll());
                Minecraft.getInstance().setScreen(new TimedMacroScreen());
            }
        }
    }
}