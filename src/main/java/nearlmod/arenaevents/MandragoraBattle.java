package nearlmod.arenaevents;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Normality;
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
import nearlmod.cards.TempestPlatoon;
import nearlmod.monsters.Mandragora;
import nearlmod.relics.NormalPerson;

public class MandragoraBattle extends AbstractArenaEvent {
    public static final String ID = "nearlmod:MandragoraBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    public MandragoraBattle() {
        super(NAME, DESCRIPTIONS[0], "resources/nearlmod/images/events/mandragorabattle.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1], CardLibrary.getCopy("Normality"));
        noCardsInRewards = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                logMetric(ID, "Fight");
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.playTempBgmInstantly("m_bat_ghosthunter02_combine.mp3", true);
                AbstractCard card = new TempestPlatoon();
                if (AbstractDungeon.ascensionLevel < 12)
                    card.upgrade();
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractNearlCard.addSpecificCardsToReward(card);
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
                AbstractDungeon.getCurrRoom().addGoldToRewards(70);
                AbstractDungeon.lastCombatMetricKey = ID;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new Mandragora(0.0F, 0.0F));
                AbstractDungeon.getCurrRoom().eliteTrigger = true;
                enterCombatFromImage();
                return;
            case 1:
                leave();
                return;
        }
        openMap();
    }

    private void leave() {
        logMetric(ID, "Leave");
        this.screen = CurScreen.LEAVE;
        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
        imageEventText.updateDialogOption(0, OPTIONS[3]);
        imageEventText.clearRemainingOptions();
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        openMap();
    }
}
