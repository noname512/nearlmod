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
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import nearlmod.events.PoemLooksEvent;
import nearlmod.monsters.CandleKnight;
import nearlmod.patches.NearlEnum;
import nearlmod.relics.CureUp;
import nearlmod.relics.PegasusHalo;
import nearlmod.relics.PoemLooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import nearlmod.cards.*;
import nearlmod.cards.friendcards.*;
import nearlmod.cards.special.*;
import nearlmod.characters.Nearl;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static nearlmod.patches.AbstractCardEnum.NEARL_GOLD;

@SpireInitializer
public class NLMOD implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber, OnStartBattleSubscriber, OnPlayerLoseBlockSubscriber, RelicGetSubscriber {

    private static final Color NearlGold = CardHelper.getColor(255, 236, 194);
    private static final String attackCard = "images/512/bg_attack_nearl.png";
    private static final String skillCard = "images/512/bg_skill_nearl.png";
    private static final String powerCard = "images/512/bg_power_nearl.png";
    private static final String energyOrb = "images/512/card_mystic_orb.png";
    private static final String attackCardPortrait = "images/1024/bg_attack_nearl.png";
    private static final String skillCardPortrait = "images/1024/bg_skill_nearl.png";
    private static final String powerCardPortrait = "images/1024/bg_power_nearl.png";
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
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        new NLMOD();
    }

    @Override
    public void receivePostInitialize() {
        initializeEvents();
        initializeMonsters();
    }

    private void initializeEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(PoemLooksEvent.ID, PoemLooksEvent.class).
                eventType(EventUtils.EventType.ONE_TIME).
                playerClass(NearlEnum.NEARL_CLASS).
                dungeonID("Exordium").
                bonusCondition(() -> (AbstractDungeon.floorNum > 6)).
                create());
        //BaseMod.addEvent("nearlmod:PoemLooksEvent", PoemLooksEvent.class, "Exordium");
    }

    private void initializeMonsters() {
        BaseMod.addMonster("nearlmod:CandleKnight", CandleKnight.NAME, () -> new MonsterGroup(new AbstractMonster[] {new CandleKnight()}));
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

        // Uncommon.
        BaseMod.addCard(new SecondSun()); // 第二轮太阳
        BaseMod.addCard(new LightArrowStrike()); // 光箭打击
        BaseMod.addCard(new SwallowLight()); // 侵吞光芒
        BaseMod.addCard(new AllOutEffort()); // 全力以赴
        BaseMod.addCard(new DanceTogether()); // 共舞一曲
        BaseMod.addCard(new DawnDuskSaber()); // 斩破晨昏
        BaseMod.addCard(new SweepWrong()); // 扫尽恶行

        // Rare.
        BaseMod.addCard(new BravetheDarkness()); // 不畏苦暗
        BaseMod.addCard(new FirstAid()); // 急救
        BaseMod.addCard(new FlamingEdge()); // 灿焰长刃
        BaseMod.addCard(new NightScouringGleam()); // 逐夜烁光
        BaseMod.addCard(new WaytoChampion()); // 冠军之路
        BaseMod.addCard(new Dawn()); // 曙光
        BaseMod.addCard(new TheReturn()); // 归来
        BaseMod.addCard(new FullSpeedAhead()); // 全力冲锋
        BaseMod.addCard(new PersonalCharm()); // 个人魅力

        // Friend.
        BaseMod.addCard(new LSSwiftSword()); // 光影迅捷剑
        BaseMod.addCard(new FlashFade()); // “明灭”
        BaseMod.addCard(new FlameShadow()); // 烛燃影息
        BaseMod.addCard(new GlimmeringTouch()); // 微光之触

        BaseMod.addCard(new SurgingBrilliance()); // 光芒涌动
        BaseMod.addCard(new DeterringRadiance()); // 慑敌辉光
        BaseMod.addCard(new DivineAvatar()); // 先贤化身
        BaseMod.addCard(new CraftsmanEcho()); // 工匠团的回响
        BaseMod.addCard(new ToAtkStance());
        BaseMod.addCard(new ToDefStance());

        BaseMod.addCard(new ArtsShield()); // 法术护盾
        BaseMod.addCard(new Sanctuary()); // 圣域
        BaseMod.addCard(new WhiteFiendProtection()); // 白恶魔的庇护
        BaseMod.addCard(new FleetingPhantom()); // 转瞬即逝的幻影

        BaseMod.addCard(new Creed()); // 信条
        BaseMod.addCard(new AutoProtect()); // 自动掩护
        BaseMod.addCard(new CreedField()); // 教条立场
        BaseMod.addCard(new BlackFiendProtection()); // 黑恶魔的庇护

        BaseMod.addCard(new PinusSylvestris()); // “红松林”
        BaseMod.addCard(new FlameHeart()); // 焰心
        BaseMod.addCard(new AllySupport()); // 同盟支援
        BaseMod.addCard(new FeathershineArrows()); // 光羽箭
        BaseMod.addCard(new StabbingLance()); // 骑枪刺击
        BaseMod.addCard(new LanceCharge()); // 夹枪冲锋
        BaseMod.addCard(new JusticeDrive()); // 正义助威
        BaseMod.addCard(new BeepActivate()); // “滴滴，启动！”
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

        Map<String, Keyword> keywords = (Map)gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            logger.info("Adding Keyword - " + v.NAMES[0]);
            BaseMod.addKeyword("nearlmod:", v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditRelics() {
        // starter.
        BaseMod.addRelicToCustomPool(new CureUp(), NEARL_GOLD);

        // boss.
        BaseMod.addRelicToCustomPool(new PegasusHalo(), NEARL_GOLD);

        // event.
        BaseMod.addRelicToCustomPool(new PoemLooks(), NEARL_GOLD);
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
    }
}