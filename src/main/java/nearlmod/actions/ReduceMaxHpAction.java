package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import nearlmod.monsters.CandleKnight;

public class ReduceMaxHpAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("nearlmod:ReduceMaxHP").TEXT;
    public ReduceMaxHpAction(AbstractCreature c, int amount) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        target = c;
    }

    @Override
    public void update() {
        target.decreaseMaxHealth(amount);
        AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(target.hb.cX - target.animX, target.hb.cY, TEXT[0] + amount, Settings.RED_TEXT_COLOR));
        isDone = true;
    }
}
