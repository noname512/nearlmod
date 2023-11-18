package nearlmod.events;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import nearlmod.monsters.CandleKnight;
import nearlmod.relics.Marigold;

public class PoemLooksEvent extends AbstractEvent {
    public static final String ID = "nearlmod:PoemLooksEvent";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, FIGHT_1, FIGHT_2, END;
    }
    public PoemLooksEvent() {
        super();
        this.body = DESCRIPTIONS[0];
        this.roomEventText.addDialogOption(OPTIONS[0]);
        this.hasDialog = true;
        this.hasFocus = true;
        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new CandleKnight());
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screen = CurScreen.FIGHT_1;
                this.roomEventText.updateDialogOption(0, OPTIONS[1]);
                break;
            case FIGHT_1:
                this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                this.screen = CurScreen.FIGHT_2;
                this.roomEventText.updateDialogOption(0, OPTIONS[2]);
                break;
            case FIGHT_2:
                AbstractDungeon.getCurrRoom().addGoldToRewards(99);
                AbstractDungeon.getCurrRoom().addRelicToRewards(new Marigold());
                enterCombat();
                AbstractDungeon.lastCombatMetricKey = "Viviana";
                break;
        }
    }
}
