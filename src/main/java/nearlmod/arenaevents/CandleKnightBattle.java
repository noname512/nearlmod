package nearlmod.arenaevents;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nearlmod.monsters.CandleKnight;

public class CandleKnightBattle extends AbstractImageEvent {
    public static final String ID = "nearlmod:CandleKnightBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, FIGHT, LEAVE
    }
    public CandleKnightBattle() {
        super(NAME, DESCRIPTIONS[0], "images/events/laughallyouwant.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[2], CardLibrary.getCopy("Normality"));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
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
                        AbstractDungeon.lastCombatMetricKey = ID;
                        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMBAT;
                        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new CandleKnight());
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
        this.imageEventText.clearRemainingOptions();
        this.imageEventText.setDialogOption(OPTIONS[3]);
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        openMap();
    }
}
