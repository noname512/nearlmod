package nearlmod.arenaevents;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.SwallowLight;
import nearlmod.monsters.CandleKnight;
import nearlmod.monsters.Mandragora;
import nearlmod.relics.Marigold;
import nearlmod.relics.NormalPerson;

public class MandragoraBattle extends AbstractArenaEvent {
    public static final String ID = "nearlmod:MandragoraBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    public MandragoraBattle() {
        super(NAME, DESCRIPTIONS[0], "resources/nearlmod/images/events/candleknightbattle.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[2], CardLibrary.getCopy("Normality"), new NormalPerson());
        noCardsInRewards = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        //TODO：完全没改
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screen = CurScreen.FIGHT;
                        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                        return;
                    case 1:
                        leave();
                        return;
                }
            case FIGHT:
                switch (buttonPressed) {
                    case 0:
                        logMetric(ID, "Fight");
                        CardCrawlGame.music.fadeOutTempBGM();
                        CardCrawlGame.music.playTempBgmInstantly("m_bat_ccs8_b1_combine.mp3", true);
                        AbstractCard card = new SwallowLight();
                        if (AbstractDungeon.ascensionLevel < 12)
                            card.upgrade();
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractNearlCard.addSpecificCardsToReward(card);
                        AbstractDungeon.getCurrRoom().addRelicToRewards(new Marigold());
                        AbstractDungeon.getCurrRoom().addGoldToRewards(99);
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
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new NormalPerson());
        openMap();
    }
}
