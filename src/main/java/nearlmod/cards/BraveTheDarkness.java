package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;

public class BraveTheDarkness extends AbstractNearlCard {
    public static final String ID = "nearlmod:BraveTheDarkness";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/bravethedarkness.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_COST = 1;
    private static final int DECREASE_STRENGTH = 99;

    public BraveTheDarkness() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
        
        damage = baseDamage = ATTACK_DMG;
        exhaust = true;
    }

    @Override
    public boolean extraTriggered() {
        return AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (!mo.isDeadOrEscaped()) {
                    calculateCardDamage(m);
                    addToBot(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
            }
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (!mo.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -DECREASE_STRENGTH), -DECREASE_STRENGTH));
                    if (!mo.hasPower("Artifact")) {
                        addToBot(new ApplyPowerAction(mo, p, new GainStrengthPower(mo, DECREASE_STRENGTH), DECREASE_STRENGTH));
                    }
                }
            }
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -DECREASE_STRENGTH), -DECREASE_STRENGTH));
            if (m != null && !m.hasPower("Artifact")) {
                addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, DECREASE_STRENGTH), DECREASE_STRENGTH));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BraveTheDarkness();
    }

    @Override
    public void switchedStance() {
        if (extraTriggered()) this.target = CardTarget.ALL_ENEMY;
        else this.target = CardTarget.ENEMY;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
