package com.terrorAndBlue.nerfCreepers;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;

import com.terrorAndBlue.nerfCreepers.entity.NerfedCreeper;
import com.terrorAndBlue.nerfCreepers.event.CreeperSpawnedHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod
(
		modid						= NerfCreepersMain.MODID,
		name 						= NerfCreepersMain.NAME,
		version 					= NerfCreepersMain.VERSION,
		dependencies 				= "required-after:Forge@[10.13.1.1217,)",
		acceptedMinecraftVersions	= "[1.7.10,)",
		canBeDeactivated = false
		)
public class NerfCreepersMain
{
	public static final String MODID = "NerfCreepers";
	public static final String NAME = "Nerf Creepers";
	public static final String VERSION = "1.0";

	@Instance(MODID)
	public static NerfCreepersMain instance;

	public int explosionDelayTicks;
	public int explosionRadius;
	
	public String customMessage;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModMetadata meta = event.getModMetadata();
		meta.modId=MODID;
		meta.name=NAME;
		meta.version=VERSION;
		meta.description="Nerf Creepers does exactly what it says on the tin. It enables servers to configure various aspects of creepers.";
		meta.url="";
		meta.updateUrl="";
		meta.authorList=Arrays.asList (new String[] { "Terror Above", "Bluesnake198" });
		meta.credits="Programmed by Terror, textured by Bluesnake";
		meta.logoFile="";//relative to the location of the mcmod.info file

		Configuration config = new Configuration(new File("config/NerfCreepers.cfg"));

		config.load();

		explosionDelayTicks = config.getInt("explosionDelayTicks", "Server Options", 30, 30, 600, "This will determine the amount of game ticks it takes a creeper to blow up.");
		explosionRadius = config.getInt("explosionRadius", "Server Options", 3, 1, 5, "This will determine the strength and range of a creeper explosion.");
		
		customMessage = config.getString("customWarningMessage", "Client Options", "A Creeper is right behind you!", "The message that will display if a creeper comes up from behind. Leave empty for no message.");

		config.save();

		EntityRegistry.registerGlobalEntityID(NerfedCreeper.class, "nerfedCreeper", EntityRegistry.findGlobalUniqueEntityId());

		MinecraftForge.EVENT_BUS.register(new CreeperSpawnedHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

	}
}
