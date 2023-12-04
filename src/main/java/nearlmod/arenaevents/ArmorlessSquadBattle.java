package nearlmod.arenaevents;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nearlmod.monsters.ArmorlessAssassin;
import nearlmod.monsters.ArmorlessCrossbowman;

public class ArmorlessSquadBattle extends AbstractImageEvent {
    public static final String ID = "nearlmod:ArmorlessSquadBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private final int monsterLevel;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, FIGHT, LEAVE
    }
    public ArmorlessSquadBattle(int monsterLevel) {
        super(NAME, DESCRIPTIONS[0], "images/events/laughallyouwant.png");
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        this.monsterLevel = monsterLevel;
    }

    public ArmorlessSquadBattle () {
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
                screen = CurScreen.FIGHT;
                AbstractDungeon.lastCombatMetricKey = ID;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                if (AbstractDungeon.ascensionLevel >= 15)
                    AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[] {
                            new ArmorlessCrossbowman(-360.0F, 0.0F, monsterLevel),
                            new ArmorlessAssassin(-130.0F, 0.0F, monsterLevel),
                            new ArmorlessCrossbowman(100.0F, 0.0F, monsterLevel)});
                else
                    AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[] {
                            new ArmorlessAssassin(-200.0F, 0.0F, monsterLevel),
                            new ArmorlessCrossbowman(80.0F, 0.0F, monsterLevel) });
                enterCombatFromImage();
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(30, 40));
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
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
