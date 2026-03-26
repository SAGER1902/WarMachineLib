package wmlib.common.network;
import wmlib.WarMachineLib;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class HandshakeHandler
{
    private static final Marker VEHICLE_HANDSHAKE = MarkerManager.getMarker("VEHICLE_HANDSHAKE");

    static void handleAcknowledge(HandshakeMessages.C2SAcknowledge message, Supplier<NetworkEvent.Context> c)
    {
        //WarMachineLib.LOGGER.debug(VEHICLE_HANDSHAKE, "Received acknowledgement from client");
        c.get().setPacketHandled(true);
    }

    static void handleVehicleProperties(HandshakeMessages.S2CVehicleProperties message, Supplier<NetworkEvent.Context> c)
    {
        //WarMachineLib.LOGGER.debug(VEHICLE_HANDSHAKE, "Received vehicle properties from server");

        AtomicBoolean updated = new AtomicBoolean(false);
        CountDownLatch block = new CountDownLatch(1);
        c.get().enqueueWork(() ->
        {
            block.countDown();
        });

        try
        {
            block.await();
        }
        catch(InterruptedException e)
        {
            Thread.interrupted();
        }

        c.get().setPacketHandled(true);

        if(updated.get())
        {
            //WarMachineLib.LOGGER.info("Successfully synchronized vehicle properties from server");
            PacketHandler.getHandshakeChannel().reply(new HandshakeMessages.C2SAcknowledge(), c.get());
        }
        else
        {
            //WarMachineLib.LOGGER.error("Failed to synchronize vehicle properties from server");
            //c.get().getNetworkManager().disconnect(new Component("Connection closed - [WarMachineLib Mod] Failed to synchronize vehicle properties from server"));
		}
    }
}