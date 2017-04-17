package com.mrcrayfish.rccar.network.message;

import com.mrcrayfish.rccar.entity.EntityCar;
import com.mrcrayfish.rccar.entity.EntityCar.AttachmentType;
import com.mrcrayfish.rccar.interfaces.IAttachment;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAddAttachment implements IMessage, IMessageHandler<MessageAddAttachment, IMessage> 
{
	private int entityId;
	private int attachmentType;
	private int slot;
	
	public MessageAddAttachment() {}
	
	public MessageAddAttachment(EntityCar car, AttachmentType type, int slot) 
	{
		this.entityId = car.getEntityId();
		this.attachmentType = type.ordinal();
		this.slot = slot;
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(this.entityId);
		buf.writeInt(this.attachmentType);
		buf.writeInt(this.slot);
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.entityId = buf.readInt();
		this.attachmentType = buf.readInt();
		this.slot = buf.readInt();
	}

	@Override
	public IMessage onMessage(MessageAddAttachment message, MessageContext ctx) 
	{
		EntityPlayer player = ctx.getServerHandler().playerEntity;
		if(message.slot > 0 && message.slot < player.inventory.getSizeInventory())
		{
			ItemStack attachmentItem = player.inventory.getStackInSlot(message.slot);
			if(!attachmentItem.isEmpty() && attachmentItem.getItem() instanceof IAttachment)
			{
				Entity entity = player.world.getEntityByID(message.entityId);
				if(entity instanceof EntityCar)
				{
					EntityCar car = (EntityCar) entity;
					AttachmentType type = AttachmentType.values()[message.attachmentType];
					car.setAttachment(type, (IAttachment) attachmentItem.getItem());
					player.inventory.setInventorySlotContents(message.slot, ItemStack.EMPTY);
				}
			}
		}
		return null;
	}
}
