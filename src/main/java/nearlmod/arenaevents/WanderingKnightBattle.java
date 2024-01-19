package nearlmod.arenaevents;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nearlmod.monsters.*;

public class WanderingKnightBattle extends AbstractArenaEvent {
    public static final String ID = "nearlmod:WanderingKnightBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private final int monsterLevel;

    public WanderingKnightBattle(int monsterLevel) {
        super(NAME, DESCRIPTIONS[0], "images/events/wanderingknight.png");
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        this.monsterLevel = monsterLevel;
    }

    public WanderingKnightBattle() {
        this(0);
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
                CardCrawlGame.music.playTempBgmInstantly("m_bat_kazimierz2_2_loop.mp3", true);
                screen = CurScreen.FIGHT;
                AbstractDungeon.lastCombatMetricKey = ID;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[] {
                        new KnightShielder(-360.0F, 0.0F, monsterLevel),
                        new UndertideGloompincer(-130.0F, 0.0F, monsterLevel),
                        new KnightTerritoryHibernator(100.0F, 0.0F, monsterLevel)});
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(30, 40));
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
                AbstractDungeon.getCurrRoom().eliteTrigger = true;
                enterCombatFromImage();
                return;
            case 1:
                logMetric(ID, "Leave");
                screen = CurScreen.LEAVE;
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
                imageEventText.updateDialogOption(0, OPTIONS[2]);
                imageEventText.clearRemainingOptions();
                openMap();
                return;
        }
        openMap();
    }
}
