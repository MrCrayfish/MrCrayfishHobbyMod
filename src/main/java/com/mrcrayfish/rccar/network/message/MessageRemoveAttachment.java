package com.mrcrayfish.rccar.network.message;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.entity.EntityCar.AttachmentType;
import com.mrcrayfish.rccar.interfaces.IAttachment;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRemoveAttachment implements IMessage, IMessageHandler<MessageRemoveAttachment, IMessage> 
{
	private int entityId;
	private int attachmentType;
	
	public MessageRemoveAttachment() {}
	
	public MessageRemoveAttachment(EntityCar car, AttachmentType type) 
	{
		this.entityId = car.getEntityId();
		this.attachmentType = type.ordinal();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(this.entityId);
		buf.writeInt(this.attachmentType);
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.entityId = buf.readInt();
		this.attachmentType = buf.readInt();
	}

	@Override
	public IMessage onMessage(MessageRemoveAttachment message, MessageContext ctx) 
	{
		EntityPlayer player = ctx.getServerHandler().playerEntity;
		Entity entity = player.world.getEntityByID(message.entityId);
		if(entity instanceof EntityCar)
		{
			EntityCar car = (EntityCar) entity;
			IAttachment attachment = car.removeAttachment(AttachmentType.values()[message.attachmentType]);
			if(attachment instanceof Item)
			{
				ItemStack attachmentItem = new ItemStack((Item)attachment);
				if(!player.inventory.addItemStackToInventory(attachmentItem))
				{
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, attachmentItem));
				}
			}
		}
		return null;
	}
}
