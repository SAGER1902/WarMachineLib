package wmlib.common.item;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;

import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.client.util.ITooltipFlag;

import wmlib.client.obj.SAObjModel;
import net.minecraft.util.ResourceLocation;

public class ItemGun_Custom extends ItemGun implements IVanishable {
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
		this.obj_tex = new ResourceLocation(objt);
		this.flash_model = new SAObjModel(fobj);
		this.fire_tex = new ResourceLocation(ftex);
		this.arm_l_posx = ax;
		this.arm_l_posy = ay;
		this.arm_l_posz = az;
		this.scopezoom = isZoom;
		this.zoomrender = zoomr;
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

   public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	   super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
   }
   public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
	  /*if (entityLiving instanceof PlayerEntity) {
		 PlayerEntity playerIn = (PlayerEntity)entityLiving;
	  }*/
   }
   
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("什么,你竟然找到了测试中的枪械？").withStyle(TextFormatting.GREEN));//name
	}
   
   public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
	  playerIn.startUsingItem(handIn);
	  ItemStack itemstack = playerIn.getItemInHand(handIn);
	  return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
	  /*ItemStack itemstack = playerIn.getItemInHand(handIn);
	  if (!itemstack.isEmpty() && !itemstack.onEntitySwing(playerIn)){
		  setDamage(itemstack, itemstack.getMaxDamage() - 1);
	  }else
	  //if(fire_flag) 
	  {
		  FireBullet(itemstack, worldIn, playerIn);
	  }
	  playerIn.awardStat(Stats.ITEM_USED.get(this));
	  return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
	  //playerIn.setActiveHand(handIn);
	 //    return ActionResult.resultConsume(itemstack);*/
   }
}
