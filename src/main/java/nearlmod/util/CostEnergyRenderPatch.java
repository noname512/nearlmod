package nearlmod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import nearlmod.characters.Nearl;

public class CostEnergyRenderPatch {
    @SpirePatch(clz = EnergyPanel.class, method = "render")
    public static class ShowTextForReserves {
        public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
            int found = CostReserves.reserveCount();
            if (AbstractDungeon.player instanceof Nearl) {
                String toShow;
                if (found > 0)
                    toShow = String.valueOf(found);
                else
                    toShow = "0";
                AbstractDungeon.player.getEnergyNumFont().getData().setScale(1.0F);
                FontHelper.renderFontCentered(sb, AbstractDungeon.player.getEnergyNumFont(), toShow, __instance.current_x + CostEnergyOrb.X_OFFSET, __instance.current_y + CostEnergyOrb.Y_OFFSET, Color.WHITE);
            }
        }
    }
}
