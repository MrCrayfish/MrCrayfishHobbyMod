package com.mrcrayfish.rccar.network;

import com.mrcrayfish.rccar.Reference;
import com.mrcrayfish.rccar.network.message.MessageExplodeCar;
import com.mrcrayfish.rccar.network.message.MessageMoveCar;
import com.mrcrayfish.rccar.network.message.MessageSyncProperties;
import com.mrcrayfish.rccar.network.message.MessageTurnCar;
import com.mrcrayfish.rccar.network.message.MessageUpdateProperties;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

	public static void init()
	{
		INSTANCE.registerMessage(MessageMoveCar.class, MessageMoveCar.class, 0, Side.SERVER);
		INSTANCE.registerMessage(MessageTurnCar.class, MessageTurnCar.class, 1, Side.SERVER);
		INSTANCE.registerMessage(MessageExplodeCar.class, MessageExplodeCar.class, 2, Side.SERVER);
		INSTANCE.registerMessage(MessageUpdateProperties.class, MessageUpdateProperties.class, 3, Side.SERVER);
		INSTANCE.registerMessage(MessageSyncProperties.class, MessageSyncProperties.class, 4, Side.CLIENT);
	}
}
