package com.vincentmet.mkm;

import com.vincentmet.mkm.normalmacros.Keybinds;
import net.minecraftforge.common.MinecraftForge;
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
		Config.readConfigToMemoryWithModFiles();
	}
}