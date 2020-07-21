package com.terrorAndBlue.nerfCreepers.event;

import com.terrorAndBlue.nerfCreepers.entity.NerfedCreeper;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class CreeperSpawnedHandler
{
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void handleCreeperSpawn(EntityJoinWorldEvent event)
	{
		if(!event.isCanceled() && event.world != null && !event.world.isRemote && event.entity != null && event.entity.getClass() == EntityCreeper.class)
		{
			event.setCanceled(true);
			NerfedCreeper nc = NerfedCreeper.fromCreeper((EntityCreeper)event.entity);
			event.world.spawnEntityInWorld(nc);
			event.entity.setDead();
		}
	}
	
	//below is reserved as backup, we shouldn't need it though
	
	/*@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void handleCreeperUpdate(LivingUpdateEvent event)
	{
		if(event.entity != null && event.entity.getClass() == EntityCreeper.class)
		{
			EntityCreeper creeper = (EntityCreeper)event.entity;
			
			if(creeper.getEntityToAttack() != null)
			{
				NerfedCreeper nc = NerfedCreeper.fromCreeper(creeper);
				creeper.worldObj.spawnEntityInWorld(nc);
				creeper.setDead();
			}
		}
	}*/
}
