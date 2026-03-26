package advancearmy.item;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;          // BlockState 路径更新
import net.minecraft.world.level.block.Blocks;                   // Blocks 包路径更新
import net.minecraft.world.entity.EntityType;                    // 路径更新
import net.minecraft.world.entity.MobSpawnType;                  // SpawnReason → MobSpawnType
import net.minecraft.world.entity.player.Player;                 // Player → Player
import net.minecraft.world.item.Item;                            // 路径更新
import net.minecraft.world.item.ItemStack;                       // 路径更新
import net.minecraft.world.item.context.UseOnContext;            // ItemUseContext → UseOnContext
import net.minecraft.world.InteractionResult;                    // 路径更新
import net.minecraft.world.InteractionHand;                      // InteractionHand → InteractionHand
import net.minecraft.nbt.CompoundTag;                            // CompoundNBT → CompoundTag
import net.minecraft.stats.Stats;                                // 路径更新
import net.minecraft.world.level.block.entity.SpawnerBlockEntity; // MobSpawnerTileEntity → SpawnerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity;        // TileEntity → BlockEntity
import net.minecraft.network.chat.Component;                     // ITextComponent → Component
import net.minecraft.ChatFormatting;                             // ChatFormatting → ChatFormatting
import net.minecraft.network.chat.MutableComponent;              // 替代 TranslationTextComponent（通过 Component.translatable()）
import net.minecraft.world.item.TooltipFlag;                     // ITooltipFlag → TooltipFlag
import net.minecraft.world.entity.EquipmentSlot;                 // EquipmentSlotType → EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment;         // 路径更新
import net.minecraft.world.level.Level;                          // Level → Level
import net.minecraft.world.entity.LivingEntity;                  // 路径更新
import net.minecraft.world.item.Items;                           // 路径更新
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import advancearmy.init.ModEntities;
import wmlib.common.item.ItemGun;
import wmlib.client.obj.SAObjModel;
import wmlib.common.bullet.EntityMissile;
import wmlib.api.IRaderItem;
import advancearmy.entity.map.SupportPoint;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
public class ItemGun_Target extends ItemGun implements IRaderItem {
	public ItemGun_Target(Item.Properties builder) {
		super(builder);
		this.obj_model = new SAObjModel("advancearmy:textures/gun/targetgun.obj");
		this.obj_tex = ResourceLocation.tryParse("advancearmy:textures/gun/gun.png");
		this.arm_l_posx=0;
		this.arm_l_posy=-0.5F;
		this.arm_l_posz=-1.5F;
	}

	public boolean fire_flag = true;
	public int fire_count = 0;
	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable("advancearmy.infor.targetgun1.desc").withStyle(ChatFormatting.GREEN));//name
	}
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
	}
	public void FireBullet(ItemStack stack, Level worldIn, Player player) 
	{
		if(player.getOffhandItem()!=null){
			ItemStack held = player.getOffhandItem();
			Item items = held.getItem();
			if(items instanceof ItemSupport){
				ItemSupport support = (ItemSupport)items;
				if(player!=null){
					if(player.getScore()>=support.xp||player.isCreative()||support.type==1){
						ItemCooldowns tracker = player.getCooldowns();
						if(!tracker.isOnCooldown(support)||support.type==1){
							if(!player.isCreative() && support.type==0){
								player.giveExperiencePoints(-support.xp);
								player.getCooldowns().addCooldown(this, support.cool);
							}
							//worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),WMSoundEvent.getSound(this.fire_sound), SoundCategory.NEUTRAL, 3.0F, 1.0F);
							Vec3 lock = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
							int range = 2;
							int ix = 0;
							int iy = 0;
							int iz = 0;
							
							int posXm = 0;
							int posYm = 0;
							int posZm = 0;
							boolean isGroundAim = false;
							for(int x2 = 0; x2 < 120; ++x2) {
								ix = (int) (player.getX() + lock.x * x2);
								iy = (int) (player.getY() + 1.5 + lock.y * x2);
								iz = (int) (player.getZ() + lock.z * x2);
								if(ix != 0 && iz != 0 && iy != 0){
									BlockPos blockpos = new BlockPos(ix,iy,iz);
									BlockState iblockstate = player.level().getBlockState(blockpos);
									if (!iblockstate.isAir()){
										posXm = ix;
										posYm = iy+1;
										posZm = iz;
										isGroundAim = true;
										break;
									}else{
									}
								}
							}
							if(support.id==1)player.sendSystemMessage(Component.translatable("SWUN探索者小队即将到达"));
							if(support.id==2)player.sendSystemMessage(Component.translatable("超时空传送正在启动"));
							if(support.id==3)player.sendSystemMessage(Component.translatable("超时空传送正在启动"));
							if(support.id==4)player.sendSystemMessage(Component.translatable("轨道空投启动"));
							if(support.id==5)player.sendSystemMessage(Component.translatable("轨道空投启动"));
							if(support.id==6)player.sendSystemMessage(Component.translatable("A-10攻击机即将前往轰炸！"));
							if(support.id==7)player.sendSystemMessage(Component.translatable("F35战机即将前往轰炸！"));
							if(support.id==8)player.sendSystemMessage(Component.translatable("3架A-10攻击机即将前往轰炸！"));
							if(support.id==9)player.sendSystemMessage(Component.translatable("3架F35战机即将前往轰炸！"));
							if(support.id==10)player.sendSystemMessage(Component.translatable("开~炮！！！！"));
							if(support.id==11){
								EntityMissile bullet = new EntityMissile(worldIn, player, null, player);
								bullet.modid="advancearmy";
								bullet.fly_sound="advancearmy.missile_fly2";
								bullet.timemax=700;
								bullet.power = 700;
								bullet.setGravity(0.01F);
								bullet.setExLevel(12);
								bullet.hitEntitySound=SASoundEvent.artillery_impact.get();
								bullet.hitBlockSound=SASoundEvent.artillery_impact.get();
								bullet.setBulletType(7);
								bullet.setModel("advancearmy:textures/entity/bullet/kh29l.obj");
								bullet.setTex("advancearmy:textures/entity/bullet/kh29t.png");
								bullet.setFX("BigMissileTrail");
								//bullet.flame = true;
								bullet.moveTo(player.getX(), player.getY()+100, player.getZ(), player.getYRot(), player.getXRot());
								bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 7F, 1F);
								if (!worldIn.isClientSide) worldIn.addFreshEntity(bullet);
								player.sendSystemMessage(Component.translatable("警告！KH29导弹已发射！！"));
							}
							if(support.id==12){
								EntityMissile bullet = new EntityMissile(worldIn, player, posXm, posYm-1, posZm, player);
								bullet.modid="advancearmy";
								bullet.fly_sound="advancearmy.missile_fly3";
								bullet.timemax=700;
								bullet.power = 1000;
								bullet.setGravity(0.01F);
								bullet.setExLevel(20);
								bullet.hitEntitySound=SASoundEvent.missile_hit1.get();
								bullet.hitBlockSound=SASoundEvent.missile_hit1.get();
								bullet.setBulletType(8);
								bullet.setModel("advancearmy:textures/entity/bullet/3m22.obj");
								bullet.setTex("advancearmy:textures/entity/bullet/3m22.png");
								bullet.setFX("BigMissileTrail");
								//bullet.flame = true;
								bullet.moveTo(player.getX(), player.getY()+100, player.getZ(), player.getYRot(), player.getXRot());
								bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 9F, 1F);
								player.sendSystemMessage(Component.translatable("警告！3M22导弹已发射！！"));
								if (!worldIn.isClientSide) worldIn.addFreshEntity(bullet);
							}
							if(support.id==13){
								EntityMissile bullet = new EntityMissile(worldIn, player, null, player);
								bullet.modid="advancearmy";
								bullet.fly_sound="advancearmy.missile_fly2";
								bullet.timemax=700;
								bullet.power = 1100;
								bullet.setGravity(0.01F);
								bullet.setExLevel(22);
								bullet.hitEntitySound=SASoundEvent.missile_hit1.get();
								bullet.hitBlockSound=SASoundEvent.missile_hit1.get();
								bullet.setBulletType(7);
								bullet.setModel("advancearmy:textures/entity/bullet/agm158.obj");
								bullet.setTex("advancearmy:textures/entity/bullet/agm158.png");
								bullet.setFX("BigMissileTrail");
								//bullet.flame = true;
								bullet.moveTo(player.getX(), player.getY()+100, player.getZ(), player.getYRot(), player.getXRot());
								bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 7F, 1F);
								player.sendSystemMessage(Component.translatable("警告！AGM-158导弹已发射！！"));
								if (!worldIn.isClientSide) worldIn.addFreshEntity(bullet);
							}
							if(support.id==14){
								EntityMissile bullet = new EntityMissile(worldIn, player, posXm, posYm-1, posZm, player);
								bullet.modid="advancearmy";
								bullet.fly_sound="advancearmy.missile_fly1";
								bullet.timemax=700;
								bullet.power = 1200;
								bullet.setGravity(0.01F);
								bullet.setExLevel(25);
								bullet.hitEntitySound=SASoundEvent.missile_hit1.get();
								bullet.hitBlockSound=SASoundEvent.missile_hit1.get();
								bullet.setBulletType(8);
								bullet.setModel("advancearmy:textures/entity/bullet/kh58.obj");
								bullet.setTex("advancearmy:textures/entity/bullet/kh58.png");
								bullet.setFX("BigMissileTrail");
								//bullet.flame = true;
								bullet.moveTo(player.getX(), player.getY()+100, player.getZ(), player.getYRot(), player.getXRot());
								bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 7F, 1F);
								player.sendSystemMessage(Component.translatable("警告！KH58导弹已发射！！"));
								if (!worldIn.isClientSide) worldIn.addFreshEntity(bullet);
							}
							if(support.id==15){
								if(isGroundAim){
									player.sendSystemMessage(Component.translatable("警告！战术核弹已发射！！"));
									worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),this.getSound("advancearmy","advancearmy.nuclear_worn"), SoundSource.NEUTRAL, 10.0F, 1.0F);
								}else{
									player.sendSystemMessage(Component.translatable("需要地面坐标！！！"));
								}
							}
							if(support.id==16)player.sendSystemMessage(Component.translatable("超时空传送正在启动"));
							if(posXm!=0 || posYm!=0 || posZm!=0){
								SupportPoint point = new SupportPoint(ModEntities.ENTITY_SPT.get(), worldIn);
								if(player!=null){
									/*if(player.isCrouching() && player.isCreative()){
									}else*/{
										point.tame(player);
										if(player.getTeam()!=null && player.getTeam() instanceof PlayerTeam){
											player.level().getScoreboard().addPlayerToTeam(point.getUUID().toString(), (PlayerTeam)player.getTeam());
										}
									}
								}
								point.setSummonID(support.id);
								point.moveTo(posXm, posYm, posZm, 0, 0);
								worldIn.addFreshEntity(point);
							}
							if(support.id==17){
								if(isGroundAim){
									player.sendSystemMessage(Component.translatable("警告！真空内爆弹已发射！！"));
									worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),this.getSound("advancearmy","advancearmy.nuclear_worn"), SoundSource.NEUTRAL, 10.0F, 1.0F);
								}else{
									player.sendSystemMessage(Component.translatable("需要地面坐标！！！"));
								}
							}
							if(support.id==18){
								player.sendSystemMessage(Component.translatable("注意:黑曜石联邦的支援即将抵达"));
							}
							if(support.type==1)held.shrink(1);
							worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),this.getSound("advancearmy","advancearmy.command_say"), SoundSource.NEUTRAL, 10.0F, 1.0F);
						}
					}else{
						worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),this.getSound("advancearmy","advancearmy.command_say"), SoundSource.NEUTRAL, 10.0F, 1.0F);
						player.sendSystemMessage(Component.translatable("没有足够的经验值!"));
					}
				}
			}
		}
	}
}
