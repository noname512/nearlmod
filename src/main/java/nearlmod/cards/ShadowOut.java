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
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.actions.SummonOrbAction;
import nearlmod.actions.UseShadowAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
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
        int extraDmg = 0;
        if (power != null) {
            extraDmg = power.amount;
            addToBot(new RemoveSpecificPowerAction(p, p, power));
            addToBot(new ApplyPowerAction(p, p, new ShadowPower(p, extraDmg)));
        }
        if (extraTriggered()) {
            for (int i = 0; i < secondMagicNumber; i++)
                addToBot(new PureDamageAllEnemiesAction(p, magicNumber + extraDmg, Viviana.ORB_ID + AbstractFriendCard.damageSuffix));
            addToBot(new UseShadowAction(p));
        } else {
            addToBot(new SummonOrbAction(new Viviana()));
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
        isMagicNumberModified = (magicNumber != baseMagicNumber);
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
