package wmlib.common.network.message;

import wmlib.common.network.play.ClientPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
public class MessageSoldierAnim implements IMessage<MessageSoldierAnim>
{
    private int entityId;
    private int animId;

    public MessageSoldierAnim() {}

    public MessageSoldierAnim(int entityId, int animId)
    {
        this.entityId = entityId;
        this.animId = animId;
    }

    @Override
    public void encode(MessageSoldierAnim message, PacketBuffer buffer)
    {
        buffer.writeVarInt(message.entityId);
        buffer.writeVarInt(message.animId);
    }

    @Override
    public MessageSoldierAnim decode(PacketBuffer buffer)
    {
        return new MessageSoldierAnim(buffer.readVarInt(), buffer.readVarInt());
    }

    @Override
    public void handle(MessageSoldierAnim message, Supplier<NetworkEvent.Context> supplier)
    {
        if(supplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
        {
            IMessage.enqueueTask(supplier, () -> ClientPlayHandler.handleSoldierFireAnim(message));
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
