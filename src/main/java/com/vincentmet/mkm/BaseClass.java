package com.vincentmet.mkm;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

import static com.vincentmet.mkm.BaseClass.MODID;

@Mod(MODID)
public class BaseClass {
	public static final String MODID = "mkm";
	public static final Path PATH_CONFIG = FMLPaths.CONFIGDIR.get().resolve("mkm");

    public BaseClass(){
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setupClient(final FMLClientSetupEvent event){
    	Keybinds.registerKeybinds();
	}
	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeEventHandler{
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event){
			if(Keybinds.OPEN_EDITOR.consumeClick() && Minecraft.getInstance().player!= null) Minecraft.getInstance().setScreen(new MacroScreen());
			Keybinds.getAllMacros().forEach(macroKeybindWrapper -> {if(macroKeybindWrapper.consumeClick() && Minecraft.getInstance().player!= null) Minecraft.getInstance().player.chat("/" + macroKeybindWrapper.getConfigValue());});
		}

		@SubscribeEvent
		public static void logoutEvent(PlayerEvent.PlayerLoggedOutEvent event){
			Config.writeConfigToDisk(PATH_CONFIG, "config.json");
		}

		@SubscribeEvent
		public static void loginEvent(PlayerEvent.PlayerLoggedInEvent event){
			Config.readConfigToMemory(PATH_CONFIG, "config.json");
		}
	}
}