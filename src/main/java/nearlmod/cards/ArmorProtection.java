package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.DefStance;

public class ArmorProtection extends AbstractNearlCard {
    public static final String ID = "nearlmod:ArmorProtection";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/glowingbluebird.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 8;
    private static final int BLOCK_EXT = 4;
    private static final int UPGRADE_PLUS_AMT = 2;
    private static final int UPGRADE_PLUS_EXT = 1;

    public ArmorProtection() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = BLOCK_EXT;
    }

    @Override
    public boolean extraTriggered() {
        return AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        addToBot(new GainBlockAction(p, p, block));
        if (extraTriggered())
            addToBot(new GainBlockAction(p, p, magicNumber));
    }

    @Override
    public void applyPowers() {
        int basicBlock = baseBlock;
        baseBlock = baseMagicNumber;
        super.applyPowers();
        magicNumber = block;
        isMagicNumberModified = magicNumber != baseMagicNumber;
        baseBlock = basicBlock;
        super.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ArmorProtection();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_AMT);
            upgradeMagicNumber(UPGRADE_PLUS_EXT);
        }
    }
}
