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
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.special.Beginning;
import nearlmod.monsters.*;
import nearlmod.relics.LateLight;

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
        noCardsInRewards = true;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                logMetric(ID, "Fight");
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractNearlCard.addSpecificCardsToReward(new Beginning());
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
                int gold = 48;
                if (AbstractDungeon.ascensionLevel >= 13) gold = 36;
                AbstractDungeon.getCurrRoom().addGoldToRewards(gold);
                AbstractDungeon.lastCombatMetricKey = ID;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[] { new CorruptKnight(-200.0F, 0.0F), new WitheredKnight(80.0F, 0.0F) });
                enterCombatFromImage();
                return;
            case 1:
                logMetric(ID, "Leave");
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
                imageEventText.updateDialogOption(0, OPTIONS[2]);
                imageEventText.clearRemainingOptions();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Regret(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new LateLight());
                openMap();
                return;
        }
        openMap();
    }
}
