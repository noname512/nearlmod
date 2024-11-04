package nearlmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import nearlmod.characters.Nearl;
import nearlmod.orbs.*;

import java.util.*;

public class CharacterSettingPatch {
    public static final float X_POS = 1400.0F * Settings.scale;
    public static final float Y_POS = 800.0F * Settings.scale;
    public static final Hitbox friendTipBtnHitbox = new Hitbox(40.0F * Settings.scale * 0.991F, 40.0F * Settings.scale);
    public static final Hitbox teamLeftHitbox = new Hitbox(50.0F * Settings.scale, 50.0F * Settings.scale);
    public static final Hitbox teamRightHitbox = new Hitbox(50.0F * Settings.scale, 50.0F * Settings.scale);
    public static final Hitbox teamTipHitbox = new Hitbox(120.0F * Settings.scale, 120.0F * Settings.scale);
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:CharacterSetting");
    public static final String[] TEXT = uiStrings.TEXT;
    public static final ArrayList<PowerTip> tips = new ArrayList<>();
    public static final int teamNum = 2;
    public static final Texture[] teams = new Texture[]{
            new Texture("resources/nearlmod/images/ui/teams/classic.png"),
            new Texture("resources/nearlmod/images/ui/teams/defender.png")
    };
    public static final List<Set<String>> friendsInTeams = new ArrayList<Set<String>>(){{
            add(new HashSet<>(Arrays.asList(Blemishine.ORB_ID, Whislash.ORB_ID, Viviana.ORB_ID, Shining.ORB_ID, Nightingale.ORB_ID)));
            add(new HashSet<>(Arrays.asList(Blemishine.ORB_ID, Aurora.ORB_ID)));
    }};
    public static int curTeam = 0;

    @SpirePatch(clz = CharacterOption.class, method = "renderRelics")
    public static class RenderBtn {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance, SpriteBatch sb) {
            if (__instance.c instanceof Nearl) {
                friendTipBtnHitbox.move(X_POS + 132.0F * Settings.scale, Y_POS - 50.0F * Settings.scale);
                friendTipBtnHitbox.render(sb);
                teamLeftHitbox.move(X_POS + 124.0F * Settings.scale, Y_POS + 24.0F * Settings.scale);
                teamLeftHitbox.render(sb);
                teamLeftHitbox.move(X_POS + 344.0F * Settings.scale, Y_POS + 24.0F * Settings.scale);
                teamLeftHitbox.render(sb);
                teamTipHitbox.move(X_POS + 234.0F * Settings.scale, Y_POS + 60.0F * Settings.scale);

                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.CHECKBOX, X_POS + 100.0F * Settings.scale, Y_POS - 82.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.991F, Settings.scale * 0.991F, 0.0F, 0, 0, 64, 64, false, false);
                if (NLMOD.friendTipMode) {
                    sb.draw(ImageMaster.TICK, X_POS + 100.0F * Settings.scale, Y_POS - 82.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.991F, Settings.scale * 0.991F, 0.0F, 0, 0, 64, 64, false, false);
                }
                FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, TEXT[0], X_POS, Y_POS - 44.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);

                FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, TEXT[2], X_POS, Y_POS + 6.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);
                sb.draw(ImageMaster.CF_LEFT_ARROW, X_POS + 100.0F * Settings.scale, Y_POS, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
                sb.draw(ImageMaster.CF_RIGHT_ARROW, X_POS + 320.0F * Settings.scale, Y_POS, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
                sb.draw(teams[curTeam], X_POS + 200.0F * Settings.scale, Y_POS, 60.0F, 60.0F, 120.0F, 120.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 120, 120, false, false);
            }
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "updateHitbox")
    public static class UpdateHitbox {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance) {
            if (__instance.c instanceof Nearl) {
                friendTipBtnHitbox.update();
                if (friendTipBtnHitbox.hovered) {
                    tips.clear();
                    tips.add(new PowerTip(TEXT[0], TEXT[1]));
                    TipHelper.queuePowerTips(InputHelper.mX - 350.0F * Settings.scale, InputHelper.mY - 50.0F * Settings.scale, tips);

                    if (InputHelper.justClickedLeft) {
                        CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
                        friendTipBtnHitbox.clickStarted = true;
                    }
                    if (friendTipBtnHitbox.clicked) {
                        NLMOD.friendTipMode = !NLMOD.friendTipMode;
                        friendTipBtnHitbox.clicked = false;
                    }
                }

                teamLeftHitbox.update();
                if (teamLeftHitbox.hovered) {
                    if (InputHelper.justClickedLeft) {
                        CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
                        teamLeftHitbox.clickStarted = true;
                    }
                    if (teamLeftHitbox.clicked) {
                        curTeam = (curTeam == 0? teamNum - 1 : curTeam - 1);
                        teamLeftHitbox.clicked = false;
                    }
                }

                teamRightHitbox.update();
                if (teamRightHitbox.hovered) {
                    if (InputHelper.justClickedLeft) {
                        CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
                        teamRightHitbox.clickStarted = true;
                    }
                    if (teamRightHitbox.clicked) {
                        curTeam = (curTeam == teamNum - 1? 0 : curTeam + 1);
                        teamRightHitbox.clicked = false;
                    }
                }

                teamTipHitbox.update();
                if (teamTipHitbox.hovered) {
                    tips.clear();
                    tips.add(new PowerTip(TEXT[curTeam * 2 + 3], TEXT[curTeam * 2 + 4]));
                    TipHelper.queuePowerTips(InputHelper.mX - 350.0F * Settings.scale, InputHelper.mY - 50.0F * Settings.scale, tips);
                }
            }
        }
    }
}
