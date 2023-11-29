package nearlmod;

import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import nearlmod.cards.special.Beginning;
import nearlmod.cards.special.BlemishinesFaintLight;
import nearlmod.events.LaughAllYouWantEvent;
import nearlmod.monsters.*;
import nearlmod.patches.NearlEnum;
import nearlmod.potions.*;
import nearlmod.relics.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import nearlmod.cards.*;
import nearlmod.cards.friendcards.*;
import nearlmod.characters.Nearl;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static nearlmod.patches.AbstractCardEnum.FRIEND_BLUE;
import static nearlmod.patches.AbstractCardEnum.NEARL_GOLD;

@SpireInitializer
public class NLMOD implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber, OnStartBattleSubscriber, OnPlayerLoseBlockSubscriber, RelicGetSubscriber {

    public static final Color NearlGold = CardHelper.getColor(255, 236, 194);
    public static final Color FriendBlue = CardHelper.getColor(202, 210, 255);
    private static final String attackCard = "images/512/bg_attack_nearl.png";
    private static final String skillCard = "images/512/bg_skill_nearl.png";
    private static final String powerCard = "images/512/bg_power_nearl.png";
    private static final String attackFriendCard = "images/512/bg_attack_friend.png";
    private static final String skillFriendCard = "images/512/bg_skill_friend.png";
    private static final String powerFriendCard = "images/512/bg_power_friend.png";
    private static final String energyOrb = "images/512/card_mystic_orb.png";
    private static final String attackCardPortrait = "images/1024/bg_attack_nearl.png";
    private static final String skillCardPortrait = "images/1024/bg_skill_nearl.png";
    private static final String powerCardPortrait = "images/1024/bg_power_nearl.png";
    private static final String attackFriendCardPortrait = "images/1024/bg_attack_friend.png";
    private static final String skillFriendCardPortrait = "images/1024/bg_skill_friend.png";
    private static final String powerFriendCardPortrait = "images/1024/bg_power_friend.png";
    private static final String energyOrbPortrait = "images/1024/card_mystic_orb.png";
    private static final String charButton = "images/charSelect/button.png";
    private static final String charPortrait = "images/charSelect/portrait.png";
    private static final String miniManaSymbol = "images/manaSymbol.png";
    private static final Logger logger = LogManager.getLogger(NLMOD.class.getName());

    public NLMOD() {
        BaseMod.subscribe(this);

        logger.info("addColor NEARL_GOLD");
        BaseMod.addColor(NEARL_GOLD,
                NearlGold, NearlGold, NearlGold, NearlGold, NearlGold, NearlGold, NearlGold,   //Background color, back color, frame color, frame outline color, description box color, glow color
                attackCard, skillCard, powerCard, energyOrb,                                   //attack background image, skill background image, power background image, energy orb image
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait,   //as above, but for card inspect view
                miniManaSymbol);

        logger.info("addColor FRIEND_BLUE");
        BaseMod.addColor(FRIEND_BLUE,
                FriendBlue, FriendBlue, FriendBlue, FriendBlue, FriendBlue, FriendBlue, FriendBlue,
                attackFriendCard, skillFriendCard, powerFriendCard, energyOrb,
                attackFriendCardPortrait, skillFriendCardPortrait, powerFriendCardPortrait, energyOrbPortrait,
                miniManaSymbol);
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        new NLMOD();
    }

    @Override
    public void receivePostInitialize() {
        initializeEvents();
        initializeMonsters();
        initializePotions();
    }

    private void initializeEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(LaughAllYouWantEvent.ID, LaughAllYouWantEvent.class).
                eventType(EventUtils.EventType.ONE_TIME).
                playerClass(NearlEnum.NEARL_CLASS).
                dungeonID("TheCity").
                //bonusCondition(() -> (AbstractDungeon.floorNum > 6)).
                create());
    }

    private void initializeMonsters() {
        BaseMod.addMonster(CandleKnight.ID, CandleKnight.NAME, () -> new MonsterGroup(new AbstractMonster[] {new CandleKnight()}));
        BaseMod.addMonster(Platinum.ID, Platinum.NAME, () -> new MonsterGroup(new AbstractMonster[] {new Platinum(0.0F, 0.0F)}));
        BaseMod.addMonster(Roy.ID, Roy.NAME, () -> new MonsterGroup(new AbstractMonster[] {new Roy(0.0F, 0.0F)}));
        BaseMod.addMonster(Monique.ID, Monique.NAME, () -> new MonsterGroup(new AbstractMonster[] {new Monique(0.0F, 0.0F)}));
        BaseMod.addMonster(CorruptKnight.ID, CorruptKnight.NAME, () -> new MonsterGroup(new AbstractMonster [] {new CorruptKnight(0.0F, 0.0F)}));
        BaseMod.addMonster(WitheredKnight.ID, WitheredKnight.NAME, () -> new MonsterGroup(new AbstractMonster [] {new WitheredKnight(0.0F, 0.0F)}));
        BaseMod.addMonster(LeftHandTytusTopola.ID, LeftHandTytusTopola.NAME, () -> new MonsterGroup(new AbstractMonster [] {new LeftHandTytusTopola(0.0F, 0.0F)}));
    }

    private void initializePotions() {
        BaseMod.addPotion(ChangeStancePotion.class, Color.GOLD, Color.CYAN, null, ChangeStancePotion.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(FriendPotion.class, Color.GOLD, Color.CYAN, null, FriendPotion.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(EssenceOfLight.class, Color.GOLD, null, null, EssenceOfLight.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(FriendshipDrink.class, Color.NAVY, Color.SKY, null, FriendshipDrink.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(BrilliantLights.class, Color.GOLD, null, null, BrilliantLights.ID, NearlEnum.NEARL_CLASS);
    }

    @Override
    public void receivePostDungeonInitialize() {}

    @Override
    public void receiveCustomModeMods(List<CustomMod> modList) {
//        modList.add(new CustomMod())
    }

    @Override
    public int receiveOnPlayerLoseBlock(int cnt) {
        return cnt;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {}

    @Override
    public void receivePostBattle(final AbstractRoom p0) {}

    @Override
    public void receiveRelicGet(AbstractRelic r) {}

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AbstractNearlCard.SecondMagicNumber());

        // Basic.
        BaseMod.addCard(new NearlStrike()); // 打击
        BaseMod.addCard(new NearlDefend()); // 防御
        BaseMod.addCard(new SwitchType()); // 形态切换
        BaseMod.addCard(new MajestyLight()); // 手执威光

        // Common.
        BaseMod.addCard(new GumboBreadBowl()); // 浓汤面包碗
        BaseMod.addCard(new KnightCrest()); // 骑士之辉
        BaseMod.addCard(new AllinOne()); // 攻防一体
        BaseMod.addCard(new SwordShield()); // 剑盾骑士
        BaseMod.addCard(new NobleLight()); // 崇高者
        BaseMod.addCard(new Guardian()); // 守护者
        BaseMod.addCard(new WornOut()); // 大道磨灭
        BaseMod.addCard(new LightsOut()); // 熄灯
        BaseMod.addCard(new GlowingBlueBird()); // 发光的蓝色羽兽
        BaseMod.addCard(new Facing()); // 直面
        BaseMod.addCard(new ChargingWithShield()); // 持盾冲锋
        BaseMod.addCard(new Unsheathed()); // 出鞘
        BaseMod.addCard(new Dreadnought()); // 无畏者
        BaseMod.addCard(new DayLike()); // 如昼
        BaseMod.addCard(new OverflowingLight()); // 溢光

        // Uncommon.
        BaseMod.addCard(new SecondSun()); // 第二轮太阳
        BaseMod.addCard(new LightSpearStrike()); // 光枪打击
        BaseMod.addCard(new SwallowLight()); // 侵吞光芒
        BaseMod.addCard(new AllOutEffort()); // 全力以赴
        BaseMod.addCard(new DanceTogether()); // 共舞一曲
        BaseMod.addCard(new DawnDuskSaber()); // 斩破晨昏
        BaseMod.addCard(new SweepWrong()); // 扫尽恶行
        BaseMod.addCard(new ReliableFriend()); // 信赖伙伴
        BaseMod.addCard(new SightImpairingShowdown()); // 有损视力的对决
        BaseMod.addCard(new GloriousKazimierz()); // 光耀卡西米尔
        BaseMod.addCard(new ScarlyMartin()); // “恐怖马丁”
        BaseMod.addCard(new Cooperate()); // 协同战斗
        BaseMod.addCard(new DefensiveFormation()); // 防御阵型
        BaseMod.addCard(new Oaths()); // 誓言
        BaseMod.addCard(new DefendHonor()); // 捍卫荣耀
        BaseMod.addCard(new FlutteringWings()); // 振翅欲飞
        BaseMod.addCard(new ShieldAndShelter()); // 盾与庇护所
        BaseMod.addCard(new SalvationForSuffers()); // 苦难者的救星
        BaseMod.addCard(new AweInspiringGlow()); // 凛然辉光
        BaseMod.addCard(new BreakThroughFetters()); // 冲破桎梏
        BaseMod.addCard(new SilverlancePegasus()); // 银枪天马
        BaseMod.addCard(new SurgeBack()); // 回涌
        BaseMod.addCard(new Duel()); // 对决
        BaseMod.addCard(new KnightCompetition()); // 骑士竞技
        BaseMod.addCard(new RadianceConverging()); // 光耀积聚
        BaseMod.addCard(new Sacrifice()); // 牺牲
        BaseMod.addCard(new LightCascade()); // 光瀑

        // Rare.
        BaseMod.addCard(new BraveTheDarkness()); // 不畏苦暗
        BaseMod.addCard(new FirstAid()); // 急救
        BaseMod.addCard(new FlamingEdge()); // 灿焰长刃
        BaseMod.addCard(new NightScouringGleam()); // 逐夜烁光
        BaseMod.addCard(new WayToChampion()); // 冠军之路
        BaseMod.addCard(new Dawn()); // 曙光
        BaseMod.addCard(new TheReturn()); // 归来
        BaseMod.addCard(new FullSpeedAhead()); // 全力冲锋
        BaseMod.addCard(new PersonalCharm()); // 个人魅力
        BaseMod.addCard(new BlazingSunsObeisance()); // 耀阳颔首
        BaseMod.addCard(new ShadowOut()); // 影灭
        BaseMod.addCard(new BladeOfBlazingSun()); // 耀阳锋刃
        BaseMod.addCard(new PathOfRadiant()); // 循光道途

        // Friend.
        BaseMod.addCard(new LSSwiftSword()); // 光影迅捷剑
        BaseMod.addCard(new FlashFade()); // “明灭”
        BaseMod.addCard(new FlameShadow()); // 烛燃影息
        BaseMod.addCard(new GlimmeringTouch()); // 微光之触

        BaseMod.addCard(new SurgingBrilliance()); // 光芒涌动
        BaseMod.addCard(new DeterringRadiance()); // 慑敌辉光
        BaseMod.addCard(new DivineAvatar()); // 先贤化身
        BaseMod.addCard(new CraftsmanEcho()); // 工匠团的回响

        BaseMod.addCard(new ArtsShield()); // 法术护盾
        BaseMod.addCard(new Sanctuary()); // 圣域
        BaseMod.addCard(new WhiteFiendProtection()); // 白恶魔的庇护
        BaseMod.addCard(new ClosedHope()); // 封闭的希望

        BaseMod.addCard(new Creed()); // 信条
        BaseMod.addCard(new AutoProtect()); // 自动掩护
        BaseMod.addCard(new CreedField()); // 教条立场
        BaseMod.addCard(new BlackFiendProtection()); // 黑恶魔的庇护

        BaseMod.addCard(new WayToHome()); // 归乡之路
        BaseMod.addCard(new VisionOfUnity()); // 比肩的愿景
        BaseMod.addCard(new Rebuke()); // 责骂
        BaseMod.addCard(new MotivationalSkills()); // 激励艺术
        BaseMod.addCard(new WhipSword()); // 鞭刃

        BaseMod.addCard(new PinusSylvestris()); // “红松林”
        BaseMod.addCard(new FlameHeart()); // 焰心
        BaseMod.addCard(new FocusedBombardment()); // 专注轰击
        BaseMod.addCard(new BombardmentStudies()); // 炮术研习
        BaseMod.addCard(new AllySupport()); // 同盟支援
        BaseMod.addCard(new FeatherShineArrows()); // 光羽箭
        BaseMod.addCard(new StabbingLance()); // 骑枪刺击
        BaseMod.addCard(new LanceCharge()); // 夹枪冲锋
        BaseMod.addCard(new JusticeDrive()); // 正义助威
        BaseMod.addCard(new BeepActivate()); // “滴滴，启动！”

        // Special
        BaseMod.addCard(new Beginning()); // 起点
        BaseMod.addCard(new BlemishinesFaintLight()); // 瑕光微明
        BaseMod.addCard(new PersonalCharmSp()); // 个人魅力SP
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new Nearl(CardCrawlGame.playerName), charButton, charPortrait, NearlEnum.NEARL_CLASS);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal("strings/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            logger.info("Adding Keyword - " + v.NAMES[0]);
            BaseMod.addKeyword("nearlmod:", v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditRelics() {
        // starter.
        BaseMod.addRelicToCustomPool(new CureUp(), NEARL_GOLD);

        // uncommon.
        BaseMod.addRelicToCustomPool(new FirstAidMode(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new UpgradedCoreCaster(), NEARL_GOLD);

        // rare.
        BaseMod.addRelicToCustomPool(new EmergencyCallBook(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new KnightFamily(), NEARL_GOLD);

        // boss.
        BaseMod.addRelicToCustomPool(new PegasusHalo(), NEARL_GOLD);

        // event.
        BaseMod.addRelicToCustomPool(new Marigold(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new LateLight(), AbstractCard.CardColor.CURSE);
        BaseMod.addRelicToCustomPool(new Revenge(), AbstractCard.CardColor.CURSE);
        BaseMod.addRelicToCustomPool(new NormalPerson(), AbstractCard.CardColor.CURSE);
    }

    @Override
    public void receiveEditStrings() {
        String cardStrings = Gdx.files.internal("strings/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String powerStrings = Gdx.files.internal("strings/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String stanceStrings = Gdx.files.internal("strings/stances.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(StanceStrings.class, stanceStrings);
        String orbStrings = Gdx.files.internal("strings/orbs.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(OrbStrings.class, orbStrings);
        String relicStrings = Gdx.files.internal("strings/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String eventStrings = Gdx.files.internal("strings/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String monsterStrings = Gdx.files.internal("strings/monsters.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String uiStrings = Gdx.files.internal("strings/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String potionStrings = Gdx.files.internal("strings/potions.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
    }

    public static boolean checkOrb(String orbId) {
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb.ID.equals(orbId))
                return true;
        return false;
    }
}