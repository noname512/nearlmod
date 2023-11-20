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
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.target == CardTarget.ALL_ENEMY) {
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!mo.isDeadOrEscaped()) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new StrengthPower(mo, -DECREASE_STRENGTH), -DECREASE_STRENGTH));
                    if (!mo.hasPower("Artifact")) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new GainStrengthPower(mo, DECREASE_STRENGTH), DECREASE_STRENGTH));
                    }
                }
            }
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new StrengthPower(m, -DECREASE_STRENGTH), -DECREASE_STRENGTH));
            if (m != null && !m.hasPower("Artifact")) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new GainStrengthPower(m, DECREASE_STRENGTH), DECREASE_STRENGTH));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BraveTheDarkness();
    }

    @Override
    public void switchedStance() {
        if (AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) this.target = CardTarget.ALL_ENEMY;
        else this.target = CardTarget.ENEMY;
    }

    public void triggerOnGlowCheck() {
        if (AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
