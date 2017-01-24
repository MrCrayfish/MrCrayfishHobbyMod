package com.mrcrayfish.rccar.network.message;

import java.util.List;

import com.mrcrayfish.rccar.entity.EntityCar;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageExplodeCar implements IMessage, IMessageHandler<MessageExplodeCar, IMessage>
{
	private String uuid;

	public MessageExplodeCar() {}

	public MessageExplodeCar(String uuid)
	{
		this.uuid = uuid;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, uuid);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.uuid = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public IMessage onMessage(MessageExplodeCar message, MessageContext ctx)
	{
		World world = ctx.getServerHandler().playerEntity.world;
		List<EntityCar> cars = world.getEntities(EntityCar.class, EntitySelectors.IS_ALIVE);
		for(EntityCar car : cars)
		{
			if(car.getUniqueID().toString().equals(message.uuid))
			{
				world.createExplosion(ctx.getServerHandler().playerEntity, car.posX, car.posY, car.posZ, 2F, true);
				car.setDead();
				break;
			}
		}
		ItemStack stack = ctx.getServerHandler().playerEntity.getHeldItemMainhand();
		if(!stack.isEmpty()) stack.setTagCompound(new NBTTagCompound());
		return null;
	}

}
