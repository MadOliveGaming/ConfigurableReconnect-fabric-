package madolivegaming.configurablereconnect.client.gui;

import madolivegaming.configurablereconnect.network.ConfigurableReconnectModVariables;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
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
	private static final Identifier BACKGROUND = Identifier.parse("configurable_reconnect:textures/screens/configurable_reconnect_gui.png");

	public ConfigurableReconnectGUIScreen(ConfigurableReconnectGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 208, 100);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
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
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.translatable("gui.configurable_reconnect.configurable_reconnect_gui.label_configurable_reconnect"), 20, 15, -12829636, false);
		guiGraphics.text(this.font, Component.translatable("gui.configurable_reconnect.configurable_reconnect_gui.label_delay"), 18, 54, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		reconnect_delay_slider = new ConfigurableReconnectModScreens.ExtendedSlider(this.leftPos + 18, this.topPos + 68, 170, 20, Component.translatable("gui.configurable_reconnect.configurable_reconnect_gui.reconnect_delay_slider_prefix"),
				Component.translatable("gui.configurable_reconnect.configurable_reconnect_gui.reconnect_delay_slider_suffix"),
				1,
				60,
				ConfigurableReconnectModVariables.MapVariables.get(null).reconnect_delay,
				1,
				0,
				true
				) {
			@Override
			protected void applyValue() {
    			ConfigurableReconnectModVariables.MapVariables.get(null).reconnect_delay = this.getValue();
			}
		};
		this.addRenderableWidget(reconnect_delay_slider);
	}
}