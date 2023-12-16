package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.GainCostAction;
import nearlmod.orbs.Whislash;
import nearlmod.patches.AbstractCardEnum;

public class ReservedForce extends AbstractNearlCard {
    public static final String ID = "nearlmod:ReservedForce";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/glowingbluebird.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 7;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DP_GAIN = 1;

    public ReservedForce() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = DP_GAIN;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Whislash.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        if (extraTriggered()) {
            addToBot(new GainCostAction(magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReservedForce();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
