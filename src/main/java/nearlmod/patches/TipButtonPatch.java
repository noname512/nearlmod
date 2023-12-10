package nearlmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import nearlmod.NLMOD;

import java.util.ArrayList;

public class TipButtonPatch {
    public static final Hitbox btnHitbox = new Hitbox(40.0F * Settings.scale * 0.991F, 40.0F * Settings.scale);
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:FriendTip");
    public static final String[] TEXT = uiStrings.TEXT;
    public static final ArrayList<PowerTip> tips = new ArrayList<>();

    @SpirePatch(clz = CharacterOption.class, method = "renderRelics")
    public static class RenderBtn {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance, SpriteBatch sb) {
            if (__instance.name.contains("临光") || __instance.name.toLowerCase().contains("nearl")) {
                TipButtonPatch.btnHitbox.move(190.9F * Settings.scale, Settings.HEIGHT / 2.0F - 190.9F * Settings.scale);
                TipButtonPatch.btnHitbox.render(sb);

                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.CHECKBOX, TipButtonPatch.btnHitbox.cX - 32.0F, TipButtonPatch.btnHitbox.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.991F, Settings.scale * 0.991F, 0.0F, 0, 0, 64, 64, false, false);
                if (NLMOD.friendTipMode) {
                    sb.draw(ImageMaster.TICK, TipButtonPatch.btnHitbox.cX - 32.0F, TipButtonPatch.btnHitbox.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.991F, Settings.scale * 0.991F, 0.0F, 0, 0, 64, 64, false, false);
                }
                FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, TipButtonPatch.TEXT[0], TipButtonPatch.btnHitbox.cX + 27.0F * Settings.scale, TipButtonPatch.btnHitbox.cY + 6.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);
            }
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "updateHitbox")
    public static class UpdateHitbox {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance) {
            if (__instance.name.contains("临光") || __instance.name.toLowerCase().contains("nearl")) {
                TipButtonPatch.btnHitbox.update();
                if (TipButtonPatch.btnHitbox.hovered) {
                    if (TipButtonPatch.tips.isEmpty()) {
                        TipButtonPatch.tips.add(new PowerTip(TipButtonPatch.TEXT[0], TipButtonPatch.TEXT[1]));
                    }
                    if (InputHelper.mX < 1400.0F * Settings.scale) {
                        TipHelper.queuePowerTips(InputHelper.mX + 60.0F * Settings.scale, InputHelper.mY - 50.0F * Settings.scale, TipButtonPatch.tips);
                    } else {
                        TipHelper.queuePowerTips(InputHelper.mX - 350.0F * Settings.scale, InputHelper.mY - 50.0F * Settings.scale, TipButtonPatch.tips);
                    }

                    if (InputHelper.justClickedLeft) {
                        CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
                        TipButtonPatch.btnHitbox.clickStarted = true;
                    }
                    if (TipButtonPatch.btnHitbox.clicked) {
                        NLMOD.friendTipMode = !NLMOD.friendTipMode;
                        TipButtonPatch.btnHitbox.clicked = false;
                    }
                }
            }
        }
    }
}
