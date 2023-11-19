package nearlmod.events;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import nearlmod.monsters.Monique;
import nearlmod.monsters.Platinum;
import nearlmod.monsters.Roy;

public class LaughAllYouWantEvent extends AbstractImageEvent {
    public static final String ID = "nearlmod:LaughAllYouWantEvent";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, FIGHT, LEAVE;
    }
    public LaughAllYouWantEvent() {
        super(NAME, DESCRIPTIONS[0], "images/events/laughallyouwant.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screen = CurScreen.FIGHT;
                        logMetric(ID, "Fight");
                        (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[]{new Platinum(-350.0F, 0.0F), new Monique(-125.0F, 0.0F), new Roy(80.0F, 0.0F)});
                        enterCombatFromImage();
                        AbstractDungeon.lastCombatMetricKey = "Laugh All You Want Battle";
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screen = CurScreen.LEAVE;
                        logMetric(ID, "Leave");
                        openMap();
                        break;
                }
                this.imageEventText.clearRemainingOptions();
                return;
        }
        openMap();
    }
}
