package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.NLMOD;
import nearlmod.actions.ShadowOutAction;
import nearlmod.actions.SummonFriendAction;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ShadowPower;

public class ShadowOut extends AbstractNearlCard {
    public static final String ID = "nearlmod:ShadowOut";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/gloriouskazimierz.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 3;
    private static final int ATTACK_TIMES = 4;
    private static final int UPGRADE_PLUS_TIMES = 1;

    public ShadowOut() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_TIMES;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Viviana.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        AbstractPower power = p.getPower("nearlmod:LightPower");
        int lightNum = 0;
        if (power != null) {
            lightNum = power.amount;
            addToBot(new RemoveSpecificPowerAction(p, p, power));
            addToBot(new ApplyPowerAction(p, p, new ShadowPower(p, lightNum)));
        }
        if (extraTriggered()) {
            addToBot(new ShadowOutAction(p, magicNumber, secondMagicNumber));
        } else {
            addToBot(new SummonFriendAction(new Viviana()));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        magicNumber = baseMagicNumber;
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof Viviana) {
                magicNumber += ((Viviana)orb).getTrustAmount();
                if (AbstractDungeon.player.getPower("nearlmod:Shadow") != null) {
                    magicNumber += AbstractDungeon.player.getPower("nearlmod:Shadow").amount;
                }
                break;
            }
        if (AbstractDungeon.player.hasPower("nearlmod:LightPower")) {
            magicNumber += AbstractDungeon.player.getPower("nearlmod:LightPower").amount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyPowers();
        int ms_count = 0;
        for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDeadOrEscaped()) {
                ms_count ++;
            }
        }
        if (ms_count == 1) {
            for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    magicNumber = AbstractNearlCard.staticCalcDmg(m, magicNumber, damageTypeForTurn, true);
                    isMagicNumberModified = (magicNumber != baseMagicNumber);
                }
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowOut();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_TIMES);
        }
    }
}
