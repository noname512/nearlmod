package nearlmod.events;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.special.PersonalCharmSp;
import nearlmod.monsters.*;
import nearlmod.orbs.Flametail;
import nearlmod.orbs.Wildmane;

public class TheSurroundedEvent extends AbstractImageEvent {
    public static final String ID = "nearlmod:TheSurroundedEvent";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, FIGHT, LEAVE
    }
    public TheSurroundedEvent() {
        super(NAME, DESCRIPTIONS[0], "images/events/thesurrounded.png");
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
                        CardCrawlGame.music.fadeOutTempBGM();
                        CardCrawlGame.music.playTempBgmInstantly("m_bat_kazimierz2_1_combine.mp3", true);
                        (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[]{new ArmorlessAssassin(-350.0F, 0.0F, 0), new ArmorlessThirdSquad(-125.0F, 0.0F, 0), new KnightTerritoryHibernator(80.0F, 0.0F, 0)});
                        AbstractDungeon.lastCombatMetricKey = "The Surrounded Battle";
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractNearlCard.addSpecificCardsToReward(new PersonalCharmSp());
                        AbstractDungeon.getCurrRoom().addGoldToRewards(30);
                        AbstractDungeon.getCurrRoom().eliteTrigger = true;
                        enterCombatFromImage();
                        AbstractDungeon.actionManager.addToBottom(new SummonFriendAction(new Flametail()));
                        AbstractDungeon.actionManager.addToBottom(new SummonFriendAction(new Wildmane()));
                        if (AbstractDungeon.ascensionLevel < 15) {
                            AbstractDungeon.actionManager.addToBottom(new SummonFriendAction(new Flametail()));
                            AbstractDungeon.actionManager.addToBottom(new SummonFriendAction(new Wildmane()));
                        }
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screen = CurScreen.LEAVE;
                        logMetric(ID, "Leave");
                        imageEventText.updateDialogOption(0, OPTIONS[2]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                this.imageEventText.clearRemainingOptions();
                return;
            default:
                openMap();
        }
        openMap();
    }
}

