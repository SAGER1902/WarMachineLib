package wmlib.common.network.message;

import wmlib.common.network.play.ClientPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
/**
 * Author: MrCrayfish
 */
public class MessageVehicleAnim implements IMessage<MessageVehicleAnim>
{
    private int entityId;
    private int animId;

    public MessageVehicleAnim() {}

    public MessageVehicleAnim(int entityId, int animId)
    {
        this.entityId = entityId;
        this.animId = animId;
    }

    @Override
    public void encode(MessageVehicleAnim message, PacketBuffer buffer)
    {
        buffer.writeVarInt(message.entityId);
        buffer.writeVarInt(message.animId);
    }

    @Override
    public MessageVehicleAnim decode(PacketBuffer buffer)
    {
        return new MessageVehicleAnim(buffer.readVarInt(), buffer.readVarInt());
    }

    @Override
    public void handle(MessageVehicleAnim message, Supplier<NetworkEvent.Context> supplier)
    {
        if(supplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
        {
            IMessage.enqueueTask(supplier, () -> ClientPlayHandler.handleVehicleFireAnim(message));
        }
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public int getAnimId()
    {
        return this.animId;
    }
}
