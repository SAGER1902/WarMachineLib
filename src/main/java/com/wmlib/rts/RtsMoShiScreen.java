package wmlib.rts;
import com.mojang.blaze3d.systems.RenderSystem;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import wmlib.WarMachineLib;
import wmlib.rts.WorldRenderHandler;
import wmlib.rts.RtsMoShiMenu;
import wmlib.util.ScreenUtils;
import wmlib.common.network.message.MessageRtsCameraMove;
import wmlib.common.network.message.MessageRtsClick;
import wmlib.common.network.message.MessageAddOne;
import wmlib.common.network.message.MessageCommander;
import wmlib.common.network.PacketHandler;
import org.lwjgl.glfw.GLFW;
import java.util.HashMap;
import wmlib.api.IArmy;
import wmlib.api.ITool;
import java.util.List;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
public class RtsMoShiScreen extends AbstractContainerScreen<RtsMoShiMenu> {
	private static final int EDGE_SCROLL_MARGIN = 20; // 屏幕边缘检测范围
	private static final double CAMERA_MOVE_SPEED = 1.0; // 相机移动速度
	private static final double CAMERA_HEIGHT_CHANGE = 0.5; // 每次滚动改变的高度
	private static final double MIN_CAMERA_HEIGHT = -50.0; // 最小高度
	private static final double MAX_CAMERA_HEIGHT = 150.0; // 最大高度
	private double cameraX, cameraY, cameraZ; // 跟踪相机位置
	public static int cameraID=-1;
	private int cameraRote;
	
	// 添加框选相关变量
	private boolean isDragging = false;
	
	private int dragStartX, dragStartY;
	private int dragEndX, dragEndY;
	private final java.util.List<Entity> selectedEntities = new java.util.ArrayList<>();
	
	private final static HashMap<String, Object> guistate = RtsMoShiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private static final int HOTBAR_HEIGHT = 22;
	private static final int HOTBAR_WIDTH = 182;

	public RtsMoShiScreen(RtsMoShiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 0;
		this.imageHeight = 0;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		// 渲染物品选择栏
		int x = (this.width - HOTBAR_WIDTH) / 2;
		int y = this.height - HOTBAR_HEIGHT - 40;
		//renderHotbar(guiGraphics, x, y);
		// 渲染框选区域
		if (isDragging) {
			int minX = Math.min(dragStartX, dragEndX);
			int maxX = Math.max(dragStartX, dragEndX);
			int minY = Math.min(dragStartY, dragEndY);
			int maxY = Math.max(dragStartY, dragEndY);
			// 渲染半透明的选择框
			guiGraphics.fill(minX, minY, maxX, maxY, 0x3FFFFFFF);
			// 渲染选择框边框
			guiGraphics.fill(minX, minY, maxX, minY + 1, 0xFFFFFFFF);
			guiGraphics.fill(minX, maxY - 1, maxX, maxY, 0xFFFFFFFF);
			guiGraphics.fill(minX, minY, minX + 1, maxY, 0xFFFFFFFF);
			guiGraphics.fill(maxX - 1, minY, maxX, maxY, 0xFFFFFFFF);
		}
		// 处理鼠标悬停位置的高亮
		if (minecraft != null && minecraft.level != null && !isDragging) {
			ScreenUtils.RaycastResult result = ScreenUtils.screenToWorld(minecraft, mouseX, mouseY);
			if (result != null) {
				if (result.blockPos != null) {
					// 如果射线击中了方块，高亮显示该方块
					if(result.hitEntity!=null){
						//WorldRenderHandler.haveent=true;
						WorldRenderHandler.setHighlightedBlock(new BlockPos((int)result.hitEntity.getX(),(int)result.hitEntity.getY(),(int)result.hitEntity.getZ()));
					}else{
						WorldRenderHandler.setHighlightedBlock(result.blockPos);
						//WorldRenderHandler.haveent=false;
					}
				}
			}
		}
		// 高亮显示所有选中的实体
		for (Entity entity : selectedEntities) {
			WorldRenderHandler.setHighlightedEntity(entity);
		}
	}

	// 添加新的方法来渲染物品选择栏
	private void renderHotbar(GuiGraphics guiGraphics, int x, int y) {
		if (this.minecraft != null && this.minecraft.player != null) {
			// 只渲染物品栏物品和选中框
			for (int i = 0; i < 9; i++) {
				int slotX = x + 1 + i * 20;
				int slotY = y + 3;
				// 渲染物品槽背景
				guiGraphics.fill(slotX - 1, slotY - 1, slotX + 17, slotY + 17, 0x80000000);
				guiGraphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0x80404040);
				// 渲染物品
				guiGraphics.renderItem(this.minecraft.player.getInventory().items.get(i), slotX, slotY);
				guiGraphics.renderItemDecorations(this.minecraft.font, this.minecraft.player.getInventory().items.get(i), slotX, slotY);
			}
			// 渲染选中框
			int selectedX = x + 1 + this.minecraft.player.getInventory().selected * 20;
			int selectedY = y + 3;
			guiGraphics.fill(selectedX - 2, selectedY - 2, selectedX + 18, selectedY + 18, 0xFFFFFFFF);
			guiGraphics.fill(selectedX - 1, selectedY - 1, selectedX + 17, selectedY + 17, 0xFF000000);
		}
	}
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		/*// 检查是否点击在物品栏上
		int hotbarX = (this.width - HOTBAR_WIDTH) / 2;
		int hotbarY = this.height - HOTBAR_HEIGHT - 40;
		// 计算点击位置是否在物品栏范围内
		if (mouseY >= hotbarY && mouseY <= hotbarY + HOTBAR_HEIGHT) {
			for (int i = 0; i < 9; i++) {
				int slotX = hotbarX + 1 + i * 20;
				int slotY = hotbarY + 3;
				
				if (mouseX >= slotX && mouseX <= slotX + 16 && 
					mouseY >= slotY && mouseY <= slotY + 16) {
					// 设置选中的物品栏位置
					this.minecraft.player.getInventory().selected = i;
					return true;
				}
			}
		}*/
		// 开始框选时清除之前的选择
		if (button == 0) {
			isDragging = true;
			dragStartX = dragEndX = (int) mouseX;
			dragStartY = dragEndY = (int) mouseY;
			// 清除之前的高亮和选择
			WorldRenderHandler.clearHighlight();
			selectedEntities.clear();
			//return true;
		}
		// 如果不是点击物品栏，则处理其他点击事件
		if (button == 0) {
			int posXm = 0;
			int posYm = 0;
			int posZm = 0;
			if (minecraft != null && minecraft.level != null) {
				boolean lctrol = GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS;
				int type = 0;
				if(lctrol){
					type=1;
				}else if(twotime>0){
					type=3;
				}
				twotime=5;
				ScreenUtils.RaycastResult result = ScreenUtils.screenToWorld(minecraft, mouseX, mouseY);
				if(result.blockPos!=null){
					posXm = result.blockPos.getX();
					posYm = result.blockPos.getY();
					posZm = result.blockPos.getZ();
				}
				if (result != null) {
					if(result.hitEntity!=null && !(result.hitEntity instanceof ITool) && result.hitEntity.getVehicle()==null){
						selectedEntities.add(result.hitEntity);
						WorldRenderHandler.setHighlightedEntity(result.hitEntity);
					}
					PacketHandler.getPlayChannel().sendToServer(new MessageRtsClick(type, posXm, posYm, posZm,
						result.hitEntity != null ? result.hitEntity.getId() : -1));
					return true;
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0 && isDragging) {
			isDragging = false;
			// 清除之前的高亮
			WorldRenderHandler.clearHighlight();
			selectedEntities.clear();
			// 处理框选区域内的实体
			handleSelection();
			return true;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (isDragging) {
			dragEndX = (int) mouseX;
			dragEndY = (int) mouseY;
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	private void handleSelection() {
		if (minecraft == null || minecraft.level == null) return;
		// 获取框选区域的边界
		int minX = Math.min(dragStartX, dragEndX);
		int maxX = Math.max(dragStartX, dragEndX);
		int minY = Math.min(dragStartY, dragEndY);
		int maxY = Math.max(dragStartY, dragEndY);
		// 如果选择区域太小，认为是点击而不是框选
		if (maxX - minX < 5 && maxY - minY < 5) {
			ScreenUtils.RaycastResult result = ScreenUtils.screenToWorld(minecraft, dragEndX, dragEndY);
			if (result != null && result.hitEntity != null) {
				selectedEntities.add(result.hitEntity);
				WorldRenderHandler.setHighlightedEntity(result.hitEntity);
			}
			return;
		}
		// 在框选区域内进行多次射线检测
		
		int min = 10;
		if(disY>50){
			min=8;
		}else if(disY>70){
			min=7;
		}else if(disY>90){
			min=6;
		}else{
			min=5;
		}
		for (int x = minX; x <= maxX; x += min) {
			for (int y = minY; y <= maxY; y += min) {
				ScreenUtils.RaycastResult result = ScreenUtils.screenToWorld(minecraft, x, y);
				if (result != null && result.hitEntity != null) {
					if (!selectedEntities.contains(result.hitEntity)) {
						if(result.hitEntity instanceof IArmy){
							IArmy unit = (IArmy) result.hitEntity;
							if (unit.getArmyOwner()==minecraft.player && !unit.getSelect()){
								selectedEntities.add(result.hitEntity);
								WorldRenderHandler.setHighlightedEntity(result.hitEntity);
								PacketHandler.getPlayChannel().sendToServer(new MessageAddOne(result.hitEntity.getId()));
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {  // ESC键
			if (this.minecraft.player != null) {
				this.minecraft.setCameraEntity(this.minecraft.player);
				PacketHandler.getPlayChannel().sendToServer(new MessageCommander(32));
			}
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		// delta > 0 表示向上滚动，delta < 0 表示向下滚动
		/*double newHeight = cameraY + (-delta * CAMERA_HEIGHT_CHANGE);
		
		// 限制高度范围
		newHeight = Math.max(MIN_CAMERA_HEIGHT, Math.min(MAX_CAMERA_HEIGHT, newHeight));*/
		
		// 如果高度发生变化
		/*if (newHeight != cameraY) {
			cameraY = newHeight;
			// 发送相机位置更新消息到服务器
			if(cameraID!=-1)PacketHandler.getPlayChannel().sendToServer(new MessageRtsCameraMove(cameraID,cameraX,(int)cameraY,cameraZ,cameraRote));
			cameraY=0;
			newHeight=0;
		}*/
		if(delta>0)cameraY=-2;
		if(delta<0)cameraY=2;
		//if(cameraID!=-1)PacketHandler.getPlayChannel().sendToServer(new MessageRtsCameraMove(cameraID,cameraX,(int)cameraY,cameraZ,cameraRote));
		return true;
	}
	int ctime;
	int disY;
	@Override
	public void containerTick() {
		super.containerTick();
		if (minecraft != null && minecraft.screen == this) {
			boolean have=false;
			List<Entity> list = minecraft.player.level().getEntities(minecraft.player, minecraft.player.getBoundingBox().inflate(100D, 100D, 100D));
			for(int k2 = 0; k2 < list.size(); ++k2) {
				Entity ent = list.get(k2);
				if(ent instanceof XiangJiEntity){
					XiangJiEntity xj = (XiangJiEntity)ent;
					if(xj.getOwner()==minecraft.player){
						cameraID=xj.getId();
						disY=Math.abs((int)ent.getY()-(int)minecraft.player.getY());
						have=true;
						break;
					}
				}
			}
			if(!have){
				++ctime;
				if(ctime>20){
					if (this.minecraft.player != null) {
						this.minecraft.setCameraEntity(this.minecraft.player);
						PacketHandler.getPlayChannel().sendToServer(new MessageCommander(32));
					}
					this.minecraft.player.closeContainer();
					this.minecraft.player.sendSystemMessage(Component.literal("距离过远,无人机已回收!"));
				}
			}
			
			boolean keya = GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS;
			boolean keyb = GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS;
			
			if(keya||keyb){
				if(keya)cameraRote=1;
				if(keyb)cameraRote=2;
			}else{
				cameraRote=0;
			}
			
			int mouseX = (int) (minecraft.mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth());
			int mouseY = (int) (minecraft.mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight());
			// 计算相机移动
			double deltaX = 0;
			double deltaZ = 0;
			// 检查水平边缘
			if (mouseX < EDGE_SCROLL_MARGIN) {
				deltaX += CAMERA_MOVE_SPEED;
			} else if (mouseX > minecraft.getWindow().getGuiScaledWidth() - EDGE_SCROLL_MARGIN) {
				deltaX -= CAMERA_MOVE_SPEED;
			}

			// 检查垂直边缘
			if (mouseY < EDGE_SCROLL_MARGIN) {
				deltaZ += CAMERA_MOVE_SPEED;
			} else if (mouseY > minecraft.getWindow().getGuiScaledHeight() - EDGE_SCROLL_MARGIN) {
				deltaZ -= CAMERA_MOVE_SPEED;
			}
			// 如果需要移动相机，标准化移动速度
			if (deltaX != 0 || deltaZ != 0 ||cameraRote!=-1||cameraY!=0) {
				// 如果同时有水平和垂直移动，需要标准化速度
				if (deltaX != 0 && deltaZ != 0) {
					double length = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
					deltaX = (deltaX / length) * CAMERA_MOVE_SPEED;
					deltaZ = (deltaZ / length) * CAMERA_MOVE_SPEED;
				}
				/*if(Math.abs(cameraX-minecraft.player.getX())<60&&Math.abs(cameraZ-minecraft.player.getZ())<60)*/{
					cameraX += deltaX;
					cameraZ += deltaZ;
				}
				if(cameraID!=-1)PacketHandler.getPlayChannel().sendToServer(new MessageRtsCameraMove(cameraID,cameraX,(int)cameraY,cameraZ,cameraRote));
				cameraRote=-1;
				cameraX=0;
				cameraZ=0;
				cameraY=0;
			}
		}
	}

	@Override
	public void init() {
		super.init();
		// 初始化相机位置为玩家当前位置
		if (minecraft != null && minecraft.player != null) {
			/*cameraX = minecraft.player.getX();
			cameraY = minecraft.player.getY() + 10;
			cameraZ = minecraft.player.getZ();*/
			// 隐藏十字准星
			minecraft.options.hideGui = true;
			// 隐藏玩家手
			minecraft.gameRenderer.setRenderHand(false);
		}
	}

	@Override
	public void removed() {
		super.removed();
		if (this.minecraft != null) {
			// 恢复玩家视角
			if (this.minecraft.player != null) {
				this.minecraft.setCameraEntity(this.minecraft.player);
				PacketHandler.getPlayChannel().sendToServer(new MessageCommander(32));
			}
			// 恢复十字准星
			minecraft.options.hideGui = false;
			// 恢复玩家手
			minecraft.gameRenderer.setRenderHand(true);
			// 清除所有高亮和选择
			WorldRenderHandler.clearHighlight();
			selectedEntities.clear();
		}
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}
}
