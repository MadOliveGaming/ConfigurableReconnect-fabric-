package madolivegaming.configurablereconnect.world.inventory;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.Container;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import madolivegaming.configurablereconnect.procedures.SetRecconectDelayOnGUIClosesProcedure;
import madolivegaming.configurablereconnect.procedures.CorrectTextWhenGuiOpenedProcedure;
import madolivegaming.configurablereconnect.init.ConfigurableReconnectModMenus;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class ConfigurableReconnectGUIMenu extends AbstractContainerMenu implements ConfigurableReconnectModMenus.MenuAccessor {
	public final Map<String, Object> menuState = new HashMap<>() {
		@Override
		public Object put(String key, Object value) {
			if (!this.containsKey(key) && this.size() >= 3)
				return null;
			return super.put(key, value);
		}
	};
	public final Level world;
	public final Player entity;
	public int x, y, z;
	private ContainerLevelAccess access = ContainerLevelAccess.NULL;
	private final Container inventory;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;
	private Supplier<Boolean> boundItemMatcher = null;
	private Entity boundEntity = null;
	private BlockEntity boundBlockEntity = null;
	private ItemStack boundItem = null;

	public ConfigurableReconnectGUIMenu(int id, Inventory inv) {
		this(id, inv, new SimpleContainer(0));
		this.x = (int) inv.player.getX();
		this.y = (int) inv.player.getY();
		this.z = (int) inv.player.getZ();
		access = ContainerLevelAccess.create(inv.player.level(), new BlockPos(x, y, z));
	}

	public ConfigurableReconnectGUIMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		this(id, inv, resolveBlockContainer(inv, extraData), extraData);
	}

	private static Container resolveBlockContainer(Inventory inv, FriendlyByteBuf extraData) {
		// The MenuType factory uses this constructor on both sides. Keep a temporary
		// mirror on the client (slot sync populates it), but on the dedicated/integrated
		// server bind procedure-opened GUIs to the real block inventory at the supplied
		// position. This makes "Open GUI at x/y/z" persist items just like a GUI
		// opened directly by its bound block entity.
		if (extraData != null && !inv.player.level().isClientSide()) {
			extraData.markReaderIndex();
			try {
				BlockPos pos = extraData.readBlockPos();
				// Item/entity-bound internal opens append marker data after BlockPos. Only
				// treat a buffer containing exactly the position as a block-bound open.
				if (extraData.readableBytes() == 0) {
					BlockEntity blockEntity = inv.player.level().getBlockEntity(pos);
					if (blockEntity instanceof Container blockContainer && blockContainer.getContainerSize() >= 0) {
						return blockContainer;
					}
				}
			} catch (IndexOutOfBoundsException ignored) {
				// Malformed/short extra data: fall back to a temporary GUI inventory.
			} finally {
				extraData.resetReaderIndex();
			}
		}
		return new SimpleContainer(0);
	}

	public ConfigurableReconnectGUIMenu(int id, Inventory inv, Container container, FriendlyByteBuf extraData) {
		this(id, inv, container);
		BlockPos pos = null;
		if (extraData != null) {
			pos = extraData.readBlockPos();
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			access = ContainerLevelAccess.create(world, pos);
		}
		// The menu coordinates are populated from extraData above. Run the GUI-open
		// procedure only now, rather than from the shared constructor where x/y/z
		// are still their default 0 values. Also only execute it on the server menu:
		// running it on the client as well makes effects such as sounds happen twice.
		if (this.entity instanceof ServerPlayer) {
			CorrectTextWhenGuiOpenedProcedure.execute(world, entity);
		}
	}

	public ConfigurableReconnectGUIMenu(int id, Inventory inv, Container container) {
		super(ConfigurableReconnectModMenus.CONFIGURABLE_RECONNECT_GUI, id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.inventory = container;
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.bound) {
			if (this.boundItemMatcher != null)
				return this.boundItemMatcher.get();
			else if (this.boundBlockEntity != null)
				return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
			else if (this.boundEntity != null)
				return this.boundEntity.isAlive();
		}
		return this.inventory.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		// The menu is removed on both logical sides; only fire the close event
		// from the server-side menu to avoid duplicate procedure execution.
		if (playerIn instanceof ServerPlayer) {
			SetRecconectDelayOnGUIClosesProcedure.execute(entity);
		}
	}

	@Override
	public Map<Integer, Slot> getSlots() {
		return Collections.unmodifiableMap(customSlots);
	}

	@Override
	public Map<String, Object> getMenuState() {
		return menuState;
	}

	public static void screenInit() {
	}
}