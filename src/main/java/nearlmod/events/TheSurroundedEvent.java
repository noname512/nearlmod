package nearlmod.events;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import nearlmod.actions.SummonOrbAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.PersonalCharmSp;
import nearlmod.monsters.*;
import nearlmod.orbs.Flametail;
import nearlmod.orbs.Wildmane;
import nearlmod.relics.Marigold;

public class TheSurroundedEvent extends AbstractImageEvent {
    public static final String ID = "nearlmod:TheSurroundedEvent";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, FIGHT, LEAVE;
    }
    public TheSurroundedEvent() {
        super(NAME, DESCRIPTIONS[0], "images/events/laughallyouwant.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
        noCardsInRewards = true;
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
                        (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[]{new ArmorlessAssassin(-350.0F, 0.0F, 0), new ArmorlessThirdSquad(-125.0F, 0.0F, 0), new UndertideGloompincer(80.0F, 0.0F, 0)});
                        AbstractDungeon.lastCombatMetricKey = "The Surrounded Battle";
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractNearlCard.addSpecificCardsToReward(new PersonalCharmSp());
                        AbstractDungeon.getCurrRoom().addGoldToRewards(30);
                        enterCombatFromImage();
                        AbstractDungeon.actionManager.addToBottom(new SummonOrbAction(new Flametail()));
                        AbstractDungeon.actionManager.addToBottom(new SummonOrbAction(new Wildmane()));
                        if (AbstractDungeon.ascensionLevel < 15) {
                            AbstractDungeon.actionManager.addToBottom(new SummonOrbAction(new Flametail()));
                            AbstractDungeon.actionManager.addToBottom(new SummonOrbAction(new Wildmane()));
                        }
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screen = CurScreen.LEAVE;
                        logMetric(ID, "Leave");
                        imageEventText.updateDialogOption(0, OPTIONS[2]);
                        imageEventText.clearRemainingOptions();
                        break;
                    case 2:
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
