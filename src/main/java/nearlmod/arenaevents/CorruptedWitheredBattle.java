package nearlmod.arenaevents;

import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nearlmod.monsters.*;

public class CorruptedWitheredBattle extends AbstractImageEvent {
    public static final String ID = "nearlmod:CorruptedWitheredBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    public CorruptedWitheredBattle() {
        super(NAME, DESCRIPTIONS[0], "images/events/laughallyouwant.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1], CardLibrary.getCopy("Regret"));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                logMetric(ID, "Fight");
                AbstractDungeon.lastCombatMetricKey = ID;
                (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMBAT;
                (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new AbstractMonster[] { new CorruptKnight(-200.0F, 0.0F), new WitheredKnight(80.0F, 0.0F) });
                enterCombatFromImage();
                return;
            case 1:
                logMetric(ID, "Leave");
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
                imageEventText.updateDialogOption(0, OPTIONS[2]);
                imageEventText.clearRemainingOptions();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Regret(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                openMap();
                return;
        }
        openMap();
    }
}
