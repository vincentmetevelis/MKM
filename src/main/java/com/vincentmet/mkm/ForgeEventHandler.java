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
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.vincentmet.mkm.BaseClass.MODID;
import static com.vincentmet.mkm.normalmacros.Keybinds.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler{
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if (Minecraft.getInstance().player != null && event.phase.equals(TickEvent.Phase.END)){
            if(OPEN_EDITOR.get().consumeClick()) Minecraft.getInstance().setScreen(new MacroScreen());
            if(PREV_MACROSET.get().consumeClick()) MacroManager.usePreviousMacroset();
            if(NEXT_MACROSET.get().consumeClick()) MacroManager.useNextMacroset();
            Keybinds.getAllMacros().forEach(macroKeybindWrapper -> {
                if(macroKeybindWrapper.get().consumeClick()){
                    String command = macroKeybindWrapper.get().getMacroGetterValue();
                    Minecraft.getInstance().player.connection.sendCommand(command);
                }
            });
            TimedMacroManager.getAllTimedMacros().forEach(timedMacro -> {
                if(System.currentTimeMillis()/50 % timedMacro.getTimingInTicks() == 0 && !Minecraft.getInstance().isPaused()){
                    Minecraft.getInstance().player.connection.sendCommand(timedMacro.getMacro());
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
            Minecraft.getInstance().player.displayClientMessage(Component.translatable("mkm.text.switched_to_macro_set", MacroManager.getCurrentMacroSetId()), false);
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
                screen.setScrollDistance(screen.getMaxScroll());//todo test
                Minecraft.getInstance().setScreen(new TimedMacroScreen());
            }
        }
    }
}