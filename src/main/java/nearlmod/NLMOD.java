package nearlmod;

import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import nearlmod.arenaevents.*;
import nearlmod.events.*;
import nearlmod.orbs.*;
import nearlmod.patches.NearlEnum;
import nearlmod.potions.*;
import nearlmod.relics.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import nearlmod.cards.*;
import nearlmod.cards.friendcards.*;
import nearlmod.cards.special.*;
import nearlmod.characters.Nearl;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static nearlmod.patches.AbstractCardEnum.*;

@SpireInitializer
public class NLMOD implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber, OnStartBattleSubscriber, OnPlayerLoseBlockSubscriber, RelicGetSubscriber {

    public static final Color NearlGold = CardHelper.getColor(255, 236, 194);
    public static final Color FriendBlue = CardHelper.getColor(202, 210, 255);
    private static final String attackCard = "resources/nearlmod/images/512/bg_attack_nearl.png";
    private static final String skillCard = "resources/nearlmod/images/512/bg_skill_nearl.png";
    private static final String powerCard = "resources/nearlmod/images/512/bg_power_nearl.png";
    private static final String attackFriendCard = "resources/nearlmod/images/512/bg_attack_friend.png";
    private static final String skillFriendCard = "resources/nearlmod/images/512/bg_skill_friend.png";
    private static final String powerFriendCard = "resources/nearlmod/images/512/bg_power_friend.png";
    private static final String energyOrb = "resources/nearlmod/images/512/card_nearl_orb.png";
    private static final String attackCardPortrait = "resources/nearlmod/images/1024/bg_attack_nearl.png";
    private static final String skillCardPortrait = "resources/nearlmod/images/1024/bg_skill_nearl.png";
    private static final String powerCardPortrait = "resources/nearlmod/images/1024/bg_power_nearl.png";
    private static final String attackFriendCardPortrait = "resources/nearlmod/images/1024/bg_attack_friend.png";
    private static final String skillFriendCardPortrait = "resources/nearlmod/images/1024/bg_skill_friend.png";
    private static final String powerFriendCardPortrait = "resources/nearlmod/images/1024/bg_power_friend.png";
    private static final String energyOrbPortrait = "resources/nearlmod/images/1024/card_nearl_orb.png";
    private static final String charButton = "resources/nearlmod/images/charSelect/button.png";
    private static final String charPortrait = "resources/nearlmod/images/charSelect/portrait.png";
    private static final String miniManaSymbol = "resources/nearlmod/images/manaSymbol.png";
    private static final Logger logger = LogManager.getLogger(NLMOD.class.getName());

    public static final HashMap<String, TextureAtlas.AtlasRegion> specialImg = new HashMap<>();
    public static boolean friendTipMode = false;

    public static CardGroup friendCards;

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
        initializePotions();
        initializeSpecialImg();
    }

    private void initializeEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(LaughAllYouWantEvent.ID, LaughAllYouWantEvent.class).
                eventType(EventUtils.EventType.NORMAL).
                playerClass(NearlEnum.NEARL_CLASS).
                dungeonID("TheCity").
                endsWithRewardsUI(true).
                create());
        BaseMod.addEvent(new AddEventParams.Builder(TheSurroundedEvent.ID, TheSurroundedEvent.class).
                eventType(EventUtils.EventType.NORMAL).
                playerClass(NearlEnum.NEARL_CLASS).
                dungeonID("TheCity").
                dungeonID("TheBeyond").
                endsWithRewardsUI(true).
                bonusCondition(() -> (AbstractDungeon.floorNum <= 40)).
                create());

        // For loadout Mod to test
        BaseMod.addEvent(CorruptedWitheredBattle.ID, CorruptedWitheredBattle.class, "Special");
        BaseMod.addEvent(LeftHandBattle.ID, LeftHandBattle.class, "Special");
        BaseMod.addEvent(CandleKnightBattle.ID, CandleKnightBattle.class, "Special");
        BaseMod.addEvent(LastKheshigBattle.ID, LastKheshigBattle.class, "Special");
        BaseMod.addEvent(BloodKnightBattle.ID, BloodKnightBattle.class, "Special");
        BaseMod.addEvent(ArmorlessSquadBattle.ID, ArmorlessSquadBattle.class, "Special");
        BaseMod.addEvent(LazuriteSquadBattle.ID, LazuriteSquadBattle.class, "Special");
        BaseMod.addEvent(WanderingKnightBattle.ID, WanderingKnightBattle.class, "Special");
    }

    private void initializePotions() {
        BaseMod.addPotion(ChangeStancePotion.class, Color.GOLD, Color.CYAN, null, ChangeStancePotion.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(FriendPotion.class, Color.GOLD, Color.CYAN, null, FriendPotion.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(EssenceOfLight.class, Color.GOLD, null, null, EssenceOfLight.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(FriendshipDrink.class, Color.NAVY, Color.SKY, null, FriendshipDrink.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(BrilliantLights.class, Color.GOLD, null, null, BrilliantLights.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(BottledCommand.class, Color.SKY, null, null, BottledCommand.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(LayeredElixir.class, Color.GOLD, Color.DARK_GRAY, null, LayeredElixir.ID, NearlEnum.NEARL_CLASS);
        BaseMod.addPotion(InvigoratingPotion.class, Color.WHITE, null, null, InvigoratingPotion.ID);
        BaseMod.addPotion(BottledGleam.class, Color.CLEAR, null, Color.WHITE, BottledGleam.ID);
        BaseMod.addPotion(AttackUpPotion.class, Color.RED, Color.ORANGE, null, AttackUpPotion.ID);
        BaseMod.addPotion(StrikeBackPotion.class, Color.SKY , null, null, StrikeBackPotion.ID);
    }

    private void initializeSpecialImg() {
        specialImg.put(Viviana.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_viviana.png"), 0, 0, 512, 512));
        specialImg.put(Shining.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_shining.png"), 0, 0, 512, 512));
        specialImg.put(Nightingale.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_nightingale.png"), 0, 0, 512, 512));
        specialImg.put(Blemishine.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_blemishine.png"), 0, 0, 512, 512));
        specialImg.put(Whislash.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_whislash.png"), 0, 0, 512, 512));
        specialImg.put(Flametail.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_flametail.png"), 0, 0, 512, 512));
        specialImg.put(Fartooth.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_fartooth.png"), 0, 0, 512, 512));
        specialImg.put(Ashlock.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_ashlock.png"), 0, 0, 512, 512));
        specialImg.put(Wildmane.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_wildmane.png"), 0, 0, 512, 512));
        specialImg.put(JusticeKnight.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_justiceknight.png"), 0, 0, 512, 512));

        specialImg.put(Aurora.ORB_ID, new TextureAtlas.AtlasRegion(new Texture("resources/nearlmod/images/512/card_friend_aurora.png"), 0, 0, 512, 512));
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
        initFriendCard();

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
        BaseMod.addCard(new CounterattackWithShield()); // 持盾反击
        BaseMod.addCard(new Unsheathed()); // 出鞘
        BaseMod.addCard(new Dreadnought()); // 无畏者
        BaseMod.addCard(new DayLike()); // 如昼
        BaseMod.addCard(new OverflowingLight()); // 溢光
        BaseMod.addCard(new ReservedForce()); // 后备力量
        BaseMod.addCard(new ArmorProtection()); // 盔甲防护
        BaseMod.addCard(new Sacrifice()); // 牺牲

        // Uncommon.
        BaseMod.addCard(new SecondSun()); // 第二轮太阳
        BaseMod.addCard(new LightSpearStrike()); // 光枪打击
        BaseMod.addCard(new SwallowLight()); // 侵吞光芒
        BaseMod.addCard(new StayGold()); // 不渝之金
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
        BaseMod.addCard(new BoldAssault()); // 奋力一搏
        BaseMod.addCard(new LightCascade()); // 光瀑
        BaseMod.addCard(new StormCounterAttack()); // 风暴反击
        BaseMod.addCard(new FieldInTheLight()); // 光中原野
        BaseMod.addCard(new MedalOfHonor()); // 荣誉之证
        BaseMod.addCard(new FanClub()); // 后援团
        BaseMod.addCard(new UpgradedKnightShield()); // 改良骑士盾牌
        BaseMod.addCard(new Flaw()); // 破绽
        BaseMod.addCard(new InfiniteLightBlade()); // 无尽光刃

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
        BaseMod.addCard(new PegasusForm()); // 天马形态
        BaseMod.addCard(new DayBreak()); // 破晓
        BaseMod.addCard(new UnitedAsOne()); // 万众一心
        BaseMod.addCard(new Followers()); // 使徒
        BaseMod.addCard(new PocketVault()); // “小金库”

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

        BaseMod.addCard(new TempestPlatoon()); // 风暴突击
        BaseMod.addCard(new Exemplars()); // “典范之人”
        BaseMod.addCard(new Solo()); // 独奏曲
        BaseMod.addCard(new FallingShield()); // 坠落之盾
        BaseMod.addCard(new Bloodbath()); // 血战
        BaseMod.addCard(new FlareGrenade()); // 照明榴弹
        BaseMod.addCard(new TempestCommand()); // 暴风号令
        BaseMod.addCard(new UltimateLineOfDefense()); // 终极防线

        BaseMod.addCard(new ArtificialSnowfall()); // 人工降雪
        BaseMod.addCard(new FrigidRespite()); // 低温静滞
        BaseMod.addCard(new HomelandProtector()); // 固守家园
        BaseMod.addCard(new ShieldPhotographyModule()); // 盾用摄制模块

        // Special
        BaseMod.addCard(new Beginning()); // 起点
        BaseMod.addCard(new BlemishinesFaintLight()); // 瑕光微明
        BaseMod.addCard(new PersonalCharmSp()); // 个人魅力SP
        BaseMod.addCard(new MedalOfChampion()); // 冠军奖章
        BaseMod.addCard(new NearlCard()); // 使徒：临光
        BaseMod.addCard(new NightingaleCard()); // 使徒：夜莺
        BaseMod.addCard(new ShiningCard()); // 使徒：闪灵
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new Nearl(CardCrawlGame.playerName), charButton, charPortrait, NearlEnum.NEARL_CLASS);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal("resources/nearlmod/strings/" + getLang() + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
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

        // common.
        BaseMod.addRelicToCustomPool(new BackupEquipment(), NEARL_GOLD);

        // uncommon.
        BaseMod.addRelicToCustomPool(new FirstAidMode(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new UpgradedCoreCaster(), NEARL_GOLD);
        BaseMod.addRelic(new HandOfConqueror(), RelicType.SHARED);
        BaseMod.addRelic(new KnightFiction(), RelicType.SHARED);

        // rare.
        BaseMod.addRelicToCustomPool(new EmergencyCallBook(), NEARL_GOLD);
        BaseMod.addRelic(new KnightFamily(), RelicType.SHARED);

        // boss.
        BaseMod.addRelicToCustomPool(new PegasusHalo(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new Lighthouse(), NEARL_GOLD);

        // event.
        BaseMod.addRelicToCustomPool(new Marigold(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new LateLight(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new Revenge(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new NormalPerson(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new ImaginaryFear(), NEARL_GOLD);
        BaseMod.addRelicToCustomPool(new BloodEntangle(), NEARL_GOLD);

        // shop.
        BaseMod.addRelicToCustomPool(new RatSwarm(), NEARL_GOLD);
    }

    @Override
    public void receiveEditStrings() {
        String lang = getLang();
        String cardStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String characterStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/character.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
        String powerStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String stanceStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/stances.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(StanceStrings.class, stanceStrings);
        String orbStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/orbs.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(OrbStrings.class, orbStrings);
        String relicStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String eventStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String monsterStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/monsters.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String uiStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String potionStrings = Gdx.files.internal("resources/nearlmod/strings/" + lang + "/potions.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
    }

    private String getLang() {
        String lang = "eng";
        if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
            lang = "zhs";
        }
        return lang;
    }

    public static boolean checkOrb(String orbId) {
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if ((orb instanceof AbstractFriend) && (orb.ID.equals(orbId)))
                return true;
        return false;
    }

    public static void initFriendCard() {
        friendCards = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        friendCards.group.add(new DivineAvatar());
        friendCards.group.add(new DeterringRadiance());
        friendCards.group.add(new SurgingBrilliance());
        friendCards.group.add(new CraftsmanEcho());

        friendCards.group.add(new WhiteFiendProtection());
        friendCards.group.add(new ArtsShield());
        friendCards.group.add(new ClosedHope());
        friendCards.group.add(new Sanctuary());

        friendCards.group.add(new BlackFiendProtection());
        friendCards.group.add(new AutoProtect());
        friendCards.group.add(new Creed());
        friendCards.group.add(new CreedField());

        friendCards.group.add(new FlameShadow());
        friendCards.group.add(new LSSwiftSword());
        friendCards.group.add(new GlimmeringTouch());
        friendCards.group.add(new FlashFade());

        friendCards.group.add(new Rebuke());
        friendCards.group.add(new MotivationalSkills());
        friendCards.group.add(new VisionOfUnity());
        friendCards.group.add(new WhipSword());

        friendCards.group.add(new FlameHeart());
        friendCards.group.add(new PinusSylvestris(false));

        friendCards.group.add(new BombardmentStudies());
        friendCards.group.add(new FocusedBombardment());

        friendCards.group.add(new LanceCharge());
        friendCards.group.add(new StabbingLance());

        friendCards.group.add(new AllySupport());
        friendCards.group.add(new FeatherShineArrows());

        friendCards.group.add(new BeepActivate());
        friendCards.group.add(new JusticeDrive());

        // Monthly Team 1
        friendCards.group.add(new Bloodbath());
        friendCards.group.add(new FlareGrenade());
        friendCards.group.add(new TempestCommand());
        friendCards.group.add(new UltimateLineOfDefense());

        friendCards.group.add(new ShieldPhotographyModule());
        friendCards.group.add(new ArtificialSnowfall());
        friendCards.group.add(new HomelandProtector());
        friendCards.group.add(new FrigidRespite());
    }
}