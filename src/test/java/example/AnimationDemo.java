package example;

import github.rainbowmori.rainbowapi.object.ui.GuiInventoryHolder;
import github.rainbowmori.rainbowapi.object.ui.animate.Animation;
import github.rainbowmori.rainbowapi.object.ui.animate.AnimationRunner;
import github.rainbowmori.rainbowapi.object.ui.animate.Frame;
import github.rainbowmori.rainbowapi.object.ui.animate.Schedule;
import github.rainbowmori.rainbowapi.object.ui.util.IntGenerator;
import github.rainbowmori.rainbowapi.object.ui.util.Option;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class AnimationDemo extends GuiInventoryHolder<ExamplePlugin> {

    private static final int SIZE = 54;
    private static final ItemStack PUFFERFISH = new ItemStack(Material.PUFFERFISH);

    private final AnimationRunner<ItemStack> animation;

    public AnimationDemo(ExamplePlugin plugin) {
        super(plugin, SIZE, "Animation Demo");
        Animation animation = Animation.infinite(new CustomFrame(0), CustomFrame::next);
        this.animation = new AnimationRunner<>(plugin, animation, getInventory()::setItem);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        animation.play(Schedule.now().append(Schedule.fixedRate(5L)));
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        animation.stop();
    }


    private static final class CustomFrame extends Frame<Boolean, ItemStack> {

        private final int active;

        private CustomFrame(int active) {
            super(index -> index == active,
                    b -> Option.some(b ? PUFFERFISH : null),
                    IntGenerator.of(normalize(active - 1), active)
            );

            this.active = active;
        }

        public CustomFrame next() {
            return new CustomFrame((active + 1) % SIZE);
        }

        private static int normalize(int index) {
            if (index < 0) index += SIZE;
            else if (index >= SIZE) index -= SIZE;
            return index;
        }
    }

}
