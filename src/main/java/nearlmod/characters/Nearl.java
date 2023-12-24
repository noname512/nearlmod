package nearlmod.characters;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.ImageMaster;
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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import nearlmod.cards.*;
import nearlmod.cards.friendcards.*;
import nearlmod.orbs.AbstractFriend;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;
import nearlmod.relics.*;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlEnum;
import nearlmod.rooms.ArenaRoom;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;
import nearlmod.util.CostEnergyOrb;
import nearlmod.util.CostReserves;
import java.util.ArrayList;
import java.util.Collections;

public class Nearl extends CustomPlayer {

    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("nearlmod:NearlCharacter");
    public static final String NAME = characterStrings.NAMES[0];
    public static final String[] TEXT = characterStrings.TEXT;
    private static final Color NearlGold = CardHelper.getColor(255, 236, 194);
    public static final String SWORD = "images/char/atkidle.png";
    public static final Texture SWORDIMG = ImageMaster.loadImage(SWORD);
    public static final String SWORDDIE = "images/char/atkdie.png";
    public static final Texture SWORDDIEIMG = ImageMaster.loadImage(SWORDDIE);
    public static final String SHIELD = "images/char/defidle.png";
    public static final Texture SHIELDIMG = ImageMaster.loadImage(SHIELD);
    public static final String SHIELDDIE = "images/char/defdie.png";
    public static final Texture SHIELDDIEIMG = ImageMaster.loadImage(SHIELDDIE);
    public static final String SHOULDER = "images/char/shoulder.png";
    public static final String[] orbTextures = {
        "images/char/orb/layer1.png",
        "images/char/orb/layer2.png",
        "images/char/orb/layer3.png",
        "images/char/orb/layer4.png",
        "images/char/orb/layer5.png",
        "images/char/orb/layer1d.png",
        "images/char/orb/layer2d.png",
        "images/char/orb/layer3d.png",
        "images/char/orb/layer4d.png"
    };

    public static CardGroup friendCards;

    public Nearl(String name) {
        // 参数列表：角色名，角色类枚举，能量面板贴图路径列表，能量面板特效贴图路径，能量面板贴图旋转速度列表，能量面板，模型资源路径，动画资源路径
        super(name, NearlEnum.NEARL_CLASS, new CostEnergyOrb(orbTextures, "images/char/orb/vfx.png", null), null, null);
        
        dialogX = 50.0F * Settings.scale;
        dialogY = 170.0F * Settings.scale;

        // 参数列表：静态贴图路径，越肩视角2贴图路径，越肩视角贴图路径，失败时贴图路径，角色选择界面信息，碰撞箱XY宽高，初始能量数
        initializeClass(SHIELD, SHOULDER, SHOULDER, SHIELDDIE, getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(3));
        loadAnimation("images/char/char_148_nearl.atlas", "images/char/char_148_nearl.json", 1.5F);
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
        ArenaRoom.enterTimes = 0;
        initFriendCard();
    }

    @Override
    public void playDeathAnimation() {
        this.state.setAnimation(0, "Die", false);
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
        return TEXT[1];
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Nearl(name);
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new MajestyLight();
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
        ArrayList<String> ret = new ArrayList<>();
        ret.add(NearlStrike.ID);
        ret.add(NearlStrike.ID);
        ret.add(NearlStrike.ID);
        ret.add(NearlStrike.ID);
        ret.add(NearlDefend.ID);
        ret.add(NearlDefend.ID);
        ret.add(NearlDefend.ID);
        ret.add(NearlDefend.ID);
        ret.add(SwitchType.ID);
        ret.add(MajestyLight.ID);
        return ret;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(CureUp.ID);
        UnlockTracker.markRelicAsSeen(CureUp.ID);
        return ret;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, TEXT[0],
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
        CostReserves.resetReserves();
        LightPower.amountForBattle = 0;
    }

    @Override
    public void onVictory() {
        super.onVictory();
        BaseMod.MAX_HAND_SIZE = 10;
    }

    @Override
    public void onStanceChange(String id) {
        if (id.equals(AtkStance.STANCE_ID)) {
            loadAnimation("images/char/char_1014_nearl2/char_1014_nearl233.atlas", "images/char/char_1014_nearl2/char_1014_nearl233.json", 1.5F);
            this.stateData.setMix("Gain_Light", "Idle", 0.5F);
            this.img = SWORDIMG;
            this.corpseImg = SWORDDIEIMG;
        } else {
            loadAnimation("images/char/char_148_nearl/char_148_nearl.atlas", "images/char/char_148_nearl/char_148_nearl37.json", 1.5F);
            this.img = SHIELDIMG;
            this.corpseImg = SHIELDDIEIMG;
        }
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    public static void initFriendCard() {
        friendCards = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        friendCards.group.add(new AllySupport());
        friendCards.group.add(new ArtsShield());
        friendCards.group.add(new AutoProtect());
        friendCards.group.add(new BeepActivate());
        friendCards.group.add(new BlackFiendProtection());
        friendCards.group.add(new BombardmentStudies());
        friendCards.group.add(new ClosedHope());
        friendCards.group.add(new CraftsmanEcho());
        friendCards.group.add(new Creed());
        friendCards.group.add(new CreedField());
        friendCards.group.add(new DeterringRadiance());
        friendCards.group.add(new DivineAvatar());
        friendCards.group.add(new FeatherShineArrows());
        friendCards.group.add(new FlameHeart());
        friendCards.group.add(new FlameShadow());
        friendCards.group.add(new FlashFade());
        friendCards.group.add(new FocusedBombardment());
        friendCards.group.add(new GlimmeringTouch());
        friendCards.group.add(new JusticeDrive());
        friendCards.group.add(new LanceCharge());
        friendCards.group.add(new LSSwiftSword());
        friendCards.group.add(new MotivationalSkills());
        friendCards.group.add(new PinusSylvestris());
        friendCards.group.add(new Rebuke());
        friendCards.group.add(new Sanctuary());
        friendCards.group.add(new StabbingLance());
        friendCards.group.add(new SurgingBrilliance());
        friendCards.group.add(new VisionOfUnity());
        friendCards.group.add(new WhipSword());
        friendCards.group.add(new WhiteFiendProtection());
    }

    public static ArrayList<AbstractCard> getUnuniqueFriendCard(boolean isPinusSylvestris) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : friendCards.group)
            if (!c.hasTag(NearlTags.IS_UNIQUE_CARD) && (!isPinusSylvestris || c.hasTag(NearlTags.IS_KNIGHT_CARD)))
                list.add(c.makeCopy());
        return list;
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        if (!Settings.FAST_MODE) {
            if (!(c instanceof AbstractFriendCard) && c.type == AbstractCard.CardType.ATTACK) {
                if (this.stance.ID.equals(AtkStance.STANCE_ID)) {
                    if (c instanceof BraveTheDarkness) {
                        this.state.setAnimation(0, "BraveTheDarkness", false);
                    } else {
                        this.state.setAnimation(0, "Skill_1_Begin", false);
                        this.state.addAnimation(0, "Skill_1_Loop", false, 0.0F);
                        this.state.addAnimation(0, "Skill_1_End", false, 0.0F);
                    }
                } else {
                    this.state.setAnimation(0, "Attack", false);
                }
                this.state.addAnimation(0, "Idle", true, 0.0F);
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(1.0F));
        }
        super.useCard(c, monster, energyOnUse);
    }

    private int getFirstOrbIndex() {
        int index = -1;
        for (int i = 0; i < orbs.size(); i++)
            if (!(orbs.get(i) instanceof AbstractFriend)) {
                index = i;
                break;
            }
        return index;
    }

    private void removeOrb(int index) {
        for (int i = index + 1; i < orbs.size(); i++) {
            Collections.swap(orbs, i, i - 1);
        }
        orbs.set(orbs.size() - 1, new EmptyOrbSlot());
        for (int i = 0; i < orbs.size(); i++) {
            orbs.get(i).setSlot(i, maxOrbs);
        }
    }

    @Override
    public void evokeWithoutLosingOrb() {
        int index = getFirstOrbIndex();
        if (index == -1 || (orbs.get(index) instanceof EmptyOrbSlot)) return;
        orbs.get(index).onEvoke();
    }

    @Override
    public void evokeOrb() {
        int index = getFirstOrbIndex();
        if (index == -1 || (orbs.get(index) instanceof EmptyOrbSlot)) return;
        orbs.get(index).onEvoke();
        removeOrb(index);
    }

    @Override
    public void removeNextOrb() {
        int index = getFirstOrbIndex();
        if (index == -1 || (orbs.get(index) instanceof EmptyOrbSlot)) return;
        removeOrb(index);
    }

    public String removeLastFriend() {
        int index = -1;
        for (int i = orbs.size() - 1; i >= 0; i--)
            if (orbs.get(i) instanceof AbstractFriend) {
                index = i;
                break;
            }
        if (index == -1) return "";
        String ret = orbs.get(index).ID;
        for (int i = index + 1; i < orbs.size(); i++) {
            Collections.swap(orbs, i, i - 1);
        }
        orbs.remove(orbs.size() - 1);
        maxOrbs--;
        for (int i = 0; i < orbs.size(); i++) {
            orbs.get(i).setSlot(i, maxOrbs);
        }
        return ret;
    }

    @Override
    public boolean hasOrb() {
        return filledOrbCount() > 0;
    }

    @Override
    public int filledOrbCount() {
        int orbCount = 0;
        for (AbstractOrb orb : orbs)
            if (!(orb instanceof AbstractFriend) && !(orb instanceof EmptyOrbSlot))
                orbCount++;
        return orbCount;
    }

    public boolean hasFriend() {
        for (AbstractOrb orb : orbs)
            if (orb instanceof AbstractFriend)
                return true;
        return false;
    }

    public AbstractFriend lastFriend() {
        for (int i = orbs.size() - 1; i >= 0; i--)
            if (orbs.get(i) instanceof AbstractFriend)
                return (AbstractFriend)orbs.get(i);
        return null;
    }
}