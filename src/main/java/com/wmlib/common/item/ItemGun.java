package wmlib.common.item;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wmlib.client.obj.SAObjModel;
import java.util.Random;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.WMSoundEvent;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;

import wmlib.common.bullet.EntityBulletBase;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;

import wmlib.common.living.AI_MissileLock;
import wmlib.common.world.WMExplosionBase;
import safx.SagerFX;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.ModList;
public class ItemGun extends Item implements IVanishable {//BowItem
	public ItemGun(Item.Properties builder) {
		super(builder);
	}

	public int isreload;
	public int retime;
	public int reloadtime;
	
	public boolean is_sr = false;

	public float arm_r_posx = 0F;
	public float arm_r_posy = 0F;
	public float arm_r_posz = 0F;
	public float arm_l_posx = 0F;
	public float arm_l_posy = -0.5F;
	public float arm_l_posz = 1F;

	public boolean obj_true = true;
	public SAObjModel obj_model = null;
	public SAObjModel arm_model = new SAObjModel("wmlib:textures/model/arm/arm_64.obj");
	public SAObjModel flash_model = null;
	public ResourceLocation obj_tex = null;
	public ResourceLocation fire_tex = null;
	/*public String obj_model_resource = "wmlib:textures/model/ak74.obj";
	public String obj_tex = "wmlib:textures/model/ak74.png";*/

	public float model_x = 1.8F;
	public float model_y = -2F;
	public float model_z = -3.5F;
	public float model_x_ads = 0F;
	public float model_y_ads = -1.7F;
	public float model_z_ads = -1.5F;

	public float Sprintrotationx = 20F;
	public float Sprintrotationy = 60F;
	public float Sprintrotationz = 0F;
	public float Sprintoffsetx = 0.5F;
	public float Sprintoffsety = 0.0F;
	public float Sprintoffsetz = 0.5F;

	public float rotationx = 0F;
	public float rotationy = 180F;
	public float rotationz = 0F;

	public float model_muzz_jump_x = 0.5F;
	public float model_cock_z = -0.4F;
	public boolean zoomrender = true;
	
	public double recoil=0.5;
	public float exlevel = 0;
	public float scopezoom;
	public int cycle = 4;
	public float movespeed = 1.0F;

	public double recoilads;
	public static String ads;
	public int aaa;
	public static boolean grenadekey;

	public Item magazine = null;
	public String fire_sound = "fire_rifle";
	public String reload_sound = "reload_mag";
	public float fire_posx = 0.3F;
	public float fire_posy = 1.5F;
	public float fire_posz = 1.0F;
	public float fire_posy_sneak = 1.3F;

	public int bulletid = 0;
	public int bullettype = 0;
	public int bulletdamage = 5;
	public int bulletcount = 1;
	public int bullettime = 50;
	public int reloadSoundStart1 = 20;
	public float bulletspeed = 4;
	public float bulletspread = 2;
	public float bulletgravity = 0.01F;
	public boolean bulletdestroy = false;
	public boolean weaponcross = false;
	public String bulletmodel1 = "wmlib:textures/entity/bullet/bullet.obj";
	public String bullettex1 = "wmlib:textures/entity/bullet/bullet.png";
	public String firefx1 = "GunCloud";
	public String bulletfx1 = null;

	/*public void setWeapon(int id,
		String model, String tex, String fx1, String fx2, 
		String sound, String soundr,float w, float h, float z,
		int damage, float speed, float recoil, float ex, boolean extrue, int count,  float gra, int maxtime, int typeid){
		this.bulletid = id;
		this.bullettype = typeid;
		this.bulletdamage = damage;
		this.bulletspeed = speed;
		this.bulletspread = recoil;
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
	}*/
	public int lockcool = 0;
	public int locktime = 0;
	public int tracktick = 0;
	public Entity mitarget = null;
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(this.lockcool<20)++lockcool;
		if(this.locktime>0)--locktime;
		if(this.locktime==1){
			if(!worldIn.isClientSide)this.mitarget=null;
		}
		if(entityIn instanceof PlayerEntity){
			PlayerEntity fireplayer = (PlayerEntity)entityIn;
			if(isSelected) {
				if(bulletid==4 && fireplayer.getUseItem()==stack && this.aim_time>=this.time_aim){
					if(bullettype<5){//at
						AI_MissileLock.ItemLock(this,fireplayer,false, 1);
					}
					if(bullettype==5){//aa
						AI_MissileLock.ItemLock(this,fireplayer,true, 1);
					}
					if(bullettype==6){//tow
					}
					if(bullettype==7){//tow block
					}
					if(bullettype==8){//block
					}
				}else{
					if(!worldIn.isClientSide)this.mitarget=null;
				}
	
				entityIn.setDeltaMovement(entityIn.getDeltaMovement().x * movespeed, entityIn.getDeltaMovement().y, entityIn.getDeltaMovement().z * movespeed);
				if(this.cooltime < 6)++this.cooltime;//枪械奔跑动画
				{
					if(fireplayer.isSprinting()||this.open_time!=0){
						if(this.run_time<this.time_run)++this.run_time;//枪械奔跑动画
					}else{
						if(this.run_time>0)--this.run_time;//枪械奔跑动画
					}
				}
			}
			if(stack.hasTag()) {
				this.Gun_Reload(stack, worldIn, fireplayer, itemSlot, isSelected);
				this.gunbase_recoil(stack, worldIn, fireplayer, itemSlot, isSelected, 0);
			}else {
			  CompoundNBT ltags = new CompoundNBT();
			  stack.setTag(ltags);
			}
		}
	}
	
	public void gunbase_recoil(ItemStack itemstack, World world, Entity entity, int i, boolean flag, int ii) {
    	//if(world.isClientSide) 
    	{
    		CompoundNBT nbt = itemstack.getTag();
        	boolean recoiled = nbt.getBoolean("Recoiled");
    		int recoiledtime = nbt.getInt("RecoiledTime");
			//nbt.putBoolean("Unbreakable", true);
    		{
    			if(recoiledtime >= ii){
    				nbt.putInt("RecoiledTime", 0);
    				nbt.putBoolean("Recoiled", true);
    			}else
    			if(!recoiled){
    				++recoiledtime;
    				nbt.putInt("RecoiledTime", recoiledtime);
    			}
    		}
    	}
    }
	
	public boolean powergun = false;//是否蓄力枪
	public int powertime = 0;
	public int powertimemax = 200;//蓄力最大时长
	public int powerlevel = 25;//蓄力档位增加的时长
	public float power_damage = 2F;//蓄力增加伤害
	public float power_exlevel = 2F;//蓄力增加爆炸
	public float power_speed = 2F;//蓄力增加子弹速度
	public int power_count = 2;//蓄力增加子弹数量
	public int power_time = 20;//蓄力增加子弹时间
	public String power_sound = "wmlib.fix";//蓄力音效名称
	public String power_sound_fire = "wmlib.fire_mast";//蓄力开火音效名称
	public int powerlevel_sound = 100;//蓄力更换开火音效需要的时间
	public int powerlevel_cost = 2;//蓄力档位增加的消耗
	public int power_sound_tick = 2;//蓄力音效播放间隔
	
    public void gunbase_powertime(ItemStack itemstack, World world, PlayerEntity player) {
    	//if(world.isClientSide) 
    	{
    		CompoundNBT nbt = itemstack.getTag();
        	//boolean recoiled = nbt.getBoolean("Recoiled");
    		int power_time_nbt = nbt.getInt("PowerTime");
    		{
    			if(power_time_nbt >= this.powertimemax){
    				//nbt.putInt("PowerTime", 0);
    			}else{
    				++power_time_nbt;
    				nbt.putInt("PowerTime", power_time_nbt);
					this.powertime = power_time_nbt;
    			}
				/*if(this.power_sound != null && this.powertime%this.power_sound_tick==0){
					world.playSound((PlayerEntity)null, player.posX, player.posY, player.posZ,
							SoundEvent.REGISTRY.getObject(new ResourceLocation(this.modid, this.power_sound)), SoundCategory.NEUTRAL, 3.0F, 1F);
				}*/
    		}
    	}
    }
	public int cooltime = 1;
	public boolean aim_fire = true;
	public float time_aim = 6F;
	public float time_run = 6F;
	
	public float open_time;
	
	public float run_time;
	public static float aim_time;
	
    public void Fire_AR_Left(ItemStack itemstack, World world, Entity entity, boolean flag, boolean flag1) {//发射
    	int li = getMaxDamage() - itemstack.getDamageValue();
    	PlayerEntity fireplayer = (PlayerEntity) entity;
		//CompoundNBT nbt = itemstack.getTag();
    	/*boolean lflag = cycleBolt(itemstack);
		boolean lflag_barst = cycleBrast(itemstack);*/
		boolean left_key = true;
    	if (entity != null && fireplayer != null && !itemstack.isEmpty()) 
		{
    		{//主手
				if (left_key) {
					{
    					/*if (lflag && lflag_barst) */{//确保动画流畅
    						/*if (li > 0) */{
								this.cooltime = 0;//开火时刻
    							//this.Fire_Base(itemstack, world, fireplayer, li, true, nbt, flag1);
								this.FireBullet(itemstack, world, fireplayer);
    						}/* else {
    							if(world.isClientSide)ItemGun.updateCheckinghSlot(entity, itemstack);
    						}*/
    					}/* else {
    						if(world.isClientSide)ItemGun.updateCheckinghSlot(entity, itemstack);
    					}*/
    				}
    			}
    		}
		}
    }
    
    public void Fire_SR_Left(ItemStack itemstack, World world, Entity entity, boolean flag, boolean flag1) {//半自动
    	int li = getMaxDamage() - itemstack.getDamageValue();
    	PlayerEntity fireplayer = (PlayerEntity) entity;
		//CompoundNBT nbt = itemstack.getTag();
		//boolean cocking = nbt.getBoolean("Cocking");
		boolean left_key = true;
		if (entity != null && fireplayer != null && !itemstack.isEmpty()) 
		{
			{//主手
				if (left_key) {
    				{
    					/*if (cocking) */{
    						/*if (li > 0) */{
								this.cooltime = 0;//开火时刻
								//this.Fire_Base(itemstack, world, fireplayer, li, true, nbt, flag1);
								this.FireBullet(itemstack, world, fireplayer);
    						}/* else {
    							if(world.isClientSide)ItemGun.updateCheckinghSlot(entity, itemstack);
    						}*/
    					}/* else {
    						if(world.isClientSide)ItemGun.updateCheckinghSlot(entity, itemstack);
    					}*/
    				}
    			}
    		}
		}
    }
	public String modid = "wmlib";
	public static SoundEvent getSound(String id,String soundName) {
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(id, soundName));
		//SoundEvent sound = new SoundEvent(new ResourceLocation(id,soundName));
		return sound;
	}
	
   /*public boolean hurt(int p_96631_1_, Random p_96631_2_, @Nullable ServerPlayerEntity p_96631_3_) {
      if (!this.isDamageableItem()) {
         return false;
      } else {
         if (p_96631_1_ > 0) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, this);
            int j = 0;

            for(int k = 0; i > 0 && k < p_96631_1_; ++k) {
               if (UnbreakingEnchantment.shouldIgnoreDurabilityDrop(this, i, p_96631_2_)) {
                  ++j;
               }
            }

            p_96631_1_ -= j;
            if (p_96631_1_ <= 0) {
               return false;
            }
         }

         if (p_96631_3_ != null && p_96631_1_ != 0) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(p_96631_3_, this, this.getDamageValue() + p_96631_1_);
         }

         int l = this.getDamageValue() + p_96631_1_;
         this.setDamageValue(l);
         return l >= this.getMaxDamage();
      }
   }*/
	double recoil1 = 1;
	public void FireBullet(ItemStack stack, World worldIn, PlayerEntity playerIn) 
	{
		if(stack.hasTag()) {
			CompoundNBT nbt = stack.getTag();
			nbt.putBoolean("Recoiled", false);
			nbt.putBoolean("Cocking", false);
		}
		//WarMachineLib.proxy.onShootAnimation(playerIn, stack);
		this.onFireAnimation(playerIn, stack);
		//if (this.isReload(stack))return;
		worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(),getSound(modid,fire_sound), SoundCategory.NEUTRAL, 3.0F, 1.0F);
		{
			recoil1=recoil;
			double xx11 = 0;
			double zz11 = 0;
			double yy11 = 0;
			float xz = 1.57F;
			if(!playerIn.getMainHandItem().isEmpty() && !playerIn.getOffhandItem().isEmpty()) {
				if(playerIn.getMainHandItem() == stack) {
					xz = 1.57F;
				}else if(playerIn.getOffhandItem() == stack){
					xz = -1.57F;
				}
			}else if (playerIn.getUseItem()==stack) {
				xz = 0;
				fire_posx=0;
				recoil=recoilads;
			}else if(playerIn.getMainHandItem() == stack) {
				xz = 1.57F;
			}else if(playerIn.getOffhandItem() == stack){
				xz = -1.57F;
			}
			
			double reco = recoil1;
			if(playerIn.isCrouching()) {
				reco = recoil1*0.5F;
			}
			playerIn.xRot += (worldIn.random.nextFloat() * -2F) * reco;
			
			double yy = fire_posy;
			if (playerIn.isCrouching()) {
				yy = fire_posy_sneak;
			}
			double zzz = fire_posz*0.1F * Math.cos(Math.toRadians(-playerIn.xRot));
			xx11 -= MathHelper.sin(playerIn.yHeadRot * 0.01745329252F) * zzz;
			zz11 += MathHelper.cos(playerIn.yHeadRot * 0.01745329252F) * zzz;
			xx11 -= MathHelper.sin(playerIn.yHeadRot * 0.01745329252F + xz) * fire_posx;
			zz11 += MathHelper.cos(playerIn.yHeadRot * 0.01745329252F + xz) * fire_posx;
			yy11 = MathHelper.sqrt(zzz* zzz) * Math.tan(Math.toRadians(-playerIn.xRot)) * 1D;
			if(this.bulletid < 10){
				if(this.firefx1!=null && ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(this.firefx1, null, playerIn.getX() + xx11, playerIn.getY() + yy + yy11, playerIn.getZ() + zz11, 0, 0, 0, 1F+this.exlevel*0.1F);
				EntityBulletBase bullet;
				for (int i = 0; i < this.bulletcount; ++i) {
					if (this.bulletid == 1) {
						bullet = new EntityBullet(playerIn.level, playerIn);
					}else if (this.bulletid == 2) {
						bullet = new EntityShell(playerIn.level, playerIn);
					}else if (this.bulletid == 3) {
						bullet = new EntityShell(playerIn.level, playerIn);
					}else if (this.bulletid == 4) {
						bullet = new EntityMissile(playerIn.level, playerIn, mitarget, playerIn);
					}else{
						bullet = new EntityBullet(playerIn.level, playerIn);
					}
					bullet.power = this.bulletdamage;
					bullet.destroy = this.bulletdestroy;
					bullet.exlevel = this.exlevel;
					bullet.timemax = this.bullettime;
					bullet.setGravity(this.bulletgravity);
					if(this.bulletfx1!=null)bullet.setFX(this.bulletfx1);
					bullet.setModel(this.bulletmodel1);
					bullet.setTex(this.bullettex1);
					bullet.setBulletType(this.bullettype);
					if(this.bullettype == 5 && this.bulletid != 4)bullet.flame = true;
					bullet.moveTo(playerIn.getX() + xx11, playerIn.getY() + yy + yy11, playerIn.getZ() + zz11, playerIn.yHeadRot, playerIn.xRot);
					bullet.shootFromRotation(playerIn, playerIn.xRot, playerIn.yHeadRot, 0.0F, this.bulletspeed, this.bulletspread);
					if (!playerIn.level.isClientSide) {
						playerIn.level.addFreshEntity(bullet);
					}
				}
			}
		}
		//setDamage(stack, stack.getMaxDamage() - 1);
		//playerIn.awardStat(Stats.ITEM_USED.get(this));
		if (!playerIn.abilities.instabuild) {
			//itemstack.shrink(1);
			stack.hurtAndBreak(1, playerIn, (p_220009_1_) -> {
			p_220009_1_.broadcastBreakEvent(playerIn.getUsedItemHand());});
		}
	}
	
    public void onFireAnimation(PlayerEntity player, ItemStack itemstack) {
		RenderParameters.rate = Math.min(RenderParameters.rate + 0.07f, 1f);
		float recoilPitchGripFactor = 1.0f;
		float recoilYawGripFactor = 1.0f;
		float recoilPitchBarrelFactor = 1.0f;
		float recoilYawBarrelFactor = 1.0f;
		float recoilPitchStockFactor = 1.0f;
		float recoilYawStockFactor = 1.0f;

		float recoilPitch1 = (float)this.recoil1;//垂直后座
		float recoilPitch2 = (float)this.recoil1/4;//垂直浮动
		float recoilYaw1 = (float)this.recoil1/10;//水平后座
		float recoilYaw2 = (float)this.recoil1/20;//水平浮动
		/*float recoilYaw1 = (float)this.recoil_ads;//水平后座
		float recoilYaw2 = (float)this.recoil_ads/4;//水平浮动*/
		
		float offsetYaw = 0;
		float offsetPitch = 0;
		if (!player.isCrouching()/* && player.getActiveItemStack() != itemstack*/) {//腰射
			offsetPitch = recoilPitch1;
			offsetPitch += ((recoilPitch2 * 2) - recoilPitch2);
			offsetPitch *= (recoilPitchGripFactor * recoilPitchBarrelFactor * recoilPitchStockFactor);

			offsetYaw = recoilYaw1;
			offsetYaw *= new Random().nextFloat() * (recoilYaw2 * 2) - recoilYaw2;
			offsetYaw *= recoilYawGripFactor * recoilYawBarrelFactor * recoilYawStockFactor;
			offsetYaw *= RenderParameters.rate;
			offsetYaw *= RenderParameters.phase ? 1 : -1;
		} else {//瞄准
			offsetYaw *= RenderParameters.phase ? 1 : -1;
			offsetPitch = recoilPitch1;
			offsetPitch += ((recoilPitch2 * 2) - recoilPitch2);
			offsetPitch *= (recoilPitchGripFactor * recoilPitchBarrelFactor * recoilPitchStockFactor);
			offsetPitch *= 0.5F;//减少后坐力百分比

			offsetYaw = recoilYaw1;
			offsetYaw *= new Random().nextFloat() * (recoilYaw2 * 2) - recoilYaw2;
			offsetYaw *= recoilYawGripFactor * recoilYawBarrelFactor * recoilYawStockFactor;
			offsetYaw *= RenderParameters.rate;
			offsetYaw *= 0.5F;//减少后坐力百分比
			offsetYaw *= RenderParameters.phase ? 1 : -1;
		}

		RenderParameters.playerRecoilPitch += offsetPitch;
		if (Math.random() > 0.5f) {
			RenderParameters.playerRecoilYaw += offsetYaw;
		} else {
			RenderParameters.playerRecoilYaw -= offsetYaw;
		}
		RenderParameters.phase = !RenderParameters.phase;
    }
	 
	private ItemStack findAmmo(PlayerEntity player)
	{
		if (this.isAmmo(player.getItemInHand(Hand.OFF_HAND)))
		{
			return player.getItemInHand(Hand.OFF_HAND);
		}
		else if (this.isAmmo(player.getItemInHand(Hand.MAIN_HAND)))
		{
			return player.getItemInHand(Hand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getContainerSize(); ++i)
			{
				ItemStack itemstack = player.inventory.getItem(i);

				if (this.isAmmo(itemstack))
				{
					return itemstack;
				}
			}

			return ItemStack.EMPTY;
		}
	}
	 
	public boolean isAmmo(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItem() == this.magazine;
	}
   
	public void setReload(ItemStack gun_item, World par2World, PlayerEntity par3PlayerEntity) 
	{
		//boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY_ARROWS, par3PlayerEntity) > 0;
		boolean linfinity = true;
		if(!par3PlayerEntity.abilities.instabuild){//2019/10/27
			for (int i = 0; i < par3PlayerEntity.inventory.getContainerSize(); ++i) {
				ItemStack itemstack = par3PlayerEntity.inventory.getItem(i);
				/*if (this.isAmmo(itemstack)) */{
					ItemStack ammo_item = itemstack;
					if (!ammo_item.isEmpty()) {
						int zan = getDamage(gun_item) - 0;
						int lii = ammo_item.getCount() - zan;
						if (!linfinity) {
							if (lii <= 0) {
								setDamage(gun_item, getDamage(gun_item) - ammo_item.getCount());
							} else {
								setDamage(gun_item, 0);
							}
						} else {
							setDamage(gun_item, 0);
						}
						if (!linfinity) {
							ammo_item.shrink(zan);
							if (ammo_item.isEmpty()) {
								par3PlayerEntity.inventory.removeItem(ammo_item);
							}
						}
						if(this.getDamage(gun_item) == 0) {
							break;
						}
					}
				}
			}
		}

	}
 
	public boolean isReload(ItemStack gun_item) {
		//if (this.getMaxDamage(gun_item) - gun_item.getDamage() > 0) {
		if (gun_item.getDamageValue() < gun_item.getMaxDamage() - 1) {
			return false;
		}else {
			return true;
		}
		//return false;
	}
	 
	 public void Gun_Reload(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
			CompoundNBT nbt = itemstack.getTag();
			if (itemstack.hasTag()/* && this.magazine != null*/) {
				PlayerEntity PlayerEntity = (PlayerEntity)entity;
				ItemGun gun = (ItemGun) itemstack.getItem();
				//if (this.isReload(itemstack) && flag && PlayerEntity.inventory.hasItemStack(new ItemStack(this.magazine))) {
				if (this.isReload(itemstack) && flag/* && !this.findAmmo(PlayerEntity).isEmpty()*/) {
						if (entity != null && entity instanceof PlayerEntity) {
							if (itemstack == PlayerEntity.getMainHandItem()) {
								int reloadti = nbt.getInt("RloadTime");
								{
									gun.retime = reloadti;
									if(gun.retime == 0) {
										//SoundEvent sound = new SoundEvent(new ResourceLocation(WarMachineLib.MOD_ID, reload_sound));
										world.playSound(PlayerEntity, PlayerEntity.getX(), PlayerEntity.getY(), PlayerEntity.getZ(), 
												this.getSound(modid, this.reload_sound), SoundCategory.NEUTRAL, 1.0F, 1.0F);
									}
									++reloadti;
									if (reloadti >= gun.reloadtime) {
										gun.retime = reloadti = 0;
										nbt.putInt("RloadTime", 0);
										{
											setReload(itemstack, world, PlayerEntity);
										}
									}else{
										nbt.putInt("RloadTime", reloadti);
									}
								}
							}
						}
				}
			}
	}

   /**
    * How long it takes to use or consume an item
    */
   public int getUseDuration(ItemStack stack) {
      return 72000;
   }
   /**
    * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
    * {@link #onItemUse}.
    */
   public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
     // boolean flag = !playerIn.findAmmo(itemstack).isEmpty();
      boolean flag = !playerIn.getProjectile(itemstack).isEmpty();

      ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
      if (ret != null) return ret;

      if (!playerIn.abilities.instabuild && !flag) {
         return ActionResult.fail(itemstack);
      } else {
         playerIn.startUsingItem(handIn);
         //return ActionResult.consume(itemstack);
		 return ActionResult.fail(itemstack);
      }
   }

   /**
    * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
    */
   /*public Predicate<ItemStack> getInventoryAmmoPredicate() {
      return ARROW_OR_FIREWORK;
   }

   public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
      return arrow;
   }*/

   public int func_230305_d_() {
      return 15;
   }
}
