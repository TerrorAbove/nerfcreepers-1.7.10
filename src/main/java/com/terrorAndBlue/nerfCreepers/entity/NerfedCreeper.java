package com.terrorAndBlue.nerfCreepers.entity;

import com.terrorAndBlue.nerfCreepers.NerfCreepersMain;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class NerfedCreeper extends EntityCreeper
{
	public static NerfedCreeper fromCreeper(EntityCreeper creeper)
	{
		NerfedCreeper nc = new NerfedCreeper(creeper.worldObj);
		nc.copyLocationAndAnglesFrom(creeper);
		NBTTagCompound c = new NBTTagCompound();
		creeper.writeEntityToNBT(c);
		nc.writeDefaultCreeperProps(c);
		nc.readEntityFromNBT(c);
		return nc;
	}
	
	public NerfedCreeper(World w)
	{
		super(w);
		fuseTime = NerfCreepersMain.instance.explosionDelayTicks;
		explosionRadius = NerfCreepersMain.instance.explosionRadius;
	}

	/**
	 * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go
	 * weird)
	 */
	private int lastActiveTime;

	/**
	 * The amount of time since the creeper was close enough to the player to ignite
	 */
	private int timeSinceIgnited;
	private int fuseTime;

	/** Explosion radius for this creeper. */
	private int explosionRadius;
	
	private void writeDefaultCreeperProps(NBTTagCompound c)
	{
		c.setShort("Fuse", (short)this.fuseTime);
		c.setByte("ExplosionRadius", (byte)this.explosionRadius);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound c)
	{
		super.writeEntityToNBT(c);
		writeDefaultCreeperProps(c);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		if (this.isEntityAlive())
		{
			this.lastActiveTime = this.timeSinceIgnited;

			if (this.func_146078_ca())
			{
				this.setCreeperState(1);
			}

			int var1 = this.getCreeperState();

			if (var1 > 0 && (this.timeSinceIgnited % 30) == 0)
			{
				this.playSound("creeper.primed", 1.0F, 0.5F);
				
				if(this.timeSinceIgnited == 0)
				{
					Entity attacking = this.findPlayerToAttack();
					if(attacking instanceof EntityPlayer)
					{
						EntityPlayer pl = (EntityPlayer)attacking;
						
						int pl_facing = MathHelper.floor_double((double)(pl.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
						int creeper_facing = MathHelper.floor_double((double)(this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
						
						boolean lookingAt = (pl_facing + creeper_facing)%2 == 0 && pl_facing != creeper_facing;
						
						if(!lookingAt)
						{
							String toSend = NerfCreepersMain.instance.customMessage;
							
							if(toSend != null && toSend.length() > 0)
							{
								pl.closeScreen();
								
								if(pl.worldObj.isRemote)
									pl.addChatComponentMessage(new ChatComponentText(toSend));
							}
						}
					}
				}
			}

			this.timeSinceIgnited += var1;

			if (this.timeSinceIgnited < 0)
			{
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime)
			{
				this.timeSinceIgnited = this.fuseTime;
			}
		}
		super.onUpdate();
	}
	
	/**
     * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
     */
    public float getCreeperFlashIntensity(float ticks)
    {
        return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * ticks) / (float)(fuseTime - 2);
    }
}
