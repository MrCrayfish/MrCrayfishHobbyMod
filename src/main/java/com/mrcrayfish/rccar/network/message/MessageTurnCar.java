package com.mrcrayfish.rccar.network.message;

import java.util.List;

import com.mrcrayfish.rccar.entity.EntityCar;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTurnCar implements IMessage, IMessageHandler<MessageTurnCar, IMessage>
{
	private String uuid;
	private float rotationYaw;

	public MessageTurnCar() {}

	public MessageTurnCar(String uuid, float rotationYaw)
	{
		this.uuid = uuid;
		this.rotationYaw = rotationYaw;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, uuid);
		buf.writeFloat(rotationYaw);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.uuid = ByteBufUtils.readUTF8String(buf);
		this.rotationYaw = buf.readFloat();
	}

	@Override
	public IMessage onMessage(MessageTurnCar message, MessageContext ctx)
	{
		World world = ctx.getServerHandler().playerEntity.world;
		List<EntityCar> cars = world.getEntities(EntityCar.class, EntitySelectors.IS_ALIVE);
		for(EntityCar car : cars)
		{
			if(car.getUniqueID().toString().equals(message.uuid))
			{
				car.rotationYaw = message.rotationYaw;
				return null;
			}
		}
		return null;
	}

}
