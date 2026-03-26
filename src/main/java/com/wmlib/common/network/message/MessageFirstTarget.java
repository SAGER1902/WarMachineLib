package wmlib.common.network.message;

import wmlib.common.network.play.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
/**
 * Author: MrCrayfish
 */
public class MessageFirstTarget implements IMessage<MessageFirstTarget>
{
    private int entityId;
    private int animId;

    public MessageFirstTarget() {}

    public MessageFirstTarget(int entityId, int animId)
    {
        this.entityId = entityId;
        this.animId = animId;
    }

    @Override
    public void encode(MessageFirstTarget message, FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(message.entityId);
        buffer.writeVarInt(message.animId);
    }

    @Override
    public MessageFirstTarget decode(FriendlyByteBuf buffer)
    {
        return new MessageFirstTarget(buffer.readVarInt(), buffer.readVarInt());
    }

    @Override
    public void handle(MessageFirstTarget message, Supplier<NetworkEvent.Context> supplier)
    {
        if(supplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
        {
            IMessage.enqueueTask(supplier, () -> ClientPlayHandler.handleFirstTarget(message));
        }
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public int getTargetId()
    {
        return this.animId;
    }
}
