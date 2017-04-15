package com.mrcrayfish.rccar.network.message;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.entity.EntityCar.Properties;
import com.mrcrayfish.rccar.network.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateProperties  implements IMessage, IMessageHandler<MessageUpdateProperties, IMessage>
{
	private int entityId;
	private Properties props;
	
	public MessageUpdateProperties() {}
	
	public MessageUpdateProperties(EntityCar car) 
	{
		this.entityId = car.getEntityId();
		this.props = car.getProperties();
	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(this.entityId);
		ByteBufUtils.writeTag(buf, this.props.writeToTag());
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.entityId = buf.readInt();
		this.props = new Properties(ByteBufUtils.readTag(buf));
	}
	
	@Override
	public IMessage onMessage(MessageUpdateProperties message, MessageContext ctx) 
	{
		World world = ctx.getServerHandler().playerEntity.world;
		Entity entity = world.getEntityByID(message.entityId);
		if(entity instanceof EntityCar)
		{
			EntityCar car = (EntityCar) entity;
			car.getProperties().sync(message.props);
			PacketHandler.INSTANCE.sendToAllAround(new MessageSyncProperties(car), new TargetPoint(car.dimension, car.posX, car.posY, car.posZ, 64));
		}
		return null;
	}

}
