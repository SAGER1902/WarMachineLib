package wmlib.init;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityGrenade;
import wmlib.common.bullet.EntityRad;
import wmlib.common.bullet.EntityFlare;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import wmlib.rts.XiangJiEntity;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD)
public class WMModEntities
{
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "wmlib");
    public static final RegistryObject<EntityType<EntityBullet>> ENTITY_BULLET = register("entitybullet",
            EntityType.Builder.<EntityBullet>of(EntityBullet::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(false).setTrackingRange(60).fireImmune().noSave().sized(0.15f, 0.15f));
    public static final RegistryObject<EntityType<EntityShell>> ENTITY_SHELL = register("entityshell",
            EntityType.Builder.<EntityShell>of(EntityShell::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(false).setTrackingRange(60).fireImmune().noSave().sized(0.3f, 0.3f));
    public static final RegistryObject<EntityType<EntityMissile>> ENTITY_MISSILE = register("entitymissile",
            EntityType.Builder.<EntityMissile>of(EntityMissile::new, MobCategory.MISC).setCustomClientFactory(EntityMissile::new).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).fireImmune().setTrackingRange(80).noSave().sized(0.3f, 0.3f));
    public static final RegistryObject<EntityType<EntityGrenade>> ENTITY_GRENADE = register("entitygrenade",
            EntityType.Builder.<EntityGrenade>of(EntityGrenade::new, MobCategory.MISC).setCustomClientFactory(EntityGrenade::new).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).fireImmune().setTrackingRange(80).noSave().sized(0.3f, 0.3f));
			
    public static final RegistryObject<EntityType<EntityFlare>> ENTITY_FLARE = register("entityflare",
            EntityType.Builder.<EntityFlare>of(EntityFlare::new, MobCategory.CREATURE).setTrackingRange(60).fireImmune()
			.setUpdateInterval(3).setCustomClientFactory(EntityFlare::new).sized(0.3f, 0.3f));
			
    public static final RegistryObject<EntityType<EntityRad>> ENTITY_RAD = register("entityrad",
            EntityType.Builder.<EntityRad>of(EntityRad::new, MobCategory.CREATURE).setTrackingRange(60).fireImmune()
			.setUpdateInterval(3).setCustomClientFactory(EntityRad::new).sized(0.3f, 0.3f));
			
    public static final RegistryObject<EntityType<XiangJiEntity>> XIANG_JI = register("entityxj",
            EntityType.Builder.<XiangJiEntity>of(XiangJiEntity::new, MobCategory.CREATURE).setTrackingRange(100).fireImmune()
			.setUpdateInterval(3).setCustomClientFactory(XiangJiEntity::new).sized(1f, 1f));
			
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTER.register(name, () -> entityTypeBuilder.build(name));
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ENTITY_FLARE.get(), EntityFlare.createAttributes().build());
		event.put(ENTITY_RAD.get(), EntityRad.createAttributes().build());
		event.put(XIANG_JI.get(), EntityRad.createAttributes().build());
    }
}
