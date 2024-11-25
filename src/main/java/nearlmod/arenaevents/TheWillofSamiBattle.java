package nearlmod.arenaevents;

import basemod.devcommands.hand.Hand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.FindYourWayThroughTheSnow;
import nearlmod.monsters.TheWillOfSami;
import nearlmod.relics.AnmasLove;
import nearlmod.relics.HandOfConqueror;
import nearlmod.relics.ImaginaryFear;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.rareRelicPool;

public class TheWillofSamiBattle extends AbstractArenaEvent {
    public static final String ID = "nearlmod:TheWillofSamiBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    public TheWillofSamiBattle() {
        super(NAME, DESCRIPTIONS[0], "resources/nearlmod/images/events/thewillofsamibattle.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1], CardLibrary.getCopy("Injury"), new ImaginaryFear());
        noCardsInRewards = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        //TODO：完全没改
        if (screen != CurScreen.INTRO) {
            openMap();
            return;
        }
        switch (buttonPressed) {
            case 0:
                logMetric(ID, "Fight");
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.playTempBgmInstantly("m_bat_rouge3boss1_combine.mp3", true);
                screen = CurScreen.FIGHT;
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractRelic relic;
                if (!AbstractDungeon.player.hasRelic(HandOfConqueror.ID) && AbstractDungeon.eventRng.randomBoolean()) {
                    relic = new HandOfConqueror();
                    rareRelicPool.remove(HandOfConqueror.ID);
                }
                else {
                    relic = new AnmasLove();
                }
                AbstractDungeon.getCurrRoom().addRelicToRewards(relic);
                AbstractDungeon.getCurrRoom().addGoldToRewards(99);
                AbstractCard card = new FindYourWayThroughTheSnow();
                if (AbstractDungeon.ascensionLevel <= 12) {
                    card.upgrade();
                }
                AbstractNearlCard.addSpecificCardsToReward(card);
                AbstractDungeon.lastCombatMetricKey = ID;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new TheWillOfSami(-80.0F, -50.0F));
                AbstractDungeon.getCurrRoom().eliteTrigger = true;
                enterCombatFromImage();
                return;
            case 1:
                logMetric(ID, "Leave");
                screen = CurScreen.LEAVE;
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                imageEventText.updateDialogOption(0, OPTIONS[2]);
                imageEventText.clearRemainingOptions();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Injury(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new ImaginaryFear());
                openMap();
                return;
        }
        openMap();
    }
}
