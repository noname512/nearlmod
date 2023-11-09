package nearlmod.characters;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import nearlmod.NLMOD;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import nearlmod.cards.*;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlEnum;
import nearlmod.powers.DefMode;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

import java.util.ArrayList;

public class Nearl extends CustomPlayer {
    private static final Color NearlGold = CardHelper.getColor(255, 236, 194);
    public static final String SWORD = "images/char/sword.png";
    public static final Texture SWORDIMG = ImageMaster.loadImage(SWORD);
    public static final String SWORDDIE = "images/char/sworddie.png";
    public static final Texture SWORDDIEIMG = ImageMaster.loadImage(SWORDDIE);
    public static final String SHIELD = "images/char/shield.png";
    public static final Texture SHIELDIMG = ImageMaster.loadImage(SHIELD);
    public static final String SHIELDDIE = "images/char/shielddie.png";
    public static final Texture SHIELDDIEIMG = ImageMaster.loadImage(SHIELDDIE);
    public static final String SHOULDER = "images/char/shoulder.png";
    public static final String[] orbTextures = {
        "images/char/orb/layer1.png",
        "images/char/orb/layer2.png",
        "images/char/orb/layer3.png",
        "images/char/orb/layer4.png",
        "images/char/orb/layer5.png",
        "images/char/orb/layer6.png",
        "images/char/orb/layer1d.png",
        "images/char/orb/layer2d.png",
        "images/char/orb/layer3d.png",
        "images/char/orb/layer4d.png",
        "images/char/orb/layer5d.png"
    };

    public enum MODE {
        ATK_MODE, DEF_MODE
    }

    public static MODE mode;

    public Nearl(String name) {
        // 参数列表：角色名，角色类枚举，能量面板贴图路径列表，能量面板特效贴图路径，能量面板贴图旋转速度列表，能量面板，模型资源路径，动画资源路径
        super(name, NearlEnum.NEARL_CLASS, orbTextures, "images/char/orb/vfx.png", null, null, null);
        
        // 对话框位置，默认就好
        dialogX = drawX + 0.0F * Settings.scale;
        dialogY = drawY + 220.0F * Settings.scale;

        // 参数列表：静态贴图路径，越肩视角2贴图路径，越肩视角贴图路径，失败时贴图路径，角色选择界面信息，碰撞箱XY宽高，初始能量数
        initializeClass(SHIELD, SHOULDER, SHOULDER, SHIELDDIE, getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(3));
    }

    @Override
    public Color getSlashAttackColor() {
        return NearlGold;
    }

    @Override
    public Color getCardRenderColor() {
        return NearlGold;
    }

    @Override
    public Color getCardTrailColor() {
        return NearlGold;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.NEARL_GOLD;
    }

    @Override
    public String getSpireHeartText() {
        return "你握紧了手中的剑枪和骑士盾……";
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return "玛嘉烈·临光";
    }

    @Override
    public String getLocalizedCharacterName() {
        return "玛嘉烈·临光";
    }

    @Override
    public String getVampireText() {
        return "";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Nearl(name);
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new NearlStrike();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.sound.playA("ATTACK_FAST", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> ret = new ArrayList<String>();
        ret.add(NearlStrike.ID);
        ret.add(NearlStrike.ID);
        /*
        ret.add(NearlStrike.ID);
        ret.add(NearlStrike.ID);
        ret.add(NearlDefend.ID);
        ret.add(NearlDefend.ID);
        */
        ret.add(NearlDefend.ID);
        ret.add(NearlDefend.ID);
        ret.add(SwitchType.ID);
        ret.add(MajestyLight.ID);
        ret.add(FlamingEdge.ID);
        ret.add(AllinOne.ID);
        return ret;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> ret = new ArrayList<String>();
        return ret;
    }
    
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo("玛嘉烈·临光", "骑士临光，是防御与治疗并重的战场核心， NL 也是身披炙阳、踏碎迷障的天马。",
                77, 77, 0, 99, 5, //starting hp, max hp, max orbs, starting gold, starting hand size
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public void applyStartOfCombatLogic() {
        super.applyStartOfCombatLogic();
        AtkStance.atkInc = 0;
        DefStance.defInc = -1;
        AtkStance.incNum = 1;
        DefStance.incNum = 1;
        AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new DefStance()));
    }

    @Override
    public void onStanceChange(String id) {
        if (id.equals(AtkStance.STANCE_ID)) {
            this.img = SWORDIMG;
            this.corpseImg = SWORDDIEIMG;
        } else {
            this.img = SHIELDIMG;
            this.corpseImg = SHIELDDIEIMG;
        }
    }
}