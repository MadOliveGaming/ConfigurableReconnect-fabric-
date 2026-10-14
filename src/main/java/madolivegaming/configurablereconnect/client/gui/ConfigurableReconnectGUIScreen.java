package madolivegaming.configurablereconnect.client.gui;

import madolivegaming.configurablereconnect.network.ConfigurableReconnectModVariables;
import madolivegaming.configurablereconnect.network.ReconnectDelayHelper;
import madolivegaming.configurablereconnect.network.ReconnectDelayConfig;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import madolivegaming.configurablereconnect.world.inventory.ConfigurableReconnectGUIMenu;
import madolivegaming.configurablereconnect.init.ConfigurableReconnectModScreens;

public class ConfigurableReconnectGUIScreen extends AbstractContainerScreen<ConfigurableReconnectGUIMenu> implements ConfigurableReconnectModScreens.FabricScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ConfigurableReconnectModScreens.ExtendedSlider reconnect_delay_slider;
	private Checkbox rejoin_voicechat_checkbox;
	private static final Identifier BACKGROUND = Identifier.parse("configurable_reconnect:textures/screens/configurable_reconnect_gui.png");

	public ConfigurableReconnectGUIScreen(
        ConfigurableReconnectGUIMenu container,
        Inventory inventory,
        Component text) {
    	super(container, inventory, text, 208, 100);
    	this.world = container.world;
    	this.x = container.x;
    	this.y = container.y;
    	this.z = container.z;
    	this.entity = container.entity;

    	// This screen has no inventory label to display.
    	this.inventoryLabelY = 1000;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		if (elementType == 2 && elementState instanceof Number n) {
			if (name.equals("reconnect_delay_slider"))
				reconnect_delay_slider.setValue(n.doubleValue());
		}
		menuStateUpdateActive = false;
	}

	@Override
	public boolean isPauseScreen() {
		return true;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = event.key();
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	public void init() {
		super.init();

		// Reconnect delay slider widget
		reconnect_delay_slider = new ConfigurableReconnectModScreens.ExtendedSlider(
				this.leftPos + 18, this.topPos + 68, 170, 20,
				Component.translatable("gui.configurable_reconnect.configurable_reconnect_gui.reconnect_delay_slider_prefix"),
				Component.translatable("gui.configurable_reconnect.configurable_reconnect_gui.reconnect_delay_slider_suffix"),
				1,
				60,
				ReconnectDelayHelper.get(),
				1,
				0,
				true
		) {
			@Override
			protected void applyValue() {
				ReconnectDelayHelper.set(this.getValue());
    			ReconnectDelayConfig.save();
			}
		};
		this.addRenderableWidget(reconnect_delay_slider);

		// Rejoin svc checkbox widget
		rejoin_voicechat_checkbox = Checkbox.builder(
        	Component.literal("Rejoin voice chat"),
        	this.font
    	)
    	.pos(this.leftPos + 18, this.topPos + 38)
    	.selected(ConfigurableReconnectModVariables.rejoin_svc)
    	.onValueChange((checkbox, selected) -> {
        	ConfigurableReconnectModVariables.rejoin_svc = selected;
        	ReconnectDelayConfig.save();
    	})
    	.build();

		this.addRenderableWidget(rejoin_voicechat_checkbox);
	}
}