package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.friendcards.AbstractFriendCard;

public class DuelPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:Duel";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DuelPower(AbstractCreature owner,int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
        this.loadRegion("curiosity"); // TODO: 换个图案
        priority = 120;
    }
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.name != null && info.type == DamageInfo.DamageType.NORMAL && info.name.endsWith(AbstractFriendCard.damageSuffix)) {
            return MathUtils.floor(damageAmount * (1 - 0.01F * amount));
        }
        else {
            return damageAmount;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DuelPower(owner, amount);
    }
}
