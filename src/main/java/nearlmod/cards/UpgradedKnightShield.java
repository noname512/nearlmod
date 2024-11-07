package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.orbs.Nightingale;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.SanctuaryPower;

public class UpgradedKnightShield extends AbstractNearlCard {
    public static final String ID = "nearlmod:UpgradedKnightShield";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/upgradedknightshield.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 8;
    private static final int UPGRADED_PLUS_BLOCK = 4;

    public UpgradedKnightShield() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Nightingale.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Nightingale.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        if (extraTriggered())
            addToBot(new ApplyPowerAction(p, p, new SanctuaryPower(p, 1)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new UpgradedKnightShield();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADED_PLUS_BLOCK);
        }
    }
}
