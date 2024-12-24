package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;

public class EndBattleAction extends AbstractGameAction {

    public EndBattleAction() {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (!isDone)
        {
            AbstractDungeon.actionManager.cleanCardQueue();
            AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
            AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
            AbstractDungeon.overlayMenu.hideCombatPanels();
            AbstractDungeon.getCurrRoom().endBattle();
            isDone = true;
        }
    }
}
