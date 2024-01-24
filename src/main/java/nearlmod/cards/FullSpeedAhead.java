package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ExsanguinationPower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

import static java.lang.Integer.min;

public class FullSpeedAhead extends AbstractNearlCard {
    public static final String ID = "nearlmod:FullSpeedAhead";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String ATKMODE_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];
    public static final String DEFMODE_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[1];
    public static final String INHAND_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[2];
    public static final String IMG_PATH = "images/cards/fullspeedahead.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public FullSpeedAhead() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.ENEMY);
        damage = baseDamage = 0;
    }

    @Override
    public boolean extraTriggered() {
        return AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        if (extraTriggered()) {
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        baseDamage = AbstractDungeon.player.currentBlock;
        if (extraTriggered()) {
            baseDamage += TempHPField.tempHp.get(AbstractDungeon.player);
        }
        super.applyPowers();
        if (extraTriggered()) {
            damage += calcStrength();
            isDamageModified = (baseDamage != damage);
        }
        rawDescription = INHAND_DESCRIPTION;
        initializeDescription();
    }

    int calcStrength() {
        int num = AtkStance.incNum + AtkStance.atkInc;
        if (AbstractDungeon.player.hasPower(ExsanguinationPower.POWER_ID)) {
            int strength = 0;
            if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
                strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;
            }
            num = min(num, -strength);
        }
        return num;
    }
    @Override
    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (extraTriggered()) {
            baseDamage += calcStrength();
        }
        super.calculateCardDamage(mo);
        if (extraTriggered()) {
            baseDamage -= calcStrength();
            isDamageModified = (baseDamage != damage);
            rawDescription = DEFMODE_DESCRIPTION;
        } else {
            rawDescription = ATKMODE_DESCRIPTION;
        }
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new FullSpeedAhead();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
