package wmlib.common.network;

import java.util.function.Supplier;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import net.minecraft.resources.ResourceLocation;
import wmlib.common.network.message.IMessage;
import wmlib.common.network.message.IMessage2;
import wmlib.common.network.message.MessageCame;
import wmlib.common.network.message.MessageRote;
import wmlib.common.network.message.MessageThrottle;
import wmlib.common.network.message.MessageFire;
import wmlib.common.network.message.MessageVehicleAnim;
import wmlib.common.network.message.MessageSoldierAnim;
import wmlib.common.network.message.MessageBulletMove;

import wmlib.common.network.message.MessageHit;
import wmlib.common.network.message.MessageTrail;
import wmlib.common.network.message.MessageCommander;
import wmlib.common.network.message.MessageChoose;

import wmlib.common.network.message.MessageShoot;
import wmlib.common.network.message.MessageFX;
import wmlib.common.network.message.MessageFirstTarget;
import wmlib.common.network.message.MessageAddOne;
import wmlib.common.network.message.MessageRtsClick;
import wmlib.common.network.message.MessageRtsCameraMove;

import wmlib.WarMachineLib;
public class PacketHandler
{
    private static final String PROTOCOL_VERSION = "1";
    private static final String PROTOCOL_VERSION2 = "2";
    private static final SimpleChannel HANDSHAKE_CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation.tryParse(WarMachineLib.MOD_ID+":"+"handshake"), () -> PROTOCOL_VERSION, s -> true, s -> true);
    private static final SimpleChannel PLAY_CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation.tryParse(WarMachineLib.MOD_ID+":"+"play"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static final SimpleChannel PLAY_CHANNEL_CLIENT = NetworkRegistry.newSimpleChannel(ResourceLocation.tryParse(WarMachineLib.MOD_ID+":"+"play2"), () -> PROTOCOL_VERSION2, PROTOCOL_VERSION2::equals, PROTOCOL_VERSION2::equals);
    private static int nextId = 0;
    private static int nextMessageId = 0;
    public static void registerPlayMessage()
    {
		registerPlayMessage(MessageCame.class, new MessageCame());
        registerPlayMessage(MessageRote.class, new MessageRote());
        registerPlayMessage(MessageThrottle.class, new MessageThrottle());
        registerPlayMessage(MessageFire.class, new MessageFire());
		registerPlayMessage(MessageHit.class, new MessageHit());
		registerPlayMessage(MessageVehicleAnim.class, new MessageVehicleAnim());
		registerPlayMessage(MessageSoldierAnim.class, new MessageSoldierAnim());
		
		registerPlayMessage(MessageCommander.class, new MessageCommander());
		registerPlayMessage(MessageShoot.class, new MessageShoot());
		registerPlayMessage(MessageChoose.class, new MessageChoose());
		registerPlayMessage(MessageAddOne.class, new MessageAddOne());
		registerPlayMessage(MessageRtsClick.class, new MessageRtsClick());
		registerPlayMessage(MessageRtsCameraMove.class, new MessageRtsCameraMove());
		registerPlayMessage(MessageFirstTarget.class, new MessageFirstTarget());
		
		registerPlayMessage2(MessageBulletMove.class, MessageBulletMove::new, LogicalSide.CLIENT);
		registerPlayMessage2(MessageTrail.class, MessageTrail::new, LogicalSide.CLIENT);
		registerPlayMessage2(MessageFX.class, MessageFX::new, LogicalSide.CLIENT);
    }

    private static <T> void registerPlayMessage(Class<T> clazz, IMessage<T> message)
    {
        PLAY_CHANNEL.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
    private static <T extends IMessage2> void registerPlayMessage2(Class<T> clazz, Supplier<T> messageSupplier, LogicalSide side)
    {
        PLAY_CHANNEL_CLIENT.registerMessage(nextMessageId++, clazz, IMessage2::encode, buffer -> {
            T t = messageSupplier.get();
            t.decode(buffer);
            return t;
        }, (t, supplier) -> {
            if(supplier.get().getDirection().getReceptionSide() != side)
                throw new RuntimeException("Attempted to handle message " + clazz.getSimpleName() + " on the wrong logical side!");
            t.handle(supplier);
        });
    }

    public static SimpleChannel getHandshakeChannel()
    {
        return HANDSHAKE_CHANNEL;
    }

    public static SimpleChannel getPlayChannel()
    {
        return PLAY_CHANNEL;
    }
    public static SimpleChannel getPlayChannel_Client()
    {
        return PLAY_CHANNEL_CLIENT;
    }
}
