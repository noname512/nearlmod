package nearlmod;

import basemod.*;
import basemod.abstracts.CustomCard;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import nearlmod.patches.NearlEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nearlmod.cards.*;
import nearlmod.characters.Nearl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static basemod.BaseMod.logger;
import static nearlmod.patches.AbstractCardEnum.NEARL_GOLD;

@SpireInitializer
public class NLMOD implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber, OnStartBattleSubscriber, OnPlayerLoseBlockSubscriber, RelicGetSubscriber {

    private static final Color NearlGold = CardHelper.getColor(255, 236, 194);
    private static final String attackCard = "images/512/bg_attack_mystic.png";
    private static final String skillCard = "images/512/bg_skill_mystic.png";
    private static final String powerCard = "images/512/bg_power_mystic.png";
    private static final String energyOrb = "images/512/card_mystic_orb.png";
    private static final String attackCardPortrait = "images/1024/bg_attack_mystic.png";
    private static final String skillCardPortrait = "images/1024/bg_skill_mystic.png";
    private static final String powerCardPortrait = "images/1024/bg_power_mystic.png";
    private static final String energyOrbPortrait = "images/1024/card_mystic_orb.png";
    private static final String charButton = "images/charSelect/button.png";
    private static final String charPortrait = "images/charSelect/portrait.png";
    private static final String miniManaSymbol = "images/manaSymbol.png";
    private static Logger logger = LogManager.getLogger(NLMOD.class.getName());

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
    public void receivePostInitialize() {}

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
        // Basic.
        BaseMod.addCard(new NearlStrike()); // 打击
        BaseMod.addCard(new NearlDefend()); // 防御
        // BaseMod.addCard(new SwitchType()); // 形态切换
        BaseMod.addCard(new MajestyLight()); // 手执威光
        BaseMod.addCard(new NightScouringGleam()); // 逐夜烁光
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
    public void receiveEditRelics() {}

    @Override
    public void receiveEditStrings() {
        String cardStrings = Gdx.files.internal("strings/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
    }
}