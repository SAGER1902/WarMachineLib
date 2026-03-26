package advancearmy.client.config;

import advancearmy.AAConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.List;
import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class GuiAdvanceArmyConfig extends Screen {
    private final Screen parentScreen;
    private ConfigList configList;
    private Button doneButton;
    private Button cancelButton;
    private boolean needsRestart = false;
    
    public GuiAdvanceArmyConfig(Screen parentScreen) {
        super(Component.translatable("advancearmy.config.title"));
        this.parentScreen = parentScreen;
    }
    
    @Override
    protected void init() {
        super.init();
        
        // 计算列表位置和大小
        int listTop = 32;
        int listBottom = this.height - 64;
        int listHeight = listBottom - listTop;
        
        // 创建配置列表 - 使用正确的构造函数参数
        this.configList = new ConfigList(
            this.minecraft, 
            this.width,        // width
            this.height,       // height (整个屏幕高度)
            listTop,           // y0 (列表顶部)
            listBottom,        // y1 (列表底部)
            25                 // itemHeight (每个条目的高度)
        );
        this.addWidget(this.configList);
        
        // 计算按钮位置和大小
        int buttonWidth = Math.min(150, (this.width - 30) / 2);
        int buttonX = (this.width - buttonWidth * 2 - 10) / 2;
        int buttonY = this.height - 28;
        
        // 完成按钮
        this.doneButton = Button.builder(Component.translatable("gui.done"), button -> {
            saveConfig();
            this.minecraft.setScreen(parentScreen);
        }).bounds(buttonX, buttonY, buttonWidth, 20).build();
        this.addRenderableWidget(this.doneButton);
        
        // 取消按钮
        this.cancelButton = Button.builder(Component.translatable("gui.cancel"), button -> {
            this.minecraft.setScreen(parentScreen);
        }).bounds(buttonX + buttonWidth + 10, buttonY, buttonWidth, 20).build();
        this.addRenderableWidget(this.cancelButton);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        
        // 标题居中显示
        guiGraphics.drawCenteredString(font, title, width / 2, 15, 0xFFFFFF);
        
        // 显示文件路径
        String configPath = "config/advancearmy-common.toml";
        guiGraphics.drawCenteredString(font, configPath, width / 2, height - 40, 0x808080);
        
        // 渲染列表
        this.configList.render(guiGraphics, mouseX, mouseY, partialTicks);
        
        // 渲染按钮
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(parentScreen);
    }
    
    private void markDirty() {
        //needsRestart = true;
    }
    
    private void saveConfig() {
        AAConfig.getSpec().save();
        AAConfig.syncConfig();
        if (needsRestart && minecraft.player != null) {
            minecraft.player.displayClientMessage(
                Component.translatable("advancearmy.config.needRestart"), 
                false
            );
        }
    }
    
    // 配置项列表类
    private class ConfigList extends ContainerObjectSelectionList<ConfigList.Entry> {
        public ConfigList(net.minecraft.client.Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight) {
            super(minecraft, width, height, y0, y1, itemHeight);
            
            // 添加布尔值配置项
            this.addEntry(new BooleanEntry(Component.translatable("advancearmy.config.vehicleDestroy"),
                    Component.translatable("advancearmy.config.vehicleDestroy.tooltip"),
                    AAConfig.VEHICLE_DESTROY));
            
            this.addEntry(new BooleanEntry(Component.translatable("advancearmy.config.spawnFriendTeam"),
                    Component.translatable("advancearmy.config.spawnFriendTeam.tooltip"),
                    AAConfig.SPAWN_TEAM));
            
            this.addEntry(new BooleanEntry(Component.translatable("advancearmy.config.structureFriendTeam"),
                    Component.translatable("advancearmy.config.structureFriendTeam.tooltip"),
                    AAConfig.STRUCTURE_TEAM));
            
            this.addEntry(new BooleanEntry(Component.translatable("advancearmy.config.spawnSoldier"),
                    Component.translatable("advancearmy.config.spawnSoldier.tooltip"),
                    AAConfig.SPAWN_SOLIDER));
            
            this.addEntry(new BooleanEntry(Component.translatable("advancearmy.config.spawnMob"),
                    Component.translatable("advancearmy.config.spawnMob.tooltip"),
                    AAConfig.SPAWN_MOB));
            
            this.addEntry(new BooleanEntry(Component.translatable("advancearmy.config.eroRaid"),
                    Component.translatable("advancearmy.config.eroRaid.tooltip"),
                    AAConfig.ERO_RAID));
            
            this.addEntry(new BooleanEntry(Component.translatable("advancearmy.config.addStructure"),
                    Component.translatable("advancearmy.config.addStructure.tooltip"),
                    AAConfig.ADD_STRUCTURE));
            
            this.addEntry(new IntEntry(Component.translatable("advancearmy.config.maxMobSpawnDay"),
                    Component.translatable("advancearmy.config.maxMobSpawnDay.tooltip"),
                    AAConfig.MAX_MOB_SPAWN_DAY, 0, 1000));
            
            this.addEntry(new IntEntry(Component.translatable("advancearmy.config.maxFriendSpawnDay"),
                    Component.translatable("advancearmy.config.maxFriendSpawnDay.tooltip"),
                    AAConfig.MAX_FRIEND_SPAWN_DAY, 0, 1000));
					
            this.addEntry(new IntEntry(Component.translatable("advancearmy.config.cycleMobEvent"),
                    Component.translatable("advancearmy.config.cycleMobEvent.tooltip"),
                    AAConfig.CYC_MOB_EVENT, 1, 100));
            this.addEntry(new IntEntry(Component.translatable("advancearmy.config.cycleFriendEvent"),
                    Component.translatable("advancearmy.config.cycleFriendEvent.tooltip"),
                    AAConfig.CYC_FRIEND_EVENT, 1, 100));
            this.addEntry(new IntEntry(Component.translatable("advancearmy.config.mobEventChance"),
                    Component.translatable("advancearmy.config.mobEventChance.tooltip"),
                    AAConfig.CHANCE_MOB_EVENT, 0, 100));
            this.addEntry(new IntEntry(Component.translatable("advancearmy.config.friendEventChance"),
                    Component.translatable("advancearmy.config.friendEventChance.tooltip"),
                    AAConfig.CHANCE_FRIEND_EVENT, 0, 100));
					
            this.addEntry(new IntEntry(Component.translatable("advancearmy.config.friendSpawnChance"),
                    Component.translatable("advancearmy.config.friendSpawnChance.tooltip"),
                    AAConfig.CHANCE_FRIEND_SPAWN, 0, 100));
            this.addEntry(new IntEntry(Component.translatable("advancearmy.config.mobSpawnChance"),
                    Component.translatable("advancearmy.config.mobSpawnChance.tooltip"),
                    AAConfig.CHANCE_MOB_SPAWN, 0, 100));
        }
        
        @Override
        public int getRowWidth() {
            return Math.min(400, this.width - 50); // 留出滚动条和边距的空间
        }
        
        @Override
        protected int getScrollbarPosition() {
            return this.x0 + this.width - 6;
        }
        
        // 配置项基类
        public abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            protected final Component name;
            protected final Component tooltip;
            
            public Entry(Component name, Component tooltip) {
                this.name = name;
                this.tooltip = tooltip;
            }
            
            @Override
            public List<? extends net.minecraft.client.gui.narration.NarratableEntry> narratables() {
                return new ArrayList<>();
            }
        }
        
        // 布尔值配置项
        private class BooleanEntry extends Entry {
            private final ForgeConfigSpec.ConfigValue<Boolean> configValue;
            private CycleButton<Boolean> button;
            
            public BooleanEntry(Component name, Component tooltip, ForgeConfigSpec.ConfigValue<Boolean> configValue) {
                super(name, tooltip);
                this.configValue = configValue;
            }
            
            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, 
                              int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
                if (this.button == null) {
                    int buttonWidth = Math.min(200, width - 110);
                    this.button = CycleButton.onOffBuilder(configValue.get())
                        .create(left + width - buttonWidth - 5, top, buttonWidth, 20, name,
                            (cycleButton, value) -> {
                                configValue.set(value);
                                markDirty();
                            });
                    this.button.setTooltip(Tooltip.create(tooltip));
                } else {
                    this.button.setX(left + width - this.button.getWidth() - 5);
                    this.button.setY(top);
                }
                
                // 渲染按钮
                this.button.render(guiGraphics, mouseX, mouseY, partialTick);
                
                // 渲染标签
                guiGraphics.drawString(GuiAdvanceArmyConfig.this.font, name, left + 5, top + 6, 0xFFFFFF);
            }
            
            @Override
            public List<? extends GuiEventListener> children() {
                return List.of(this.button);
            }
            
            @Override
            public List<? extends net.minecraft.client.gui.narration.NarratableEntry> narratables() {
                return List.of(this.button);
            }
        }
        
        // 整数值配置项
        private class IntEntry extends Entry {
            private final ForgeConfigSpec.ConfigValue<Integer> configValue;
            private final int min;
            private final int max;
            private EditBox editBox;
            
            public IntEntry(Component name, Component tooltip, ForgeConfigSpec.ConfigValue<Integer> configValue, 
                           int min, int max) {
                super(name, tooltip);
                this.configValue = configValue;
                this.min = min;
                this.max = max;
            }
            
            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, 
                              int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
                if (this.editBox == null) {
                    int editBoxWidth = Math.min(100, width - 110);
                    this.editBox = new EditBox(GuiAdvanceArmyConfig.this.font, 
                        left + width - editBoxWidth - 5, top, editBoxWidth, 20, name) {
                        @Override
                        public void insertText(String textToWrite) {
                            super.insertText(textToWrite);
                            try {
                                int value = Integer.parseInt(this.getValue());
                                if (value >= min && value <= max) {
                                    configValue.set(value);
                                    markDirty();
                                }
                            } catch (NumberFormatException e) {
                                // 忽略无效输入
                            }
                        }
                    };
                    this.editBox.setValue(String.valueOf(configValue.get()));
                    this.editBox.setTooltip(Tooltip.create(tooltip));
                    this.editBox.setFilter(s -> {
                        if (s.isEmpty()) return true;
                        try {
                            Integer.parseInt(s);
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    });
                } else {
                    this.editBox.setX(left + width - this.editBox.getWidth() - 5);
                    this.editBox.setY(top);
                }
                
                // 渲染编辑框
                this.editBox.render(guiGraphics, mouseX, mouseY, partialTick);
                
                // 渲染标签
                guiGraphics.drawString(GuiAdvanceArmyConfig.this.font, name, left + 5, top + 6, 0xFFFFFF);
            }
            
            @Override
            public List<? extends GuiEventListener> children() {
                return List.of(this.editBox);
            }
            
            @Override
            public List<? extends net.minecraft.client.gui.narration.NarratableEntry> narratables() {
                return List.of(this.editBox);
            }
        }
    }
}