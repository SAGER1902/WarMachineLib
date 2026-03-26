package wmlib.common.network.message;

import wmlib.common.network.play.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

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
    public void encode(MessageVehicleAnim message, FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(message.entityId);
        buffer.writeVarInt(message.animId);
    }

    @Override
    public MessageVehicleAnim decode(FriendlyByteBuf buffer)
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
