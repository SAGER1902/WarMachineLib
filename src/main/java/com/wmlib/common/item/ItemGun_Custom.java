package wmlib.common.item;
import java.util.List;
import java.util.function.Predicate;
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

import wmlib.client.obj.SAObjModel;
import net.minecraft.resources.ResourceLocation;


public class ItemGun_Custom extends ItemGun{
   public ItemGun_Custom(Item.Properties builder,boolean issr, String objm, String objt, int cyc, String modid, float isZoom, boolean zoomr, 
   float gx,float gy,float gz,float runx,float runy,float runz, float aimx,float aimy,float aimz, float fireup,float fireback,
	float ax, float ay, float az,/**/int id, String model, String tex, String fobj, String ftex, String fx1, String fx2, String sound, String soundr,
	float w, float h, float z, float sneaky,int damage, float speed, float spread, float ex, boolean extrue,
	int count,  float gra, int maxtime, int typeid) {
		super(builder);
		//render
		/*this.obj_model_resource = objm;
		this.obj_tex = objt;*/
		this.obj_model = new SAObjModel(objm);
		this.obj_tex = ResourceLocation.tryParse(objt);
		this.flash_model = new SAObjModel(fobj);
		this.fire_tex = ResourceLocation.tryParse(ftex);
		this.arm_l_posx = ax;
		this.arm_l_posy = ay;
		this.arm_l_posz = az;
		this.scopezoom = isZoom;
		this.zoomHide = zoomr;
		this.model_x = gx;
		this.model_y = gy;
		this.model_z = gz;
		this.Sprintrotationx = runx;
		this.Sprintrotationy = runy;
		this.Sprintrotationz = runz;
		this.model_x_ads = aimx;
		this.model_y_ads = aimy;
		this.model_z_ads = aimz;
		this.model_muzz_jump_x = fireup;
		this.model_cock_z = fireback;
		//fire
		this.is_sr = issr;
		this.modid=modid;
		this.cycle = cyc;
		this.recoil = 2.0F;
		this.recoilads = 1.0F;
		this.reloadtime = 80;
		this.bulletid = id;
		this.bullettype = typeid;
		this.bulletdamage = damage;
		this.bulletspeed = speed;
		this.bulletspread = spread;
		this.exlevel = ex;
		this.bulletdestroy = extrue;
		this.bulletcount = count;
		this.bulletgravity = gra;
		this.bullettime = maxtime;
		this.bulletmodel1 = model;
		this.bullettex1 = tex;
		this.firefx1 = fx1;
		this.bulletfx1 = fx2;
		this.fire_sound = sound;
		this.reload_sound = soundr;
		this.fire_posx = w;
		this.fire_posy = h;
		this.fire_posz = z;
		this.fire_posy_sneak = sneaky;
		//this.magazine = ;
   }

   public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
   }
   
	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable("什么,你竟然找到了测试中的枪械？").withStyle(ChatFormatting.GREEN));//name
	}
   
   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
	  playerIn.startUsingItem(handIn);
	  ItemStack itemstack = playerIn.getItemInHand(handIn);
	  return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
   }
}
