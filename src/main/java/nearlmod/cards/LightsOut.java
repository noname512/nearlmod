package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ShadowPower;

public class LightsOut extends AbstractNearlCard {
    public static final String ID = "nearlmod:LightsOut";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/lightsout.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 8;
    private static final int SHADOW_GAIN = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int UPGRADE_PLUS_SHADOW = 2;

    public LightsOut() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = SHADOW_GAIN;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Viviana.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        if (extraTriggered())
            addToBot(new ApplyPowerAction(p, p, new ShadowPower(p, magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightsOut();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_SHADOW);
        }
    }
}
