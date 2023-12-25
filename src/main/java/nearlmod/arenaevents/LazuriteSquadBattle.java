package nearlmod.arenaevents;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nearlmod.monsters.ArmorlessCleanupSquad;
import nearlmod.monsters.ArmorlessThirdSquad;

public class LazuriteSquadBattle extends AbstractArenaEvent {
    public static final String ID = "nearlmod:LazuriteSquadBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private final int monsterLevel;

    public LazuriteSquadBattle(int monsterLevel) {
        super(NAME, DESCRIPTIONS[0], "images/events/lazuritesquad.png");
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        this.monsterLevel = monsterLevel;
    }

    public LazuriteSquadBattle() {
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
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[] { new ArmorlessThirdSquad(-200.0F, 0.0F, monsterLevel), new ArmorlessCleanupSquad(80.0F, 0.0F, monsterLevel) });
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
