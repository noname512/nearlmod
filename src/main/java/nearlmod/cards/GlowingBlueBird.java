package nearlmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.orbs.Nightingale;
import nearlmod.patches.AbstractCardEnum;

public class GlowingBlueBird extends AbstractNearlCard {
    public static final String ID = "nearlmod:GlowingBlueBird";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/glowingbluebird.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 7;
    private static final int HP_GAIN = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int UPGRADE_PLUS_HP = 1;

    public GlowingBlueBird() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = HP_GAIN;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Nightingale.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (extraTriggered())
            addToBot(new AddTemporaryHPAction(p, p, magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GlowingBlueBird();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_HP);
        }
    }
}
