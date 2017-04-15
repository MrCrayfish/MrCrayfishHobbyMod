package com.mrcrayfish.rccar.network.message;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.entity.EntityCar.Properties;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncProperties  implements IMessage, IMessageHandler<MessageSyncProperties, IMessage>
{
	private int entityId;
	private Properties props;
	
	public MessageSyncProperties() 
	{
		this.props = new Properties();
	}
	
	public MessageSyncProperties(EntityCar car) 
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
		this.props.readFromTag(ByteBufUtils.readTag(buf));
	}
	
	@Override
	public IMessage onMessage(MessageSyncProperties message, MessageContext ctx) 
	{
		World world = Minecraft.getMinecraft().world;
		Entity entity = world.getEntityByID(message.entityId);
		if(entity instanceof EntityCar)
		{
			EntityCar car = (EntityCar) entity;
			car.getProperties().sync(message.props);
		}
		return null;
	}

}
