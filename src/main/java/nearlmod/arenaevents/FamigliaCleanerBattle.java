package nearlmod.arenaevents;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.GuardianOfTheLaw;
import nearlmod.monsters.FamigliaCleaner;
import nearlmod.relics.OldRules;
import nearlmod.relics.Revenge;

import java.util.ArrayList;

public class FamigliaCleanerBattle extends AbstractArenaEvent {
    public static final String ID = "nearlmod:FamigliaCleanerBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    public FamigliaCleanerBattle() {
        super(NAME, DESCRIPTIONS[0], "resources/nearlmod/images/events/famigliacleanerbattle.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1], new OldRules());
        noCardsInRewards = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (screen != CurScreen.INTRO) {
            openMap();
            return;
        }
        switch (buttonPressed) {
            case 0:
                logMetric(ID, "Fight");
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.playTempBgmInstantly("m_bat_act21side_01_combine.mp3", true);
                screen = CurScreen.FIGHT;
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractCard card = new GuardianOfTheLaw();
                if (AbstractDungeon.ascensionLevel < 12)
                    card.upgrade();
                AbstractNearlCard.addSpecificCardsToReward(card);
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
                AbstractDungeon.getCurrRoom().addGoldToRewards(50);
                AbstractDungeon.lastCombatMetricKey = ID;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new FamigliaCleaner(0.0F, 0.0F));
                AbstractDungeon.getCurrRoom().eliteTrigger = true;
                enterCombatFromImage();
                return;
            case 1:
                logMetric(ID, "Leave");
                screen = CurScreen.LEAVE;
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                imageEventText.updateDialogOption(0, OPTIONS[2]);
                imageEventText.clearRemainingOptions();
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new OldRules());
                openMap();
                return;
        }
        openMap();
    }
}
