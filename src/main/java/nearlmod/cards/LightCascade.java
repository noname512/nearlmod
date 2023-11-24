package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;
import nearlmod.powers.PoemsLooksPower;
import nearlmod.powers.ShadowPower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class LightCascade extends AbstractNearlCard {
    public static final String ID = "nearlmod:LightCascade";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public LightCascade() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        tags.add(NearlTags.IS_USE_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = 0;
        if (p.hasPower(LightPower.POWER_ID)) {
            amount = p.getPower(LightPower.POWER_ID).amount;
            if (p.hasPower(PoemsLooksPower.POWER_ID)) {
                addToBot(new ApplyPowerAction(p, p, new ShadowPower(p, amount)));
            }
            addToBot(new RemoveSpecificPowerAction(p, p, p.getPower(LightPower.POWER_ID)));
        }
        if (p.stance.ID.equals(DefStance.STANCE_ID)) {
            amount += AtkStance.atkInc + AtkStance.incNum;
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
        addToBot(new DamageAllEnemiesAction(p, amount, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightCascade();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
